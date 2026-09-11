package com.example.modules.members

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
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
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var filterRoleByPengurusOnly by remember { mutableStateOf(false) }
    var filterStatusSelected by remember { mutableStateOf<VerificationStatus?>(null) }
    var selectedMemberForReview by remember { mutableStateOf<DriverMember?>(null) }
    var showExportDialog by remember { mutableStateOf(false) }
    val isLeadership = currentMember?.role?.isLeadership == true

    Column(modifier = modifier.fillMaxSize().background(DrgBackground).padding(horizontal = 16.dp)) {
        TabRow(selectedTabIndex = selectedTab, containerColor = DrgSurface, contentColor = DrgGreenPrimary, modifier = Modifier.padding(top = 10.dp)) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Anggota (${members.size})", fontWeight = FontWeight.Bold, fontSize = 10.sp) })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Posko (${poskoList.size})", fontWeight = FontWeight.Bold, fontSize = 10.sp) })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Atur Role & Izin", fontWeight = FontWeight.Bold, fontSize = 10.sp) })
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Riwayat (${adminLogs.size})", fontWeight = FontWeight.Bold, fontSize = 10.sp) })
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (selectedTab == 0) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onRecordKopdarAttendance, colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary), shape = RoundedCornerShape(10.dp), modifier = Modifier.weight(1f).height(38.dp), contentPadding = PaddingValues(horizontal = 6.dp)) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Absen", tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Absen Kopdar (+50 XP)", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
                if (isLeadership) {
                    Button(onClick = { selectedTab = 2 }, colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary), shape = RoundedCornerShape(10.dp), modifier = Modifier.height(38.dp), contentPadding = PaddingValues(horizontal = 8.dp)) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = "Role", tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Atur Otoritas", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (selectedTab == 0 || selectedTab == 1) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = searchQuery, onValueChange = { searchQuery = it },
                    placeholder = { Text(if (selectedTab == 0) "Cari nama atau plat..." else "Cari posko...", fontSize = 11.sp) },
                    singleLine = true, modifier = Modifier.weight(1f), shape = RoundedCornerShape(10.dp)
                )
                if (selectedTab == 0) {
                    Button(onClick = { showExportDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary), shape = RoundedCornerShape(10.dp), modifier = Modifier.height(48.dp)) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Ekspor", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Ekspor", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        when (selectedTab) {
            0 -> MembersTabSection(members = members, searchQuery = searchQuery, filterRoleByPengurusOnly = filterRoleByPengurusOnly, onTogglePengurusOnly = { filterRoleByPengurusOnly = it }, filterStatusSelected = filterStatusSelected, onSelectStatus = { filterStatusSelected = it }, onSelectMember = { selectedMemberForReview = it })
            1 -> PoskoTabSection(currentMember = currentMember, poskoList = poskoList, searchQuery = searchQuery)
            2 -> RolePermissionManagementScreen(members = members, currentMember = currentMember, onBack = { selectedTab = 0 }, onSavePermissions = onUpdateMemberPermissions)
            3 -> AdminLogsTabSection(adminLogs = adminLogs)
        }
    }

    if (showExportDialog) {
        val finalForExport = members.filter { m ->
            val matchesSearch = m.name.contains(searchQuery, ignoreCase = true) || m.motorcyclePlate.contains(searchQuery, ignoreCase = true)
            val matchesPengurus = !filterRoleByPengurusOnly || m.role != MemberRole.ANGGOTA
            val matchesStatus = filterStatusSelected == null || m.verificationStatus == filterStatusSelected
            matchesSearch && matchesPengurus && matchesStatus
        }
        ExportReportDialog(filteredMembers = finalForExport, filterRoleByPengurusOnly = filterRoleByPengurusOnly, filterStatusSelected = filterStatusSelected, searchQuery = searchQuery, onDismiss = { showExportDialog = false })
    }

    selectedMemberForReview?.let { member ->
        MemberManageDialog(member = member, currentMember = currentMember, onDismiss = { selectedMemberForReview = null }, onAddReview = onAddReview, onUpdateMemberRole = onUpdateMemberRole, onUpdateMemberVerification = onUpdateMemberVerification)
    }
}
