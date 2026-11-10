package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.rules.TemporaryFolder
import com.example.core.backup.BackupCrypto
import com.example.core.database.AppDatabase
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import com.example.core.database.driverMemberDao
import java.io.File
import java.io.IOException
import java.nio.charset.Charsets

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BackupManagerTest {

    @get:Rule
    val tmpDir = TemporaryFolder()

    private val context: Context = ApplicationProvider.getApplicationContext()

    // Use in-memory Room DB per test to avoid Robolectric shadow FS issues
    private lateinit var db: AppDatabase
    private lateinit var dao: AppDatabase.DriverMemberDao

    @Before
    fun setupDb() {
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .fallbackToDestructiveMigration()
            .build()
        dao = db.memberDao()
        runBlocking { dao.deleteAllMembers() }
    }

    @After
    fun cleanupDb() {
        AppDatabase.closeInstance()
        db.close()
    }

    private fun sampleMember(id: String = "B1") = DriverMember(
        id = id, name = "Backup Test", driverId = "DRV-$id", phone = "0811",
        role = MemberRole.ANGGOTA, motorcyclePlate = "AB9999ZZ", motorcycleModel = "Honda"
    )

    @Test
    fun exportCreatesNonEmptyFile() {
        runBlocking { dao.insertMember(sampleMember()) }
        val target = File(context.cacheDir, "backup_test.bak")
        target.delete()
        val res = BackupManager.export(context, target)
        assertTrue("export sukses: ${res.exceptionOrNull()}", res.isSuccess)
        assertTrue("file ada", target.exists())
        assertTrue("size>0", target.length() > 0)
        target.delete()
    }

    @Test
    fun importRestoresData() {
        runBlocking { dao.insertMember(sampleMember("IMP1")) }
        val target = File(context.cacheDir, "backup_imp.bak")
        target.delete()
        BackupManager.export(context, target)

        // Hapus member dari DB (simulasi data hilang)
        runBlocking { dao.deleteMember(sampleMember("IMP1")) }

        // Verifikasi member sudah terhapus dari DB
        val before = runBlocking { dao.getMemberById("IMP1") }
        assertTrue("terhapus dari DB sebelum import", before == null)

        // Import kembalikan member ke DB
        val imp = BackupManager.import(context, target)
        assertTrue("import sukses: ${imp.exceptionOrNull()}", imp.isSuccess)

        // Verifikasi member kembali ke DB
        val after = runBlocking { dao.getMemberById("IMP1") }
        assertTrue("ter-restore ke DB", after != null)
        target.delete()
    }

    @Test
    fun importRejectsInvalidFile() {
        val bogus = File(context.cacheDir, "bogus.bak")
        bogus.writeText("ini bukan database")
        val res = BackupManager.import(context, bogus)
        assertFalse("invalid ditolak", res.isSuccess)
        bogus.delete()
    }

    @Test
    fun importRejectsMissingFile() {
        val missing = File(context.cacheDir, "nope.bak")
        missing.delete()
        val res = BackupManager.import(context, missing)
        assertFalse("missing ditolak", res.isSuccess)
    }
}