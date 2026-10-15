package com.example.modules.auth

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark

@Composable
fun RegisterSuccessDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Pendaftaran Berhasil! 🎉",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DrgTextDark
            )
        },
        text = {
            Text(
                text = "Data registrasi Anda telah tersimpan. Pengurus / Satgas akan memverifikasi berkas KTA Anda.",
                fontSize = 13.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Lanjut ke Halaman Login",
                    fontWeight = FontWeight.Bold,
                    color = DrgGrabGreenPrimary
                )
            }
        }
    )
}
