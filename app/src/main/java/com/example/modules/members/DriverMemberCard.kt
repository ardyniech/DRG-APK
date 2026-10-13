package com.example.modules.members

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.RoleBadge
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun DriverMemberCard(
    member: DriverMember,
    onMemberClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(20.dp)
    val highlightBrush = Brush.linearGradient(
        listOf(Color.White, Color.White.copy(alpha = 0.5f), DrgGreenPrimary.copy(alpha = 0.2f))
    )

    Surface(
        onClick = onMemberClick,
        shape = cardShape,
        color = Color.White.copy(alpha = 0.88f),
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, cardShape, ambientColor = Color(0x14003617), spotColor = Color(0x0E003617))
            .border(1.2.dp, highlightBrush, cardShape)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = member.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = DrgTextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    RoleBadge(role = member.role)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${member.motorcycleModel} (${member.motorcyclePlate})",
                    fontSize = 11.sp,
                    color = DrgTextSecondary
                )
                Text(
                    text = "ID: ${member.id} • ⭐ ${member.rating} (${member.reviewCount} ulasan)",
                    fontSize = 10.sp,
                    color = DrgTextMuted
                )
            }

            Text(
                text = if (member.isOnline) "🟢 On-Shift" else "⚪ Off-Shift",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (member.isOnline) DrgGreenPrimary else DrgTextMuted
            )
        }
    }
}
