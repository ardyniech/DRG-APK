package com.example.modules.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.ui.theme.DrgRedDanger
import com.example.ui.theme.DrgTextSecondary

@Composable
fun ProfileLogoutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    DrgLuminousGlassModal(
        title = "Konfirmasi Logout",
        subtitle = "Tutup sesi KTA Digital Anda",
        icon = Icons.AutoMirrored.Filled.Logout,
        accentColor = DrgRedDanger,
        onDismiss = onDismiss,
        footer = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Batal") }
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = DrgRedDanger),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) { Text("Ya, Keluar", fontWeight = FontWeight.Bold, color = Color.White) }
            }
        }
    ) {
        Text(
            "Apakah Anda yakin ingin keluar dari akun ini? Sesi Anda akan ditutup dengan aman.",
            fontSize = 12.sp,
            color = DrgTextSecondary
        )
    }
}
