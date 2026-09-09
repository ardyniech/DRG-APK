package com.example.modules.members.role_management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.*
import com.example.ui.theme.*

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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("Kelola Otoritas Anggota", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DrgTextDark)
                Text("${member.name} (${member.driverId})", fontSize = 12.sp, color = DrgTextMuted)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Jabatan / Peran Struktural:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                ExposedDropdownMenuBox(
                    expanded = roleDropdownExpanded,
                    onExpandedChange = { roleDropdownExpanded = !roleDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedRole.title,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleDropdownExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = roleDropdownExpanded,
                        onDismissRequest = { roleDropdownExpanded = false }
                    ) {
                        MemberRole.values().forEach { r ->
                            DropdownMenuItem(
                                text = { Text("${r.title} (${r.shortName})", fontSize = 12.sp) },
                                onClick = {
                                    selectedRole = r
                                    roleDropdownExpanded = false
                                    if (r == MemberRole.BENDAHARA) canKas = true
                                    if (r == MemberRole.SEKRETARIS) canVerify = true
                                    if (r == MemberRole.SATGAS) { canSos = true; canPosko = true }
                                }
                            )
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Text("Izin Operasional Khusus (Toggle On/Off):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)

                PermissionSwitchRow(Icons.Default.AccountBalanceWallet, "Kelola Kas Keuangan", "Akses buku kas & catat transaksi bendahara", canKas, { canKas = it })
                PermissionSwitchRow(Icons.Default.VerifiedUser, "Verifikasi Calon Anggota", "Hak menyetujui screening driver pendaftar", canVerify, { canVerify = it })
                PermissionSwitchRow(Icons.Default.Emergency, "Pancar Radar Darurat SOS", "Akses trigger sirine darurat & notifikasi bahaya", canSos, { canSos = it })
                PermissionSwitchRow(Icons.Default.Place, "Kelola Posko & Pemetaan", "Akses tambah & kelola posko serta hazard", canPosko, { canPosko = it })

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                PermissionSwitchRow(Icons.Default.Block, "Tangguhkan Akun (Suspend)", "Nonaktifkan hak akses aplikasi untuk anggota ini", isSuspended, { isSuspended = it })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalStatus = if (isSuspended) VerificationStatus.SUSPENDED else VerificationStatus.VERIFIED
                    onSave(selectedRole, canKas, canVerify, canSos, canPosko, finalStatus)
                },
                colors = ButtonDefaults.buttonColors(containerColor = DrgGrabGreenPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Simpan Otoritas", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = DrgTextMuted)
            }
        }
    )
}
