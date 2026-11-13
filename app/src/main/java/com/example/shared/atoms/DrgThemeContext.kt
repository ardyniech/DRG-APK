package com.example.shared.atoms

import androidx.compose.runtime.staticCompositionLocalOf
import com.example.shared.models.AppVisualTheme

/**
 * Global active luminous theme propagated through the entire Compose hierarchy
 * so every screen, sub-tab card, dialog, and record form automatically matches 1-DNA styling.
 */
val LocalAppVisualTheme = staticCompositionLocalOf { AppVisualTheme.EMERALD_AURORA }
