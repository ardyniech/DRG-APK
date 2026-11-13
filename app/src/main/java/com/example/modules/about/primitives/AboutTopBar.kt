package com.example.modules.about.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgTextPrimary

@Composable
fun AboutTopBar(onBack: () -> Unit) {
    val theme = LocalAppVisualTheme.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(theme.glassBg)
                .border(1.2.dp, Color.White.copy(alpha = 0.9f), CircleShape)
                .bouncyClick { onBack() }
                .testTag("about_back"),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = theme.primaryColor, modifier = Modifier.size(20.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text("Tentang Aplikasi", fontWeight = FontWeight.ExtraBold, fontSize = 17.sp, color = DrgTextPrimary)
            Text("Manifest SSOT, Arsitektur & Peta Fitur DRG", fontSize = 11.sp, color = theme.primaryColor, fontWeight = FontWeight.SemiBold)
        }
    }
}
