package com.example.modules.devtools

import com.example.ui.theme.drgFeatureBenihBadge
import com.example.ui.theme.drgFeatureBenihText
import com.example.ui.theme.drgFeatureAnakBadge
import com.example.ui.theme.drgFeatureAnakText
import com.example.ui.theme.drgFeatureMatureBadge
import com.example.ui.theme.drgFeatureMatureText
import com.example.ui.theme.drgFeatureLulusBadge
import com.example.ui.theme.drgFeatureLulusText

enum class FeatureStage(
    val label: String,
    val description: String,
    val badgeColor: Long,
    val textColor: Long
) {
    BENIH("Benih (Seed)", "Konsep/Schema saja, belum ada controller", drgFeatureBenihBadge, drgFeatureBenihText),
    ANAK("Anak (Incubating)", "UI ada, logic parsial, rentan dead button", drgFeatureAnakBadge, drgFeatureAnakText),
    MATURE("Mature (Remaja)", "ViewModel + Room DAO + Flow aktif", drgFeatureMatureBadge, drgFeatureMatureText),
    LULUS("Lulus (Graduated)", "Siap rilis: tidak ada dead button/cacat kritis teridentifikasi", drgFeatureLulusBadge, drgFeatureLulusText)
}

data class FeatureAuditItem(
    val name: String,
    val module: String,
    val stage: FeatureStage,
    val hasDeadButtons: Boolean = false,
    val isShallow: Boolean = false,
    val deadButtonDetails: String? = null,
    val shallowReason: String? = null,
    val codeLineCount: Int = 110
)
