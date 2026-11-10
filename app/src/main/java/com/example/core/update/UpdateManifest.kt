package com.example.core.update

import org.json.JSONObject

/** Satu patch delta: dari APK versi/sha tertentu ke rilis terbaru. */
data class UpdatePatch(
    val fromVersionCode: Int,
    val fromSha256: String,
    val url: String,
    val sizeBytes: Long,
    val sha256: String
)

/** Isi `update.json` yang dipublikasikan CI di GitHub Releases. */
data class UpdateRelease(
    val versionCode: Int,
    val versionName: String,
    val apkUrl: String,
    val apkSizeBytes: Long,
    val apkSha256: String,
    val patches: List<UpdatePatch>,
    val changelog: List<String>
) {
    /**
     * Pilih patch yang cocok dengan APK terpasang. sha256 wajib sama supaya
     * patch tidak pernah diterapkan ke base yang salah (hasil korup).
     */
    fun patchFor(currentVersionCode: Int, installedSha256: String): UpdatePatch? =
        patches.firstOrNull {
            it.fromVersionCode == currentVersionCode &&
                it.fromSha256.equals(installedSha256, ignoreCase = true)
        }
}

/** Parser toleran: field hilang/korup → null, bukan crash. */
object UpdateManifestParser {

    fun parse(json: String): UpdateRelease? = runCatching {
        val root = JSONObject(json)
        val apk = root.getJSONObject("apk")
        val patches = root.optJSONArray("patches")?.let { arr ->
            (0 until arr.length()).map { i ->
                val p = arr.getJSONObject(i)
                UpdatePatch(
                    fromVersionCode = p.getInt("fromVersionCode"),
                    fromSha256 = p.getString("fromSha256"),
                    url = p.getString("url"),
                    sizeBytes = p.optLong("size"),
                    sha256 = p.getString("sha256")
                )
            }
        } ?: emptyList()
        val changelog = root.optJSONArray("changelog")?.let { arr ->
            (0 until arr.length()).map { arr.optString(it, "") }
        } ?: emptyList()
        UpdateRelease(
            versionCode = root.getInt("versionCode"),
            versionName = root.optString("versionName", "?"),
            apkUrl = apk.getString("url"),
            apkSizeBytes = apk.optLong("size"),
            apkSha256 = apk.getString("sha256"),
            patches = patches,
            changelog = changelog
        )
    }.getOrNull()
}
