package com.example.modules.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.example.shared.models.RoleAuditLogEntity
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RoleAuditTrailCard(log: RoleAuditLogEntity, modifier: Modifier = Modifier) {
    val formattedDate = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID")).format(Date(log.timestamp))

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DrgSurface,
        modifier = modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(24.dp).background(DrgGreenContainer, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = "Audit", tint = DrgGreenPrimary, modifier = Modifier.size(14.dp))
                    }
                    Text(text = log.targetMemberName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                }
                Text(text = formattedDate, fontSize = 10.sp, color = DrgTextSecondary)
            }

            Text(
                text = "${log.previousRole} ➔ ${log.newRole}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = DrgGreenPrimary
            )

            if (log.reason.isNotBlank()) {
                Text(text = "Alasan: \"${log.reason}\"", fontSize = 11.sp, color = DrgTextSecondary)
            }

            Text(
                text = "Diubah oleh: ${log.actionByMemberName} (${log.actionByMemberId})",
                fontSize = 10.sp,
                color = DrgTextSecondary.copy(alpha = 0.8f)
            )
        }
    }
}
