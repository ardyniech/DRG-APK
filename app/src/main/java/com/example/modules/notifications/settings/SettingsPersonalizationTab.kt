package com.example.modules.notifications.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.AppFocusMode
import com.example.shared.models.AppVisualTheme
import com.example.shared.models.NotificationPreference
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary

@Composable
fun SettingsPersonalizationTab(
    visualTheme: AppVisualTheme,
    onSelectVisualTheme: (AppVisualTheme) -> Unit,
    focusMode: AppFocusMode,
    onSelectFocusMode: (AppFocusMode) -> Unit,
    initialPref: NotificationPreference,
    onSavePref: (NotificationPreference) -> Unit
) {
    var pref by remember(initialPref) { mutableStateOf(initialPref) }
    val updatePref: (NotificationPreference) -> Unit = { updated -> pref = updated; onSavePref(updated) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DrgLuminousGlassCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), backgroundColor = visualTheme.glassBg, borderHighlightColor = visualTheme.glassBorderColor, contentPadding = 16.dp) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Tema Visual Aplikasi", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                Text("Terapkan atmosfer kaca terang pilihan ke seluruh layar.", fontSize = 11.sp, color = DrgTextSecondary)
                AppVisualTheme.entries.forEach { theme ->
                    val selected = theme == visualTheme
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (selected) theme.pillBg else Color.White.copy(alpha = 0.58f))
                            .border(1.4.dp, if (selected) theme.primaryColor else Color.White, RoundedCornerShape(16.dp))
                            .bouncyClick { onSelectVisualTheme(theme) }
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                            .testTag("settings_theme_${theme.name.lowercase()}")
                    ) {
                        Box(modifier = Modifier.size(22.dp).clip(CircleShape).background(theme.primaryColor).border(2.dp, Color.White, CircleShape))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(theme.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                            Text(theme.subtitle, fontSize = 11.sp, color = DrgTextSecondary)
                        }
                        if (selected) Text("Aktif", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
                    }
                }
            }
        }

        DrgLuminousGlassCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), backgroundColor = visualTheme.glassBg, borderHighlightColor = visualTheme.glassBorderColor, contentPadding = 16.dp) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Mode Fokus Dashboard", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppFocusMode.entries.forEach { mode ->
                        val selected = mode == focusMode
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (selected) visualTheme.primaryColor else Color.White.copy(alpha = 0.65f))
                                .border(1.2.dp, if (selected) visualTheme.primaryColor else Color.White, RoundedCornerShape(16.dp))
                                .bouncyClick { onSelectFocusMode(mode) }
                                .padding(12.dp)
                                .testTag("settings_mode_${mode.tag.lowercase()}")
                        ) {
                            Text(if (mode == AppFocusMode.SATGAS_TRC) "Satgas (A)" else "Admin (B)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (selected) Color.White else DrgTextDark)
                            Text(if (mode == AppFocusMode.SATGAS_TRC) "Radar & Respon" else "Kas & Anggota", fontSize = 10.sp, color = if (selected) Color.White.copy(alpha = 0.85f) else DrgTextSecondary)
                        }
                    }
                }
            }
        }

        DrgLuminousGlassCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), backgroundColor = visualTheme.glassBg, borderHighlightColor = visualTheme.glassBorderColor, contentPadding = 16.dp) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Notifikasi & Personalisasi Komunitas", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                PrefSwitchRow("Peringatan Darurat SOS", pref.sosAlerts, visualTheme.primaryColor) { updatePref(pref.copy(sosAlerts = it)) }
                PrefSwitchRow("Pengingat Kopdar & Misi", pref.kopdarReminders, visualTheme.primaryColor) { updatePref(pref.copy(kopdarReminders = it)) }
                PrefSwitchRow("Update Kas Komunitas", pref.kasUpdates, visualTheme.primaryColor) { updatePref(pref.copy(kasUpdates = it)) }
                PrefSwitchRow("Aktivitas Forum Bengkel", pref.forumActivity, visualTheme.primaryColor) { updatePref(pref.copy(forumActivity = it)) }
                PrefSwitchRow("Apresiasi Poin Rekan", pref.peerPointsNotif, visualTheme.primaryColor) { updatePref(pref.copy(peerPointsNotif = it)) }
                PrefSwitchRow("Penugasan Tim Satgas", pref.taskAssignments, visualTheme.primaryColor) { updatePref(pref.copy(taskAssignments = it)) }
            }
        }
    }
}

@Composable
private fun PrefSwitchRow(label: String, checked: Boolean, accent: Color, onCheckedChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = DrgTextDark)
        Switch(checked = checked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = accent))
    }
}
