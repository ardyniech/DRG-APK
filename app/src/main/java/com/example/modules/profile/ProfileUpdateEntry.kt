package com.example.modules.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.core.update.ApkInstaller
import com.example.core.update.UpdateChecker
import com.example.core.update.UpdateManager
import com.example.core.update.UpdateUiState
import com.example.shared.atoms.DrgGlassCard
import com.example.shared.atoms.DrgIconTileBox
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextPrimary
import kotlinx.coroutines.launch
/**
 * Kartu OTA delta: cek update hanya mengunduh `update.json` (KB), lalu patch
 * kecil — bukan APK penuh. Fallback ke APK penuh bila patch tidak tersedia.
 */
@Composable
fun ProfileUpdateEntry(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var state by remember { mutableStateOf<UpdateUiState>(UpdateUiState.Idle) }
    var busy by remember { mutableStateOf(false) }
    var hint by remember { mutableStateOf<String?>(null) }
    val available = state as? UpdateUiState.Available
    val ready = state as? UpdateUiState.ReadyToInstall
    fun check(force: Boolean = false) {
        if (busy) return
        hint = null
        busy = true
        scope.launch {
            state = UpdateManager.check(context, BuildConfig.UPDATE_MANIFEST_URL, force)
            busy = false
        }
    }
    fun prepare() {
        val target = available ?: return
        if (busy) return
        hint = null
        busy = true
        scope.launch {
            state = UpdateManager.prepare(context, target) { state = it }
            busy = false
        }
    }
    ProfileUpdateAutoActions(
        state = state,
        context = context,
        allowAutoCheck = true,
        onCheck = { check() },
        onMissingFile = {
            hint = null
            state = UpdateUiState.Failed("File update hilang — ketuk untuk cek ulang")
        },
        onPermissionNeeded = { hint = it }
    )
    DrgGlassCard(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), onClick = { check(true) }) {
        Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DrgIconTileBox(
                icon = Icons.Default.SystemUpdate,
                tint = DrgGreenPrimary,
                size = 40.dp,
                iconSize = 20.dp,
                contentDescription = "Pembaruan aplikasi"
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Pembaruan Aplikasi", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                Text(updateStatusText(state, busy), fontSize = 11.sp, color = DrgTextMuted)
                hint?.let { Text(it, fontSize = 10.sp, color = DrgGreenPrimary) }
                if (available != null && (UpdateChecker.isMetered(context) || (UpdateChecker.batteryLevel(context) in 1..19 && !UpdateChecker.isCharging(context)))) Text("💡 Saran: update saat WiFi + charging (hemat kuota & baterai). Tetap bisa update sekarang.", fontSize = 10.sp, color = DrgTextMuted)
            }
            when {
                ready != null -> Button(
                    onClick = {
                        handleInstallOutcome(
                            ApkInstaller.install(context, ready.apk),
                            setHint = { hint = it },
                            setFailed = { msg -> state = UpdateUiState.Failed(msg) }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Pasang", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                available != null -> Button(
                    onClick = { prepare() },
                    enabled = !busy,
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) { Text(if (available.usesPatch) "Patch" else "APK", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold) }

                else -> Icon(Icons.Default.SystemUpdate, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(22.dp))
            }
        }
    }
}
