package com.example.modules.members

import androidx.compose.foundation.BorderStroke
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
import com.example.shared.atoms.LocalAppVisualTheme
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
    val theme = LocalAppVisualTheme.current
    var showRoleDropdown by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Otoritas Pengurus & Peran", fontWeight = FontWeight.Bold, fontSize = 11.5.sp, color = theme.primaryColor)
        Text(text = "Ubah jabatan kepengurusan atau batasi status verifikasi.", fontSize = 10.sp, color = DrgTextSecondary)

        if (!isAdmin) {
            Text(
                text = "Mode Baca Saja: Hanya Ketua, Sekretaris, atau Dewan Etika (Admin) yang dapat mengubah peran/verifikasi.",
                fontSize = 9.5.sp,
                color = DrgRedPanic,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { showRoleDropdown = true },
                enabled = isAdmin,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, theme.primaryColor.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth().height(40.dp)
            ) {
                Text("Peran: ${member.role.shortName} (Klik Ubah)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isAdmin) theme.primaryColor else DrgTextMuted)
            }
            DropdownMenu(expanded = showRoleDropdown, onDismissRequest = { showRoleDropdown = false }, modifier = Modifier.background(Color(0xFAFFFFFF))) {
                MemberRole.entries.forEach { r ->
                    DropdownMenuItem(
                        text = { Text("${r.title} (${r.shortName})", fontSize = 11.sp, color = DrgTextDark) },
                        onClick = { onUpdateRole(member.id, r); showRoleDropdown = false }
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { onUpdateVerification(member.id, VerificationStatus.VERIFIED) },
                enabled = isAdmin,
                colors = ButtonDefaults.buttonColors(containerColor = theme.pillBg, disabledContainerColor = DrgOutline.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).height(38.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Aktifkan", color = if (isAdmin) theme.primaryColor else DrgTextMuted, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { onUpdateVerification(member.id, VerificationStatus.SUSPENDED) },
                enabled = isAdmin,
                colors = ButtonDefaults.buttonColors(containerColor = DrgRedPanic.copy(alpha = 0.14f), disabledContainerColor = DrgOutline.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f).height(38.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Suspend", color = if (isAdmin) DrgRedPanic else DrgTextMuted, fontSize = 10.5.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
