package com.example.modules.about

data class AboutManifest(
    val app: AboutApp,
    val build: AboutBuild,
    val summary: AboutSummary,
    val phases: List<AboutPhase>,
    val features: List<AboutFeature>,
    val plannedFeatures: List<AboutFeature>,
    val refinements: List<AboutRefinement>,
    val backlog: List<String>
)

data class AboutApp(
    val name: String,
    val tagline: String,
    val packageName: String,
    val audience: String,
    val philosophy: List<String>
)

data class AboutBuild(val version: String, val commit: String, val commitDate: String)

data class AboutSummary(
    val overallProgress: Int,
    val totalFeatures: Int,
    val done: Int,
    val inProgress: Int,
    val planned: Int,
    val modules: Int,
    val totalSourceFiles: Int,
    val totalLoc: Int,
    val testSuites: Int
)

data class AboutPhase(
    val id: String,
    val title: String,
    val target: String,
    val state: String,
    val summary: String,
    val progress: Int,
    val featureCount: Int,
    val doneCount: Int
)

data class AboutFeature(
    val id: String,
    val name: String,
    val description: String,
    val phase: String,
    val status: String,
    val progress: Int
)

data class AboutRefinement(
    val id: String,
    val title: String,
    val detail: String,
    val phase: String,
    val state: String
)

object FeatureStatus {
    const val DONE = "DONE"
    const val IN_PROGRESS = "IN_PROGRESS"
    const val PLANNED = "PLANNED"

    fun label(status: String): String = when (status) {
        DONE -> "Selesai"
        IN_PROGRESS -> "Dibangun"
        else -> "Rencana"
    }
}
