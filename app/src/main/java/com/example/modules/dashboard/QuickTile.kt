package com.example.modules.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgGlassCard
import com.example.shared.atoms.DrgIconTileBox
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

/**
 * Tile lebar kanonis — shell DrgGlassCard + DrgIconTileBox (duplikat LauncherTile di-unify).
 */
@Composable
fun QuickTile(
    title: String,
    desc: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val tileShape = RoundedCornerShape(20.dp)
    val brush = Brush.linearGradient(listOf(Color.White, Color.White.copy(alpha = 0.5f), color.copy(alpha = 0.15f)))
    DrgGlassCard(
        modifier = modifier,
        shape = tileShape,
        backgroundColor = Color.White.copy(alpha = 0.82f),
        borderBrush = brush,
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DrgIconTileBox(icon = icon, tint = color, size = 38.dp, iconSize = 20.dp, contentDescription = title)
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                Text(desc, fontSize = 10.sp, color = DrgTextSecondary)
            }
        }
    }
}
