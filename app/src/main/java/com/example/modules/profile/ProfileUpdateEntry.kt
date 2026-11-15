package com.example.modules.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.core.update.*
import com.example.shared.atoms.DrgIconTileBox
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextPrimary
import kotlinx.coroutines.launch

@Composable
fun ProfileUpdateEntry(
    modifier: Modifier = Modifier,
    allowAutoCheck: Boolean = true
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val theme = LocalAppVisualTheme.current
    var state by remember { mutableStateOf<UpdateUiState>(UpdateUiState.Idle) }
    var busy by remember { mutableStateOf(false) }
    var hint by remember { mutableStateOf<String?>(null) }
    var showChangelog by remember { mutableStateOf(false) }
    val available = state as? UpdateUiState.Available
    val ready = state as? UpdateUiState.ReadyToInstall

    fun check(force: Boolean = false) {
        if (busy) return
        hint = null; busy = true
        scope.launch { state = UpdateManager.check(context, BuildConfig.UPDATE_MANIFEST_URL, force); busy = false }
    }

    fun prepare() {
        val target = available ?: return
        if (busy) return
        hint = null; busy = true
        scope.launch { state = UpdateManager.prepare(context, target) { state = it }; busy = false }
    }

    ProfileUpdateAutoActions(
        state = state, context = context, allowAutoCheck = allowAutoCheck, onCheck = { check() },
        onMissingFile = { hint = null; state = UpdateUiState.Failed("File update hilang — ketuk untuk cek ulang") },
        onPermissionNeeded = { hint = it }
    )
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth().testTag("settings_ota_update_card"),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 14.dp,
        onClick = { if (!busy && ready == null && available == null) check(true) }
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DrgIconTileBox(icon = Icons.Default.SystemUpdate, tint = theme.primaryColor, size = 40.dp, iconSize = 20.dp, contentDescription = "Pembaruan aplikasi OTA")
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Pembaruan Aplikasi OTA • v${BuildConfig.VERSION_NAME} (#${BuildConfig.VERSION_CODE})", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                Text(updateStatusText(state, busy), fontSize = 11.sp, color = DrgTextMuted)
                hint?.let { Text(it, fontSize = 10.sp, color = theme.primaryColor) }
                if (available != null && (UpdateChecker.isMetered(context) || (UpdateChecker.batteryLevel(context) in 1..19 && !UpdateChecker.isCharging(context)))) {
                    Text("Saran: update saat WiFi + charging. Tetap bisa update sekarang.", fontSize = 10.sp, color = DrgTextMuted)
                }
                if (available != null && available.release.changelog.isNotEmpty()) {
                    TextButton(onClick = { showChangelog = true }, contentPadding = PaddingValues(0.dp), modifier = Modifier.defaultMinSize(minHeight = 36.dp)) {
                        Text("Lihat perubahan (${available.release.changelog.size})", fontSize = 10.sp, color = theme.primaryColor)
                    }
                }
            }
            when {
                ready != null -> Button(
                    onClick = { handleInstallOutcome(ApkInstaller.install(context, ready.apk), setHint = { hint = it }, setFailed = { msg -> state = UpdateUiState.Failed(msg) }) },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor), shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp).testTag("btn_install_ota_update")
                ) { Text("Pasang", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                available != null -> Button(
                    onClick = { prepare() }, enabled = !busy,
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor), shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp).testTag("btn_download_ota_update")
                ) { Text(if (available.usesPatch) "Patch" else "APK", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                busy -> CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.5.dp, color = theme.primaryColor)
                else -> FilledTonalButton(
                    onClick = { check(true) },
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = theme.pillBg, contentColor = theme.primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier.defaultMinSize(minHeight = 48.dp).testTag("btn_check_ota_update")
                ) { Text("Cek", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            }
        }
    }
    available?.release?.let { release -> ProfileUpdateChangelogDialog(release, showChangelog) { showChangelog = false } }
}
