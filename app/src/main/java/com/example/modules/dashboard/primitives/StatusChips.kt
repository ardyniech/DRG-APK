package com.example.modules.dashboard.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun StatusChips(
    currentStatus: String,
    onStatusChange: (String, Boolean) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        listOf(
            Triple("Aktif Narik", true, DrgGreenPrimary),
            Triple("Siaga Posko", true, DrgAmberSecondary),
            Triple("Rehat", false, DrgTextSecondary)
        ).forEach { (status, online, color) ->
            val isSelected = currentStatus.contains(status, ignoreCase = true)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) color.copy(alpha = 0.15f) else DrgSurface)
                    .border(1.dp, if (isSelected) color else DrgOutline.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .clickable { onStatusChange(status, online) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = status,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) color else DrgTextSecondary
                )
            }
        }
    }
}
