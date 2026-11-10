package com.example.modules.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.update.UpdateRelease

/** Dialog changelog lengkap rilis update. */
@Composable
fun ProfileUpdateChangelogDialog(release: UpdateRelease, show: Boolean, onDismiss: () -> Unit) {
    if (!show) return
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Apa yang baru di v${release.versionName}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                release.changelog.forEach { item -> Text("• $item", fontSize = 12.sp) }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Tutup") } }
    )
}
