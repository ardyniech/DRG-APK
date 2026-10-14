package com.example.shared.atoms

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DrgFloatingLogo(
    modifier: Modifier = Modifier,
    logoRes: Int,
    baseSize: Int = 108
) {
    val infinite = rememberInfiniteTransition(label = "drg-floating-logo")
    val floatY = infinite.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(3800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatY"
    )
    val floatX = infinite.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(5200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatX"
    )
    val rotate = infinite.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(9000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotate"
    )

    Box(
        modifier = modifier
            .size(baseSize.dp)
            .offset(x = floatX.value.dp, y = floatY.value.dp)
            .graphicsLayer {
                rotationZ = rotate.value
                shadowElevation = 8f
            }
            .rotate(rotate.value)
    ) {
        DrgGlassLogoMark(logoRes = logoRes, baseSize = baseSize.dp)
    }
}

@Composable
fun DrgGlassLogoMark(logoRes: Int, baseSize: androidx.compose.ui.unit.Dp = 108.dp) {
    DrgGlassCard(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(22.dp),
        backgroundColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.92f),
        borderWidth = 1.6.dp,
        elevation = 10.dp
    ) {
        Box(
            modifier = Modifier.size(baseSize),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = "Logo DRG",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize().padding(5.dp)
            )
        }
    }
}