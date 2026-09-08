package com.example.modules.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun LoginQuickDemoRow(
    members: List<DriverMember>,
    selectedId: String,
    onSelectMember: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    Column(modifier = modifier) {
        Text("Akses Cepat Pengurus / Anggota (Uji Demo):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextSecondary)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(members.take(5)) { member ->
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color = if (selectedId == member.id) DrgGrabGreenPrimary else DrgBorderLight,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onSelectMember(member.id)
                        }
                        .padding(12.dp)
                ) {
                    Column {
                        Text(member.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, color = DrgTextDark)
                        Text(member.role.shortName, fontSize = 10.sp, color = DrgGrabGreenPrimary, fontWeight = FontWeight.SemiBold)
                        Text(member.motorcyclePlate, fontSize = 10.sp, color = DrgTextMuted, maxLines = 1)
                    }
                }
            }
        }
    }
}
