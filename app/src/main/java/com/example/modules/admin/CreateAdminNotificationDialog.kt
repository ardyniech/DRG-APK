package com.example.modules.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.NotificationSeverity
import com.example.ui.theme.*

@Composable
fun CreateAdminNotificationDialog(
    onDismiss: () -> Unit,
    onSend: (String, String, NotificationSeverity) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kirim Pengumuman Komunitas", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul Pengumuman", fontSize = 11.sp) }, singleLine = true)
                OutlinedTextField(value = msg, onValueChange = { msg = it }, label = { Text("Isi Pesan", fontSize = 11.sp) }, maxLines = 3)
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotBlank() && msg.isNotBlank()) onSend(title, msg, NotificationSeverity.WARNING) },
                colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary)
            ) { Text("Kirim Broadcast") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}
