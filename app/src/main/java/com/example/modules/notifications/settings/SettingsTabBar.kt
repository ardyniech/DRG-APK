package com.example.modules.notifications.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.AppVisualTheme
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary

enum class SettingsHubTab(val title: String, val icon: ImageVector, val testTag: String) {
    PERSONALISASI("Tampilan", Icons.Default.Palette, "settings_tab_personalization"),
    KEAMANAN("Keamanan", Icons.Default.Shield, "settings_tab_security"),
    GPS_PETA("GPS & Peta", Icons.Default.GpsFixed, "settings_tab_gps"),
    SISTEM("Sistem", Icons.Default.SystemUpdate, "settings_tab_system")
}

@Composable
fun SettingsTabBar(
    selectedTab: SettingsHubTab,
    onSelectTab: (SettingsHubTab) -> Unit,
    onBack: () -> Unit,
    visualTheme: AppVisualTheme,
    modifier: Modifier = Modifier
) {
    val accent = visualTheme.primaryColor

    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = visualTheme.glassBorderColor,
        contentPadding = 12.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(visualTheme.pillBg.copy(alpha = 0.85f))
                        .border(1.dp, Color.White, RoundedCornerShape(14.dp))
                        .testTag("settings_back_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali ke Profil", tint = accent)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Pusat Pengaturan", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                    Text("Tema, komunitas, GPS, alarm SOS & sistem terpadu", fontSize = 11.sp, color = DrgTextSecondary)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SettingsHubTab.entries.forEach { tab ->
                    val isSelected = tab == selectedTab
                    val chipBg by animateColorAsState(
                        targetValue = if (isSelected) accent else Color.White.copy(alpha = 0.62f),
                        animationSpec = tween(220),
                        label = "settingsTabBg"
                    )
                    val contentTint by animateColorAsState(
                        targetValue = if (isSelected) Color.White else DrgTextSecondary,
                        animationSpec = tween(220),
                        label = "settingsTabTint"
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .heightIn(min = 40.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(chipBg)
                            .border(1.dp, if (isSelected) accent else Color.White, RoundedCornerShape(14.dp))
                            .bouncyClick { onSelectTab(tab) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .testTag(tab.testTag)
                    ) {
                        Icon(tab.icon, contentDescription = null, tint = contentTint, modifier = Modifier.size(16.dp))
                        Text(tab.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = contentTint)
                    }
                }
            }
        }
    }
}
