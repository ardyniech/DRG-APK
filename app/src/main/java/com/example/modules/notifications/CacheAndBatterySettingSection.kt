package com.example.modules.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@Composable
fun CacheAndBatterySettingSection(
    cacheSizeDesc: String,
    selectedPolicy: MapCachePolicy = MapCachePolicy.CACHE_FIRST,
    onSelectPolicy: (MapCachePolicy) -> Unit = {},
    selectedSyncProfile: LocationSyncPowerProfile = LocationSyncPowerProfile.ADAPTIVE_ECO,
    onSelectSyncProfile: (LocationSyncPowerProfile) -> Unit = {},
    isDataSaverEnabled: Boolean,
    onToggleDataSaver: (Boolean) -> Unit,
    isPowerSaverEnabled: Boolean,
    onTogglePowerSaver: (Boolean) -> Unit,
    onPrecacheMap: () -> Unit,
    onClearCache: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = theme.pillBg.copy(alpha = 0.45f),
        borderHighlightColor = theme.glassBorderColor.copy(alpha = 0.65f),
        contentPadding = 12.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Storage, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Cache Peta Offline & GPS Hemat Daya", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
            }

            MapCachePolicySelector(selectedPolicy = selectedPolicy, onSelectPolicy = onSelectPolicy)
            LocationSyncProfileSelector(selectedProfile = selectedSyncProfile, onSelectProfile = onSelectSyncProfile)

            Column {
                Text("Kapasitas Cache Tersimpan", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextDark)
                Text(
                    "Ukuran saat ini: $cacheSizeDesc (Disk + Memori)",
                    fontSize = 9.5.sp,
                    fontFamily = SpaceGroteskFontFamily,
                    color = DrgTextSecondary
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onPrecacheMap,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp), tint = theme.primaryColor)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Pre-cache Malang", fontSize = 10.sp, color = theme.primaryColor, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onClearCache,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(14.dp), tint = DrgTextSecondary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Hapus Cache", fontSize = 10.sp, color = DrgTextSecondary)
                }
            }
        }
    }
}
