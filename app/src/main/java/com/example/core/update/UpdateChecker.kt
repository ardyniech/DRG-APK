package com.example.core.update

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

/**
 * Jaringan updater: ambil `update.json` (kecil, KB) dan unduh patch/APK.
 * Tidak ada retry agresif — HP driver sering sinyal jelek, gagal cepat lebih baik.
 */
object UpdateChecker {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    /** versionCode aplikasi yang sedang terpasang. */
    @Suppress("DEPRECATION")
    fun currentVersionCode(context: Context): Int = runCatching {
        context.packageManager.getPackageInfo(context.packageName, 0).versionCode
    }.getOrDefault(0)

    /** sha256 APK yang sedang terpasang — kunci pencocokan patch. */
    fun installedApkSha256(context: Context): String =
        sha256(File(context.applicationInfo.sourceDir))

    fun sha256(file: File): String {
        val md = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { input ->
            val buf = ByteArray(1 shl 16)
            while (true) {
                val n = input.read(buf)
                if (n <= 0) break
                md.update(buf, 0, n)
            }
        }
        return md.digest().joinToString("") { "%02x".format(it) }
    }

    /** Ambil manifest; null kalau offline / JSON rusak / bukan 2xx. */
    fun fetchManifest(url: String): UpdateRelease? = runCatching {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            UpdateManifestParser.parse(body)
        }
    }.getOrNull()

    /** Unduh [url] ke [target] sambil melaporkan progres byte. */
    fun download(url: String, target: File, onProgress: (Long, Long) -> Unit = { _, _ -> }): File {
        val request = Request.Builder().url(url).build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IllegalStateException("HTTP ${response.code}")
            val body = response.body ?: throw IllegalStateException("Body kosong")
            val total = body.contentLength()
            target.parentFile?.mkdirs()
            body.byteStream().use { input ->
                target.outputStream().use { out ->
                    val buf = ByteArray(1 shl 16)
                    var done = 0L
                    while (true) {
                        val n = input.read(buf)
                        if (n <= 0) break
                        out.write(buf, 0, n)
                        done += n
                        onProgress(done, total)
                    }
                }
            }
        }
        return target
    }

    /**
     * True kalau koneksi aktif terukur (data seluler) — untuk mode hemat kuota.
     * Kalau tidak ada info jaringan, anggap terukur (konservatif).
     */
    fun isMetered(context: Context): Boolean = runCatching {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            as? android.net.ConnectivityManager ?: return true
        val network = cm.activeNetwork ?: return true
        val caps = cm.getNetworkCapabilities(network) ?: return true
        !caps.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
    }.getOrDefault(true)

    /** Persentase baterai (0-100). -1 kalau tidak diketahui. */
    fun batteryLevel(context: Context): Int = runCatching {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as? android.os.BatteryManager
        bm?.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: -1
    }.getOrDefault(-1)

    /** True kalau perangkat sedang charging. */
    fun isCharging(context: Context): Boolean = runCatching {
        val intent = context.registerReceiver(
            null, android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED)
        )
        val status = intent?.getIntExtra(android.os.BatteryManager.EXTRA_STATUS, -1) ?: -1
        status == android.os.BatteryManager.BATTERY_STATUS_CHARGING ||
            status == android.os.BatteryManager.BATTERY_STATUS_FULL
    }.getOrDefault(false)
}


