package com.example.core.update

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.BuildConfig
import com.example.MainActivity

/**
 * Cek update di background (WorkManager) dan beri notifikasi kalau ada versi baru.
 * Hemat baterai: dijalankan periodik dengan constraint jaringan.
 */
class UpdateCheckWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val context = applicationContext
        val current = UpdateChecker.currentVersionCode(context)
        val release = UpdateChecker.fetchManifest(BuildConfig.UPDATE_MANIFEST_URL) ?: return Result.success()
        if (release.versionCode <= current) return Result.success()

        val patch = release.patchFor(current, UpdateChecker.installedApkSha256(context))
        val sizeKb = (patch?.sizeBytes ?: release.apkSizeBytes) / 1024
        postNotification(context, release.versionName, sizeKb)
        return Result.success()
    }

    private fun postNotification(context: Context, versionName: String, sizeKb: Long) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Pembaruan Aplikasi", NotificationManager.IMPORTANCE_DEFAULT)
            nm.createNotificationChannel(channel)
        }
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle("Update DRG Driver v$versionName tersedia")
            .setContentText("Patch ~${sizeKb} KB (hemat kuota). Ketuk untuk buka.")
            .setAutoCancel(true)
            .setContentIntent(pi)
            .build()
        nm.notify(NOTIF_ID, notif)
    }

    companion object {
        const val CHANNEL_ID = "drg_update"
        const val NOTIF_ID = 4242
        const val WORK_NAME = "drg_update_check"
    }
}
