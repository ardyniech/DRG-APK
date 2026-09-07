package com.example.modules.members

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var rating by remember { mutableStateOf(5) }
    var comment by remember { mutableStateOf("") }
    var showRoleDropdown by remember { mutableStateOf(false) }

    val currentUserRole = currentMember?.role ?: MemberRole.ANGGOTA
    val isAdmin = RoleManager.canManageRoles(currentUserRole) || RoleManager.canManageMembers(currentUserRole)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Kelola & Apresiasi: ${member.name}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DrgTextPrimary
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // PART 1: Peer Review
                Text(
                    text = "Apresiasi & Ulasan Solidaritas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = DrgGreenPrimary
                )
                Text(
                    text = "Beri rating etika berkendara dan kesetiakawanan di jalan.",
                    fontSize = 10.sp,
                    color = DrgTextSecondary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    (1..5).forEach { star ->
                        IconButton(
                            onClick = { rating = star },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$star Bintang",
                                tint = if (star <= rating) DrgAmberSecondary else DrgTextMuted,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    placeholder = { Text("Contoh: Rekan sangat solid membantu mogok...", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth().height(70.dp),
                    shape = RoundedCornerShape(8.dp)
                )

                Button(
                    onClick = {
                        onAddReview(member.id, rating, comment)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(36.dp)
                ) {
                    Text("Kirim Ulasan & Rating", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                HorizontalDivider(color = DrgOutline.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 4.dp))

                // PART 2: Admin Authority & Role Management
                Text(
                    text = "🔧 Otoritas Pengurus & Peran",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = DrgAmberSecondary
                )
                Text(
                    text = "Ubah jabatan kepengurusan atau batasi status verifikasi.",
                    fontSize = 10.sp,
                    color = DrgTextSecondary
                )

                if (!isAdmin) {
                    Text(
                        text = "🔒 Mode Baca Saja: Hanya Ketua, Sekretaris, atau Dewan Etika (Admin) yang dapat mengubah peran/verifikasi.",
                        fontSize = 9.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }

                // Role Dropdown / Selector
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { showRoleDropdown = true },
                        enabled = isAdmin,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(36.dp)
                    ) {
                        Text("Peran: ${member.role.shortName} (Klik Ubah)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    DropdownMenu(
                        expanded = showRoleDropdown,
                        onDismissRequest = { showRoleDropdown = false },
                        modifier = Modifier.background(DrgSurface)
                    ) {
                        MemberRole.values().forEach { r ->
                            DropdownMenuItem(
                                text = { Text("${r.title} (${r.shortName})", fontSize = 11.sp) },
                                onClick = {
                                    onUpdateMemberRole(member.id, r)
                                    showRoleDropdown = false
                                }
                            )
                        }
                    }
                }

                // Verification Status Quick Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = { onUpdateMemberVerification(member.id, VerificationStatus.VERIFIED) },
                        enabled = isAdmin,
                        colors = ButtonDefaults.buttonColors(containerColor = DrgGreenContainer, disabledContainerColor = DrgOutline.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.weight(1f).height(32.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Aktifkan", color = if (isAdmin) DrgGreenPrimary else DrgTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onUpdateMemberVerification(member.id, VerificationStatus.SUSPENDED) },
                        enabled = isAdmin,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE), disabledContainerColor = DrgOutline.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.weight(1f).height(32.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Suspend", color = if (isAdmin) Color.Red else DrgTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup", color = DrgTextSecondary)
            }
        }
    )
}
