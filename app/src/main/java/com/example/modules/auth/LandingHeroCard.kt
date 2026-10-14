package com.example.modules.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgGlassCard
import com.example.shared.atoms.cinematic.CinematicReveal
import com.example.ui.theme.DrgGrabGreenContainer
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextMuted

@Composable
fun LandingHeroCard(modifier: Modifier = Modifier) {
    DrgGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        backgroundColor = DrgTextDark.copy(alpha = 0.92f),
        elevation = 10.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            CinematicReveal(index = 0) {
                LandingNightStage()
            }
            Spacer(modifier = Modifier.height(12.dp))

            CinematicReveal(index = 1) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(DrgGrabGreenContainer)
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint = DrgGrabGreenPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            "Official DRG Driver • Malang Raya",
                            fontSize = 11.sp,
                            color = DrgGrabGreenPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Driver Riang Gembira",
                        fontSize = 22.sp,
                        color = Color.White
                    )
                    Text(
                        "Kompak • Solid • Gacor di Malang Raya",
                        fontSize = 12.sp,
                        color = DrgTextMuted
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}