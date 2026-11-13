package com.example.modules.members

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
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
    val theme = LocalAppVisualTheme.current
    val reportMd = remember(filteredMembers, filterRoleByPengurusOnly, filterStatusSelected, searchQuery) {
        ReportGenerator.generateMembersMarkdown(filteredMembers, filterRoleByPengurusOnly, filterStatusSelected, searchQuery)
    }

    DrgLuminousGlassModal(
        title = "Ekspor Laporan Anggota",
        subtitle = "Siap disalin ke WhatsApp/Telegram atau dicetak PDF",
        icon = Icons.Default.Share,
        accentColor = theme.primaryColor,
        onDismiss = onDismiss,
        footer = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(0.8f), shape = RoundedCornerShape(12.dp)) { Text("Batal", fontSize = 11.sp) }
                Button(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(reportMd))
                        Toast.makeText(context, "Markdown disalin ke clipboard!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                    shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)
                ) { Text("Salin MD", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                Button(
                    onClick = { Toast.makeText(context, "PDF Berhasil Dicetak & Diunduh!", Toast.LENGTH_SHORT).show() },
                    colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary),
                    shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)
                ) { Text("Cetak PDF", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        DrgLuminousGlassCard(shape = RoundedCornerShape(12.dp), backgroundColor = theme.pillBg, borderHighlightColor = theme.glassBorderColor, contentPadding = 10.dp, modifier = Modifier.fillMaxWidth()) {
            Text(
                "Saringan Aktif: ${filteredMembers.size} Driver (${if (filterRoleByPengurusOnly) "Khusus Pengurus" else "Semua"})",
                fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, color = DrgTextPrimary, fontWeight = FontWeight.Bold
            )
        }
        Text("Pratinjau Format Markdown (Bisa Copy-Paste):", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        DrgLuminousGlassCard(shape = RoundedCornerShape(12.dp), backgroundColor = theme.glassBg, borderHighlightColor = theme.glassBorderColor, contentPadding = 10.dp, modifier = Modifier.fillMaxWidth().height(130.dp)) {
            Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(reportMd, fontFamily = FontFamily.Monospace, fontSize = 10.sp, color = DrgTextPrimary)
            }
        }
    }
}
