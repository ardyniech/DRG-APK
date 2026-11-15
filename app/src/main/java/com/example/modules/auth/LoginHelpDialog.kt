package com.example.modules.auth

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
import com.example.core.sync.EmergencySmsFallbackHelper
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.utils.WhatsAppLauncher
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextSlate

@Composable
fun LoginHelpDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassModal(
        title = "Bantuan Akses & KTA DRG",
        subtitle = "Panduan masuk Google Sign-In & penautan KTA Malang Raya",
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
                        WhatsAppLauncher.openChat(
                            context,
                            EmergencySmsFallbackHelper.DEFAULT_HOTLINE_TRC,
                            "Halo Admin/Satgas DRG Malang Raya, saya butuh bantuan akses Google Sign-In & verifikasi KTA."
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1.3f)
                ) { Text("Chat Admin WA", color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        Text("Akses DRG Driver kini menggunakan Google Sign-In terverifikasi untuk keamanan data & sinkronisasi cloud.", fontSize = 12.sp, color = DrgTextSlate)
        Text("Jika mengalami kendala akun atau verifikasi KTA:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DrgTextSlate)
        Text("• Ketuk 'Masuk dengan Google' di layar utama dengan akun Google aktif Anda.", fontSize = 11.sp, color = DrgTextMuted)
        Text("• Belum punya KTA? Ketuk 'DAFTAR KTA' lalu hubungi Satgas Keanggotaan via WhatsApp.", fontSize = 11.sp, color = DrgTextMuted)
    }
}
