package com.example.modules.members.role_management

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.shared.models.DriverMember
import com.example.shared.models.VerificationStatus
import com.example.ui.theme.*

@Composable
fun RoleMemberCard(
    member: DriverMember,
    onManageClick: () -> Unit,
    modifier: Modifier = Modifier,
    canEdit: Boolean = true
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .border(0.5.dp, DrgOutline, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(member.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DrgTextDark)
                    Text("${member.motorcyclePlate} • ${member.driverId}", fontSize = 11.sp, color = DrgTextMuted)
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(member.role.badgeColorHex).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = member.role.shortName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color(member.role.badgeColorHex),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (member.canManageKas) PermissionBadge("Kas", DrgAmberSecondary)
                if (member.canVerifyDrivers) PermissionBadge("Screening", DrgBlueInfo)
                if (member.canBroadcastSos) PermissionBadge("SOS", DrgRedDanger)
                if (member.canManagePosko) PermissionBadge("Posko", DrgGrabGreenPrimary)
                if (member.verificationStatus == VerificationStatus.SUSPENDED) {
                    PermissionBadge("SUSPENDED", DrgRedDanger)
                }
            }

            if (canEdit) {
                Button(
                    onClick = onManageClick,
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGrabGreenPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(Icons.Default.ManageAccounts, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Atur Jabatan & Izin Akses", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun PermissionBadge(label: String, color: Color) {
    Surface(shape = RoundedCornerShape(4.dp), color = color.copy(alpha = 0.12f)) {
        Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = color, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
    }
}
