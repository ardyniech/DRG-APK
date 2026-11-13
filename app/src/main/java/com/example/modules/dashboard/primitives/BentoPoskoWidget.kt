package com.example.modules.dashboard.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
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
import com.example.ui.theme.DrgBlueAccent
import com.example.modules.dashboard.theme.DashboardVisualTheme
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.SpaceGroteskFontFamily

/**
 * Bento Grid Tile 3: Frosted Glass Posko Rehat & Bengkel Mitra
 */
@Composable
fun BentoPoskoWidget(
    poskoCount: Int,
    visualTheme: DashboardVisualTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val poskoTint = DrgBlueAccent

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
                        .background(poskoTint.copy(alpha = 0.16f))
                        .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Place, contentDescription = "Posko", tint = poskoTint, modifier = Modifier.size(18.dp))
                }
                Text(text = "POSKO REHAT", fontSize = 10.sp, fontWeight = FontWeight.Black, color = poskoTint)
            }

            Column {
                Text(text = "Posko & Bengkel", fontSize = 10.sp, color = DrgTextSecondary)
                Text(
                    text = "$poskoCount Titik Aktif",
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DrgTextDark
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Radius 1.2 KM", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = DrgTextSecondary)
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(poskoTint.copy(alpha = 0.15f)).padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = "PETA", fontSize = 9.sp, fontWeight = FontWeight.Black, color = poskoTint)
                }
            }
        }
    }
}
