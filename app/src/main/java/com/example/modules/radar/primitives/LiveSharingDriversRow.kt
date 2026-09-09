package com.example.modules.radar.primitives

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.ui.theme.DrgGreenContainer
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgOutline
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

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
