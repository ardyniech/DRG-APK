package com.example.modules.radar.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgTextSecondary

@Composable
fun RadarFilterChipsRow(
    filters: List<String>,
    selectedFilter: String,
    onSelectFilter: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    Row(
        modifier = modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        filters.forEach { filter ->
            val selected = selectedFilter == filter
            val shape = RoundedCornerShape(14.dp)
            Box(
                modifier = Modifier
                    .clip(shape)
                    .background(if (selected) theme.primaryColor else theme.glassBg)
                    .border(
                        1.dp,
                        if (selected) Color.White.copy(alpha = 0.65f) else theme.glassBorderColor.copy(alpha = 0.45f),
                        shape
                    )
                    .bouncyClick { onSelectFilter(filter) }
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    text = filter,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) Color.White else DrgTextSecondary
                )
            }
        }
    }
}
