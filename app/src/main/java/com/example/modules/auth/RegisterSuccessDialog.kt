package com.example.modules.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgTextSlate

@Composable
fun RegisterSuccessDialog(onDismiss: () -> Unit) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassModal(
        title = "Pendaftaran Berhasil! 🎉",
        subtitle = "Data registrasi KTA Anda telah tercatat",
        icon = Icons.Default.Celebration,
        accentColor = theme.primaryColor,
        onDismiss = onDismiss,
        footer = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Lanjut ke Halaman Login", fontWeight = FontWeight.Bold, color = Color.White) }
        }
    ) {
        Text(
            "Data registrasi Anda telah tersimpan. Pengurus / Satgas akan memverifikasi berkas KTA Anda.",
            fontSize = 12.sp,
            color = DrgTextSlate
        )
    }
}
