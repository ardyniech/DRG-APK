package com.drg.driver.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.modules.emergency.SosFloatingActionButton

@Composable
fun PanicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SosFloatingActionButton(
        onClick = onClick,
        modifier = modifier
    )
}
