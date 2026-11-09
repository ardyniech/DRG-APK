package com.example.modules.devtools

enum class SymptomSeverity(val label: String, val badgeColorHex: Long, val textColorHex: Long) {
    CRITICAL("Kritis (High Risk)", 0xFFFFEBEE, 0xFFC62828),
    WARNING("Peringatan (Medium)", 0xFFFFF8E1, 0xFFF57F17),
    PREVENTIVE("Gejala Awal (Early Warning)", 0xFFE0F2FE, 0xFF075985)
}

data class RootCauseFinding(
    val id: String,
    val title: String,
    val preFailureSymptom: String,
    val sourceFile: String,
    val sourceFunction: String,
    val rootCauseTechnical: String,
    val severity: SymptomSeverity,
    val actionableFix: String,
    val isAutoDetected: Boolean = true
)
