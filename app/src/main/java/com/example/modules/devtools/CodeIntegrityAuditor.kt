package com.example.modules.devtools

data class CodeSmellItem(
    val smellId: String,
    val title: String,
    val category: String,
    val severity: String,
    val affectedModule: String,
    val smellDescription: String,
    val recommendedFix: String
)

data class CodeIntegritySubFeature(
    val subFeatureName: String,
    val smells: List<CodeSmellItem>
) {
    val hasPendingIssues: Boolean get() = smells.isNotEmpty()
}

data class CodeIntegrityFeatureTree(
    val featureName: String,
    val subFeatures: List<CodeIntegritySubFeature>
) {
    val totalPendingIssues: Int get() = subFeatures.sumOf { it.smells.size }
}

object CodeIntegrityAuditor {
    fun auditCodebase(): List<CodeIntegrityFeatureTree> = listOf(
        CodeIntegrityFeatureTree(
            featureName = "Core Architecture & State",
            subFeatures = listOf(
                CodeIntegritySubFeature(
                    subFeatureName = "Room v13 Persistence",
                    smells = emptyList()
                ),
                CodeIntegritySubFeature(
                    subFeatureName = "MVI StateFlow Dispatchers",
                    smells = listOf(
                        CodeSmellItem(
                            smellId = "SMELL-01",
                            title = "GlobalScope usage detected",
                            category = "Coroutine Management",
                            severity = "Sedang",
                            affectedModule = "core/sync/BackgroundSyncEngine.kt",
                            smellDescription = "Penggunaan GlobalScope tanpa supervisor job berisiko orphan coroutine.",
                            recommendedFix = "Ganti dengan viewModelScope atau CoroutineScope terikat lifecycle."
                        )
                    )
                )
            )
        ),
        CodeIntegrityFeatureTree(
            featureName = "Jetpack Compose UI Primitives",
            subFeatures = listOf(
                CodeIntegritySubFeature(
                    subFeatureName = "Material 3 Component Migration",
                    smells = listOf(
                        CodeSmellItem(
                            smellId = "SMELL-02",
                            title = "Deprecated Divider API",
                            category = "UI Deprecation",
                            severity = "Rendah",
                            affectedModule = "modules/devtools/DevBugHunterTab.kt",
                            smellDescription = "Pemanggilan Divider lama di Compose Material 3.",
                            recommendedFix = "Gunakan HorizontalDivider sesuai rekomendasi M3."
                        )
                    )
                ),
                CodeIntegritySubFeature(
                    subFeatureName = "Touch Target & Accessibility",
                    smells = emptyList()
                )
            )
        ),
        CodeIntegrityFeatureTree(
            featureName = "Emergency & SOS Engine",
            subFeatures = listOf(
                CodeIntegritySubFeature(
                    subFeatureName = "Haptic & SMS Fallback",
                    smells = emptyList()
                )
            )
        )
    )
}
