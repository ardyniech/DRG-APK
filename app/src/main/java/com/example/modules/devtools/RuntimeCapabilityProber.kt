package com.example.modules.devtools

data class CapabilityProbeItem(
    val featureName: String,
    val targetClass: String,
    val isPresentInRunningApk: Boolean,
    val introductionVersion: String,
    val operationalStatus: String
)

data class RuntimeCapabilitiesReport(
    val items: List<CapabilityProbeItem>,
    val presentCount: Int,
    val totalCount: Int,
    val compatibilityPercentage: Float,
    val summaryNote: String
)

object RuntimeCapabilityProber {
    private val CORE_CAPABILITIES = listOf(
        Pair("Kas Transparan & Laporan Keuangan", "com.example.modules.treasury.KasTransparanScreenKt") to "v2.0+",
        Pair("Rating Bengkel & Forum Komunitas", "com.example.modules.forum_workshop.WorkshopScreenKt") to "v2.0+",
        Pair("Gamifikasi & Badge Loyalitas", "com.example.modules.gamification.GamificationLeaderboardScreenKt") to "v2.5+",
        Pair("Simulasi Lapangan Dedicated Workflow", "com.example.modules.workflow.FieldWorkflowScreenKt") to "v3.0+",
        Pair("P2P Walkie Talkie & Audio Intercom", "com.example.modules.services.PttWalkieTalkieDialogKt") to "v3.5+",
        Pair("Audit Keamanan Ketat & Root Engine", "com.example.modules.devtools.SecurityAuditEngine") to "v4.0+",
        Pair("Topologi Kluster 5 Node Server", "com.example.modules.devtools.NodeTopologyScanner") to "v4.5+",
        Pair("Device Android Optimizer Advisor", "com.example.modules.devtools.DeviceAdvisorEngine") to "v4.8+",
        Pair("Hierarki Pohon Fase Pengembangan", "com.example.modules.devtools.PhaseTreeScanner") to "v5.0+",
        Pair("Audit UI/UX Real Experience Driver", "com.example.modules.devtools.UiUxAuditEngine") to "v5.0+"
    )

    fun probeRunningCapabilities(): RuntimeCapabilitiesReport {
        val list = CORE_CAPABILITIES.map { (pair, intro) ->
            val (name, className) = pair
            val isPresent = try {
                Class.forName(className)
                true
            } catch (_: ClassNotFoundException) {
                false
            } catch (_: NoClassDefFoundError) {
                false
            }

            CapabilityProbeItem(
                featureName = name,
                targetClass = className,
                isPresentInRunningApk = isPresent,
                introductionVersion = intro,
                operationalStatus = if (isPresent) "Terkompilasi di APK (cek operasionalitas di fase/devtools)" else "Belum terkompilasi di APK ini"
            )
        }

        val present = list.count { it.isPresentInRunningApk }
        val total = list.size
        val pct = if (total > 0) (present.toFloat() / total) * 100f else 0f
        val note = if (present == total) {
            "Semua modul target ($present/$total) terdeteksi/terkompilasi di APK ini (bukan jaminan fitur beroperasi penuh)."
        } else {
            "Modul terdeteksi $present/$total di APK ini (probe class-presence)."
        }

        return RuntimeCapabilitiesReport(
            items = list,
            presentCount = present,
            totalCount = total,
            compatibilityPercentage = pct,
            summaryNote = note
        )
    }
}
