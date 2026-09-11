package com.example.modules.profile.primitives

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.shared.models.VerificationStatus
import com.example.ui.theme.*

@Composable
fun KtaHolographicWatermark(member: DriverMember?, modifier: Modifier = Modifier) {
    val isVerified = member?.verificationStatus == VerificationStatus.VERIFIED
    val securityHash = if (member != null) {
        val raw = "${member.id}:${member.motorcyclePlate}:${member.role.name}"
        raw.hashCode().toString(16).uppercase().takeLast(6)
    } else "000000"

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isVerified) DrgGreenContainer.copy(alpha = 0.6f) else Color(0xFFFFF3E0),
        modifier = modifier
            .fillMaxWidth()
            .border(
                0.8.dp,
                if (isVerified) DrgGreenPrimary.copy(alpha = 0.5f) else DrgAmberSecondary.copy(alpha = 0.5f),
                RoundedCornerShape(8.dp)
            )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = "Security Stamp",
                    tint = if (isVerified) DrgGreenPrimary else DrgAmberSecondary,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isVerified) "ORIGINAL DIGITAL KTA" else "SCREENING IN REVIEW",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isVerified) DrgGreenDark else Color(0xFFE65100),
                    letterSpacing = 0.5.sp
                )
            }
            Text(
                text = "SEC-HASH: #$securityHash",
                fontFamily = FontFamily.Monospace,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = DrgTextMuted
            )
        }
    }
}
