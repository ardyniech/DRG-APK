package com.example.modules.members

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.members.role_management.RolePermissionManagementScreen
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.*
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgAmberSecondary
import com.example.ui.theme.DrgTextDark

@Composable
fun MembersAndPoskoScreen(
    currentMember: DriverMember?,
    members: List<DriverMember>,
    poskoList: List<PoskoLocation>,
    adminLogs: List<AdminLog> = emptyList(),
    onRecordKopdarAttendance: () -> Unit,
    onAddReview: (String, Int, String) -> Unit,
    onUpdateMemberRole: (String, MemberRole) -> Unit,
    onUpdateMemberVerification: (String, VerificationStatus) -> Unit,
    onUpdateMemberPermissions: (String, MemberRole, Boolean, Boolean, Boolean, Boolean, VerificationStatus) -> Unit = { _, _, _, _, _, _, _ -> },
    onAddPosko: (String, String, String, Double, Double) -> Unit = { _, _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    val accent = theme.primaryColor
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var filterRoleByPengurusOnly by remember { mutableStateOf(false) }
    var filterStatusSelected by remember { mutableStateOf<VerificationStatus?>(null) }
    var selectedMemberForReview by remember { mutableStateOf<DriverMember?>(null) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showAddPoskoDialog by remember { mutableStateOf(false) }
    val isLeadership = currentMember?.role?.isLeadership == true
    val tabs = listOf("Anggota (${members.size})", "Posko (${poskoList.size})", "Otoritas", "Riwayat (${adminLogs.size})")

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 14.dp)) {
        DrgLuminousGlassCard(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), shape = RoundedCornerShape(18.dp), backgroundColor = theme.glassBg, borderHighlightColor = theme.glassBorderColor, contentPadding = 4.dp) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                itemsIndexed(tabs) { idx, title ->
                    val active = selectedTab == idx
                    Box(modifier = Modifier.clip(RoundedCornerShape(14.dp)).background(if (active) accent else Color.White.copy(alpha = 0.65f)).border(1.dp, if (active) accent else Color.White, RoundedCornerShape(14.dp)).bouncyClick { selectedTab = idx }.padding(horizontal = 11.dp, vertical = 8.dp)) {
                        Text(title, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = if (active) Color.White else DrgTextDark)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (selectedTab == 0) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onRecordKopdarAttendance, colors = ButtonDefaults.buttonColors(containerColor = accent), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f).height(40.dp)) {
                    Icon(Icons.Default.CheckCircle, "Absen", tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Absen Kopdar (+50 XP)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                }
                if (isLeadership) {
                    Button(onClick = { selectedTab = 2 }, colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary), shape = RoundedCornerShape(12.dp), modifier = Modifier.height(40.dp)) {
                        Icon(Icons.Default.Security, "Role", tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Otoritas", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (selectedTab == 0 || selectedTab == 1) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = searchQuery, onValueChange = { searchQuery = it }, placeholder = { Text(if (selectedTab == 0) "Cari nama atau plat..." else "Cari posko...", fontSize = 11.sp) }, singleLine = true, modifier = Modifier.weight(1f), shape = RoundedCornerShape(14.dp))
                if (selectedTab == 0) {
                    Button(onClick = { showExportDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary), shape = RoundedCornerShape(14.dp), modifier = Modifier.height(48.dp)) {
                        Icon(Icons.Default.Share, "Ekspor", modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("Ekspor", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                if (selectedTab == 1 && currentMember?.canManagePosko == true) {
                    Button(onClick = { showAddPoskoDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = accent), shape = RoundedCornerShape(14.dp), modifier = Modifier.height(48.dp)) {
                        Icon(Icons.Default.Add, "Tambah Posko", modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(4.dp)); Text("Tambah", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        when (selectedTab) {
            0 -> MembersTabSection(members, searchQuery, filterRoleByPengurusOnly, { filterRoleByPengurusOnly = it }, filterStatusSelected, { filterStatusSelected = it }, { selectedMemberForReview = it })
            1 -> PoskoTabSection(currentMember, poskoList, searchQuery)
            2 -> RolePermissionManagementScreen(members = members, currentMember = currentMember, onBack = { selectedTab = 0 }, onSavePermissions = onUpdateMemberPermissions)
            3 -> AdminLogsTabSection(adminLogs = adminLogs)
        }
    }
    if (showExportDialog) {
        val finalForExport = members.filter { m -> (m.name.contains(searchQuery, true) || m.motorcyclePlate.contains(searchQuery, true)) && (!filterRoleByPengurusOnly || m.role != MemberRole.ANGGOTA) && (filterStatusSelected == null || m.verificationStatus == filterStatusSelected) }
        ExportReportDialog(finalForExport, filterRoleByPengurusOnly, filterStatusSelected, searchQuery, onDismiss = { showExportDialog = false })
    }
    if (showAddPoskoDialog) {
        PoskoAddDialog(currentMember?.currentLat ?: -7.9839, currentMember?.currentLng ?: 112.6214, { showAddPoskoDialog = false }) { name, area, phone, lat, lng -> onAddPosko(name, area, phone, lat, lng); showAddPoskoDialog = false }
    }
    selectedMemberForReview?.let { MemberManageDialog(it, currentMember, { selectedMemberForReview = null }, onAddReview, onUpdateMemberRole, onUpdateMemberVerification) }
}
