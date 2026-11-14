package com.example.shared.atoms.cinematic.cyberojol

import androidx.compose.ui.graphics.drawscope.DrawScope

class CyberOjolRenderPart(
    val id: String,
    val origin: Vec3D,
    val priority: Int = 0,
    val drawAction: DrawScope.(point: ProjectedPoint, yawCos: Float, cam: CameraTransform) -> Unit
)

data class ProjectedRenderPart(
    val part: CyberOjolRenderPart,
    val projected: ProjectedPoint
) : Comparable<ProjectedRenderPart> {
    // Painter's algorithm: parts with largest depth (furthest away) are rendered FIRST
    override fun compareTo(other: ProjectedRenderPart): Int {
        val depthDiff = other.projected.depth.compareTo(this.projected.depth)
        return if (depthDiff != 0) depthDiff else this.part.priority.compareTo(other.part.priority)
    }
}
