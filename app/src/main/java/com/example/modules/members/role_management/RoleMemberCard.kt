package com.example.modules.members.role_management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageAccounts
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
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import com.example.ui.theme.*

@Composable
fun RoleMemberCard(
    member: DriverMember,
    onManageClick: () -> Unit,
    modifier: Modifier = Modifier,
    canEdit: Boolean = true
) {
    val theme = LocalAppVisualTheme.current
    val roleColor = Color(member.role.badgeColorHex)
    DrgLuminousGlassCard(
        shape = RoundedCornerShape(18.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = roleColor.copy(alpha = 0.45f),
        contentPadding = 12.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(member.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DrgTextDark)
                    Text("${member.motorcyclePlate} • ${member.driverId}", fontFamily = SpaceGroteskFontFamily, fontSize = 11.sp, color = DrgTextMuted)
                }
                Surface(shape = RoundedCornerShape(8.dp), color = roleColor.copy(alpha = 0.14f)) {
                    Text(member.role.shortName, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = roleColor, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                if (member.canManageKas) PermissionBadge("Kas", DrgAmberSecondary)
                if (member.canVerifyDrivers) PermissionBadge("Screening", DrgBlueInfo)
                if (member.canBroadcastSos) PermissionBadge("SOS", DrgRedDanger)
                if (member.canManagePosko) PermissionBadge("Posko", theme.primaryColor)
                if (member.verificationStatus == VerificationStatus.SUSPENDED) PermissionBadge("SUSPENDED", DrgRedDanger)
            }

            if (member.role == MemberRole.SUPER_ADMIN) {
                Surface(shape = RoundedCornerShape(10.dp), color = DrgBlueInfo.copy(alpha = 0.12f), modifier = Modifier.fillMaxWidth()) {
                    Text("🛡️ Akun Sistem Root (Permanen)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgBlueInfo, modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp))
                }
            } else if (canEdit) {
                Button(
                    onClick = onManageClick,
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(Icons.Default.ManageAccounts, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text("Atur Jabatan & Izin Akses", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun PermissionBadge(label: String, color: Color) {
    Surface(shape = RoundedCornerShape(6.dp), color = color.copy(alpha = 0.14f)) {
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = color, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
    }
}
