package com.example.modules.notifications

import androidx.compose.foundation.border
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
    Surface(
        color = DrgBackground,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Storage, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Cache Peta Offline & GPS Hemat Daya", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
            }

            // Map Cache Policy Selector
            MapCachePolicySelector(selectedPolicy = selectedPolicy, onSelectPolicy = onSelectPolicy)

            // Location Sync Background Power Profile Selector
            LocationSyncProfileSelector(selectedProfile = selectedSyncProfile, onSelectProfile = onSelectSyncProfile)

            // Cache size and quick actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Kapasitas Cache Tersimpan", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextPrimary)
                    Text("Ukuran saat ini: $cacheSizeDesc (Disk + Memori)", fontSize = 9.5.sp, color = DrgTextSecondary)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onPrecacheMap,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp), tint = DrgGreenPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Pre-cache Malang", fontSize = 10.sp, color = DrgGreenPrimary, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onClearCache,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(14.dp), tint = DrgTextSecondary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Hapus Cache", fontSize = 10.sp, color = DrgTextSecondary)
                }
            }
        }
    }
}
