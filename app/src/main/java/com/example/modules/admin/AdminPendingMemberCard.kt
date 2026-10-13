package com.example.modules.admin

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DrgSurface,
        modifier = Modifier.fillMaxWidth().border(1.dp, DrgAmberSecondary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(member.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                    Text("${member.motorcycleModel} • ${member.motorcyclePlate}", fontSize = 11.sp, color = DrgTextSecondary)
                    Text("NIK: ${member.nik} • SIM: ${member.simNumber}", fontSize = 10.sp, color = DrgTextMuted)
                }
            }
            if (canApprove) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onApprove,
                        colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text("Setujui (Approve)", fontSize = 11.sp, color = Color.White)
                    }
                    OutlinedButton(
                        onClick = { showRejectDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgRedPanic),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text("Tolak (Reject)", fontSize = 11.sp)
                    }
                }
            }
        }
    }

    if (showRejectDialog) {
        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            title = { Text("Tolak Dokumen Verifikasi", fontSize = 14.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Berikan alasan penolakan untuk ${member.name}:", fontSize = 12.sp)
                    OutlinedTextField(
                        value = reasonInput,
                        onValueChange = { reasonInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onReject(reasonInput)
                    showRejectDialog = false
                }) {
                    Text("Konfirmasi Tolak", color = DrgRedPanic)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
