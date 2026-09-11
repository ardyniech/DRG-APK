package com.example.modules.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.members.role_management.RolePermissionManagementScreen
import com.example.shared.models.*
import com.example.ui.theme.*

@Composable
fun AdminGovernanceScreen(
    currentMember: DriverMember?,
    members: List<DriverMember>,
    onApproveScreening: (String) -> Unit,
    onSendNotification: (String, String, NotificationSeverity) -> Unit,
    onOpenRoleManagement: () -> Unit = {},
    onUpdateMemberPermissions: (String, MemberRole, Boolean, Boolean, Boolean, Boolean, VerificationStatus) -> Unit = { _, _, _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    var showNotifDialog by remember { mutableStateOf(false) }
    val isLeadershipOrAdmin = currentMember?.role?.isLeadership == true || currentMember?.role?.name in listOf("SEKRETARIS", "ADMIN")

    LazyColumn(
        modifier = modifier.fillMaxSize().background(DrgBackground).padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item { AdminGovernanceHeaderCard(currentMember = currentMember) }
        item { AiCommunityHealthCard(memberCount = members.size) }

        if (isLeadershipOrAdmin) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onOpenRoleManagement,
                        colors = ButtonDefaults.buttonColors(containerColor = DrgGrabGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(46.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(Icons.Default.ManageAccounts, contentDescription = "Role", tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Atur Jabatan & Izin", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                    }
                    Button(
                        onClick = { showNotifDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(46.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(Icons.Default.Campaign, contentDescription = "Broadcast", tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Kirim Siaran", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                    }
                }
            }
        }

        item { Text("Pendaftaran Driver Baru Dalam Verifikasi", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DrgTextPrimary) }

        val pendingScreenings = members.filter { it.verificationStatus == VerificationStatus.PENDING_SCREENING }
        if (pendingScreenings.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().background(DrgSurface, RoundedCornerShape(12.dp)).padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Semua calon anggota telah diverifikasi.", fontSize = 12.sp, color = DrgTextSecondary)
                }
            }
        } else {
            items(pendingScreenings) { pending ->
                PendingMemberCard(member = pending, canApprove = isLeadershipOrAdmin, onApprove = { onApproveScreening(pending.id) })
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = DrgSurface,
                modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("📋 Evaluasi Kode Etik: KONDUSIF", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgGreenPrimary)
                    Text("Dewan Etika memantau rating & ulasan dari sesama driver.", fontSize = 11.sp, color = DrgTextSecondary)
                }
            }
        }
    }

    if (showNotifDialog) {
        CreateAdminNotificationDialog(
            onDismiss = { showNotifDialog = false },
            onSend = { title, msg, sev ->
                onSendNotification(title, msg, sev)
                showNotifDialog = false
            }
        )
    }
}
