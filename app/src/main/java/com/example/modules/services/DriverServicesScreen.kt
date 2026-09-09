package com.example.modules.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.InsuranceClaimItem
import com.example.shared.models.ServiceMarketItem
import com.example.ui.theme.*

@Composable
fun DriverServicesScreen(
    onOrderMarketItem: (ServiceMarketItem) -> Unit = {},
    onClaimInsurance: (InsuranceClaimItem) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(0) }
    val categories = listOf("Pasar Koperasi", "Dana Talangan", "AI Driver Assistant", "Escort Satgas")

    val koperasiItems = listOf(
        ServiceMarketItem("M1", "Helm SNI Official DRG Gold", "Atribut", 150000, 20, "Posko Utama Klojen", true, 25),
        ServiceMarketItem("M2", "Oli Motul 10W-40 1L (Subsidi)", "Sparepart", 45000, 10, "Posko 2 Suhat", true, 30),
        ServiceMarketItem("M3", "Jas Hujan Double Press Satgas", "Atribut", 85000, 15, "Posko 3 Batu", true, 15),
        ServiceMarketItem("M4", "Holder HP Alumunium Anti-Getar", "Peralatan", 65000, 10, "Posko Utama Klojen", true, 20)
    )

    val protectionClaims = listOf(
        InsuranceClaimItem("C1", "Dana Talangan Mogok / Ban Pecah", 500000, "Siaga 24 Jam", "Kecemasan Jalan", "Build"),
        InsuranceClaimItem("C2", "Restitusi Servis Layar HP Pecah", 300000, "Proses 1x24 Jam", "Alat Kerja", "PhoneAndroid"),
        InsuranceClaimItem("C3", "Santunan Kecelakaan Satgas Jalur", 2000000, "Diverifikasi Pengurus", "Jiwa & Medis", "MedicalServices")
    )

    var showAiDialog by remember { mutableStateOf(false) }
    var showPttDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
    ) {
        // Top Filter Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedCategory,
            edgePadding = 12.dp,
            containerColor = DrgSurface,
            contentColor = DrgAmberSecondary,
            divider = {}
        ) {
            categories.forEachIndexed { index, title ->
                Tab(
                    selected = selectedCategory == index,
                    onClick = { selectedCategory = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedCategory == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedCategory == index) DrgAmberSecondary else DrgTextSecondary
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (selectedCategory) {
                0 -> {
                    item { MarketHeaderCard() }
                    item { KoperasiWholesaleBanner() }
                    items(koperasiItems.size) { idx ->
                        MarketItemCard(item = koperasiItems[idx], onOrder = onOrderMarketItem)
                    }
                }
                1 -> {
                    item { ProtectionHeaderCard() }
                    items(protectionClaims.size) { idx ->
                        ClaimItemCard(claim = protectionClaims[idx], onClaim = onClaimInsurance)
                    }
                }
                2 -> {
                    item {
                        AiAssistantCard(onOpenAi = { showAiDialog = true })
                    }
                }
                3 -> {
                    item {
                        EscortSatgasCard(onOpenPtt = { showPttDialog = true })
                    }
                }
            }
        }
    }

    // Fully Interactive Dialogs
    if (showAiDialog) {
        AiAssistantDialog(onDismiss = { showAiDialog = false })
    }

    if (showPttDialog) {
        PttWalkieTalkieDialog(onDismiss = { showPttDialog = false })
    }
}
