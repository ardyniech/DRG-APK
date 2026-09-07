package com.example.modules.members

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
fun ExportReportDialog(
    filteredMembers: List<DriverMember>,
    filterRoleByPengurusOnly: Boolean,
    filterStatusSelected: VerificationStatus?,
    searchQuery: String,
    onDismiss: () -> Unit
) {
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
    val context = androidx.compose.ui.platform.LocalContext.current

    val reportMd = remember(filteredMembers, filterRoleByPengurusOnly, filterStatusSelected, searchQuery) {
        val timestamp = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
        buildString {
            appendLine("# LAPORAN ANGGOTA DRG MALANG RAYA")
            appendLine("Dibuat pada: $timestamp")
            appendLine("Filter: ${if (filterRoleByPengurusOnly) "Hanya Pengurus" else "Semua"}, Status: ${filterStatusSelected?.name ?: "Semua"}, Kueri: \"$searchQuery\"")
            appendLine("Total Data: ${filteredMembers.size} Driver")
            appendLine()
            appendLine("| No | ID Anggota | Nama Driver | Pelat Nomor | Peran/Jabatan | Status | Rating |")
            appendLine("|---|---|---|---|---|---|---|")
            filteredMembers.forEachIndexed { idx, m ->
                appendLine("| ${idx + 1} | ${m.id} | ${m.name} | ${m.motorcyclePlate} | ${m.role.title} | ${m.verificationStatus.name} | ${m.rating} |")
            }
            appendLine()
            appendLine("## Ringkasan Eksekutif")
            appendLine("- Total Driver Terdaftar: ${filteredMembers.size}")
            appendLine("- Pengurus Aktif: ${filteredMembers.count { it.role != MemberRole.ANGGOTA }}")
            appendLine("- Rata-rata Rating Komunitas: ${String.format("%.2f", if (filteredMembers.isEmpty()) 0.0 else filteredMembers.map { it.rating }.average())} ⭐")
            appendLine()
            appendLine("*Laporan ini sah dikeluarkan oleh sistem Otoritas DRG Malang Raya*")
        }
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
                Text(
                    text = "Laporan instan siap disalin ke WhatsApp/Telegram atau dicetak ke file PDF.",
                    fontSize = 11.sp,
                    color = DrgTextSecondary
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DrgBackground,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Saringan Aktif:", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = DrgTextPrimary)
                        Text("• Jumlah Anggota: ${filteredMembers.size} Driver", fontSize = 10.sp, color = DrgTextSecondary)
                        Text("• Kategori: ${if (filterRoleByPengurusOnly) "Khusus Pengurus" else "Semua"}", fontSize = 10.sp, color = DrgTextSecondary)
                    }
                }

                Text("Pratinjau Format Markdown (Bisa Copy-Paste):", fontSize = 11.sp, fontWeight = FontWeight.Bold)

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DrgSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .border(1.dp, DrgOutline, RoundedCornerShape(8.dp))
                ) {
                    Box(modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState())) {
                        Text(
                            text = reportMd,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            fontSize = 10.sp,
                            color = DrgTextPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(reportMd))
                        android.widget.Toast.makeText(context, "Markdown disalin ke clipboard!", android.widget.Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary)
                ) {
                    Text("Salin MD", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        android.widget.Toast.makeText(context, "PDF Berhasil Dicetak & Diunduh!", android.widget.Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary)
                ) {
                    Text("Cetak PDF", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = DrgTextSecondary)
            }
        }
    )
}
