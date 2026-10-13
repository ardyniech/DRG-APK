package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shared.models.DriverMember
import com.example.shared.models.VerificationStatus
import com.example.ui.theme.*
import com.example.modules.dashboard.primitives.StatusChips
import com.example.modules.dashboard.primitives.GlassCard

@Composable
fun DashboardHeader(
    member: DriverMember,
    onStatusChange: (VerificationStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for avatar
            Box(
                modifier = Modifier.size(64.dp).clip(CircleShape).background(DrgGoldReward),
                contentAlignment = Alignment.Center
            ) {
                Text(member.name.take(1), color = DrgSurface, style = MaterialTheme.typography.headlineMedium)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(member.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                StatusChips(currentStatus = member.currentStatus, onStatusChange = { _, _ -> /* TODO: Implement */ })
            }
        }
    }
}
