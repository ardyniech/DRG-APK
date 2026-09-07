package com.example.shared.models

enum class AccessLevel {
    ADMIN,
    PENGURUS,
    MEMBER
}

data class UserRole(
    val accessLevel: AccessLevel,
    val name: String,
    val description: String,
    val permissions: List<String>
)
