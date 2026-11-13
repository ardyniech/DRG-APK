package com.example.modules.members

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.RoleManager
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus

@Composable
fun MemberManageDialog(
    member: DriverMember,
    currentMember: DriverMember?,
    onDismiss: () -> Unit,
    onAddReview: (String, Int, String) -> Unit,
    onUpdateMemberRole: (String, MemberRole) -> Unit,
    onUpdateMemberVerification: (String, VerificationStatus) -> Unit
) {
    val isAdmin = RoleManager.canManageRoles(currentMember)
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassModal(
        title = "Kelola & Apresiasi",
        subtitle = "${member.name} (${member.motorcyclePlate})",
        icon = Icons.Default.ManageAccounts,
        accentColor = theme.primaryColor,
        onDismiss = onDismiss,
        footer = {
            OutlinedButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Text("Tutup")
            }
        }
    ) {
        MemberReviewSection(
            memberId = member.id,
            onSubmitReview = { id, rating, comment ->
                onAddReview(id, rating, comment)
                onDismiss()
            }
        )
        HorizontalDivider(color = theme.glassBorderColor.copy(alpha = 0.45f), modifier = Modifier.padding(vertical = 4.dp))
        MemberAuthoritySection(
            member = member,
            isAdmin = isAdmin,
            onUpdateRole = onUpdateMemberRole,
            onUpdateVerification = onUpdateMemberVerification
        )
    }
}
