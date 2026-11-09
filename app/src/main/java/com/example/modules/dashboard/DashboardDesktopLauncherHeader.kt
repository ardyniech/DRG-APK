package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGreenLight
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

/**
 * Header row for the desktop-style launcher card.
 *
 * @param activeCount number of launcher apps shown in the badge.
 * @param modifier optional layout modifier.
 */
@Composable
internal fun DashboardDesktopLauncherHeader(
    activeCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .testTag("launcher_header"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(DrgGreenPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.GridView,
                    contentDescription = null,
                    tint = DrgGreenPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column {
                Text(
                    "Launcher Aplikasi DRG",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DrgTextPrimary
                )
                Text(
                    "Pintasan Akses Cepat Ekosistem",
                    fontSize = 10.sp,
                    color = DrgTextSecondary
                )
            }
        }
        Surface(shape = RoundedCornerShape(12.dp), color = DrgGreenLight) {
            Text(
                "$activeCount Active",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = DrgGreenPrimary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
