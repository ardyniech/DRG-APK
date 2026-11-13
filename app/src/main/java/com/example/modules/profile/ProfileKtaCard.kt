package com.example.modules.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shared.atoms.DrgGlassCard
import com.example.modules.profile.primitives.KtaDriverAvatarAndDetails
import com.example.modules.profile.primitives.KtaFooterRow
import com.example.modules.profile.primitives.KtaHeaderRow
import com.example.modules.profile.primitives.KtaHolographicWatermark
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun KtaDigitalCard(member: DriverMember?) {
    DrgGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        elevation = 8.dp,
        borderWidth = 1.5.dp,
        borderBrush = Brush.linearGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.96f),
                DrgGoldReward.copy(alpha = 0.5f),
                DrgGreenPrimary.copy(alpha = 0.35f),
                Color.White.copy(alpha = 0.8f)
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            KtaHeaderRow()
            HorizontalDivider(color = DrgOutline.copy(alpha = 0.4f), thickness = 1.dp)
            KtaDriverAvatarAndDetails(member = member)
            KtaHolographicWatermark(member = member)
            KtaFooterRow(member = member)
        }
    }
}
