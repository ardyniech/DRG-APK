package com.example.modules.dashboard.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.SpaceGroteskFontFamily

/**
 * Bento Grid Tile 4: Frosted Glass Interkom & Walkie-Talkie Mesh
 */
@Composable
fun BentoIntercomWidget(
    visualTheme: DashboardVisualTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val purple = Color(0xFF7C3AED)

    GlassCard(
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = visualTheme.glassBorderColor,
        contentPadding = 12.dp,
        onClick = onClick
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(purple.copy(alpha = 0.16f))
                        .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Headset, contentDescription = "Interkom", tint = purple, modifier = Modifier.size(18.dp))
                }
                Text(text = "INTERKOM MESH", fontSize = 10.sp, fontWeight = FontWeight.Black, color = purple)
            }

            Column {
                Text(text = "Kanal Komunitas", fontSize = 10.sp, color = DrgTextSecondary)
                Text(
                    text = "CH-01 Malang",
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DrgTextDark
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Standby PTT", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = DrgTextSecondary)
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(purple.copy(alpha = 0.15f)).padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = "AUDIO", fontSize = 9.sp, fontWeight = FontWeight.Black, color = purple)
                }
            }
        }
    }
}
