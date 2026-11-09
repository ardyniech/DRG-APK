package com.example.modules.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgGlassPrimaryButton
import com.example.shared.atoms.DrgGlassSecondaryButton
import com.example.ui.theme.DrgTextMuted

@Composable
fun LandingAuthMenuButtons(
    onDismiss: () -> Unit,
    onQuickDemo: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DrgGlassPrimaryButton(
            text = "Masuk Cepat • Demo Ketua",
            icon = Icons.Default.Login,
            onClick = { onDismiss(); onQuickDemo() },
            modifier = Modifier.fillMaxWidth().testTag("btn_quick_demo")
        )

        DrgGlassSecondaryButton(
            text = "Masuk Anggota Terdaftar",
            icon = Icons.Default.Login,
            onClick = { onDismiss(); onLogin() },
            modifier = Modifier.fillMaxWidth().testTag("btn_to_login")
        )

        DrgGlassSecondaryButton(
            text = "Daftar Driver Baru",
            icon = Icons.Default.PersonAdd,
            onClick = { onDismiss(); onRegister() },
            modifier = Modifier.fillMaxWidth().testTag("btn_to_register")
        )

        DrgGlassSecondaryButton(
            text = "Tentang Fitur & Komunitas",
            icon = Icons.Default.Info,
            onClick = { onDismiss(); onAbout() },
            modifier = Modifier.fillMaxWidth().testTag("btn_to_about")
        )

        Text(
            text = "P2P Device-to-Device • Nol Server • Data Terenkripsi",
            fontSize = 10.sp,
            color = DrgTextMuted
        )
    }
}
