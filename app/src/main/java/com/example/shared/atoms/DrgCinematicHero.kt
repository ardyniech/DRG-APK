package com.example.shared.atoms

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgWarmGradientStart
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DrgCinematicHeroBackdrop(modifier: Modifier = Modifier) {
    val time = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        time.animateTo(
            targetValue = 2f * Math.PI.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(26000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Box(modifier = modifier.fillMaxWidth().height(220.dp)) {
        DrgAtmosphericGlow(modifier = Modifier.fillMaxSize(), cinematic = true)
    }
}

@Composable
fun DrgHolographicTextBanner(
    modifier: Modifier = Modifier,
    primaryText: String = "Driver Riang Gembira",
    secondaryText: String = "Kompak • Solid • Gacor di Malang Raya"
) {
    val shine = rememberInfiniteTransition(label = "holo-shine")
    val alphaShine = shine.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shine"
    )

    DrgGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White.copy(alpha = 0.88f)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = primaryText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = DrgTextDark,
                modifier = Modifier.alpha(alphaShine.value)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = secondaryText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = DrgTextMuted
            )
        }
    }
}