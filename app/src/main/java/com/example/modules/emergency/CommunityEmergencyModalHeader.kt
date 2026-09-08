package com.example.modules.emergency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun CommunityEmergencyModalHeader(isSosAlarmEnabled: Boolean, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Peringatan",
                tint = DrgRedDanger,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Lapor Darurat Komunitas",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DrgRedDanger
            )
        }
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isSosAlarmEnabled) DrgRedContainer else DrgSurfaceVariant
        ) {
            Text(
                text = if (isSosAlarmEnabled) "🔊 Sirine Siaga" else "🔇 Senyap",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSosAlarmEnabled) DrgRedDanger else DrgTextSecondary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
        }
    }
}
