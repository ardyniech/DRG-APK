package com.example.modules.devtools

data class CiStepStatus(
    val stepNumber: Int,
    val name: String,
    val command: String,
    val isMandatory: Boolean,
    val status: String,
    val executionTimeEstimate: String,
    val failurePolicy: String
)

data class CiCdPipelineInfo(
    val workflowName: String,
    val targetBranches: List<String>,
    val timeoutMinutes: Int,
    val isConcurrencyCancelled: Boolean,
    val steps: List<CiStepStatus>,
    val totalTestSuites: Int,
    val artifactRetentionDays: Int
)

object CiCdStatusScanner {
    /** Konfigurasi pipeline (definisi); status eksekusi aktual UNKNOWN di runtime app. */
    fun inspectPipeline(): CiCdPipelineInfo = CiCdPipelineInfo(
        workflowName = "DRG Driver Android CI/CD",
        targetBranches = listOf("main", "develop"),
        timeoutMinutes = 25,
        isConcurrencyCancelled = true,
        totalTestSuites = -1,
        artifactRetentionDays = 14,
        steps = listOf(
            CiStepStatus(1, "Architecture Quality Audit", "python3 .github/scripts/check_file_length.py", true, "UNKNOWN di runtime (cek GitHub Actions)", "~", "HARD FAIL"),
            CiStepStatus(2, "Bawel Minus Audit", "python3 .github/scripts/audit_minus.py || true", true, "UNKNOWN di runtime (warn-only di CI)", "~", "WARN"),
            CiStepStatus(3, "Regenerate About Manifest", "python3 .github/scripts/generate_about_manifest.py", true, "UNKNOWN di runtime (cek GitHub Actions)", "~", "INFO"),
            CiStepStatus(4, "ktlint (warn-only)", "ktlint (CLI) || echo warn", false, "UNKNOWN di runtime (warn-only)", "~", "WARN"),
            CiStepStatus(5, "Android Lint", "./gradlew :app:lintDebug --no-daemon", true, "UNKNOWN di runtime (cek lint report)", "~", "FAIL on lint issues"),
            CiStepStatus(6, "Unit Tests", "./gradlew :app:testDebugUnitTest --no-daemon", true, "UNKNOWN di runtime (cek GitHub Actions)", "~", "HARD FAIL"),
            CiStepStatus(7, "Assemble Debug APK", "./gradlew assembleDebug --no-daemon", true, "UNKNOWN di runtime (cek artifacts)", "~", "Upload artifact")
        )
    )
}
