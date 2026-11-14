package com.example.modules.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGlassSurfaceStrong
import com.example.ui.theme.DrgGoldReward
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgRedPanic
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSlate

@Composable
fun LandingFeatureHighlights(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FeatureMiniChip(
            icon = Icons.Default.Navigation,
            iconTint = DrgGrabGreenPrimary,
            title = "Radar Posko",
            subtitle = "Peta & Kopdar",
            modifier = Modifier.weight(1f)
        )
        FeatureMiniChip(
            icon = Icons.Default.AccountBalanceWallet,
            iconTint = DrgGoldReward,
            title = "Kas Terbuka",
            subtitle = "Transparan",
            modifier = Modifier.weight(1f)
        )
        FeatureMiniChip(
            icon = Icons.Default.Warning,
            iconTint = DrgRedPanic,
            title = "Tombol SOS",
            subtitle = "Siaga 24 Jam",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun FeatureMiniChip(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(DrgGlassSurfaceStrong)
            .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
            .padding(vertical = 10.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
        Text(text = subtitle, fontSize = 9.sp, fontWeight = FontWeight.Medium, color = DrgTextSlate)
    }
}
