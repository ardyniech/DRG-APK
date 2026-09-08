package com.example.modules.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LoginHelpDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Bantuan Lupa ID / KTA", fontWeight = FontWeight.Bold, color = DrgTextDark) },
        text = {
            Column {
                Text("Jangan khawatir, KTA digital Anda aman tersimpan di pangkalan data DRG Malang Raya.", fontSize = 13.sp, color = DrgTextSlate)
                Spacer(modifier = Modifier.height(10.dp))
                Text("Untuk mengetahui ID Anda, Anda bisa:", fontSize = 13.sp, color = DrgTextSlate)
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Menanyakan kepada Ketua Basis / Admin Grup WA Basis Anda.", fontSize = 12.sp, color = DrgTextMuted)
                Spacer(modifier = Modifier.height(2.dp))
                Text("• Menghubungi Satgas Pelayanan Keanggotaan DRG via WhatsApp.", fontSize = 12.sp, color = DrgTextMuted)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    Toast.makeText(context, "Membuka WA Admin DRG Malang...", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = DrgGrabGreenPrimary)
            ) {
                Text("Chat Admin WA", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup", color = DrgTextMuted)
            }
        }
    )
}
