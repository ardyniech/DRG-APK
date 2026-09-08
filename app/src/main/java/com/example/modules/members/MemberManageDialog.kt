package com.example.modules.members

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.RoleManager
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import com.example.ui.theme.*

@Composable
fun MemberManageDialog(
    member: DriverMember,
    currentMember: DriverMember?,
    onDismiss: () -> Unit,
    onAddReview: (String, Int, String) -> Unit,
    onUpdateMemberRole: (String, MemberRole) -> Unit,
    onUpdateMemberVerification: (String, VerificationStatus) -> Unit
) {
    val currentUserRole = currentMember?.role ?: MemberRole.ANGGOTA
    val isAdmin = RoleManager.canManageRoles(currentUserRole) || RoleManager.canManageMembers(currentUserRole)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Kelola & Apresiasi: ${member.name}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary) },
        text = {
            Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                MemberReviewSection(memberId = member.id, onSubmitReview = { id, rating, comment ->
                    onAddReview(id, rating, comment)
                    onDismiss()
                })
                HorizontalDivider(color = DrgOutline.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 4.dp))
                MemberAuthoritySection(member = member, isAdmin = isAdmin, onUpdateRole = onUpdateMemberRole, onUpdateVerification = onUpdateMemberVerification)
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text("Tutup", color = DrgTextSecondary) } }
    )
}
