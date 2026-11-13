package com.example.modules.notifications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.cache.LocationSyncPowerProfile
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@Composable
fun LocationSyncProfileSelector(
    selectedProfile: LocationSyncPowerProfile,
    onSelectProfile: (LocationSyncPowerProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Sinkronisasi GPS Latar Belakang (Hemat Baterai)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
        Text("Menyesuaikan interval pengiriman koordinat agar baterai awet:", fontSize = 9.5.sp, color = DrgTextSecondary)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            LocationSyncPowerProfile.entries.forEach { profile ->
                val isSelected = profile == selectedProfile
                OutlinedButton(
                    onClick = { onSelectProfile(profile) },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 0.8.dp,
                        color = if (isSelected) theme.primaryColor else theme.glassBorderColor.copy(alpha = 0.5f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) theme.pillBg else DrgGlassSurface
                    )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = when (profile) {
                                LocationSyncPowerProfile.ADAPTIVE_ECO -> "Adaptif"
                                LocationSyncPowerProfile.ULTRA_SAVER -> "Ultra Saver"
                                LocationSyncPowerProfile.HIGH_PRECISION -> "Akurasi"
                            },
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                            color = if (isSelected) theme.primaryColor else DrgTextDark
                        )
                        Text(
                            text = "${profile.normalIntervalSec}s / ${profile.stationaryIntervalSec}s",
                            fontSize = 8.5.sp,
                            fontFamily = SpaceGroteskFontFamily,
                            color = if (isSelected) theme.primaryColor else DrgTextSecondary
                        )
                    }
                }
            }
        }
    }
}
