package com.example.modules.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgGlassCard
import com.example.ui.theme.*

@Composable
fun LandingAboutCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DrgGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("landing_about_card"),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = Color.White.copy(alpha = 0.85f),
        elevation = 4.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(DrgGrabGreenContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Tentang Aplikasi",
                    tint = DrgGrabGreenPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Tentang Aplikasi & Roadmap DRG",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgTextDark
                )
                Text(
                    text = "Visi Solidaritas & Komando Driver Malang Raya",
                    fontSize = 11.sp,
                    color = DrgTextMuted
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = DrgGrabGreenPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
