package com.example.modules.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.ui.theme.*

@Composable
fun SatgasShiftScheduleCard(currentMember: DriverMember?) {
    var isDutyOn by remember { mutableStateOf(currentMember?.role == MemberRole.SATGAS) }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DrgSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DrgGreenPrimary.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Piket Satgas",
                        tint = DrgGreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Jadwal Piket Siaga Satgas (Shift)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DrgTextPrimary
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isDutyOn) DrgGreenContainer else DrgSurfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isDutyOn) "🟢 ON-DUTY (SIAGA)" else "⚪ OFF-DUTY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDutyOn) DrgGreenPrimary else DrgTextMuted
                    )
                }
            }

            Text(
                text = "Shift Malam Ini (20:00 - 02:00): Bang Jack (Posko Barat) • Pak Heru (Posko Selatan)",
                fontSize = 11.sp,
                color = DrgTextSecondary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daftar Piket Siaga Malam Ini",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DrgTextPrimary
                )
                Switch(
                    checked = isDutyOn,
                    onCheckedChange = { isDutyOn = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = DrgGreenPrimary)
                )
            }
        }
    }
}
