package com.example.modules.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.shared.atoms.DrgFloatingLogo
import com.example.shared.atoms.DrgGlassCard
import com.example.shared.atoms.cinematic.FilmGrainOverlay
import com.example.shared.atoms.cinematic.RoadRushBackdrop
import com.example.shared.atoms.cinematic.SpeedometerGauge

@Composable
fun LandingNightStage(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().height(248.dp).clip(RoundedCornerShape(26.dp))) {
        RoadRushBackdrop(modifier = Modifier.fillMaxSize())
        FilmGrainOverlay(modifier = Modifier.fillMaxSize())

        DrgGlassCard(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 14.dp, top = 14.dp)
                .size(84.dp),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color.White.copy(alpha = 0.10f),
            borderWidth = 1.dp,
            elevation = 4.dp
        ) {
            SpeedometerGauge(
                modifier = Modifier.padding(6.dp),
                diameter = 72.dp,
                maxSpeed = 120
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        ) {
            DrgFloatingLogo(logoRes = R.drawable.drg_app_icon, baseSize = 92)
        }
    }
}