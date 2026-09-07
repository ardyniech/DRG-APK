package com.example.modules.emergency

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun SosEscortModeCard(
    isEscortActive: Boolean,
    onToggleEscort: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isEscortActive) DrgGreenContainer else DrgSurface,
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isEscortActive) DrgGreenPrimary else DrgOutline.copy(alpha = 0.6f),
                RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Mode Pantauan",
                        tint = if (isEscortActive) DrgGreenPrimary else DrgTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Mode Pantau Jalur Rawan (Escort)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DrgTextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = if (isEscortActive)
                        "Perjalanan Anda sedang dipantau live oleh Satgas DRG."
                    else
                        "Aktifkan saat melintasi jalur gelap/sepi malam hari.",
                    fontSize = 11.sp,
                    color = DrgTextSecondary
                )
            }
            Switch(
                checked = isEscortActive,
                onCheckedChange = onToggleEscort,
                colors = SwitchDefaults.colors(checkedThumbColor = DrgGreenPrimary)
            )
        }
    }
}
