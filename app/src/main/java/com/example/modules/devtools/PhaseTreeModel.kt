package com.example.modules.devtools

enum class PhaseTreeStatus(val label: String) {
    LULUS_SEMPURNA("Lulus Sempurna"),
    TERKENDALA_CACAT("Terkendala Cacat"),
    DALAM_PENGERJAAN("Dalam Pengerjaan"),
    PENDING("Belum Dimulai")
}

data class DevFeatureLeaf(
    val id: String,
    val name: String,
    val status: PhaseTreeStatus,
    val hasDefect: Boolean,
    val whatIsIt: String,
    val whyRequired: String,
    val defectExplanation: String? = null
)

data class DevSubPhase(
    val id: String,
    val title: String,
    val features: List<DevFeatureLeaf>
) {
    val completedCount: Int get() = features.count { it.status == PhaseTreeStatus.LULUS_SEMPURNA }
    val totalCount: Int get() = features.size
    val hasAnyDefect: Boolean get() = features.any { it.hasDefect }
    val status: PhaseTreeStatus get() = when {
        hasAnyDefect -> PhaseTreeStatus.TERKENDALA_CACAT
        completedCount == totalCount -> PhaseTreeStatus.LULUS_SEMPURNA
        completedCount > 0 -> PhaseTreeStatus.DALAM_PENGERJAAN
        else -> PhaseTreeStatus.PENDING
    }
}

data class DevPhase(
    val phaseNumber: Int,
    val title: String,
    val objective: String,
    val subPhases: List<DevSubPhase>
) {
    val totalFeatures: Int get() = subPhases.sumOf { it.totalCount }
    val completedFeatures: Int get() = subPhases.sumOf { it.completedCount }
    val defectFeatures: Int get() = subPhases.sumOf { it.features.count { f -> f.hasDefect } }
    val completionPercentage: Float get() = if (totalFeatures > 0) (completedFeatures.toFloat() / totalFeatures) * 100f else 0f
    val hasAnyDefect: Boolean get() = subPhases.any { it.hasAnyDefect }
    val overallStatus: PhaseTreeStatus get() = when {
        hasAnyDefect -> PhaseTreeStatus.TERKENDALA_CACAT
        completedFeatures == totalFeatures -> PhaseTreeStatus.LULUS_SEMPURNA
        completedFeatures > 0 -> PhaseTreeStatus.DALAM_PENGERJAAN
        else -> PhaseTreeStatus.PENDING
    }
}
