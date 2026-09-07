package com.example.modules.members

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.shared.models.PoskoLocation
import com.example.ui.theme.*

@Composable
fun PoskoTabSection(
    currentMember: DriverMember?,
    poskoList: List<PoskoLocation>,
    searchQuery: String
) {
    var sortByNearest by remember { mutableStateOf(false) }

    val userLat = currentMember?.currentLat ?: -7.9839
    val userLng = currentMember?.currentLng ?: 112.6214

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = if (sortByNearest) "📍 Diurutkan terdekat (GPS)" else "Semua Posko Aktif",
            fontSize = 11.sp,
            color = DrgTextSecondary,
            fontWeight = FontWeight.Bold
        )
        FilterChip(
            selected = sortByNearest,
            onClick = { sortByNearest = !sortByNearest },
            label = { Text("Terdekat (GPS)", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = DrgGreenContainer,
                selectedLabelColor = DrgGreenPrimary
            )
        )
    }

    val rawPosko = poskoList.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.area.contains(searchQuery, ignoreCase = true)
    }
    val filteredPosko = if (sortByNearest) {
        rawPosko.sortedBy { p -> calculateDistance(userLat, userLng, p.lat, p.lng) }
    } else {
        rawPosko
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        items(filteredPosko) { p ->
            val dist = calculateDistance(userLat, userLng, p.lat, p.lng)
            PoskoCard(posko = p, distanceKm = dist)
        }
    }
}

private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val r = 6371.0
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    return r * c
}
