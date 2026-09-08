package com.example.modules.members

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import com.example.ui.theme.*

@Composable
fun MembersTabSection(
    members: List<DriverMember>,
    searchQuery: String,
    filterRoleByPengurusOnly: Boolean,
    onTogglePengurusOnly: (Boolean) -> Unit,
    filterStatusSelected: VerificationStatus?,
    onSelectStatus: (VerificationStatus?) -> Unit,
    onSelectMember: (DriverMember) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Saring:", fontSize = 11.sp, color = DrgTextSecondary, fontWeight = FontWeight.Bold)
            FilterChip(
                selected = !filterRoleByPengurusOnly && filterStatusSelected == null,
                onClick = {
                    onTogglePengurusOnly(false)
                    onSelectStatus(null)
                },
                label = { Text("Semua", fontSize = 10.sp) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgGreenContainer, selectedLabelColor = DrgGreenPrimary)
            )
            FilterChip(
                selected = filterRoleByPengurusOnly,
                onClick = {
                    val next = !filterRoleByPengurusOnly
                    onTogglePengurusOnly(next)
                    if (next) onSelectStatus(null)
                },
                label = { Text("Pengurus", fontSize = 10.sp) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgGreenContainer, selectedLabelColor = DrgGreenPrimary)
            )
            FilterChip(
                selected = filterStatusSelected == VerificationStatus.VERIFIED,
                onClick = {
                    val next = if (filterStatusSelected == VerificationStatus.VERIFIED) null else VerificationStatus.VERIFIED
                    onSelectStatus(next)
                    if (next != null) onTogglePengurusOnly(false)
                },
                label = { Text("Aktif", fontSize = 10.sp) },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgGreenContainer, selectedLabelColor = DrgGreenPrimary)
            )
        }

        val filteredMembers = members.filter { m ->
            val matchesSearch = m.name.contains(searchQuery, ignoreCase = true) || m.motorcyclePlate.contains(searchQuery, ignoreCase = true)
            val matchesPengurus = !filterRoleByPengurusOnly || m.role != MemberRole.ANGGOTA
            val matchesStatus = filterStatusSelected == null || m.verificationStatus == filterStatusSelected
            matchesSearch && matchesPengurus && matchesStatus
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
            items(filteredMembers) { m ->
                DriverMemberCard(member = m, onMemberClick = { onSelectMember(m) })
            }
        }
    }
}
