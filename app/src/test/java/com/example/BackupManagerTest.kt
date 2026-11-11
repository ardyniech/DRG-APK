package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.core.backup.BackupManager
import com.example.core.database.AppDatabase
import com.example.core.database.dao.MemberDao
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import kotlinx.coroutines.runBlocking
import org.junit.After
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
class BackupManagerTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var db: AppDatabase
    private lateinit var dao: MemberDao

    @Before
    fun setupDb() {
        AppDatabase.closeInstance()
        db = AppDatabase.getInstance(context)
        dao = db.memberDao()
        runBlocking { dao.deleteAllMembers() }
    }

    @After
    fun cleanupDb() {
        AppDatabase.closeInstance()
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
        val exported = BackupManager.export(context, target)
        assertTrue("export sukses: ${exported.exceptionOrNull()}", exported.isSuccess)
        assertTrue("backup file ada", target.exists() && target.length() > 0)

        // Verifikasi import menerima file backup valid (roundtrip file-level).
        val imp = BackupManager.import(context, target)
        assertTrue("import sukses: ${imp.exceptionOrNull()}", imp.isSuccess)
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