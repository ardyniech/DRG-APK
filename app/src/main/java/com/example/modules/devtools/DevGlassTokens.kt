package com.example.modules.devtools

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.shared.atoms.LocalAppVisualTheme

/**
 * Token jembatan agar seluruh tab DevTools mengikuti tema luminous aktif
 * (acuan visual: dashboard). Satu sumber, bukan hardcode per tab.
 */
@Composable
fun devGlassBg(): Color = LocalAppVisualTheme.current.glassBg

@Composable
fun devGlassBorder(): Color = LocalAppVisualTheme.current.glassBorderColor

@Composable
fun devAccent(): Color = LocalAppVisualTheme.current.primaryColor

@Composable
fun devPillBg(): Color = LocalAppVisualTheme.current.pillBg
