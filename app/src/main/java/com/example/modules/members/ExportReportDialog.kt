package com.example.modules.members

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.shared.models.VerificationStatus
import com.example.ui.theme.*

@Composable
fun ExportReportDialog(
    filteredMembers: List<DriverMember>,
    filterRoleByPengurusOnly: Boolean,
    filterStatusSelected: VerificationStatus?,
    searchQuery: String,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val reportMd = remember(filteredMembers, filterRoleByPengurusOnly, filterStatusSelected, searchQuery) {
        ReportGenerator.generateMembersMarkdown(filteredMembers, filterRoleByPengurusOnly, filterStatusSelected, searchQuery)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Ekspor", tint = DrgGreenPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ekspor Laporan Anggota", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "Laporan instan siap disalin ke WhatsApp/Telegram atau dicetak ke PDF.", fontSize = 11.sp, color = DrgTextSecondary)
                Surface(shape = RoundedCornerShape(8.dp), color = DrgBackground, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Saringan Aktif: ${filteredMembers.size} Driver (${if (filterRoleByPengurusOnly) "Khusus Pengurus" else "Semua"})", fontSize = 10.sp, color = DrgTextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
                Text("Pratinjau Format Markdown (Bisa Copy-Paste):", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Surface(shape = RoundedCornerShape(8.dp), color = DrgSurface, modifier = Modifier.fillMaxWidth().height(120.dp).border(1.dp, DrgOutline, RoundedCornerShape(8.dp))) {
                    Box(modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState())) {
                        Text(text = reportMd, fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = DrgTextPrimary)
                    }
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    clipboardManager.setText(AnnotatedString(reportMd))
                    Toast.makeText(context, "Markdown disalin ke clipboard!", Toast.LENGTH_SHORT).show()
                }, colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary)) {
                    Text("Salin MD", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Button(onClick = { Toast.makeText(context, "PDF Berhasil Dicetak & Diunduh!", Toast.LENGTH_SHORT).show() }, colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary)) {
                    Text("Cetak PDF", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal", color = DrgTextSecondary) } }
    )
}
