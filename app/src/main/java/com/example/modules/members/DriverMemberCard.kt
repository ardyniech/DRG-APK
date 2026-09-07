package com.example.modules.members

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    Surface(
        onClick = onMemberClick,
        shape = RoundedCornerShape(12.dp),
        color = DrgSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = member.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DrgTextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    RoleBadge(role = member.role)
                }
                Text(
                    text = "${member.motorcycleModel} (${member.motorcyclePlate})",
                    fontSize = 11.sp,
                    color = DrgTextSecondary
                )
                Text(
                    text = "ID: ${member.id} • Rating: ⭐ ${member.rating} (${member.reviewCount} Ulasan)",
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
