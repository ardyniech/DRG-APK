package com.example.modules.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun PendingMemberCard(
    member: DriverMember,
    canApprove: Boolean,
    onApprove: () -> Unit,
    onReject: (String) -> Unit
) {
    var showRejectDialog by remember { mutableStateOf(false) }
    var reasonInput by remember { mutableStateOf("Dokumen SIM/KTP kurang jelas") }
    var validationError by remember { mutableStateOf<String?>(null) }
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassCard(
        shape = RoundedCornerShape(18.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = DrgAmberSecondary.copy(alpha = 0.65f),
        contentPadding = 12.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(member.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                    Text("${member.motorcycleModel} • ${member.motorcyclePlate}", fontSize = 11.sp, color = DrgTextSecondary)
                    Text("NIK: ${member.nik} • SIM: ${member.simNumber}", fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, color = DrgTextMuted)
                }
            }
            if (canApprove) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                    ) { Text("Setujui (Approve)", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold) }
                    OutlinedButton(
                        onClick = { showRejectDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgRedPanic),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                    ) { Text("Tolak (Reject)", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }

    if (showRejectDialog) {
        DrgLuminousGlassModal(
            title = "Tolak Dokumen Verifikasi",
            subtitle = "Berikan alasan penolakan untuk ${member.name}",
            icon = Icons.Default.Cancel,
            accentColor = DrgRedPanic,
            validationError = validationError,
            onDismiss = { showRejectDialog = false; validationError = null },
            footer = {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { showRejectDialog = false; validationError = null }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Batal") }
                    Button(
                        onClick = {
                            if (reasonInput.isBlank()) { validationError = "Alasan penolakan wajib diisi agar driver tahu apa yang harus diperbaiki."; return@Button }
                            onReject(reasonInput.trim())
                            showRejectDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DrgRedPanic),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) { Text("Konfirmasi Tolak", color = Color.White, fontWeight = FontWeight.Bold) }
                }
            }
        ) {
            OutlinedTextField(
                value = reasonInput,
                onValueChange = { reasonInput = it; validationError = null },
                label = { Text("Alasan Penolakan", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
            )
        }
    }
}
