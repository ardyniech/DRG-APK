package com.example.shared.atoms.cinematic.cyberojol

data class Vec3D(
    val x: Float,
    val y: Float,
    val z: Float
)

data class ProjectedPoint(
    val screenX: Float,
    val screenY: Float,
    val depth: Float,
    val scaleFactor: Float,
    val isVisible: Boolean
)

data class CameraTransform(
    val yawDegrees: Float,
    val pitchDegrees: Float,
    val zoomFactor: Float,
    val focalDistance: Float = 620f
)

enum class CameraPreset(val label: String, val yaw: Float, val pitch: Float) {
    CHASE_REAR("CHASE 0°", 0f, 6f),
    PROFILE_RIGHT("PROFILE 90°", 90f, 2f),
    FRONT_ONCOMING("FRONT 180°", 180f, 4f),
    PROFILE_LEFT("PROFILE 270°", 270f, 2f),
    DRONE_TOP("DRONE 3D", 45f, 32f)
}
