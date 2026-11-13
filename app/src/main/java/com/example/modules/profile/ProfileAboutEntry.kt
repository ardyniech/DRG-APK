package com.example.modules.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.shared.atoms.DrgIconTileBox
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.AppVisualTheme
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextMuted

@Composable
fun ProfileAboutEntry(
    onOpenAbout: () -> Unit,
    visualTheme: AppVisualTheme = AppVisualTheme.EMERALD_AURORA,
    modifier: Modifier = Modifier
) {
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth().bouncyClick { onOpenAbout() }.testTag("btn_open_about"),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = visualTheme.glassBorderColor,
        contentPadding = 14.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                DrgIconTileBox(
                    icon = Icons.Default.Info,
                    tint = visualTheme.primaryColor,
                    size = 42.dp,
                    iconSize = 20.dp,
                    contentDescription = "Tentang Aplikasi"
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Tentang Aplikasi & Peta Jalan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                    Text("Spesifikasi Sistem v${BuildConfig.VERSION_NAME}", fontSize = 11.sp, color = DrgTextMuted)
                }
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = visualTheme.primaryColor,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}