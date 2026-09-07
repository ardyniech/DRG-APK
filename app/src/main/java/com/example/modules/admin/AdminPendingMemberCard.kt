package com.example.modules.admin

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
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun PendingMemberCard(member: DriverMember, canApprove: Boolean, onApprove: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DrgSurface,
        modifier = Modifier.fillMaxWidth().border(1.dp, DrgAmberSecondary.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(member.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                Text("${member.motorcycleModel} • ${member.motorcyclePlate}", fontSize = 11.sp, color = DrgTextSecondary)
                Text("No. HP: ${member.phone}", fontSize = 10.sp, color = DrgTextMuted)
            }
            if (canApprove) {
                Button(
                    onClick = onApprove,
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("Setujui (Approve)", fontSize = 11.sp)
                }
            }
        }
    }
}
