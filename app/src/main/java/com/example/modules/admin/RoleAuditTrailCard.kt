package com.example.modules.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.RoleAuditLogEntity
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.SpaceGroteskFontFamily
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RoleAuditTrailCard(log: RoleAuditLogEntity, modifier: Modifier = Modifier) {
    val formattedDate = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID")).format(Date(log.timestamp))
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassCard(
        shape = RoundedCornerShape(18.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 12.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(24.dp).background(theme.pillBg, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Shield, contentDescription = "Audit", tint = theme.primaryColor, modifier = Modifier.size(14.dp))
                    }
                    Text(log.targetMemberName, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                }
                Text(formattedDate, fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, color = DrgTextSecondary)
            }
            Text("${log.previousRole} ➔ ${log.newRole}", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = theme.primaryColor)
            if (log.reason.isNotBlank()) Text("Alasan: \"${log.reason}\"", fontSize = 11.sp, color = DrgTextSecondary)
            Text("Diubah oleh: ${log.actionByMemberName} (${log.actionByMemberId})", fontSize = 10.sp, color = DrgTextSecondary.copy(alpha = 0.8f))
        }
    }
}
