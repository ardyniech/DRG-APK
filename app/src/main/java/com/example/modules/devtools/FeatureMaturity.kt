package com.example.modules.devtools

enum class FeatureStage(
    val label: String,
    val description: String,
    val badgeColorHex: Long,
    val textColorHex: Long
) {
    BENIH("Benih (Seed)", "Konsep/Schema saja, belum ada controller", 0xFFE0E7FF, 0xFF3730A3),
    ANAK("Anak (Incubating)", "UI ada, logic parsial, rentan dead button", 0xFFFEF3C7, 0xFF92400E),
    MATURE("Mature (Remaja)", "ViewModel + Room DAO + Flow aktif", 0xFFE0F2FE, 0xFF075985),
    LULUS("Lulus (Graduated)", "Siap rilis: tidak ada dead button/cacat kritis teridentifikasi", 0xFFDCFCE7, 0xFF166534)
}

data class FeatureAuditItem(
    val name: String,
    val module: String,
    val stage: FeatureStage,
    val hasDeadButtons: Boolean = false,
    val isShallow: Boolean = false,
    val deadButtonDetails: String? = null,
    val shallowReason: String? = null,
    val codeLineCount: Int = 110
)
