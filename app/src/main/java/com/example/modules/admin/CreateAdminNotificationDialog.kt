package com.example.modules.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.NotificationSeverity

@Composable
fun CreateAdminNotificationDialog(
    onDismiss: () -> Unit,
    onSend: (String, String, NotificationSeverity) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    var severity by remember { mutableStateOf(NotificationSeverity.WARNING) }
    var validationError by remember { mutableStateOf<String?>(null) }
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassModal(
        title = "Kirim Pengumuman Komunitas",
        subtitle = "Siaran langsung muncul di beranda seluruh driver",
        icon = Icons.Default.Campaign,
        accentColor = theme.primaryColor,
        validationError = validationError,
        onDismiss = onDismiss,
        footer = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Batal") }
                Button(
                    onClick = {
                        when {
                            title.isBlank() -> validationError = "Judul pengumuman wajib diisi."
                            msg.isBlank() -> validationError = "Isi pesan pengumuman wajib diisi."
                            else -> onSend(title.trim(), msg.trim(), severity)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) { Text("Kirim Broadcast", color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(NotificationSeverity.INFO to "Info", NotificationSeverity.WARNING to "Penting", NotificationSeverity.SUCCESS to "Apresiasi").forEach { (sev, label) ->
                FilterChip(
                    selected = severity == sev, onClick = { severity = sev },
                    label = { Text(label, fontSize = 10.sp) }, modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = theme.primaryColor, selectedLabelColor = Color.White)
                )
            }
        }
        OutlinedTextField(
            value = title, onValueChange = { title = it; validationError = null },
            label = { Text("Judul Pengumuman", fontSize = 11.sp) }, singleLine = true,
            shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = msg, onValueChange = { msg = it; validationError = null },
            label = { Text("Isi Pesan", fontSize = 11.sp) }, maxLines = 3,
            shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
        )
    }
}
