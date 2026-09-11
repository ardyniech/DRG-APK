package com.example.modules.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.modules.profile.primitives.KtaDriverAvatarAndDetails
import com.example.modules.profile.primitives.KtaFooterRow
import com.example.modules.profile.primitives.KtaHeaderRow
import com.example.modules.profile.primitives.KtaHolographicWatermark
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun KtaDigitalCard(member: DriverMember?) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DrgSurface,
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, DrgGreenPrimary, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            KtaHeaderRow()
            Divider(color = DrgOutline.copy(alpha = 0.5f))
            KtaDriverAvatarAndDetails(member = member)
            KtaHolographicWatermark(member = member)
            KtaFooterRow(member = member)
        }
    }
}
