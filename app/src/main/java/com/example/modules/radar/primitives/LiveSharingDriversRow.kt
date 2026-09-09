package com.example.modules.radar.primitives

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun LiveSharingDriversRow(
    drivers: List<DriverMember>,
    selectedDriver: DriverMember?,
    onSelectDriver: (DriverMember) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeSharingDrivers = drivers.filter { it.isOnline && it.isLocationSharingConsent }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .testTag("live_sharing_drivers_row")
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.GpsFixed,
                        contentDescription = "Radar Live",
                        tint = DrgGreenPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Rekan Berbagi GPS Aktif",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = DrgTextPrimary
                    )
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = DrgGreenContainer
                ) {
                    Text(
                        text = "${activeSharingDrivers.size} Online",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DrgGreenPrimary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            if (activeSharingDrivers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada rekan berbagi GPS di radius ini.",
                        fontSize = 11.sp,
                        color = DrgTextSecondary
                    )
                }
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(activeSharingDrivers) { driver ->
                        SharingDriverItem(
                            driver = driver,
                            isSelected = selectedDriver?.id == driver.id,
                            onClick = { onSelectDriver(driver) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SharingDriverItem(
    driver: DriverMember,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulseRing")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val itemBorderColor = if (isSelected) DrgGreenPrimary else DrgOutline.copy(alpha = 0.4f)
    val itemBgColor = if (isSelected) DrgGreenContainer.copy(alpha = 0.4f) else Color.Transparent

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .width(72.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(itemBgColor)
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 4.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Pulse circle ring for active sharing
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .scale(pulseScale)
                    .border(1.5.dp, DrgGreenPrimary.copy(alpha = 0.45f), CircleShape)
            )

            // Actual Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(DrgGreenContainer)
                    .border(2.dp, itemBorderColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = driver.name.take(1).uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = DrgGreenPrimary
                )
            }
        }

        Text(
            text = driver.name.split(" ").firstOrNull() ?: driver.name,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 10.sp,
            color = DrgTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
