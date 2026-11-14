package com.example.shared.atoms.cinematic.cockpit

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.shared.atoms.cinematic.turntable.Ojol360AssetLoader
import com.example.ui.theme.DrgLaneMarker

fun DrawScope.drawCockpitHighwayRoad(
    context: Context,
    rushProgress: Float,
    steerOffset: Float,
    panX: Float
) {
    // 1. Draw High-Res Panorama of Mount Arjuno in Malang
    val bgBmp = Ojol360AssetLoader.loadBackground(context)
    if (bgBmp != null) {
        val bgOffsetLeft = (panX * 0.15f).toInt()
        drawImage(
            image = bgBmp,
            srcOffset = IntOffset.Zero,
            srcSize = IntSize(bgBmp.width, bgBmp.height),
            dstOffset = IntOffset(bgOffsetLeft, 0),
            dstSize = IntSize(size.width.toInt(), size.height.toInt())
        )
    }

    // 2. Animated Perspective Road Markings rushing toward cockpit
    val horizonY = size.height * 0.43f
    val roadCenterX = size.width * 0.5f + panX * 0.25f
    val dashCount = 8

    // Center Dashed White Lines
    for (i in 0 until dashCount) {
        val dashStep = (i.toFloat() / dashCount + rushProgress) % 1.0f
        val projected = projectRoadDash(dashStep, horizonY, size.height, roadCenterX, steerOffset)

        drawRect(
            color = Color.White.copy(alpha = projected.alpha),
            topLeft = Offset(projected.centerX - projected.width * 0.5f, projected.topY),
            size = Size(projected.width, projected.length)
        )
    }

    // Left and Right Warning Edge Markings
    for (i in 0 until dashCount) {
        val dashStep = (i.toFloat() / dashCount + rushProgress) % 1.0f
        val leftProj = projectRoadDash(dashStep, horizonY, size.height, roadCenterX - size.width * 0.32f, steerOffset)
        val rightProj = projectRoadDash(dashStep, horizonY, size.height, roadCenterX + size.width * 0.32f, steerOffset)

        drawRect(
            color = DrgLaneMarker.copy(alpha = leftProj.alpha * 0.75f),
            topLeft = Offset(leftProj.centerX - leftProj.width * 0.5f, leftProj.topY),
            size = Size(leftProj.width, leftProj.length)
        )
        drawRect(
            color = DrgLaneMarker.copy(alpha = rightProj.alpha * 0.75f),
            topLeft = Offset(rightProj.centerX - rightProj.width * 0.5f, rightProj.topY),
            size = Size(rightProj.width, rightProj.length)
        )
    }
}
