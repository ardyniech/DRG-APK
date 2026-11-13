package com.example.shared.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.SpaceGroteskFontFamily

@Composable
fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = iconColor.copy(alpha = 0.45f),
        contentPadding = 12.dp,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DrgIconTileBox(icon = icon, tint = iconColor)
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 11.sp, color = DrgTextSecondary, fontWeight = FontWeight.SemiBold)
                Text(value, fontFamily = SpaceGroteskFontFamily, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                Text(subtitle, fontSize = 10.sp, color = DrgTextSecondary)
            }
        }
    }
}
