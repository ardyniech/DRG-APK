package com.example

import com.example.shared.atoms.cinematic.ojol360.DEFAULT_ZOOM
import com.example.shared.atoms.cinematic.ojol360.MAX_ZOOM
import com.example.shared.atoms.cinematic.ojol360.MIN_ZOOM
import com.example.shared.atoms.cinematic.ojol360.calcPerspectiveScale
import com.example.shared.atoms.cinematic.ojol360.calcYawCos
import com.example.shared.atoms.cinematic.ojol360.calcYawSin
import com.example.shared.atoms.cinematic.ojol360.clampZoom
import com.example.shared.atoms.cinematic.ojol360.normalizeAngle
import com.example.shared.atoms.cinematic.ojol360.quadrantLabel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CinematicOjol360Test {

    @Test
    fun `normalizeAngle correctly bounds yaw to 0 until 360`() {
        assertEquals(0f, normalizeAngle(0f), 0.01f)
        assertEquals(180f, normalizeAngle(180f), 0.01f)
        assertEquals(0f, normalizeAngle(360f), 0.01f)
        assertEquals(90f, normalizeAngle(450f), 0.01f)
        assertEquals(270f, normalizeAngle(-90f), 0.01f)
        assertEquals(180f, normalizeAngle(-540f), 0.01f)
    }

    @Test
    fun `clampZoom preserves valid zoom and clamps extremities`() {
        assertEquals(DEFAULT_ZOOM, clampZoom(1.0f), 0.01f)
        assertEquals(MIN_ZOOM, clampZoom(0.1f), 0.01f)
        assertEquals(MAX_ZOOM, clampZoom(10.0f), 0.01f)
        assertEquals(1.75f, clampZoom(1.75f), 0.01f)
    }

    @Test
    fun `quadrantLabel accurately identifies four camera cardinal angles`() {
        assertTrue(quadrantLabel(0f).contains("Belakang"))
        assertTrue(quadrantLabel(350f).contains("Belakang"))
        assertTrue(quadrantLabel(90f).contains("Kanan"))
        assertTrue(quadrantLabel(180f).contains("Depan"))
        assertTrue(quadrantLabel(270f).contains("Kiri"))
    }

    @Test
    fun `perspective trigonometric calculations are smooth and non-inverted`() {
        // cos at 0 is 1.0, at 180 is -1.0, at 90 is ~0.0
        assertEquals(1.0f, calcYawCos(0f), 0.01f)
        assertEquals(-1.0f, calcYawCos(180f), 0.01f)
        assertEquals(0.0f, calcYawCos(90f), 0.01f)

        // sin at 90 is 1.0, at 270 is -1.0
        assertEquals(1.0f, calcYawSin(90f), 0.01f)
        assertEquals(-1.0f, calcYawSin(270f), 0.01f)

        // Perspective scale scales with depth factor
        val rearScale = calcPerspectiveScale(0f, 1.0f)
        val frontScale = calcPerspectiveScale(180f, 1.0f)
        assertTrue(rearScale > frontScale)
    }

    @Test
    fun `adversarial bad input values are safely sanitized`() {
        // NaN & Infinite inputs should fallback safely
        assertEquals(0f, normalizeAngle(Float.NaN), 0.01f)
        assertEquals(0f, normalizeAngle(Float.POSITIVE_INFINITY), 0.01f)
        assertEquals(0f, normalizeAngle(Float.NEGATIVE_INFINITY), 0.01f)

        assertEquals(DEFAULT_ZOOM, clampZoom(Float.NaN), 0.01f)
        assertEquals(DEFAULT_ZOOM, clampZoom(Float.POSITIVE_INFINITY), 0.01f)
        assertEquals(DEFAULT_ZOOM, clampZoom(Float.NEGATIVE_INFINITY), 0.01f)
    }
}
