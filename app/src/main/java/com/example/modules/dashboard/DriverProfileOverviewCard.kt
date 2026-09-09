package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.RoleBadge
import com.example.shared.models.DriverMember
import com.example.shared.models.VerificationStatus
import com.example.ui.theme.*

@Composable
fun DriverProfileOverviewCard(
    member: DriverMember?,
    onStatusChange: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isVerified = member?.verificationStatus == VerificationStatus.VERIFIED
    val isOnline = member?.isOnline == true
    val currentStatus = member?.currentStatus ?: "Aktif Narik"

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(1.2.dp, DrgGreenPrimary.copy(alpha = 0.22f), RoundedCornerShape(20.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(DrgGreenContainer)
                        .border(1.5.dp, DrgGreenPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = member?.name?.take(2)?.uppercase() ?: "DR",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = DrgGreenPrimary
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = member?.name ?: "Driver DRG",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = DrgTextPrimary
                        )
                        if (isVerified) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Verified, contentDescription = "Terverifikasi", tint = DrgGreenPrimary, modifier = Modifier.size(15.dp))
                        }
                    }
                    Text(
                        text = "${member?.driverId ?: member?.id ?: "DRG-001"} • ${member?.baseArea ?: "Malang Raya"}",
                        fontSize = 11.sp,
                        color = DrgTextSecondary
                    )
                }
                member?.role?.let { RoleBadge(role = it) }
            }

            HorizontalDivider(color = DrgOutline.copy(alpha = 0.2f), thickness = 0.8.dp)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.DirectionsBike, contentDescription = null, tint = DrgTextSecondary, modifier = Modifier.size(13.dp))
                    Text(text = "${member?.motorcycleModel ?: "Motor"} (${member?.motorcyclePlate ?: "N 1234 XX"})", fontSize = 11.sp, color = DrgTextSecondary)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = DrgAmberSecondary, modifier = Modifier.size(13.dp))
                    Text(text = "${member?.rating ?: 4.9} • ${member?.loyaltyPoints ?: 0} Poin", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextPrimary)
                }
            }

            // Interactive Duty Chips
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(
                    Triple("Aktif Narik", true, DrgGreenPrimary),
                    Triple("Siaga Posko", true, DrgAmberSecondary),
                    Triple("Rehat", false, DrgTextSecondary)
                ).forEach { (status, online, color) ->
                    val isSelected = currentStatus.contains(status, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) color.copy(alpha = 0.15f) else DrgSurface)
                            .border(1.dp, if (isSelected) color else DrgOutline.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .clickable { onStatusChange(status, online) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = status,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) color else DrgTextSecondary
                        )
                    }
                }
            }
        }
    }
}
