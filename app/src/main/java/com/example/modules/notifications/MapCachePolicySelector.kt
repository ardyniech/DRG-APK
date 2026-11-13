package com.example.modules.notifications

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.cache.MapCachePolicy
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@Composable
fun MapCachePolicySelector(
    selectedPolicy: MapCachePolicy,
    onSelectPolicy: (MapCachePolicy) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Strategi Cache Tile Peta (Offline & Cepat)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            MapCachePolicy.entries.forEach { policy ->
                val isSelected = policy == selectedPolicy
                OutlinedButton(
                    onClick = { onSelectPolicy(policy) },
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        width = if (isSelected) 1.5.dp else 0.8.dp,
                        color = if (isSelected) theme.primaryColor else theme.glassBorderColor.copy(alpha = 0.5f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) theme.pillBg else Color(0xDCFFFFFF)
                    )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = when (policy) {
                                MapCachePolicy.CACHE_FIRST -> "Cache Cepat"
                                MapCachePolicy.NETWORK_FIRST -> "Online Baru"
                                MapCachePolicy.OFFLINE_ONLY -> "Offline 100%"
                            },
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                            color = if (isSelected) theme.primaryColor else DrgTextDark
                        )
                        Text(
                            text = when (policy) {
                                MapCachePolicy.CACHE_FIRST -> "Hemat Kuota"
                                MapCachePolicy.NETWORK_FIRST -> "Live Tile"
                                MapCachePolicy.OFFLINE_ONLY -> "Bebas Sinyal"
                            },
                            fontSize = 8.5.sp,
                            color = if (isSelected) theme.primaryColor else DrgTextSecondary
                        )
                    }
                }
            }
        }
    }
}
