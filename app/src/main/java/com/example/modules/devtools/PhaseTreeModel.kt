package com.example.modules.devtools

enum class PhaseTreeStatus(val label: String) {
    LULUS_SEMPURNA("Lulus Sempurna"),
    TERKENDALA_CACAT("Terkendala Cacat"),
    DALAM_PENGERJAAN("Dalam Pengerjaan"),
    PENDING("Belum Dimulai"),
    DEFERRED("Ditunda (Deferred)")
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
    val deferredCount: Int get() = features.count { it.status == PhaseTreeStatus.DEFERRED }
    val inScopeCount: Int get() = features.count { it.status != PhaseTreeStatus.DEFERRED }
    val totalCount: Int get() = features.size
    val hasAnyDefect: Boolean get() = features.any { it.hasDefect }
    val status: PhaseTreeStatus get() = when {
        hasAnyDefect -> PhaseTreeStatus.TERKENDALA_CACAT
        inScopeCount == 0 -> PhaseTreeStatus.DEFERRED
        completedCount == inScopeCount -> PhaseTreeStatus.LULUS_SEMPURNA
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
    val inScopeFeatures: Int get() = subPhases.sumOf { it.inScopeCount }
    val deferredFeatures: Int get() = subPhases.sumOf { it.deferredCount }
    val completedFeatures: Int get() = subPhases.sumOf { it.completedCount }
    val defectFeatures: Int get() = subPhases.sumOf { sub -> sub.features.count { it.hasDefect } }
    val completionPercentage: Float
        get() = if (inScopeFeatures > 0) (completedFeatures.toFloat() / inScopeFeatures) * 100f else 0f
    val hasAnyDefect: Boolean get() = subPhases.any { it.hasAnyDefect }
    val overallStatus: PhaseTreeStatus get() = when {
        hasAnyDefect -> PhaseTreeStatus.TERKENDALA_CACAT
        inScopeFeatures == 0 -> PhaseTreeStatus.DEFERRED
        completedFeatures == inScopeFeatures -> PhaseTreeStatus.LULUS_SEMPURNA
        completedFeatures > 0 -> PhaseTreeStatus.DALAM_PENGERJAAN
        else -> PhaseTreeStatus.PENDING
    }
}

/** Metrik agregat PhaseTree. Deferred dikecualikan dari denominator progres in-scope. */
data class PhaseTreeMetrics(
    val totalFeatures: Int,
    val inScopeFeatures: Int,
    val completedFeatures: Int,
    val inProgressFeatures: Int,
    val defectFeatures: Int,
    val deferredFeatures: Int
) {
    val progressPercentage: Float
        get() = if (inScopeFeatures > 0) (completedFeatures.toFloat() / inScopeFeatures) * 100f else 0f
    val progressGapCount: Int get() = inProgressFeatures
}
