package com.example.modules.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DrgBottomNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    badgeCount: Int = 0,
    badgeColor: Color = DrgAmberSecondary,
    onClick: () -> Unit
) {
    val tint = if (isSelected) DrgGrabGreenPrimary else DrgTextMuted
    val bg = if (isSelected) DrgGreenContainer else Color.Transparent

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = bg,
        modifier = Modifier.height(52.dp).padding(horizontal = 2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Box {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier.size(22.dp)
                )
                if (badgeCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 8.dp, y = (-4).dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(badgeColor)
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "$badgeCount",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = tint
            )
        }
    }
}
