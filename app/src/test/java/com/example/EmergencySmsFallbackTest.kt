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
class EmergencySmsFallbackTest {

    @Test
    fun testFormatEmergencySmsWithGpsCoordinates() {
        val sms = EmergencySmsFallbackHelper.formatEmergencySms(
            driverName = "Budi Santoso",
            plateNumber = "N 5678 CD",
            type = EmergencyType.MOGOK,
            locationNote = "Depan Gerbang Tol Singosari",
            lat = -7.8921,
            lng = 112.6644
        )

        assertTrue(sms.contains("[SOS DRG]"))
        assertTrue(sms.contains("Budi Santoso"))
        assertTrue(sms.contains("N 5678 CD"))
        assertTrue(sms.contains(EmergencyType.MOGOK.label))
        assertTrue(sms.contains("https://maps.google.com/?q=-7.8921,112.6644"))
    }

    @Test
    fun testFormatEmergencySmsWithoutGpsCoordinates() {
        val sms = EmergencySmsFallbackHelper.formatEmergencySms(
            driverName = "Ahmad",
            plateNumber = "N 1234 AB",
            type = EmergencyType.BEGAL,
            locationNote = "Jalur Hutan Pakis",
            lat = 0.0,
            lng = 0.0
        )

        assertTrue(sms.contains("GPS: Nonaktif"))
        assertTrue(sms.contains("Jalur Hutan Pakis"))
    }

    @Test
    fun testCreateSmsIntentProperties() {
        val intent = EmergencySmsFallbackHelper.createSmsIntent(
            phoneNumber = "0812-3456-7890",
            message = "Test SOS SMS Body"
        )

        assertEquals("smsto:081234567890", intent.dataString)
        assertEquals("Test SOS SMS Body", intent.getStringExtra("sms_body"))
    }
}
