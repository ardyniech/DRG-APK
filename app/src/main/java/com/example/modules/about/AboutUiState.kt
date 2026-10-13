package com.example.modules.about

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

sealed interface AboutUiState {
    object Loading : AboutUiState
    data class Error(val message: String) : AboutUiState
    data class Ready(val manifest: AboutManifest) : AboutUiState
}

@Composable
fun rememberAboutUiState(context: Context): State<AboutUiState> {
    val state = remember { mutableStateOf<AboutUiState>(AboutUiState.Loading) }
    LaunchedEffect(Unit) {
        state.value = AboutManifestRepository.load(context).fold(
            onSuccess = { AboutUiState.Ready(it) },
            onFailure = { AboutUiState.Error(it.message ?: "Gagal memuat data fitur.") }
        )
    }
    return state
}
