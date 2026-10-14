package com.example.shared.atoms.cinematic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.math.abs
import kotlin.random.Random

internal data class SkylineBlock(
    val offset: Float,
    val width: Float,
    val height: Float,
    val litWindows: Float,
    val layerSpeed: Float
)

internal fun buildSkyline(seed: Int, count: Int, minHeight: Float, maxHeight: Float): List<SkylineBlock> {
    val random = Random(seed)
    return List(count) { index ->
        SkylineBlock(
            offset = index.toFloat() / count.toFloat(),
            width = 0.03f + abs(random.nextFloat()) * 0.05f,
            height = minHeight + abs(random.nextFloat()) * (maxHeight - minHeight),
            litWindows = random.nextFloat(),
            layerSpeed = 0.6f + abs(random.nextFloat()) * 0.8f
        )
    }
}

internal fun wrapUnit(value: Float): Float {
    val wrapped = value % 1f
    return if (wrapped < 0f) wrapped + 1f else wrapped
}

internal data class RainDrop(val x: Float, val y: Float, val length: Float, val speed: Float)

internal fun buildRain(seed: Int, count: Int): List<RainDrop> {
    val random = Random(seed)
    return List(count) {
        RainDrop(
            x = random.nextFloat(),
            y = random.nextFloat(),
            length = 0.02f + random.nextFloat() * 0.05f,
            speed = 0.9f + random.nextFloat() * 0.7f
        )
    }
}

internal data class GrainSpeck(val x: Float, val y: Float, val size: Float, val alpha: Float)

internal fun buildGrain(seed: Int, count: Int): List<GrainSpeck> {
    val random = Random(seed)
    return List(count) {
        GrainSpeck(
            x = random.nextFloat(),
            y = random.nextFloat(),
            size = 0.6f + random.nextFloat() * 1.6f,
            alpha = 0.05f + random.nextFloat() * 0.12f
        )
    }
}

@Composable
internal fun rememberSkyline(count: Int, minHeight: Float, maxHeight: Float): List<SkylineBlock> =
    remember(count, minHeight, maxHeight) { buildSkyline(seed = 20251014, count = count, minHeight = minHeight, maxHeight = maxHeight) }

@Composable
internal fun rememberRain(count: Int): List<RainDrop> =
    remember(count) { buildRain(seed = 90210, count = count) }

@Composable
internal fun rememberGrain(count: Int): List<GrainSpeck> =
    remember(count) { buildGrain(seed = 31415, count = count) }