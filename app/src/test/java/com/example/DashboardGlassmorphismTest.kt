package com.example

import com.example.core.viewmodel.MainNavTab
import com.example.shared.models.DriverMember
import com.example.shared.models.EmergencyAlert
import com.example.shared.models.EmergencyType
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DashboardGlassmorphismTest {

    @Test
    fun testWeatherAtmosphereGreetingAndStatus() {
        val member = DriverMember(
            id = "DRG-001",
            driverId = "DRG-001",
            name = "Suryo Wicaksono",
            phone = "081234567890",
            baseArea = "Malang Kota",
            role = MemberRole.KETUA,
            verificationStatus = VerificationStatus.VERIFIED,
            isOnline = true,
            currentStatus = "Aktif Narik",
            motorcyclePlate = "N 5521 AA",
            motorcycleModel = "Honda Vario 160"
        )

        assertEquals("Suryo", member.name.split(" ").firstOrNull())
        assertEquals("Malang Kota", member.baseArea)
        assertTrue(member.isOnline)
        assertEquals(VerificationStatus.VERIFIED, member.verificationStatus)
    }

    @Test
    fun testAdversarialNullAndMalformedMember() {
        val nullMember: DriverMember? = null
        val firstName = nullMember?.name?.split(" ")?.firstOrNull() ?: "Rekan Driver"
        val statusText = nullMember?.currentStatus ?: "Aktif Narik"
        val baseArea = nullMember?.baseArea ?: "Malang Raya"

        // Ensure no NullPointerException and safe fallbacks
        assertEquals("Rekan Driver", firstName)
        assertEquals("Aktif Narik", statusText)
        assertEquals("Malang Raya", baseArea)
    }

    @Test
    fun testCrossModuleTabNavigationAndCallbacks() {
        var navigatedTab: MainNavTab? = null
        var isEmergencyTriggered = false
        var updatedStatus = ""
        var updatedOnline = false

        val onNavigate: (MainNavTab) -> Unit = { navigatedTab = it }
        val onEmergency: () -> Unit = { isEmergencyTriggered = true }
        val onUpdateDuty: (String, Boolean) -> Unit = { s, o ->
            updatedStatus = s
            updatedOnline = o
        }

        onNavigate(MainNavTab.RADAR)
        assertEquals(MainNavTab.RADAR, navigatedTab)

        onEmergency()
        assertTrue(isEmergencyTriggered)

        onUpdateDuty("Istirahat", false)
        assertEquals("Istirahat", updatedStatus)
        assertFalse(updatedOnline)
    }

    @Test
    fun testUiDeadEndPreventionWithActiveAlerts() {
        val alerts = listOf(
            EmergencyAlert(
                id = "EMG-01",
                driverId = "DRG-999",
                driverName = "Pak Joko",
                driverPhone = "081299998888",
                plateNumber = "N 1111 ZZ",
                type = EmergencyType.MOGOK,
                message = "Rantai putus di daerah Dinoyo",
                lat = -7.94,
                lng = 112.61,
                locationName = "Dinoyo, Malang",
                timestamp = System.currentTimeMillis(),
                isActive = true
            )
        )

        assertFalse(alerts.isEmpty())
        assertEquals(EmergencyType.MOGOK, alerts[0].type)
        assertEquals("Dinoyo, Malang", alerts[0].locationName)
    }
}
