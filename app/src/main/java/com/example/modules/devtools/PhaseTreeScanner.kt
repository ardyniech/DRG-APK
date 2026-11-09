package com.example.modules.devtools

object PhaseTreeScanner {
    fun scanFullTree(): List<DevPhase> = listOf(
        PhaseTreePhase1And2.createPhase1(),
        PhaseTreePhase1And2.createPhase2(),
        PhaseTreePhase3And4.createPhase3(),
        PhaseTreePhase3And4.createPhase4(),
        PhaseTreePhase5.createPhase5()
    )

    fun getTotalMetrics(): Triple<Int, Int, Int> {
        val phases = scanFullTree()
        val total = phases.sumOf { it.totalFeatures }
        val completed = phases.sumOf { it.completedFeatures }
        val defect = phases.sumOf { it.defectFeatures }
        return Triple(total, completed, defect)
    }
}
