package com.example.modules.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.update.UpdateRelease
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgTextSecondary

@Composable
fun ProfileUpdateChangelogDialog(release: UpdateRelease, show: Boolean, onDismiss: () -> Unit) {
    if (!show) return
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassModal(
        title = "Apa yang baru di v${release.versionName}",
        subtitle = "Catatan rilis & peningkatan fitur DRG",
        icon = Icons.Default.NewReleases,
        accentColor = theme.primaryColor,
        onDismiss = onDismiss,
        footer = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Tutup", color = Color.White, fontWeight = FontWeight.Bold) }
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            release.changelog.forEach { item -> Text("• $item", fontSize = 12.sp, color = DrgTextSecondary) }
        }
    }
}
