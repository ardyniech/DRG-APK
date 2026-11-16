package com.example.core.security

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import java.io.File
import java.security.MessageDigest

data class AppIntegrityReport(
    val isSignaturePresent: Boolean,
    val signatureSha256: String,
    val installerPackage: String?,
    val isTrustedInstaller: Boolean,
    val isDebuggableFlagSet: Boolean,
    val hasHookingArtifacts: Boolean
) {
    val isIntegrityHealthy: Boolean
        get() = isSignaturePresent && !hasHookingArtifacts
}

/**
 * Verifikator integritas paket & anti-tamper runtime (signing digest, installer, anti-hook).
 */
object AppIntegrityVerifier {
    private val trustedInstallers = setOf(
        "com.android.vending",
        "com.google.android.feedback",
        "com.android.packageinstaller"
    )
    private val hookPaths = listOf(
        "/data/local/tmp/frida-server",
        "/data/local/tmp/re.frida.server",
        "/system/framework/XposedBridge.jar"
    )
    private val hookClasses = listOf(
        "de.robv.android.xposed.XposedBridge",
        "com.saurik.substrate.MS"
    )

    fun verify(context: Context): AppIntegrityReport {
        val pm = context.packageManager
        val pkg = context.packageName
        val sha256 = readSigningDigest(pm, pkg)
        val installer = readInstallerPackage(pm, pkg)
        val debuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        val hooked = detectHooks()
        return AppIntegrityReport(
            isSignaturePresent = sha256.isNotBlank(),
            signatureSha256 = sha256,
            installerPackage = installer,
            isTrustedInstaller = installer != null && installer in trustedInstallers,
            isDebuggableFlagSet = debuggable,
            hasHookingArtifacts = hooked
        )
    }

    @Suppress("DEPRECATION")
    private fun readSigningDigest(pm: PackageManager, pkg: String): String = runCatching {
        val sigBytes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val info = pm.getPackageInfo(pkg, PackageManager.GET_SIGNING_CERTIFICATES)
            info.signingInfo?.apkContentsSigners?.firstOrNull()?.toByteArray()
        } else {
            val info = pm.getPackageInfo(pkg, PackageManager.GET_SIGNATURES)
            info.signatures?.firstOrNull()?.toByteArray()
        } ?: return@runCatching ""
        MessageDigest.getInstance("SHA-256").digest(sigBytes)
            .joinToString(":") { "%02X".format(it) }
    }.getOrDefault("")

    @Suppress("DEPRECATION")
    private fun readInstallerPackage(pm: PackageManager, pkg: String): String? = runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            pm.getInstallSourceInfo(pkg).installingPackageName
        } else {
            pm.getInstallerPackageName(pkg)
        }
    }.getOrNull()

    private fun detectHooks(): Boolean {
        if (hookPaths.any { File(it).exists() }) return true
        return hookClasses.any { cls -> runCatching { Class.forName(cls) }.isSuccess }
    }
}
