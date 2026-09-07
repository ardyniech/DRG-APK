package com.example.shared.models

data class ServiceMarketItem(
    val id: String,
    val title: String,
    val category: String,
    val priceRp: Long,
    val pointSubsidy: Int,
    val poskoLocation: String,
    val isReadyStock: Boolean,
    val discountPercent: Int = 0
)

data class InsuranceClaimItem(
    val id: String,
    val title: String,
    val maxCoverageRp: Long,
    val status: String,
    val category: String,
    val iconName: String
)
