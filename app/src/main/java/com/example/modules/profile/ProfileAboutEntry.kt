package com.example.modules.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.ui.theme.*

@Composable
fun ProfileAboutEntry(onOpenAbout: () -> Unit, modifier: Modifier = Modifier) {
    val cardShape = RoundedCornerShape(20.dp)
    val highlightBrush = Brush.linearGradient(
        listOf(Color.White, Color.White.copy(alpha = 0.5f), DrgGreenPrimary.copy(alpha = 0.2f))
    )

    Surface(
        shape = cardShape,
        color = Color.White.copy(alpha = 0.85f),
        tonalElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, cardShape, ambientColor = Color(0x14003617), spotColor = Color(0x0E003617))
            .border(1.2.dp, highlightBrush, cardShape)
            .clickable(onClick = onOpenAbout)
    ) {
        Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DrgGreenContainer.copy(alpha = 0.8f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Tentang Aplikasi",
                        tint = DrgGreenPrimary,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Tentang Aplikasi & Sistem", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Text(
                        "Peta jalan & spesifikasi v${BuildConfig.VERSION_NAME}",
                        fontSize = 11.sp,
                        color = DrgTextMuted
                    )
                }
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = DrgGreenPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}