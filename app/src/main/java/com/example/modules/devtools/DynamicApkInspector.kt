package com.example.modules.devtools

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ApkVersionInfo(
    val packageName: String,
    val installedVersionCode: Long,
    val installedVersionName: String,
    val latestTargetVersionCode: Long,
    val isOutdatedApk: Boolean,
    val versionSkewDescription: String,
    val installTimeFormatted: String,
    val updateTimeFormatted: String,
    val targetSdkVersion: Int,
    val isDebuggable: Boolean
)

object DynamicApkInspector {
    /** Runtime app tidak tahu versi "latest" tanpa cek manifest rilis; jangan fabrikasi target. */
    fun inspectRunningApk(context: Context): ApkVersionInfo {
        val pm = context.packageManager
        val pkgInfo: PackageInfo? = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                pm.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                pm.getPackageInfo(context.packageName, 0)
            }
        } catch (_: Exception) { null }

        val vCode = if (pkgInfo != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) pkgInfo.longVersionCode
            else @Suppress("DEPRECATION") pkgInfo.versionCode.toLong()
        } else -1L
        val vName = pkgInfo?.versionName ?: "Unknown"
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        val installTime = if (pkgInfo != null) sdf.format(Date(pkgInfo.firstInstallTime)) else "Unknown"
        val updateTime = if (pkgInfo != null) sdf.format(Date(pkgInfo.lastUpdateTime)) else "Unknown"
        val targetSdk = context.applicationInfo.targetSdkVersion
        val isDebug = (context.applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0
        val skewDesc = "Info APK terpasang (runtime). Cek update via UPDATE_MANIFEST_URL/GitHub Releases; status 'latest' tidak diprobe di sini."

        return ApkVersionInfo(
            packageName = context.packageName,
            installedVersionCode = vCode,
            installedVersionName = vName,
            latestTargetVersionCode = -1L,
            isOutdatedApk = false,
            versionSkewDescription = skewDesc,
            installTimeFormatted = installTime,
            updateTimeFormatted = updateTime,
            targetSdkVersion = targetSdk,
            isDebuggable = isDebug
        )
    }
}
