package com.example.modules.notifications.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.cache.LocationSyncPowerProfile
import com.example.core.cache.MapCachePolicy
import com.example.modules.notifications.CacheAndBatterySettingSection
import com.example.modules.notifications.LiveLocationSharingSettingRow
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.AppVisualTheme
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary

@Composable
fun SettingsGpsCacheTab(
    visualTheme: AppVisualTheme,
    isLocationSharingConsent: Boolean,
    onToggleLocationSharing: (Boolean) -> Unit,
    cacheSizeDesc: String,
    selectedCachePolicy: MapCachePolicy,
    onSelectCachePolicy: (MapCachePolicy) -> Unit,
    selectedSyncProfile: LocationSyncPowerProfile,
    onSelectSyncProfile: (LocationSyncPowerProfile) -> Unit,
    isDataSaverEnabled: Boolean,
    onToggleDataSaver: (Boolean) -> Unit,
    isPowerSaverEnabled: Boolean,
    onTogglePowerSaver: (Boolean) -> Unit,
    onPrecacheMap: () -> Unit,
    onClearCache: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DrgLuminousGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            backgroundColor = visualTheme.glassBg,
            borderHighlightColor = visualTheme.glassBorderColor,
            contentPadding = 16.dp
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                LiveLocationSharingSettingRow(
                    isLocationSharingConsent = isLocationSharingConsent,
                    onToggleLocationSharing = onToggleLocationSharing
                )
                HorizontalDivider(color = Color.White.copy(alpha = 0.8f))
                GpsToggleRow("Mode Hemat Data Tile", "Kurangi resolusi prefetch ubin peta saat kuota terbatas", isDataSaverEnabled, visualTheme.primaryColor, onToggleDataSaver)
                GpsToggleRow("Mode Hemat Baterai GPS", "Jeda polling posisi saat motor berhenti lama", isPowerSaverEnabled, visualTheme.primaryColor, onTogglePowerSaver)
            }
        }

        DrgLuminousGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            backgroundColor = visualTheme.glassBg,
            borderHighlightColor = visualTheme.glassBorderColor,
            contentPadding = 12.dp
        ) {
            CacheAndBatterySettingSection(
                cacheSizeDesc = cacheSizeDesc,
                selectedPolicy = selectedCachePolicy,
                onSelectPolicy = onSelectCachePolicy,
                selectedSyncProfile = selectedSyncProfile,
                onSelectSyncProfile = onSelectSyncProfile,
                isDataSaverEnabled = isDataSaverEnabled,
                onToggleDataSaver = onToggleDataSaver,
                isPowerSaverEnabled = isPowerSaverEnabled,
                onTogglePowerSaver = onTogglePowerSaver,
                onPrecacheMap = onPrecacheMap,
                onClearCache = onClearCache
            )
        }
    }
}

@Composable
private fun GpsToggleRow(title: String, subtitle: String, checked: Boolean, accent: Color, onToggle: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
            Text(subtitle, fontSize = 10.sp, color = DrgTextSecondary)
        }
        Switch(checked = checked, onCheckedChange = onToggle, colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = accent))
    }
}
