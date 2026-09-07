package com.example.modules.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.shared.models.EmergencyAlert
import com.example.ui.theme.*

@Composable
fun SosActiveAlertsEmptyCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DrgSurface)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Aman",
                tint = DrgGreenPrimary,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Kondisi Jalanan Terpantau Aman",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = DrgTextPrimary
            )
            Text(
                text = "Tidak ada laporan sinyal darurat aktif dari rekan driver.",
                fontSize = 11.sp,
                color = DrgTextSecondary
            )
        }
    }
}
