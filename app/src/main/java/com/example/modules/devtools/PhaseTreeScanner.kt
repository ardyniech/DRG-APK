package com.example.modules.devtools

object PhaseTreeScanner {
    fun scanFullTree(): List<DevPhase> = listOf(
        PhaseTreePhase1.createPhase1(),
        PhaseTreePhase2.createPhase2(),
        PhaseTreePhase3And4.createPhase3(),
        PhaseTreePhase3And4.createPhase4(),
        PhaseTreePhase5.createPhase5()
    )

    fun getMetrics(): PhaseTreeMetrics {
        val leaves = scanFullTree().flatMap { phase -> phase.subPhases.flatMap { it.features } }
        return PhaseTreeMetrics(
            totalFeatures = leaves.size,
            inScopeFeatures = leaves.count { it.status != PhaseTreeStatus.DEFERRED },
            completedFeatures = leaves.count { it.status == PhaseTreeStatus.LULUS_SEMPURNA },
            inProgressFeatures = leaves.count { it.status == PhaseTreeStatus.DALAM_PENGERJAAN && !it.hasDefect },
            defectFeatures = leaves.count { it.hasDefect },
            deferredFeatures = leaves.count { it.status == PhaseTreeStatus.DEFERRED }
        )
    }

    /** Kompatibilitas: (total, lulus, cacat). Progres in-scope pakai [getMetrics]. */
    fun getTotalMetrics(): Triple<Int, Int, Int> {
        val m = getMetrics()
        return Triple(m.totalFeatures, m.completedFeatures, m.defectFeatures)
    }
}
