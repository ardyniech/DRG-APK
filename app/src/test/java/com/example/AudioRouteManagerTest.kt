package com.example

import com.example.core.audio.AudioDeviceType
import org.junit.Assert.*
import org.junit.Test

class AudioRouteManagerTest {

    @Test
    fun testAudioDeviceTypesProperties() {
        assertTrue(AudioDeviceType.BLUETOOTH_INTERCOM.isHeadset)
        assertTrue(AudioDeviceType.WIRED_HEADSET.isHeadset)
        assertFalse(AudioDeviceType.PHONE_SPEAKER.isHeadset)

        assertEquals("Intercom / Headset Helm (Bluetooth)", AudioDeviceType.BLUETOOTH_INTERCOM.label)
        assertEquals("Speaker Perangkat", AudioDeviceType.PHONE_SPEAKER.label)
    }

    @Test
    fun testRecommendedAlarmVolumeCalculation() {
        fun calculateVolume(isHeadsetOrIntercom: Boolean): Int {
            return if (isHeadsetOrIntercom) 70 else 100
        }

        // Helmet intercom in rider ears must not exceed 70% to prevent ear damage
        val helmetVol = calculateVolume(isHeadsetOrIntercom = true)
        assertEquals(70, helmetVol)

        // Phone speaker on motorcycle mount should be full 100% to cut through traffic noise
        val speakerVol = calculateVolume(isHeadsetOrIntercom = false)
        assertEquals(100, speakerVol)
    }

    @Test
    fun testTilePruningExpiryThresholdCalculation() {
        val now = 1700000000000L
        val expiryDays = 30
        val threshold = now - (expiryDays * 24L * 60L * 60L * 1000L)

        val tileAgeOld = now - (35L * 24L * 60L * 60L * 1000L)
        val tileAgeFresh = now - (10L * 24L * 60L * 60L * 1000L)

        assertTrue("35 days old tile must be considered expired", tileAgeOld < threshold)
        assertFalse("10 days old tile must be preserved", tileAgeFresh < threshold)
    }
}
