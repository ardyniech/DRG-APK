package com.example.modules.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MilitaryTech
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
fun GamificationCard(member: DriverMember?) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DrgSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DrgGreenContainer, RoundedCornerShape(14.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MilitaryTech,
                    contentDescription = "Gamifikasi",
                    tint = DrgGreenPrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Reputasi & Poin Jalur Komunitas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DrgTextPrimary
                )
            }
            Text(
                text = "Level: ${member?.loyaltyTier ?: "Road Captain"} (${member?.loyaltyPoints ?: 0} XP)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DrgGreenDark
            )
            Text(
                text = "Lencana Aktif: 🛡️ Pejuang Aspal • ⚡ Respon SOS Cepat • 🤝 Donatur Kas",
                fontSize = 12.sp,
                color = DrgTextSecondary
            )
        }
    }
}
