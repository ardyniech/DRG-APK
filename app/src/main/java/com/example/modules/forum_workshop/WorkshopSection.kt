package com.example.modules.forum_workshop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.shared.models.DriverReview
import com.example.shared.models.WorkshopPartner
import com.example.ui.theme.DrgTextSecondary

@Composable
fun WorkshopSection(
    workshops: List<WorkshopPartner>,
    onCallWorkshop: (String) -> Unit,
    currentMember: DriverMember? = null,
    onRateWorkshop: (WorkshopPartner, Float, String, String) -> Unit = { _, _, _, _ -> },
    onBookingWorkshop: (WorkshopPartner, String) -> Unit = { _, _ -> },
    reviewsMap: Map<String, List<DriverReview>> = emptyMap(),
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    var reviewingWorkshop by remember { mutableStateOf<WorkshopPartner?>(null) }
    var viewingReviewsWorkshop by remember { mutableStateOf<WorkshopPartner?>(null) }
    var bookingWorkshop by remember { mutableStateOf<WorkshopPartner?>(null) }
    val categories = listOf("Semua", "★ Terpercaya (4.5+)", "Tambal Ban", "Bengkel Umum", "Kelistrikan")

    val filtered = remember(workshops, searchQuery, selectedCategory) {
        workshops.filter { bkl ->
            val matchesSearch = bkl.name.contains(searchQuery, true) || bkl.services.contains(searchQuery, true)
            val matchesCat = when (selectedCategory) {
                "Semua" -> true
                "★ Terpercaya (4.5+)" -> bkl.rating >= 4.5f
                else -> bkl.services.contains(selectedCategory, true)
            }
            matchesSearch && matchesCat
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(top = 4.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        WorkshopFilterBar(
            searchQuery = searchQuery,
            onSearchChange = { searchQuery = it },
            selectedCategory = selectedCategory,
            onCategoryChange = { selectedCategory = it },
            categories = categories
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
            if (filtered.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Tidak ada bengkel rekanan yang cocok.", color = DrgTextSecondary, fontSize = 12.sp)
                    }
                }
            } else {
                items(filtered) { bkl ->
                    WorkshopCard(
                        bkl = bkl,
                        onCall = { onCallWorkshop(bkl.phone) },
                        onRateClick = { reviewingWorkshop = bkl },
                        onViewReviewsClick = { viewingReviewsWorkshop = bkl },
                        onBookingClick = { bookingWorkshop = bkl }
                    )
                }
            }
        }
    }

    reviewingWorkshop?.let { bkl ->
        ReviewWorkshopDialog(
            workshop = bkl,
            onDismiss = { reviewingWorkshop = null },
            onSubmitReview = { rating, srv, cmt ->
                onRateWorkshop(bkl, rating, srv, cmt)
                reviewingWorkshop = null
            }
        )
    }

    bookingWorkshop?.let { bkl ->
        BookingWorkshopDialog(
            workshop = bkl,
            currentMember = currentMember,
            onDismiss = { bookingWorkshop = null },
            onConfirmBooking = { msg ->
                onBookingWorkshop(bkl, msg)
                bookingWorkshop = null
            }
        )
    }

    viewingReviewsWorkshop?.let { bkl ->
        WorkshopReviewsSheet(
            workshop = bkl,
            reviews = reviewsMap[bkl.id] ?: emptyList(),
            onDismiss = { viewingReviewsWorkshop = null },
            onOpenWriteReview = { reviewingWorkshop = bkl }
        )
    }
}
