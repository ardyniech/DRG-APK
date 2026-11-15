package com.example.core.audio

import android.util.Base64
import kotlin.math.sqrt

/** ITU-T G.711 mu-law 8kHz audio codec for bandwidth-efficient PTT transmissions. */
object PttAudioCodec {
    const val SAMPLE_RATE_HZ = 8000
    const val MAX_DURATION_MS = 12_000L
    const val MIN_DURATION_MS = 150L
    const val MAX_BASE64_LENGTH = 175_000
    private const val BIAS = 0x84
    private const val CLIP = 32635

    fun linearToMulaw(sample: Short): Byte {
        var pcm = sample.toInt()
        val sign = if (pcm < 0) { pcm = -pcm; 0x80 } else 0
        if (pcm > CLIP) pcm = CLIP
        pcm += BIAS
        var exponent = 7
        var expMask = 0x4000
        while ((pcm and expMask) == 0 && exponent > 0) {
            exponent--; expMask = expMask shr 1
        }
        val mantissa = (pcm shr (exponent + 3)) and 0x0F
        return (sign or (exponent shl 4) or mantissa).inv().toByte()
    }

    fun mulawToLinear(mulaw: Byte): Short {
        val inv = mulaw.toInt().inv() and 0xFF
        val sign = inv and 0x80
        val exponent = (inv shr 4) and 0x07
        val mantissa = inv and 0x0F
        var sample = ((mantissa shl 3) + BIAS) shl exponent
        sample -= BIAS
        return (if (sign != 0) -sample else sample).toShort()
    }

    fun encodePcmToBase64(pcm: ShortArray, sampleCount: Int = pcm.size): String {
        val count = sampleCount.coerceIn(0, pcm.size)
        if (count == 0) return ""
        val encoded = ByteArray(count) { idx -> linearToMulaw(pcm[idx]) }
        val base64 = Base64.encodeToString(encoded, Base64.NO_WRAP)
        return if (base64.length <= MAX_BASE64_LENGTH) base64 else base64.take(MAX_BASE64_LENGTH)
    }

    fun decodeBase64ToPcm(base64: String): ShortArray {
        if (base64.isBlank()) return ShortArray(0)
        return try {
            val bytes = Base64.decode(base64.trim(), Base64.DEFAULT)
            ShortArray(bytes.size) { idx -> mulawToLinear(bytes[idx]) }
        } catch (_: IllegalArgumentException) {
            ShortArray(0)
        }
    }

    fun computeNormalizedRms(pcm: ShortArray, count: Int = pcm.size): Float {
        val safeCount = count.coerceIn(0, pcm.size)
        if (safeCount == 0) return 0f
        var sumSq = 0.0
        for (i in 0 until safeCount) {
            val v = pcm[i].toDouble()
            sumSq += v * v
        }
        val rms = sqrt(sumSq / safeCount)
        return (rms / 8000.0).toFloat().coerceIn(0f, 1f)
    }

    fun estimateDurationMs(sampleCount: Int): Long =
        ((sampleCount.toLong() * 1000L) / SAMPLE_RATE_HZ).coerceAtLeast(0L)
}
