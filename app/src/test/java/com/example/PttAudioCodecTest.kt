package com.example

import com.example.core.audio.PttAudioCodec
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class PttAudioCodecTest {

    @Test
    fun testMulawRoundTripCodec() {
        val originalSample: Short = 1234
        val mulaw = PttAudioCodec.linearToMulaw(originalSample)
        val decoded = PttAudioCodec.mulawToLinear(mulaw)
        // G.711 mu-law is lossy, but decoded sample should be reasonably close
        assertTrue(kotlin.math.abs(originalSample - decoded) < 1500)
    }

    @Test
    fun testBase64EncodeDecodeRoundTrip() {
        val pcm = ShortArray(160) { (it * 100).toShort() }
        val base64 = PttAudioCodec.encodePcmToBase64(pcm)
        assertNotNull(base64)
        assertTrue(base64.isNotBlank())

        val decodedPcm = PttAudioCodec.decodeBase64ToPcm(base64)
        assertEquals(pcm.size, decodedPcm.size)
    }

    @Test
    fun testMalformedBase64Fallback() {
        val decoded = PttAudioCodec.decodeBase64ToPcm("not-valid-base64!!!")
        assertNotNull(decoded)
        assertEquals(0, decoded.size)
    }

    @Test
    fun testRmsAndDurationCalculation() {
        val silentPcm = ShortArray(800) { 0 }
        val rms = PttAudioCodec.computeNormalizedRms(silentPcm)
        assertEquals(0f, rms, 0.001f)

        val duration = PttAudioCodec.estimateDurationMs(8000)
        assertEquals(1000L, duration)
    }
}
