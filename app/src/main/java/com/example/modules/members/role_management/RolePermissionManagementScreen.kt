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
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.*
import com.example.ui.theme.*

@Composable
fun RolePermissionManagementScreen(
    members: List<DriverMember>,
    onBack: () -> Unit,
    onSavePermissions: (memberId: String, role: MemberRole, canKas: Boolean, canVerify: Boolean, canSos: Boolean, canPosko: Boolean, status: VerificationStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedMemberForEdit by remember { mutableStateOf<DriverMember?>(null) }

    val filteredMembers = remember(members, searchQuery) {
        members.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.motorcyclePlate.contains(searchQuery, ignoreCase = true) ||
            it.driverId.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text("Tata Kelola Jabatan & Izin", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DrgTextDark)
                Text("Transparansi otoritas struktural komunitas DRG", fontSize = 11.sp, color = DrgTextMuted)
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari nama, plat, atau ID anggota...", fontSize = 12.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DrgTextMuted) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            shape = RoundedCornerShape(10.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DrgAmberSecondary.copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxWidth().border(1.dp, DrgAmberSecondary.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = DrgAmberSecondary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Seluruh perubahan jabatan dan toggle izin operasional akan otomatis tercatat di Tab Riwayat.",
                            fontSize = 11.sp,
                            color = DrgTextDark
                        )
                    }
                }
            }

            items(filteredMembers) { member ->
                RoleMemberCard(
                    member = member,
                    onManageClick = { selectedMemberForEdit = member }
                )
            }
        }
    }

    selectedMemberForEdit?.let { target ->
        MemberRolePermissionDialog(
            member = target,
            onDismiss = { selectedMemberForEdit = null },
            onSave = { newRole, canKas, canVerify, canSos, canPosko, newStatus ->
                onSavePermissions(target.id, newRole, canKas, canVerify, canSos, canPosko, newStatus)
                selectedMemberForEdit = null
            }
        )
    }
}
