package com.example.shared.models

enum class CrashSensitivity(
    val displayName: String,
    val thresholdG: Float,
    val description: String
) {
    LOW("Rendah", 3.5f, "Hanya benturan sangat keras (3.5G)"),
    MEDIUM("Sedang", 2.8f, "Standar aman berkendara motor (2.8G)"),
    HIGH("Tinggi", 2.2f, "Sangat sensitif getaran/benturan (2.2G)");

    companion object {
        fun fromName(name: String?): CrashSensitivity {
            return entries.firstOrNull { it.name == name } ?: MEDIUM
        }
    }
}
