package com.example.shared.atoms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.DrgBackground

@Composable
fun DrgScreenContainer(
    modifier: Modifier = Modifier,
    backgroundColor: Color = DrgBackground,
    showAtmosphericGlow: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (showAtmosphericGlow) {
            DrgAtmosphericGlow(modifier = Modifier.fillMaxSize())
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            content = content
        )
    }
}