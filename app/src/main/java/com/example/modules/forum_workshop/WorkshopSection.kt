package com.example.modules.forum_workshop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.WorkshopPartner
import com.example.ui.theme.*

@Composable
fun WorkshopSection(
    workshops: List<WorkshopPartner>,
    onCallWorkshop: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    val categories = listOf("Semua", "Tambal Ban", "Bengkel Umum", "Kelistrikan", "Panggilan 24 Jam")

    val filteredWorkshops = remember(workshops, searchQuery, selectedCategory) {
        workshops.filter { bkl ->
            val matchesSearch = bkl.name.contains(searchQuery, ignoreCase = true) || 
                                bkl.services.contains(searchQuery, ignoreCase = true) ||
                                bkl.address.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "Semua" || bkl.services.contains(selectedCategory, ignoreCase = true)
            matchesSearch && matchesCategory
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(top = 4.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari bengkel rekanan...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Cari", modifier = Modifier.size(18.dp)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DrgSurface,
                unfocusedContainerColor = DrgSurface,
                focusedBorderColor = DrgGreenPrimary,
                unfocusedBorderColor = DrgOutline
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().height(52.dp)
        )

        // Category Filter Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(categories) { cat ->
                val isSelected = selectedCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = DrgGreenPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = DrgSurface,
                        labelColor = DrgTextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = DrgOutline,
                        selectedBorderColor = DrgGreenPrimary
                    )
                )
            }
        }

        // Info Banner
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = DrgAmberContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Build, contentDescription = "Promo", tint = DrgAmberSecondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Diskon khusus anggota DRG s/d 20% cukup tunjukkan KTA digital.",
                    fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = DrgOnAmberContainer
                )
            }
        }

        // Workshop List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (filteredWorkshops.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Tidak ada bengkel rekanan yang cocok.", color = DrgTextSecondary, fontSize = 12.sp)
                    }
                }
            } else {
                items(filteredWorkshops) { bkl ->
                    WorkshopCard(bkl = bkl, onCall = { onCallWorkshop(bkl.phone) })
                }
            }
        }
    }
}

@Composable
private fun WorkshopCard(
    bkl: WorkshopPartner,
    onCall: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = DrgSurface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = bkl.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                    Text(text = "📍 ${bkl.address} (${bkl.distanceKm} KM)", fontSize = 11.sp, color = DrgTextSecondary)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DrgAmberSecondary)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(text = "DISC ${bkl.discountPercent}%", fontSize = 9.sp, fontWeight = FontWeight.Black, color = Color.White)
                }
            }

            Text(text = "🔧 Layanan: ${bkl.services}", fontSize = 11.sp, color = DrgTextPrimary)
            
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(Icons.Default.Star, contentDescription = "Rating", tint = DrgAmberSecondary, modifier = Modifier.size(13.dp))
                Text(text = "${bkl.rating} (${bkl.reviewCount} Ulasan) • Buka: ${bkl.openHours}", fontSize = 10.sp, color = DrgTextMuted)
            }

            Button(
                onClick = onCall,
                colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(36.dp)
            ) {
                Icon(imageVector = Icons.Default.Phone, contentDescription = "Hubungi", modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Hubungi Bengkel / Booking", fontSize = 11.sp)
            }
        }
    }
}

