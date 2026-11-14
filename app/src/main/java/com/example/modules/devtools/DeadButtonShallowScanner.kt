package com.example.modules.devtools

object DeadButtonShallowScanner {
    fun getAuditedFeatures(): List<FeatureAuditItem> = listOf(
        FeatureAuditItem(
            name = "Emergency SOS & Tactical Haptics",
            module = "Emergency",
            stage = FeatureStage.LULUS,
            codeLineCount = 118
        ),
        FeatureAuditItem(
            name = "Kas Transparan & Export Text Share",
            module = "Treasury",
            stage = FeatureStage.LULUS,
            codeLineCount = 118
        ),
        FeatureAuditItem(
            name = "Bengkel Rekanan & Rating 1-5 Star",
            module = "Forum & Bengkel",
            stage = FeatureStage.LULUS,
            codeLineCount = 108
        ),
        FeatureAuditItem(
            name = "Field Workflow Realistic Simulation",
            module = "Workflow",
            stage = FeatureStage.LULUS,
            codeLineCount = 106
        ),
        FeatureAuditItem(
            name = "Posko Radar & Driver Proximity",
            module = "Radar",
            stage = FeatureStage.LULUS,
            codeLineCount = 115
        ),
        FeatureAuditItem(
            name = "P2P Walkie Talkie Voice Fallback",
            module = "Services",
            stage = FeatureStage.MATURE,
            hasDeadButtons = false,
            codeLineCount = 112
        ),
    )

    fun countDeadButtons(): Int = getAuditedFeatures().count { it.hasDeadButtons }
    fun countShallowFeatures(): Int = getAuditedFeatures().count { it.isShallow }
}
