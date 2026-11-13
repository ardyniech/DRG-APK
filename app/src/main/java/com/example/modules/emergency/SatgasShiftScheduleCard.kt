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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.ui.theme.*

@Composable
fun SatgasShiftScheduleCard(currentMember: DriverMember?) {
    val theme = LocalAppVisualTheme.current
    var isDutyOn by remember { mutableStateOf(currentMember?.role == MemberRole.SATGAS) }

    DrgLuminousGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color(0xEBFFFFFF),
        borderHighlightColor = if (isDutyOn) theme.primaryColor.copy(alpha = 0.65f) else theme.glassBorderColor,
        contentPadding = 14.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, contentDescription = "Piket Satgas", tint = theme.primaryColor, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Jadwal Piket Siaga Satgas", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextDark)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDutyOn) theme.pillBg else Color(0xFFF1F5F9))
                        .border(0.8.dp, if (isDutyOn) theme.primaryColor.copy(alpha = 0.35f) else theme.glassBorderColor, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (isDutyOn) "ON-DUTY (SIAGA)" else "OFF-DUTY",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isDutyOn) theme.primaryColor else DrgTextMuted
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
                Text("Daftar Piket Siaga Malam Ini", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextDark)
                Switch(
                    checked = isDutyOn,
                    onCheckedChange = { isDutyOn = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = theme.primaryColor, checkedTrackColor = theme.primaryColor.copy(alpha = 0.3f))
                )
            }
        }
    }
}
