package com.example.shared.models

data class AdminLog(
    val id: String,
    val actorName: String,
    val actorRole: String,
    val action: String,
    val targetName: String,
    val timestamp: String
)
