package com.example.modules.radar.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgSurface

@Composable
fun MapEngineSwitchBar(
    isGoogleMapsActive: Boolean,
    onSelectEngine: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = DrgSurface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledTonalButton(
                onClick = { onSelectEngine(false) },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (!isGoogleMapsActive) DrgGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (!isGoogleMapsActive) DrgSurface else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 48.dp)
            ) {
                Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Peta Bebas (OSM)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            FilledTonalButton(
                onClick = { onSelectEngine(true) },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = if (isGoogleMapsActive) DrgGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isGoogleMapsActive) DrgSurface else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .defaultMinSize(minHeight = 48.dp)
            ) {
                Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Google Maps", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
