package com.example.modules.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGlassSurfaceBright
import com.example.ui.theme.DrgGrabGreenDark
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark

@Composable
fun LauncherIconItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    tag: String,
    isPrimary: Boolean = false,
    iconTint: Color = DrgGrabGreenPrimary
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .testTag(tag)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(
                    if (isPrimary) {
                        Brush.verticalGradient(listOf(DrgGrabGreenPrimary, DrgGrabGreenDark))
                    } else {
                        Brush.verticalGradient(listOf(DrgGlassSurfaceBright, DrgGlassSurfaceBright))
                    }
                )
                .border(1.dp, if (isPrimary) Color.Transparent else Color.White.copy(alpha = 0.8f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isPrimary) Color.White else iconTint,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isPrimary) FontWeight.Bold else FontWeight.Medium,
            color = if (isPrimary) DrgGrabGreenPrimary else DrgTextDark
        )
    }
}
