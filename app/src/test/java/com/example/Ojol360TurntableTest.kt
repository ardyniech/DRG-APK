package com.example

import com.example.shared.atoms.cinematic.turntable.DEFAULT_TURNTABLE_ZOOM
import com.example.shared.atoms.cinematic.turntable.MAX_TURNTABLE_ZOOM
import com.example.shared.atoms.cinematic.turntable.MIN_TURNTABLE_ZOOM
import com.example.shared.atoms.cinematic.turntable.angleToFrameIndex
import com.example.shared.atoms.cinematic.turntable.clampTurntableZoom
import com.example.shared.atoms.cinematic.turntable.normalizeTurntableAngle
import com.example.shared.atoms.cinematic.turntable.turntableQuadrantLabel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class Ojol360TurntableTest {

    @Test
    fun `normalizeTurntableAngle correctly wraps angle between 0 and 360`() {
        assertEquals(0f, normalizeTurntableAngle(0f), 0.01f)
        assertEquals(180f, normalizeTurntableAngle(180f), 0.01f)
        assertEquals(0f, normalizeTurntableAngle(360f), 0.01f)
        assertEquals(45f, normalizeTurntableAngle(405f), 0.01f)
        assertEquals(270f, normalizeTurntableAngle(-90f), 0.01f)
    }

    @Test
    fun `angleToFrameIndex accurately maps 16 turntable frames across 360 degrees`() {
        // Frame 0 is at 0 degrees
        assertEquals(0, angleToFrameIndex(0f))
        assertEquals(0, angleToFrameIndex(355f))

        // Frame 4 is around 90 degrees (4 * 22.5 = 90)
        assertEquals(4, angleToFrameIndex(90f))

        // Frame 8 is around 180 degrees (8 * 22.5 = 180)
        assertEquals(8, angleToFrameIndex(180f))

        // Frame 12 is around 270 degrees (12 * 22.5 = 270)
        assertEquals(12, angleToFrameIndex(270f))
    }

    @Test
    fun `clampTurntableZoom preserves scale within bounds`() {
        assertEquals(DEFAULT_TURNTABLE_ZOOM, clampTurntableZoom(1.0f), 0.01f)
        assertEquals(MIN_TURNTABLE_ZOOM, clampTurntableZoom(0.2f), 0.01f)
        assertEquals(MAX_TURNTABLE_ZOOM, clampTurntableZoom(5.0f), 0.01f)
    }

    @Test
    fun `turntableQuadrantLabel correctly identifies four viewpoints`() {
        assertTrue(turntableQuadrantLabel(0f).contains("Depan"))
        assertTrue(turntableQuadrantLabel(90f).contains("Kiri"))
        assertTrue(turntableQuadrantLabel(180f).contains("Belakang"))
        assertTrue(turntableQuadrantLabel(270f).contains("Kanan"))
    }

    @Test
    fun `adversarial bad input values are safely sanitized`() {
        assertEquals(0f, normalizeTurntableAngle(Float.NaN), 0.01f)
        assertEquals(0f, normalizeTurntableAngle(Float.POSITIVE_INFINITY), 0.01f)

        assertEquals(DEFAULT_TURNTABLE_ZOOM, clampTurntableZoom(Float.NaN), 0.01f)
        assertEquals(DEFAULT_TURNTABLE_ZOOM, clampTurntableZoom(Float.POSITIVE_INFINITY), 0.01f)

        assertEquals(0, angleToFrameIndex(Float.NaN))
    }
}
