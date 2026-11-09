package com.example.shared.atoms.cinematic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

data class CinematicScene(
    val headline: String,
    val caption: String,
    val durationMillis: Long
)

internal val CinematicShots = listOf(
    CinematicScene(
        headline = "JALAN ADALAH RUANG KERJA",
        caption = "Nol server. Nol biaya. Semua nyimpan di HP kamu sendiri.",
        durationMillis = 4800L
    ),
    CinematicScene(
        headline = "KOMUNITAS YANG MENJAGA",
        caption = "Radar langsung, SOS satu sentuh, kas transparan, forum bengkel.",
        durationMillis = 4800L
    ),
    CinematicScene(
        headline = "DATA TIDAK PINDAH KE MANA",
        caption = "P2P device-to-device. Serverless. Terenkripsi di perangkat.",
        durationMillis = 4800L
    )
)

@Stable
class CinematicDirectorState(
    val sceneIndex: Int,
    val ctaVisible: Boolean,
    val openMenu: () -> Unit,
    val closeMenu: () -> Unit,
    val skipToNext: () -> Unit,
    val replay: () -> Unit
) {
    val scene: CinematicScene get() = CinematicShots[sceneIndex.coerceIn(0, CinematicShots.lastIndex)]
    val isLastScene: Boolean get() = sceneIndex >= CinematicShots.lastIndex
    val skipToCta: () -> Unit get() = openMenu
}

@Composable
fun rememberCinematicDirector(): CinematicDirectorState {
    var sceneIndex by remember { mutableIntStateOf(0) }
    var ctaVisible by remember { mutableStateOf(false) }

    // Continuous scenic looping — NEVER pop up CTA automatically
    LaunchedEffect(sceneIndex) {
        val duration = CinematicShots[sceneIndex.coerceIn(0, CinematicShots.lastIndex)].durationMillis
        delay(duration)
        sceneIndex = (sceneIndex + 1) % CinematicShots.size
    }

    return CinematicDirectorState(
        sceneIndex = sceneIndex,
        ctaVisible = ctaVisible,
        openMenu = { ctaVisible = true },
        closeMenu = { ctaVisible = false },
        skipToNext = { sceneIndex = (sceneIndex + 1) % CinematicShots.size },
        replay = {
            sceneIndex = 0
            ctaVisible = false
        }
    )
}