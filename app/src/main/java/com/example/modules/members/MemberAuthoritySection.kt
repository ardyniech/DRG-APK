package com.example.modules.members

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus
import com.example.ui.theme.*

@Composable
fun MemberAuthoritySection(
    member: DriverMember,
    isAdmin: Boolean,
    onUpdateRole: (String, MemberRole) -> Unit,
    onUpdateVerification: (String, VerificationStatus) -> Unit
) {
    var showRoleDropdown by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "🔧 Otoritas Pengurus & Peran", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DrgAmberSecondary)
        Text(text = "Ubah jabatan kepengurusan atau batasi status verifikasi.", fontSize = 10.sp, color = DrgTextSecondary)

        if (!isAdmin) {
            Text(
                text = "🔒 Mode Baca Saja: Hanya Ketua, Sekretaris, atau Dewan Etika (Admin) yang dapat mengubah peran/verifikasi.",
                fontSize = 9.sp, color = Color.Red, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 2.dp)
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { showRoleDropdown = true },
                enabled = isAdmin,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(36.dp)
            ) {
                Text("Peran: ${member.role.shortName} (Klik Ubah)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            DropdownMenu(expanded = showRoleDropdown, onDismissRequest = { showRoleDropdown = false }, modifier = Modifier.background(DrgSurface)) {
                MemberRole.values().forEach { r ->
                    DropdownMenuItem(text = { Text("${r.title} (${r.shortName})", fontSize = 11.sp) }, onClick = {
                        onUpdateRole(member.id, r)
                        showRoleDropdown = false
                    })
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Button(
                onClick = { onUpdateVerification(member.id, VerificationStatus.VERIFIED) },
                enabled = isAdmin,
                colors = ButtonDefaults.buttonColors(containerColor = DrgGreenContainer, disabledContainerColor = DrgOutline.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(6.dp), modifier = Modifier.weight(1f).height(32.dp), contentPadding = PaddingValues(0.dp)
            ) {
                Text("Aktifkan", color = if (isAdmin) DrgGreenPrimary else DrgTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { onUpdateVerification(member.id, VerificationStatus.SUSPENDED) },
                enabled = isAdmin,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE), disabledContainerColor = DrgOutline.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(6.dp), modifier = Modifier.weight(1f).height(32.dp), contentPadding = PaddingValues(0.dp)
            ) {
                Text("Suspend", color = if (isAdmin) Color.Red else DrgTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
