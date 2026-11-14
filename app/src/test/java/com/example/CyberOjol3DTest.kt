package com.example

import com.example.shared.atoms.cinematic.cyberojol.CameraPreset
import com.example.shared.atoms.cinematic.cyberojol.CameraTransform
import com.example.shared.atoms.cinematic.cyberojol.MAX_CAM_ZOOM
import com.example.shared.atoms.cinematic.cyberojol.MAX_PITCH
import com.example.shared.atoms.cinematic.cyberojol.MIN_CAM_ZOOM
import com.example.shared.atoms.cinematic.cyberojol.MIN_PITCH
import com.example.shared.atoms.cinematic.cyberojol.Vec3D
import com.example.shared.atoms.cinematic.cyberojol.buildChassis3DParts
import com.example.shared.atoms.cinematic.cyberojol.buildRider3DParts
import com.example.shared.atoms.cinematic.cyberojol.clampCamZoom
import com.example.shared.atoms.cinematic.cyberojol.clampPitch
import com.example.shared.atoms.cinematic.cyberojol.normalizeAngle
import com.example.shared.atoms.cinematic.cyberojol.project3DPoint
import com.example.shared.atoms.cinematic.cyberojol.rotatePitch
import com.example.shared.atoms.cinematic.cyberojol.rotateYaw
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CyberOjol3DTest {

    @Test
    fun `3D vector yaw rotation rotates point in horizontal plane`() {
        val forward = Vec3D(0f, 0f, 100f)

        // 90 deg yaw turns forward (+Z) to left (-X) or right
        val turned90 = rotateYaw(forward, 90f)
        assertEquals(-100f, turned90.x, 0.5f)
        assertEquals(0f, turned90.y, 0.01f)
        assertEquals(0f, turned90.z, 0.5f)

        // 180 deg yaw turns forward to backward (-Z)
        val turned180 = rotateYaw(forward, 180f)
        assertEquals(0f, turned180.x, 0.5f)
        assertEquals(-100f, turned180.z, 0.5f)
    }

    @Test
    fun `3D vector pitch rotation tilts camera up and down`() {
        val upright = Vec3D(0f, 100f, 0f)
        val pitched0 = rotatePitch(upright, 0f)
        assertEquals(100f, pitched0.y, 0.01f)

        // Clamped pitch limits
        assertEquals(MAX_PITCH, clampPitch(90f), 0.01f)
        assertEquals(MIN_PITCH, clampPitch(-50f), 0.01f)
    }

    @Test
    fun `3D perspective projection projects world coordinates to screen`() {
        val cam = CameraTransform(yawDegrees = 0f, pitchDegrees = 0f, zoomFactor = 1.0f)
        val origin = Vec3D(0f, 0f, 0f)
        val screenW = 1080f
        val screenH = 2400f

        val projected = project3DPoint(origin, cam, screenW, screenH, centerOffsetY = 0.5f)
        assertTrue(projected.isVisible)
        assertEquals(540f, projected.screenX, 1.0f)
        assertEquals(1200f, projected.screenY, 1.0f)
        assertTrue(projected.scaleFactor > 0f)
    }

    @Test
    fun `3D mesh parts list contains complete vehicle and rider geometry`() {
        val chassis = buildChassis3DParts()
        val rider = buildRider3DParts()

        assertTrue(chassis.any { it.id == "front_wheel" })
        assertTrue(chassis.any { it.id == "rear_wheel" })
        assertTrue(chassis.any { it.id == "headlights" })
        assertTrue(rider.any { it.id == "rider_helmet" })
        assertTrue(rider.any { it.id == "rider_torso" })
    }

    @Test
    fun `Camera presets maintain valid coordinates`() {
        CameraPreset.values().forEach { preset ->
            assertTrue(preset.yaw in 0f..360f)
            assertTrue(preset.pitch in MIN_PITCH..MAX_PITCH)
        }
    }

    @Test
    fun `adversarial bad inputs are safely clamped and sanitized`() {
        assertEquals(0f, normalizeAngle(Float.NaN), 0.01f)
        assertEquals(0f, normalizeAngle(Float.POSITIVE_INFINITY), 0.01f)

        assertEquals(1.0f, clampCamZoom(Float.NaN), 0.01f)
        assertEquals(MIN_CAM_ZOOM, clampCamZoom(0.01f), 0.01f)
        assertEquals(MAX_CAM_ZOOM, clampCamZoom(100f), 0.01f)

        assertEquals(6f, clampPitch(Float.NaN), 0.01f)
    }
}
