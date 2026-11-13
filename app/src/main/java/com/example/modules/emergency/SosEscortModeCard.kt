package com.example.modules.emergency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@Composable
fun SosEscortModeCard(
    isEscortActive: Boolean,
    onToggleEscort: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = if (isEscortActive) theme.pillBg.copy(alpha = 0.65f) else Color(0xEBFFFFFF),
        borderHighlightColor = if (isEscortActive) theme.primaryColor else theme.glassBorderColor.copy(alpha = 0.7f),
        contentPadding = 14.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Mode Pantauan",
                        tint = if (isEscortActive) theme.primaryColor else DrgTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Mode Pantau Jalur Rawan (Escort)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DrgTextDark
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = if (isEscortActive)
                        "Perjalanan Anda sedang dipantau live oleh Satgas DRG."
                    else
                        "Aktifkan saat melintasi jalur gelap atau sepi malam hari.",
                    fontSize = 11.sp,
                    color = DrgTextSecondary
                )
            }
            Switch(
                checked = isEscortActive,
                onCheckedChange = onToggleEscort,
                colors = SwitchDefaults.colors(checkedThumbColor = theme.primaryColor, checkedTrackColor = theme.primaryColor.copy(alpha = 0.3f))
            )
        }
    }
}
