package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.core.viewmodel.coordinators.PendingRegistrationDraft
import com.example.core.viewmodel.coordinators.PendingRegistrationPrefs
import com.example.modules.auth.AuthStage
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class UnifiedGoogleAuthCleanupTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun `auth stage excludes legacy local login stage`() {
        val stages = AuthStage.entries.map { it.name }
        assertFalse("Legacy LOGIN stage must remain removed", stages.contains("LOGIN"))
        assertEquals(listOf("LANDING", "REGISTER", "ABOUT"), stages)
    }

    @Test
    fun `pending registration draft is saved and consumed once`() {
        val prefs = context.getSharedPreferences("test_pending_reg_${System.nanoTime()}", Context.MODE_PRIVATE)
        val draft = PendingRegistrationDraft(
            draftId = "DRG-TEST-1",
            name = "Budi Malang",
            phone = "081234567899",
            plate = "N 4321 AB",
            model = "Honda Beat",
            area = "Klojen, Malang"
        )
        PendingRegistrationPrefs.save(prefs, draft)
        val consumed = PendingRegistrationPrefs.consume(prefs)
        assertEquals(draft, consumed)
        assertNull("Draft must be cleared after consume", PendingRegistrationPrefs.consume(prefs))
    }

    @Test
    fun `negative bad input and null prefs handled safely`() {
        assertNull(PendingRegistrationPrefs.consume(null))
        val prefs = context.getSharedPreferences("test_blank_reg_${System.nanoTime()}", Context.MODE_PRIVATE)
        PendingRegistrationPrefs.save(prefs, PendingRegistrationDraft("", "   ", "", "", "", ""))
        assertNull("Blank name draft must be ignored", PendingRegistrationPrefs.consume(prefs))
    }

    @Test
    fun `pending member has zero hardcoded dummy pii`() {
        val member = PendingRegistrationPrefs.buildPendingMember(
            draftId = "DRG-X",
            driverId = "DRG-REG-X",
            name = "Surya Driver",
            phone = "081999888777",
            plate = "N 9999 ZZ",
            model = "Yamaha NMax",
            area = "Lowokwaru, Malang"
        )
        assertEquals(MemberRole.ANGGOTA, member.role)
        assertEquals(VerificationStatus.PENDING_SCREENING, member.verificationStatus)
        assertEquals("", member.nik)
        assertEquals("", member.simNumber)
        assertEquals("", member.emergencyPhone)
    }
}
