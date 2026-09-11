package com.example.modules.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.MainNavTab
import com.example.ui.theme.DrgTextPrimary

@Composable
fun CommunityStatusSection(
    activeMemberCount: Int,
    activeHazardCount: Int,
    kasFormatted: String,
    poskoCount: Int,
    onNavigateTab: (MainNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Status Ekosistem Komunitas",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = DrgTextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        CommunityLiveStatusCard(
            activeMemberCount = activeMemberCount,
            activeHazardCount = activeHazardCount,
            kasFormatted = kasFormatted,
            poskoCount = poskoCount,
            onNavigateTab = onNavigateTab
        )
    }
}
