package com.drg.driver.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PanicButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = Color(0xFFDC2626), // DrgRedPanic
        contentColor = Color.White,
        modifier = Modifier.size(72.dp)
    ) {
        Icon(Icons.Default.Warning, contentDescription = "SOS", modifier = Modifier.size(32.dp))
    }
}