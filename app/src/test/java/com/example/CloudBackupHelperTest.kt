package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.core.backup.BackupCrypto
import com.example.core.backup.BackupManager
import com.example.core.backup.CloudBackupHelper
import com.example.core.firebase.FirestoreBackupSyncRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CloudBackupHelperTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(
                context,
                FirebaseOptions.Builder()
                    .setApplicationId("1:123456:android:abcdef")
                    .setApiKey("test-api-key")
                    .setProjectId("demo-drg-test")
                    .build()
            )
        }
    }

    @Test
    fun backupCrypto_isEncryptedDistinguishesCipherFormatFromPlaintext() {
        assertFalse(BackupCrypto.isEncrypted("plaintext json".toByteArray()))
        assertFalse(BackupCrypto.isEncrypted("SQLite format 3".toByteArray()))
        assertTrue(BackupCrypto.isEncrypted("1234567890123456:abcdefghijklmnopqrstuvwxyz".toByteArray()))
    }

    @Test
    fun uploadEncryptedDatabase_rejectsUnencryptedExportWithoutUploadingPlaintext() = runTest {
        val dbFile = context.getDatabasePath("drg_community.db")
        dbFile.parentFile?.mkdirs()
        dbFile.writeBytes("SQLite format 3\u0000plaintext-sensitive-pii".toByteArray())

        val app = FirebaseApp.getInstance()
        val repo = FirestoreBackupSyncRepository(
            db = FirebaseFirestore.getInstance(app, "(default)"),
            auth = FirebaseAuth.getInstance(app)
        )

        val result = CloudBackupHelper.uploadEncryptedDatabase(context, repo, "test_backup")
        assertTrue("Expected failure when Keystore encryption is unavailable", result.isFailure)
        val message = result.exceptionOrNull()?.message.orEmpty()
        assertTrue(
            "Expected security rejection message but got: $message",
            message.contains("Keystore", ignoreCase = true)
        )
        assertFalse(File(context.cacheDir, "drg_cloud_export.bak").exists())
    }
}
