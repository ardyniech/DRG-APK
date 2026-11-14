package com.example.shared.atoms.cinematic.cyberojol

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.PI
import kotlin.math.cos

fun DrawScope.renderCyberOjol3DScene(
    cam: CameraTransform,
    rushProgress: Float,
    bounceY: Float
) {
    // 1. Draw 3D Highway & Horizon Environment
    drawCyber3DHighway(cam, rushProgress)

    // 2. Project & Depth-Sort 3D Vehicle & Rider Parts
    val allParts = buildChassis3DParts() + buildRider3DParts()
    val projectedParts = mutableListOf<ProjectedRenderPart>()

    for (part in allParts) {
        val displacedOrigin = Vec3D(part.origin.x, part.origin.y + bounceY, part.origin.z)
        val proj = project3DPoint(displacedOrigin, cam, size.width, size.height)
        if (proj.isVisible) {
            projectedParts.add(ProjectedRenderPart(part, proj))
        }
    }

    projectedParts.sort() // Furthest away rendered first

    // 3. Ground Ambient Occlusion Shadow
    val shadowW = 160f * clampCamZoom(cam.zoomFactor)
    val shadowH = 34f * clampCamZoom(cam.zoomFactor)
    drawOval(
        color = Color.Black.copy(alpha = 0.48f),
        topLeft = Offset(size.width * 0.5f - shadowW * 0.5f, size.height * 0.76f - shadowH * 0.5f),
        size = Size(shadowW, shadowH)
    )

    // 4. Render All 3D Geometry in Depth Order
    val yawRad = (normalizeAngle(cam.yawDegrees) * PI / 180.0).toFloat()
    val yawCos = cos(yawRad)

    for (item in projectedParts) {
        item.part.drawAction(this, item.projected, yawCos, cam)
    }
}
