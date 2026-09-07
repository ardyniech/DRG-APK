package com.example.shared.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.MemberRole
import com.example.shared.models.VerificationStatus

@Composable
fun RoleBadge(
    role: MemberRole,
    modifier: Modifier = Modifier
) {
    val bgColor = Color(role.badgeColorHex).copy(alpha = 0.15f)
    val textColor = Color(role.badgeColorHex)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = role.shortName,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun VerificationBadge(
    status: VerificationStatus,
    modifier: Modifier = Modifier
) {
    val (label, bg, fg) = when (status) {
        VerificationStatus.VERIFIED -> Triple("Terverifikasi DRG", Color(0xFFE8F8F0), Color(0xFF00875A))
        VerificationStatus.PENDING_SCREENING -> Triple("Screening", Color(0xFFFFF3E0), Color(0xFFFF8F00))
        VerificationStatus.SUSPENDED -> Triple("Disanksi", Color(0xFFFFEBEE), Color(0xFFE53935))
        VerificationStatus.REJECTED -> Triple("Ditolak", Color(0xFFF1F5F9), Color(0xFF64748B))
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            color = fg,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
