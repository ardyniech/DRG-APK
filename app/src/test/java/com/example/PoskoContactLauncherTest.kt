package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.shared.models.PoskoLocation
import com.example.shared.utils.PoskoContactLauncher
import com.example.shared.utils.PoskoCoordinationReason
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PoskoContactLauncherTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testPoskoCoordinationTemplates() {
        val testPosko = PoskoLocation(
            id = "POSKO-01",
            name = "Posko Alun-Alun",
            area = "Klojen",
            address = "Jl. Merdeka Barat No. 5",
            lat = -7.9822,
            lng = 112.6303,
            coordinatorName = "Hadi Santoso",
            phone = "081234567899",
            facilities = "Kopi, Cas HP, Musholla",
            isMainPosko = true
        )

        val checkinMsg = PoskoCoordinationReason.CHECKIN_REST.template(testPosko)
        assertTrue(checkinMsg.contains("Hadi Santoso"))
        assertTrue(checkinMsg.contains("Posko Alun-Alun"))

        val facilityMsg = PoskoCoordinationReason.FACILITY_INQUIRY.template(testPosko)
        assertTrue(facilityMsg.contains("ketersediaan tempat istirahat"))

        val emergencyMsg = PoskoCoordinationReason.EMERGENCY_SUPPORT.template(testPosko)
        assertTrue(emergencyMsg.contains("LOGISTIK POSKO DRG"))
    }

    @Test
    fun testOpenPoskoWhatsAppSuccess() {
        val testPosko = PoskoLocation(
            id = "POSKO-02",
            name = "Posko Soekarno Hatta",
            area = "Lowokwaru",
            address = "Jl. Soekarno Hatta No. 12",
            lat = -7.9450,
            lng = 112.6150,
            coordinatorName = "Rahmat Hidayat",
            phone = "081398765432",
            facilities = "Kopi, Wifi, Tempat Rehat"
        )

        val result = PoskoContactLauncher.openPoskoWhatsApp(context, testPosko, PoskoCoordinationReason.CHECKIN_REST)
        assertTrue("Posko WhatsApp intent launch should succeed", result)
    }

    @Test
    fun testOpenPoskoDialerSuccess() {
        val testPosko = PoskoLocation(
            id = "POSKO-02",
            name = "Posko Soekarno Hatta",
            area = "Lowokwaru",
            address = "Jl. Soekarno Hatta No. 12",
            lat = -7.9450,
            lng = 112.6150,
            coordinatorName = "Rahmat Hidayat",
            phone = "081398765432",
            facilities = "Kopi, Wifi, Tempat Rehat"
        )

        val result = PoskoContactLauncher.openPoskoDialer(context, testPosko)
        assertTrue("Posko dialer intent launch should succeed", result)
    }

    @Test
    fun testPoskoContactWithEmptyPhone() {
        val poskoNoPhone = PoskoLocation(
            id = "POSKO-03",
            name = "Posko Cadangan",
            area = "Sukun",
            address = "Jl. S. Supriadi",
            lat = -8.0010,
            lng = 112.6200,
            coordinatorName = "Pengurus Sementara",
            phone = "",
            facilities = "Tempat Parkir"
        )

        val waResult = PoskoContactLauncher.openPoskoWhatsApp(context, poskoNoPhone)
        assertFalse("Posko WA should fail gracefully when phone is empty", waResult)

        val dialerResult = PoskoContactLauncher.openPoskoDialer(context, poskoNoPhone)
        assertFalse("Posko dialer should fail gracefully when phone is empty", dialerResult)
    }
}
