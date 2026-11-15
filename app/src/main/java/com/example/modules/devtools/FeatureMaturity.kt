package com.example.modules.devtools

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.DrgFeatureAnakBadge
import com.example.ui.theme.DrgFeatureAnakText
import com.example.ui.theme.DrgFeatureBenihBadge
import com.example.ui.theme.DrgFeatureBenihText
import com.example.ui.theme.DrgFeatureLulusBadge
import com.example.ui.theme.DrgFeatureLulusText
import com.example.ui.theme.DrgFeatureMatureBadge
import com.example.ui.theme.DrgFeatureMatureText

enum class FeatureStage(
    val label: String,
    val description: String,
    val badgeColor: Color,
    val textColor: Color
) {
    BENIH("Benih (Seed)", "Konsep/Schema saja, belum ada controller", DrgFeatureBenihBadge, DrgFeatureBenihText),
    ANAK("Anak (Incubating)", "UI ada, logic parsial, rentan dead button", DrgFeatureAnakBadge, DrgFeatureAnakText),
    MATURE("Mature (Remaja)", "ViewModel + Room DAO + Flow aktif", DrgFeatureMatureBadge, DrgFeatureMatureText),
    LULUS("Lulus (Graduated)", "Siap rilis: tidak ada dead button/cacat kritis teridentifikasi", DrgFeatureLulusBadge, DrgFeatureLulusText)
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
