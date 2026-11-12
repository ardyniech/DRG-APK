package com.example.modules.devtools

data class BuildDeploymentInfo(
    val lastCommitHash: String,
    val buildDurationSeconds: Int,
    val testCoveragePercentage: Float,
    val totalTestSuites: Int,
    val failedCommitsCount: Int,
    val lastFailureLog: String?,
    val isPipelineHealthy: Boolean
)

object BuildDeploymentEngine {
    /** Runtime app tidak bisa probe git/CI/coverage; jangan tampilkan angka fabrikasi. */
    fun inspectBuildStatus(): BuildDeploymentInfo = BuildDeploymentInfo(
        lastCommitHash = "UNKNOWN (cek git/CI: gh run list / git log)",
        buildDurationSeconds = -1,
        testCoveragePercentage = -1f,
        totalTestSuites = -1,
        failedCommitsCount = -1,
        lastFailureLog = null,
        isPipelineHealthy = false // unknown ≠ healthy
    )
}
