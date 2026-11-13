package com.example.modules.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.AppVisualTheme
import com.example.shared.models.DriverMember
import com.example.shared.models.VerificationStatus
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.*

@Composable
fun ProfileGlassHeroCard(
    member: DriverMember?,
    visualTheme: AppVisualTheme,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = visualTheme.primaryColor
    val isVerified = member?.verificationStatus == VerificationStatus.VERIFIED
    val initials = (member?.name ?: "DRG").split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("")

    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = visualTheme.glassBorderColor,
        contentPadding = 16.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(visualTheme.pillBg.copy(alpha = 0.9f)).border(1.dp, Color.White, RoundedCornerShape(12.dp)).padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text("${member?.role?.shortName ?: "Driver"} • ${member?.driverId ?: "DRG-000"}", fontFamily = SpaceGroteskFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accent)
                    if (isVerified) Icon(Icons.Filled.Verified, contentDescription = "Terverifikasi", tint = accent, modifier = Modifier.size(14.dp))
                }
                Box(
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)).background(Brush.verticalGradient(listOf(accent.copy(alpha = 0.22f), visualTheme.pillBg))).border(1.3.dp, Color.White, RoundedCornerShape(16.dp)).bouncyClick { onOpenSettings() }.testTag("profile_settings_launcher"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Settings, contentDescription = "Buka Pengaturan", tint = accent, modifier = Modifier.size(22.dp))
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier.size(56.dp).clip(CircleShape).background(Brush.linearGradient(listOf(accent, visualTheme.secondaryColor))).border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) { Text(initials.ifBlank { "DR" }, fontFamily = SpaceGroteskFontFamily, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White) }
                    Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(if (member?.isOnline == true) DrgGreenPrimary else DrgTextMuted).border(2.dp, Color.White, CircleShape))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(member?.name ?: "Driver DRG", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                    Text("${member?.baseArea ?: "Malang Raya"} • ${member?.motorcyclePlate ?: "-"}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = DrgTextSecondary)
                    Text(member?.currentStatus ?: "Aktif Narik", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accent)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ProfileMetricPill("Poin", "${member?.loyaltyPoints ?: 0}", accent, Modifier.weight(1f))
                ProfileMetricPill("Kopdar", "${member?.kopdarAttendanceCount ?: 0}x", visualTheme.secondaryColor, Modifier.weight(1f))
                ProfileMetricPill("Darah", member?.bloodType?.ifBlank { "-" } ?: "-", DrgRedPanic, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ProfileMetricPill(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clip(RoundedCornerShape(14.dp)).background(Color.White.copy(alpha = 0.65f)).border(1.dp, Color.White, RoundedCornerShape(14.dp)).padding(vertical = 8.dp)
    ) {
        Text(value, fontFamily = SpaceGroteskFontFamily, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = color)
    }
}
