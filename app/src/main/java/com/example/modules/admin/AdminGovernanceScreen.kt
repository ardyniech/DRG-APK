package com.example.modules.admin

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
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.*
import com.example.shared.utils.rememberSharedRecordBadgeState
import com.example.ui.theme.*

@Composable
fun AdminGovernanceScreen(
    currentMember: DriverMember?,
    members: List<DriverMember>,
    onApproveScreening: (String) -> Unit,
    onRejectScreening: (String, String) -> Unit = { _, _ -> },
    onSendNotification: (String, String, NotificationSeverity) -> Unit,
    onOpenRoleManagement: () -> Unit = {},
    onUpdateMemberPermissions: (String, MemberRole, Boolean, Boolean, Boolean, Boolean, VerificationStatus) -> Unit = { _, _, _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    var showNotifDialog by remember { mutableStateOf(false) }
    val isLeadershipOrAdmin = currentMember?.role?.isLeadership == true || currentMember?.role?.name in listOf("SEKRETARIS", "ADMIN", "SUPER_ADMIN")
    val pendingScreenings = remember(members) { members.filter { it.verificationStatus == VerificationStatus.PENDING_SCREENING } }
    val theme = LocalAppVisualTheme.current
    val badgeState = rememberSharedRecordBadgeState(listOf("admin"))
    LaunchedEffect(pendingScreenings.size) { badgeState.markSeen("admin", pendingScreenings.size) }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item { AdminGovernanceHeaderCard(currentMember = currentMember) }
        item { AiCommunityHealthCard(memberCount = members.size) }

        if (isLeadershipOrAdmin) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onOpenRoleManagement,
                        colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f).height(46.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(Icons.Default.ManageAccounts, contentDescription = "Role", tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Atur Jabatan & Izin", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                    }
                    Button(
                        onClick = { showNotifDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.weight(1f).height(46.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(Icons.Default.Campaign, contentDescription = "Broadcast", tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Kirim Siaran", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                    }
                }
            }
        }

        item { Text("Pendaftaran Driver Baru Dalam Verifikasi", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DrgTextPrimary) }

        if (pendingScreenings.isEmpty()) {
            item {
                DrgLuminousGlassCard(shape = RoundedCornerShape(18.dp), backgroundColor = theme.glassBg, borderHighlightColor = theme.glassBorderColor, contentPadding = 16.dp, modifier = Modifier.fillMaxWidth()) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Semua calon anggota telah diverifikasi.", fontSize = 12.sp, color = DrgTextSecondary)
                    }
                }
            }
        } else {
            items(pendingScreenings) { pending ->
                PendingMemberCard(pending, isLeadershipOrAdmin, { onApproveScreening(pending.id) }, { reason -> onRejectScreening(pending.id, reason) })
            }
        }

        item {
            DrgLuminousGlassCard(shape = RoundedCornerShape(18.dp), backgroundColor = theme.glassBg, borderHighlightColor = theme.glassBorderColor, contentPadding = 14.dp, modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("📋 Evaluasi Kode Etik: KONDUSIF", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = theme.primaryColor)
                    Text("Dewan Etika memantau rating & ulasan dari sesama driver.", fontSize = 11.sp, color = DrgTextSecondary)
                }
            }
        }
    }

    if (showNotifDialog) {
        CreateAdminNotificationDialog(
            onDismiss = { showNotifDialog = false },
            onSend = { title, msg, sev -> onSendNotification(title, msg, sev); showNotifDialog = false }
        )
    }
}
