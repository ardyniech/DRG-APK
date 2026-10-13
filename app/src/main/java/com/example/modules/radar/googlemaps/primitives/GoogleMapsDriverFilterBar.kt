package com.example.modules.radar.googlemaps.primitives

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.radar.googlemaps.models.DriverMapFilter
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgSurface

@Composable
fun GoogleMapsDriverFilterBar(
    selectedFilter: DriverMapFilter,
    onSelectFilter: (DriverMapFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        DriverMapFilter.entries.forEach { filter ->
            val isSelected = filter == selectedFilter
            FilterChip(
                selected = isSelected,
                onClick = { onSelectFilter(filter) },
                label = { Text(text = filter.label, fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DrgGreenPrimary,
                    selectedLabelColor = DrgSurface
                ),
                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
            )
        }
    }
}
