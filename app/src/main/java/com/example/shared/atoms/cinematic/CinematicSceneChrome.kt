package com.example.shared.atoms.cinematic

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgTextMuted

@Composable
fun CinematicSceneChrome(
    scene: CinematicScene,
    sceneIndex: Int,
    ctaVisible: Boolean,
    onSkip: () -> Unit,
    onReplay: () -> Unit,
    modifier: Modifier = Modifier
) {
    val reveal by animateFloatAsState(
        targetValue = if (ctaVisible) 0f else 1f,
        animationSpec = tween(500),
        label = "chrome-reveal"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 18.dp, start = 20.dp, end = 20.dp)
                .alpha(reveal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CinematicShotProgress(
                shotCount = CinematicShots.size,
                activeIndex = sceneIndex,
                running = !ctaVisible
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CinematicChromeChip(text = "DRG • MALANG", onClick = null)
                CinematicChromeChip(
                    text = "Masuk • Menu",
                    onClick = onSkip
                )
            }
        }

        // Headline & caption positioned in the sky area, never blocking motorcycle dashboard
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 84.dp)
                .alpha(reveal),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = scene.headline,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = scene.caption,
                fontSize = 12.sp,
                color = DrgTextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CinematicChromeChip(text: String, onClick: (() -> Unit)?) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.Black.copy(alpha = 0.38f))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(text, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}
