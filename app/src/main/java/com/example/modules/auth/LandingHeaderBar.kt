package com.example.modules.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.ui.theme.DrgGlassSurfaceBright
import com.example.ui.theme.DrgGrabGreenContainer
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSlate

@Composable
fun LandingHeaderBar(
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(DrgGrabGreenContainer)
                    .border(1.5.dp, DrgGrabGreenPrimary.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.TwoWheeler,
                    contentDescription = null,
                    tint = DrgGrabGreenPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column {
                Text(
                    text = "DRG DRIVER",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    color = DrgTextDark
                )
                Text(
                    text = "Malang Raya • Kompak & Solid",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = DrgTextSlate
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(DrgGlassSurfaceBright)
                    .border(1.dp, DrgGrabGreenPrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "🟢 1,248 Online",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgGrabGreenPrimary
                )
            }

            IconButton(
                onClick = onAboutClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DrgGlassSurfaceBright)
                    .testTag("btn_landing_about")
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Tentang DRG",
                    tint = DrgTextDark,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
