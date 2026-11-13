package com.example.modules.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.PlusJakartaFontFamily

/** Animated destination item inside the sliding liquid glass navigation capsule. */
@Composable
fun DrgBottomNavItem(
    label: String,
    activeIcon: ImageVector,
    inactiveIcon: ImageVector,
    isSelected: Boolean,
    testTagKey: String,
    accentColor: Color,
    hasLiveDot: Boolean = false,
    badgeCount: Int = 0,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val tint by animateColorAsState(
        targetValue = if (isSelected) accentColor else DrgTextSecondary.copy(alpha = 0.84f),
        animationSpec = tween(220),
        label = "navItemTint"
    )
    val iconScale by animateFloatAsState(
        targetValue = if (isSelected) 1.12f else 0.96f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow),
        label = "navItemScale"
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .minimumInteractiveComponentSize()
            .clip(CircleShape)
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick()
            }
            .testTag(testTagKey),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Box(contentAlignment = Alignment.TopEnd) {
                Icon(
                    imageVector = if (isSelected) activeIcon else inactiveIcon,
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier.size(21.dp).scale(iconScale)
                )
                DrgBottomNavBadge(badgeCount = badgeCount, hasLiveDot = hasLiveDot)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontFamily = PlusJakartaFontFamily,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = tint,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
