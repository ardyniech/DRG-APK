package com.example.modules.members.role_management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.*
import com.example.ui.theme.DrgTextDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberRolePermissionDialog(
    member: DriverMember,
    onDismiss: () -> Unit,
    onSave: (MemberRole, Boolean, Boolean, Boolean, Boolean, VerificationStatus) -> Unit
) {
    var selectedRole by remember { mutableStateOf(member.role) }
    var canKas by remember { mutableStateOf(member.canManageKas) }
    var canVerify by remember { mutableStateOf(member.canVerifyDrivers) }
    var canSos by remember { mutableStateOf(member.canBroadcastSos) }
    var canPosko by remember { mutableStateOf(member.canManagePosko) }
    var isSuspended by remember { mutableStateOf(member.verificationStatus == VerificationStatus.SUSPENDED) }
    var roleDropdownExpanded by remember { mutableStateOf(false) }
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassModal(
        title = "Kelola Otoritas Anggota",
        subtitle = "${member.name} (${member.driverId})",
        icon = Icons.Default.ManageAccounts,
        accentColor = theme.primaryColor,
        onDismiss = onDismiss,
        footer = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Batal") }
                Button(
                    onClick = {
                        val finalStatus = if (isSuspended) VerificationStatus.SUSPENDED else VerificationStatus.VERIFIED
                        onSave(selectedRole, canKas, canVerify, canSos, canPosko, finalStatus)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) { Text("Simpan Otoritas", fontWeight = FontWeight.Bold, color = Color.White) }
            }
        }
    ) {
        Text("Jabatan / Peran Struktural:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
        ExposedDropdownMenuBox(expanded = roleDropdownExpanded, onExpandedChange = { roleDropdownExpanded = !roleDropdownExpanded }) {
            OutlinedTextField(
                value = selectedRole.title, onValueChange = {}, readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleDropdownExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(), shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(expanded = roleDropdownExpanded, onDismissRequest = { roleDropdownExpanded = false }) {
                MemberRole.values().filter { it != MemberRole.SUPER_ADMIN }.forEach { r ->
                    DropdownMenuItem(
                        text = { Text("${r.title} (${r.shortName})", fontSize = 12.sp) },
                        onClick = {
                            selectedRole = r; roleDropdownExpanded = false
                            if (r == MemberRole.BENDAHARA) canKas = true
                            if (r == MemberRole.SEKRETARIS) canVerify = true
                            if (r == MemberRole.SATGAS) { canSos = true; canPosko = true }
                        }
                    )
                }
            }
        }
        HorizontalDivider(color = theme.glassBorderColor.copy(alpha = 0.45f))
        Text("Izin Operasional Khusus (Toggle On/Off):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
        PermissionSwitchRow(Icons.Default.AccountBalanceWallet, "Kelola Kas Keuangan", "Akses buku kas & catat transaksi bendahara", canKas, { canKas = it })
        PermissionSwitchRow(Icons.Default.VerifiedUser, "Verifikasi Calon Anggota", "Hak menyetujui screening driver pendaftar", canVerify, { canVerify = it })
        PermissionSwitchRow(Icons.Default.Emergency, "Pancar Radar Darurat SOS", "Akses trigger sirine darurat & notifikasi bahaya", canSos, { canSos = it })
        PermissionSwitchRow(Icons.Default.Place, "Kelola Posko & Pemetaan", "Akses tambah & kelola posko serta hazard", canPosko, { canPosko = it })
        HorizontalDivider(color = theme.glassBorderColor.copy(alpha = 0.45f))
        PermissionSwitchRow(Icons.Default.Block, "Tangguhkan Akun (Suspend)", "Nonaktifkan hak akses aplikasi untuk anggota ini", isSuspended, { isSuspended = it })
    }
}
