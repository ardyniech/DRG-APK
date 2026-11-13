package com.example.modules.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextSlate

@Composable
fun LoginHelpDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassModal(
        title = "Bantuan Lupa ID / KTA",
        subtitle = "Pemulihan akses anggota resmi DRG Malang Raya",
        icon = Icons.Default.SupportAgent,
        accentColor = theme.primaryColor,
        onDismiss = onDismiss,
        footer = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                    Text("Tutup", color = DrgTextMuted)
                }
                Button(
                    onClick = {
                        onDismiss()
                        Toast.makeText(context, "Membuka WA Admin DRG Malang...", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.3f)
                ) { Text("Chat Admin WA", color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        Text("Jangan khawatir, KTA digital Anda aman tersimpan di pangkalan data DRG Malang Raya.", fontSize = 12.sp, color = DrgTextSlate)
        Text("Untuk mengetahui ID atau reset PIN Anda, Anda bisa:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DrgTextSlate)
        Text("• Menanyakan kepada Ketua Basis / Admin Grup WA Basis Anda.", fontSize = 11.sp, color = DrgTextMuted)
        Text("• Menghubungi Satgas Pelayanan Keanggotaan DRG via WhatsApp.", fontSize = 11.sp, color = DrgTextMuted)
    }
}
