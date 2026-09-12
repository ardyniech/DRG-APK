package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.utils.CoordinationReason
import com.example.shared.utils.DriverContactLauncher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DriverContactInteractionTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testPhoneSanitizationAndFormatting() {
        val phoneLocal = "0812-3456-7890"
        val formatted = DriverContactLauncher.formatPhoneNumberForWhatsApp(phoneLocal)
        assertEquals("6281234567890", formatted)

        val phoneIntl = "+62 813 9999 1111"
        val formattedIntl = DriverContactLauncher.formatPhoneNumberForWhatsApp(phoneIntl)
        assertEquals("6281399991111", formattedIntl)

        val phoneStandard = "6285712345678"
        val formattedStandard = DriverContactLauncher.formatPhoneNumberForWhatsApp(phoneStandard)
        assertEquals("6285712345678", formattedStandard)
    }

    @Test
    fun testBuildWhatsAppUriStructure() {
        val uri = DriverContactLauncher.buildWhatsAppUri("6281234567890", "Halo Cak Budi")
        assertEquals("https", uri.scheme)
        assertEquals("wa.me", uri.host)
        assertTrue(uri.toString().contains("6281234567890"))
        assertTrue(uri.toString().contains("text="))
    }

    @Test
    fun testCoordinationReasonTemplates() {
        val testDriver = DriverMember(
            id = "DRV-101",
            name = "Budi Utomo",
            driverId = "DRG-MLG-001",
            phone = "081234567890",
            role = MemberRole.ANGGOTA,
            motorcyclePlate = "N 1234 AB",
            motorcycleModel = "Honda Vario 160"
        )

        val routineMsg = CoordinationReason.ROUTINE.template(testDriver)
        assertTrue(routineMsg.contains("Budi Utomo"))
        assertTrue(routineMsg.contains("DRG-MLG-001"))

        val satgasMsg = CoordinationReason.SATGAS_ALERT.template(testDriver)
        assertTrue(satgasMsg.contains("SIAGA SATGAS DRG"))

        val roadMsg = CoordinationReason.ROAD_ASSISTANCE.template(testDriver)
        assertTrue(roadMsg.contains("Bantuan Kendaraan"))
    }

    @Test
    fun testOpenDriverWhatsAppWithValidDriver() {
        val testDriver = DriverMember(
            id = "DRV-101",
            name = "Budi Utomo",
            driverId = "DRG-MLG-001",
            phone = "081234567890",
            role = MemberRole.ANGGOTA,
            motorcyclePlate = "N 1234 AB",
            motorcycleModel = "Honda Vario 160"
        )

        val result = DriverContactLauncher.openDriverWhatsApp(context, testDriver, CoordinationReason.ROUTINE)
        assertTrue("Driver WhatsApp interaction should succeed in Robolectric test", result)
    }

    @Test
    fun testOpenDriverDialerWithValidDriver() {
        val testDriver = DriverMember(
            id = "DRV-101",
            name = "Budi Utomo",
            driverId = "DRG-MLG-001",
            phone = "081234567890",
            role = MemberRole.ANGGOTA,
            motorcyclePlate = "N 1234 AB",
            motorcycleModel = "Honda Vario 160"
        )

        val result = DriverContactLauncher.openDriverDialer(context, testDriver)
        assertTrue("Driver dialer interaction should succeed", result)
    }

    @Test
    fun testOpenEmergencyWhatsAppWithValidDriver() {
        val testDriver = DriverMember(
            id = "DRV-102",
            name = "Agus Santoso",
            driverId = "DRG-MLG-002",
            phone = "081234567890",
            emergencyPhone = "081398765432",
            role = MemberRole.SATGAS,
            motorcyclePlate = "N 5678 CD",
            motorcycleModel = "Yamaha NMAX"
        )

        val result = DriverContactLauncher.openEmergencyWhatsApp(context, testDriver, "Bantuan Darurat")
        assertTrue("Emergency WhatsApp interaction should succeed", result)
    }

    @Test
    fun testDriverContactWithEmptyPhone() {
        val driverNoPhone = DriverMember(
            id = "DRV-103",
            name = "Driver Tanpa HP",
            driverId = "DRG-MLG-003",
            phone = "",
            emergencyPhone = "",
            role = MemberRole.ANGGOTA,
            motorcyclePlate = "N 9999 XX",
            motorcycleModel = "Honda Beat"
        )

        val result = DriverContactLauncher.openDriverWhatsApp(context, driverNoPhone)
        assertFalse("Should fail gracefully when driver phone is empty", result)

        val dialerResult = DriverContactLauncher.openDriverDialer(context, driverNoPhone)
        assertFalse("Dialer should fail gracefully when phone is empty", dialerResult)
    }
}
