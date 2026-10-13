package com.example.modules.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.modules.profile.primitives.KtaDriverAvatarAndDetails
import com.example.modules.profile.primitives.KtaFooterRow
import com.example.modules.profile.primitives.KtaHeaderRow
import com.example.modules.profile.primitives.KtaHolographicWatermark
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun KtaDigitalCard(member: DriverMember?) {
    val cardShape = RoundedCornerShape(26.dp)
    val highlightBrush = Brush.linearGradient(
        colors = listOf(
            Color.White,
            DrgGoldReward.copy(alpha = 0.5f),
            DrgGreenPrimary.copy(alpha = 0.35f),
            Color.White.copy(alpha = 0.8f)
        )
    )

    Surface(
        shape = cardShape,
        color = Color.White.copy(alpha = 0.88f),
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, cardShape, ambientColor = DrgGreenPrimary.copy(alpha = 0.18f), spotColor = DrgGoldReward.copy(alpha = 0.12f))
            .border(1.5.dp, highlightBrush, cardShape)
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
