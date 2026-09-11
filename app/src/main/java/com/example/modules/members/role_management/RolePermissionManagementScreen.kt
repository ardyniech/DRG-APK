package com.example.modules.members.role_management

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.RoleManager
import com.example.shared.models.*
import com.example.ui.theme.*

@Composable
fun RolePermissionManagementScreen(
    members: List<DriverMember>,
    onBack: () -> Unit,
    onSavePermissions: (String, MemberRole, Boolean, Boolean, Boolean, Boolean, VerificationStatus) -> Unit,
    modifier: Modifier = Modifier,
    currentMember: DriverMember? = null
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(RoleFilterCategory.SEMUA) }
    var selectedMemberForEdit by remember { mutableStateOf<DriverMember?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val canEdit = remember(currentMember) { RoleManager.canManageRoles(currentMember) }

    val filteredMembers = remember(members, searchQuery, selectedCategory) {
        members.filter { m ->
            val matchSearch = m.name.contains(searchQuery, true) || m.motorcyclePlate.contains(searchQuery, true) || m.driverId.contains(searchQuery, true)
            val matchCat = when (selectedCategory) {
                RoleFilterCategory.SEMUA -> true
                RoleFilterCategory.PENGURUS -> m.role in listOf(MemberRole.KETUA, MemberRole.WAKIL_KETUA, MemberRole.SEKRETARIS, MemberRole.BENDAHARA, MemberRole.DEWAN_ETIKA)
                RoleFilterCategory.SATGAS -> m.role == MemberRole.SATGAS
                RoleFilterCategory.ANGGOTA -> m.role == MemberRole.ANGGOTA
            }
            matchSearch && matchCat
        }
    }

    Column(modifier = modifier.fillMaxSize().background(DrgBackground).padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali") }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text("Tata Kelola Jabatan & Otoritas", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DrgTextDark)
                Text(if (canEdit) "Wewenang Khusus Ketua & Pengurus DRG" else "Transparansi Struktur Organisasi DRG", fontSize = 11.sp, color = DrgTextMuted)
            }
        }

        RoleSummaryStatsRow(members = members, modifier = Modifier.padding(bottom = 8.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari nama, KTA, atau plat nomor...", fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DrgTextMuted) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
            shape = RoundedCornerShape(10.dp)
        )

        RoleFilterBar(selectedCategory = selectedCategory, onSelectCategory = { selectedCategory = it }, modifier = Modifier.padding(bottom = 8.dp))

        errorMessage?.let { msg ->
            Surface(shape = RoundedCornerShape(8.dp), color = DrgRedDanger.copy(alpha = 0.1f), modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                Text(msg, color = DrgRedDanger, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(8.dp))
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
            items(filteredMembers) { member ->
                RoleMemberCard(member = member, onManageClick = { selectedMemberForEdit = member }, canEdit = canEdit)
            }
        }
    }

    selectedMemberForEdit?.let { target ->
        MemberRolePermissionDialog(
            member = target,
            onDismiss = { selectedMemberForEdit = null },
            onSave = { newRole, canKas, canVerify, canSos, canPosko, newStatus ->
                val (isValid, msg) = RoleManager.validateRoleChange(currentMember, target, newRole, members)
                if (isValid) {
                    errorMessage = null
                    onSavePermissions(target.id, newRole, canKas, canVerify, canSos, canPosko, newStatus)
                    selectedMemberForEdit = null
                } else {
                    errorMessage = msg
                }
            }
        )
    }
}
