package com.example.modules.about.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.about.AboutApp
import com.example.modules.about.AboutBuild
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AboutHeaderCard(app: AboutApp, build: AboutBuild) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = DrgGlassSurfaceStrong,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 18.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(theme.pillBg)
                        .border(1.dp, theme.primaryColor.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Groups, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(28.dp))
                }
                Column {
                    Text(app.name, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = DrgTextDark)
                    Text(app.tagline, fontSize = 11.sp, color = DrgTextSecondary)
                }
            }
            Text(
                "Versi ${build.version} · commit ${build.commit} · ${build.commitDate}",
                fontSize = 10.5.sp,
                fontFamily = SpaceGroteskFontFamily,
                color = DrgTextMuted
            )
            Text("Sasaran: ${app.audience}", fontSize = 11.sp, color = DrgTextSecondary)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                app.philosophy.forEach { token ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(theme.pillBg)
                            .border(0.8.dp, theme.primaryColor.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(token, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
                    }
                }
            }
        }
    }
}
