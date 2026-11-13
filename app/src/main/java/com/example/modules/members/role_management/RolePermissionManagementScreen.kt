package com.example.modules.members.role_management

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.RoleManager
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.*
import com.example.shared.utils.bouncyClick
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
    var activeSubTab by remember { mutableIntStateOf(0) }
    val canEdit = remember(currentMember) { RoleManager.canManageRoles(currentMember) }
    val theme = LocalAppVisualTheme.current

    val filteredMembers = remember(members, searchQuery, selectedCategory) {
        members.filter { m ->
            val matchSearch = m.name.contains(searchQuery, true) || m.motorcyclePlate.contains(searchQuery, true) || m.driverId.contains(searchQuery, true)
            val matchCat = when (selectedCategory) {
                RoleFilterCategory.SEMUA -> true
                RoleFilterCategory.PENGURUS -> m.role in listOf(MemberRole.KETUA, MemberRole.WAKIL_KETUA, MemberRole.SEKRETARIS, MemberRole.BENDAHARA, MemberRole.DEWAN_ETIKA, MemberRole.SUPER_ADMIN)
                RoleFilterCategory.SATGAS -> m.role == MemberRole.SATGAS
                RoleFilterCategory.ANGGOTA -> m.role == MemberRole.ANGGOTA
            }
            matchSearch && matchCat
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(theme.glassBg).border(1.2.dp, Color.White.copy(alpha = 0.9f), CircleShape).bouncyClick { onBack() },
                contentAlignment = Alignment.Center
            ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DrgTextPrimary, modifier = Modifier.size(20.dp)) }
            Column(modifier = Modifier.weight(1f)) {
                Text("Tata Kelola Jabatan & Otoritas", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = DrgTextDark)
                Text(if (canEdit) "Wewenang Khusus Super Admin & Pengurus" else "Transparansi Struktur Organisasi DRG", fontSize = 11.sp, color = theme.primaryColor, fontWeight = FontWeight.SemiBold)
            }
        }

        DrgLuminousGlassCard(shape = RoundedCornerShape(18.dp), backgroundColor = theme.glassBg, borderHighlightColor = theme.glassBorderColor, contentPadding = 4.dp, modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("Daftar Anggota", "Matriks Otoritas").forEachIndexed { idx, label ->
                    val sel = activeSubTab == idx
                    Box(
                        modifier = Modifier.weight(1f).clip(RoundedCornerShape(13.dp)).background(if (sel) theme.primaryColor else Color.Transparent).bouncyClick { activeSubTab = idx }.padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) { Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (sel) Color.White else DrgTextSecondary) }
                }
            }
        }

        if (activeSubTab == 1) {
            RoleMatrixSection(modifier = Modifier.padding(bottom = 10.dp))
        } else {
            RoleSummaryStatsRow(members = members, modifier = Modifier.padding(bottom = 8.dp))
            OutlinedTextField(
                value = searchQuery, onValueChange = { searchQuery = it },
                placeholder = { Text("Cari nama, KTA, atau plat nomor...", fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = theme.primaryColor) },
                singleLine = true, modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp), shape = RoundedCornerShape(14.dp)
            )
            RoleFilterBar(selectedCategory = selectedCategory, onSelectCategory = { selectedCategory = it }, modifier = Modifier.padding(bottom = 8.dp))
            errorMessage?.let { msg ->
                Surface(shape = RoundedCornerShape(10.dp), color = DrgRedDanger.copy(alpha = 0.12f), modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Text(msg, color = DrgRedDanger, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(8.dp))
                }
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                items(filteredMembers) { member ->
                    RoleMemberCard(member = member, onManageClick = { selectedMemberForEdit = member }, canEdit = canEdit)
                }
            }
        }
    }

    selectedMemberForEdit?.let { target ->
        MemberRolePermissionDialog(
            member = target, onDismiss = { selectedMemberForEdit = null },
            onSave = { newRole, canKas, canVerify, canSos, canPosko, newStatus ->
                val (isValid, msg) = RoleManager.validateRoleChange(currentMember, target, newRole, members)
                if (isValid) {
                    errorMessage = null; onSavePermissions(target.id, newRole, canKas, canVerify, canSos, canPosko, newStatus); selectedMemberForEdit = null
                } else { errorMessage = msg }
            }
        )
    }
}
