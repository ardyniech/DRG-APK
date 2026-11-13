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
    /** Audit statis heuristik; untuk bukti faktual lihat check_file_length.py & audit_minus.py. */
    fun auditCodebase(): List<CodeIntegrityFeatureTree> = listOf(
        CodeIntegrityFeatureTree(
            featureName = "Core Architecture & State",
            subFeatures = listOf(
                CodeIntegritySubFeature("Room v14 Persistence", emptyList()),
                CodeIntegritySubFeature(
                    subFeatureName = "MVI StateFlow Dispatchers",
                    smells = listOf(
                        CodeSmellItem(
                            smellId = "SMELL-01",
                            title = "Risiko orphan coroutine (heuristik)",
                            category = "Coroutine Management",
                            severity = "Info",
                            affectedModule = "core/sync/* (periksa penggunaan scope)",
                            smellDescription = "Pastikan coroutine terikat lifecycle/scope terkelola; hindari GlobalScope untuk pekerjaan UI/fitur.",
                            recommendedFix = "Gunakan viewModelScope/CoroutineScope terikat lifecycle + SupervisorJob bila perlu."
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
                            title = "Migrasi API M3 (heuristik)",
                            category = "UI Deprecation",
                            severity = "Info",
                            affectedModule = "modules/** (cek saat refactor)",
                            smellDescription = "Gunakan komponen Material 3 terkini (mis. HorizontalDivider) saat menyentuh file terkait.",
                            recommendedFix = "Ganti API deprecated secara bertahap saat perubahan terverifikasi."
                        )
                    )
                ),
                CodeIntegritySubFeature("Touch Target & Accessibility", emptyList())
            )
        ),
        CodeIntegrityFeatureTree("Emergency & SOS Engine", listOf(CodeIntegritySubFeature("Haptic & SMS Fallback", emptyList())))
    )
}
