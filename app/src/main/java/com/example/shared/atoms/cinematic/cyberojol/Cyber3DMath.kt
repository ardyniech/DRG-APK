package com.example.shared.atoms.cinematic.cyberojol

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

const val MIN_CAM_ZOOM = 0.55f
const val MAX_CAM_ZOOM = 3.20f
const val MIN_PITCH = -15f
const val MAX_PITCH = 42f

fun normalizeAngle(degrees: Float): Float {
    if (degrees.isNaN() || degrees.isInfinite()) return 0f
    val mod = degrees % 360f
    return if (mod < 0f) mod + 360f else mod
}

fun clampPitch(degrees: Float): Float {
    if (degrees.isNaN() || degrees.isInfinite()) return 6f
    return degrees.coerceIn(MIN_PITCH, MAX_PITCH)
}

fun clampCamZoom(zoom: Float): Float {
    if (zoom.isNaN() || zoom.isInfinite()) return 1.0f
    return zoom.coerceIn(MIN_CAM_ZOOM, MAX_CAM_ZOOM)
}

fun rotateYaw(v: Vec3D, yawDeg: Float): Vec3D {
    val rad = (normalizeAngle(yawDeg) * PI / 180.0).toFloat()
    val cosVal = cos(rad)
    val sinVal = sin(rad)
    return Vec3D(
        x = v.x * cosVal - v.z * sinVal,
        y = v.y,
        z = v.x * sinVal + v.z * cosVal
    )
}

fun rotatePitch(v: Vec3D, pitchDeg: Float): Vec3D {
    val rad = (clampPitch(pitchDeg) * PI / 180.0).toFloat()
    val cosVal = cos(rad)
    val sinVal = sin(rad)
    return Vec3D(
        x = v.x,
        y = v.y * cosVal - v.z * sinVal,
        z = v.y * sinVal + v.z * cosVal
    )
}

fun project3DPoint(
    v: Vec3D,
    cam: CameraTransform,
    screenWidth: Float,
    screenHeight: Float,
    centerOffsetY: Float = 0.62f
): ProjectedPoint {
    val rotatedYaw = rotateYaw(v, cam.yawDegrees)
    val rotatedPitch = rotatePitch(rotatedYaw, cam.pitchDegrees)

    val cameraZ = rotatedPitch.z + cam.focalDistance
    if (cameraZ <= 10f) {
        return ProjectedPoint(0f, 0f, cameraZ, 0f, isVisible = false)
    }

    val perspective = (cam.focalDistance / cameraZ) * clampCamZoom(cam.zoomFactor)
    val centerX = screenWidth * 0.5f
    val centerY = screenHeight * centerOffsetY

    val screenX = centerX + rotatedPitch.x * perspective
    val screenY = centerY + rotatedPitch.y * perspective

    return ProjectedPoint(
        screenX = screenX,
        screenY = screenY,
        depth = cameraZ,
        scaleFactor = perspective,
        isVisible = true
    )
}
