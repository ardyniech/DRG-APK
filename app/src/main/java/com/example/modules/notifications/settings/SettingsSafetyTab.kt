package com.example.modules.notifications.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.modules.notifications.CrashGuardSettingSection
import com.example.modules.notifications.SosAlarmSettingSection
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.AppVisualTheme
import com.example.shared.models.CrashSensitivity

@Composable
fun SettingsSafetyTab(
    visualTheme: AppVisualTheme,
    isSosAlarmEnabled: Boolean,
    onToggleSosAlarm: (Boolean) -> Unit,
    onTestSosAlarm: () -> Unit,
    onStopSosAlarm: () -> Unit,
    isCrashGuardEnabled: Boolean,
    onToggleCrashGuard: (Boolean) -> Unit,
    crashSensitivity: CrashSensitivity,
    onSelectCrashSensitivity: (CrashSensitivity) -> Unit,
    onSimulateCrash: () -> Unit
) {
    var isTestingSound by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose { if (isTestingSound) onStopSosAlarm() }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DrgLuminousGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            backgroundColor = visualTheme.glassBg,
            borderHighlightColor = visualTheme.glassBorderColor,
            contentPadding = 12.dp
        ) {
            SosAlarmSettingSection(
                isSosAlarmEnabled = isSosAlarmEnabled,
                onToggleSosAlarm = {
                    if (isTestingSound) { onStopSosAlarm(); isTestingSound = false }
                    onToggleSosAlarm(it)
                },
                isTestingSound = isTestingSound,
                onTestToggle = {
                    if (!isTestingSound) { isTestingSound = true; onTestSosAlarm() }
                    else { isTestingSound = false; onStopSosAlarm() }
                }
            )
        }

        DrgLuminousGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            backgroundColor = visualTheme.glassBg,
            borderHighlightColor = visualTheme.glassBorderColor,
            contentPadding = 12.dp
        ) {
            CrashGuardSettingSection(
                isCrashGuardEnabled = isCrashGuardEnabled,
                onToggleCrashGuard = onToggleCrashGuard,
                sensitivity = crashSensitivity,
                onSelectSensitivity = onSelectCrashSensitivity,
                onSimulateCrash = onSimulateCrash
            )
        }
    }
}
