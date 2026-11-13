package com.example.modules.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgRedPanic
import com.example.ui.theme.SpaceGroteskFontFamily

/** Compact notification count pill or live pulse dot overlaid on bottom navigation icons. */
@Composable
fun DrgBottomNavBadge(
    badgeCount: Int,
    hasLiveDot: Boolean,
    modifier: Modifier = Modifier
) {
    if (badgeCount > 0) {
        Box(
            modifier = modifier
                .offset(x = 8.dp, y = (-4).dp)
                .defaultMinSize(minWidth = 15.dp, minHeight = 14.dp)
                .clip(CircleShape)
                .background(DrgRedPanic)
                .border(1.dp, Color.White, CircleShape)
                .padding(horizontal = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (badgeCount > 99) "99+" else "$badgeCount",
                fontFamily = SpaceGroteskFontFamily,
                fontSize = 8.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                lineHeight = 9.sp
            )
        }
    } else if (hasLiveDot) {
        Box(
            modifier = modifier
                .offset(x = 4.dp, y = (-2).dp)
                .size(7.dp)
                .clip(CircleShape)
                .background(DrgRedPanic)
                .border(1.dp, Color.White, CircleShape)
        )
    }
}
