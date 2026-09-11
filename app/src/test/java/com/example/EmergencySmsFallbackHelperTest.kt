package com.example

import com.example.core.sync.EmergencySmsFallbackHelper
import com.example.shared.models.EmergencyType
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class EmergencySmsFallbackHelperTest {

    @Test
    fun testFormatEmergencySmsWithGps() {
        val sms = EmergencySmsFallbackHelper.formatEmergencySms(
            driverName = "Budi Hartono",
            plateNumber = "N 1234 AB",
            type = EmergencyType.BEGAL,
            locationNote = "Dekat flyover Arjosari",
            lat = -7.9351,
            lng = 112.6578
        )

        assertTrue(sms.contains("[SOS DRG]"))
        assertTrue(sms.contains("Budi Hartono"))
        assertTrue(sms.contains("N 1234 AB"))
        assertTrue(sms.contains("https://maps.google.com/?q=-7.9351,112.6578"))
    }

    @Test
    fun testFormatEmergencySmsWithoutGps() {
        val sms = EmergencySmsFallbackHelper.formatEmergencySms(
            driverName = "Agus Santoso",
            plateNumber = "N 5678 CD",
            type = EmergencyType.MOGOK,
            locationNote = "Jalan Soekarno Hatta",
            lat = 0.0,
            lng = 0.0
        )

        assertTrue(sms.contains("GPS: Nonaktif"))
        assertTrue(sms.contains("Agus Santoso"))
    }

    @Test
    fun testCreateSmsIntent() {
        val intent = EmergencySmsFallbackHelper.createSmsIntent(
            phoneNumber = "0812-3456-7890",
            message = "Test SOS message"
        )

        assertEquals("smsto:081234567890", intent.dataString)
        assertEquals("Test SOS message", intent.getStringExtra("sms_body"))
    }
}
