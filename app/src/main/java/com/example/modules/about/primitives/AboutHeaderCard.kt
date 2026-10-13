package com.example.modules.about.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.about.AboutApp
import com.example.modules.about.AboutBuild
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AboutHeaderCard(app: AboutApp, build: AboutBuild) {
    val cardShape = RoundedCornerShape(26.dp)
    val highlightBrush = Brush.linearGradient(
        listOf(Color.White, Color.White.copy(alpha = 0.5f), DrgGreenPrimary.copy(alpha = 0.25f))
    )

    Surface(
        shape = cardShape,
        color = Color.White.copy(alpha = 0.88f),
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, cardShape, ambientColor = Color(0x14003617), spotColor = Color(0x0E003617))
            .border(1.2.dp, highlightBrush, cardShape)
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(52.dp).clip(RoundedCornerShape(16.dp)).background(DrgGreenContainer.copy(alpha = 0.8f)).border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Groups, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(28.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(app.name, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = DrgTextPrimary)
                    Text(app.tagline, fontSize = 11.sp, color = DrgTextSecondary)
                }
            }
            Text("Versi ${build.version} · commit ${build.commit} · ${build.commitDate}", fontSize = 10.5.sp, color = DrgTextMuted)
            Text("Sasaran: ${app.audience}", fontSize = 11.sp, color = DrgTextSecondary)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                app.philosophy.forEach { token ->
                    Surface(shape = RoundedCornerShape(10.dp), color = DrgGreenContainer.copy(alpha = 0.7f), modifier = Modifier.border(0.8.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(10.dp))) {
                        Text(token, fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold, color = DrgGreenPrimary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
            }
        }
    }
}
