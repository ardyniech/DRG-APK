package com.example.modules.dashboard.primitives

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.*

@Composable
fun StatusChips(
    currentStatus: String,
    onStatusChange: (String, Boolean) -> Unit,
    accentColor: Color = DrgGreenPrimary
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            Triple("Aktif Narik", true, accentColor),
            Triple("Siaga Posko", true, DrgWarningText),
            Triple("Rehat", false, DrgTextSecondary)
        ).forEach { (status, online, color) ->
            val isSelected = currentStatus.contains(status, ignoreCase = true)
            val bgColor by animateColorAsState(
                if (isSelected) color.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.52f),
                tween(250), label = "chipBg"
            )
            val borderColor by animateColorAsState(
                if (isSelected) color.copy(alpha = 0.75f) else Color.White.copy(alpha = 0.85f),
                tween(250), label = "chipBorder"
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(bgColor)
                    .border(1.2.dp, borderColor, RoundedCornerShape(14.dp))
                    .bouncyClick { onStatusChange(status, online) }
                    .padding(horizontal = 6.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    if (isSelected) {
                        Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(color))
                    }
                    Text(
                        text = status,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) color else DrgTextSecondary
                    )
                }
            }
        }
    }
}
