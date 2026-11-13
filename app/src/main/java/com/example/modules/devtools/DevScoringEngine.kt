package com.example.modules.devtools

data class ProjectHealthScore(
    val baseScore: Float = 95.0f,
    val deadButtonsCount: Int = DeadButtonShallowScanner.countDeadButtons(),
    val shallowFeaturesCount: Int = DeadButtonShallowScanner.countShallowFeatures(),
    val bugPenaltiesCount: Int = ProductionDefectScanner.scanKnownDefects().count { it.type == DefectType.PRODUCTION_DEFECT },
    val progressPenaltiesCount: Int = 1,
    val penaltyPerDeadButton: Float = 4.0f,
    val penaltyPerShallow: Float = 3.0f,
    val penaltyPerBug: Float = 2.0f,
    val penaltyPerProgressGap: Float = 2.0f
) {
    val totalPenalty: Float
        get() = (deadButtonsCount * penaltyPerDeadButton) +
                (shallowFeaturesCount * penaltyPerShallow) +
                (bugPenaltiesCount * penaltyPerBug) +
                (progressPenaltiesCount * penaltyPerProgressGap)

    val finalScore: Float
        get() = (baseScore - totalPenalty).coerceIn(0f, 100f)

    val grade: String
        get() = when {
            finalScore >= 90f -> "Grade A (Optimal)"
            finalScore >= 80f -> "Grade B+ (Stabil & Bersih)"
            finalScore >= 70f -> "Grade B (Peringatan Ketat: Ada Dead Buttons & Dangkal)"
            else -> "Grade C (Kritis - Wajib Refactoring)"
        }
}
