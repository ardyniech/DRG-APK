package com.example.shared.atoms.cinematic.rider

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgClusterBtnBg
import com.example.ui.theme.DrgClusterBtnBorder
import com.example.ui.theme.DrgClusterBtnTactile
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgSegmentGlow
import com.example.ui.theme.DrgSlateTextMuted

@Composable
fun RiderClusterButtons(
    onOpenMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left tactile rubber SEL button
        TactileClusterButton(
            label = "SEL",
            subLabel = "MENU",
            onClick = onOpenMenu,
            tag = "btn_cluster_sel"
        )

        // Center glowing interactive action button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(DrgClusterBtnBg)
                .border(1.5.dp, Brush.horizontalGradient(listOf(DrgGrabGreenPrimary, DrgSegmentGlow)), RoundedCornerShape(20.dp))
                .clickable { onOpenMenu() }
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .testTag("btn_cluster_start_drg"),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(16.dp).clip(CircleShape).background(DrgGrabGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "START • MASUK DRG",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Right tactile rubber SET button
        TactileClusterButton(
            label = "SET",
            subLabel = "INFO",
            onClick = onOpenMenu,
            tag = "btn_cluster_set"
        )
    }
}

@Composable
private fun TactileClusterButton(
    label: String,
    subLabel: String,
    onClick: () -> Unit,
    tag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(DrgClusterBtnTactile)
            .border(1.dp, DrgClusterBtnBorder, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .testTag(tag),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DrgSlateTextMuted)
            Text(text = subLabel, fontSize = 7.sp, fontWeight = FontWeight.SemiBold, color = DrgGrabGreenPrimary)
        }
    }
}
