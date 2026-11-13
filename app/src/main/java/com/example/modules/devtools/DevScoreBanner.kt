package com.example.modules.devtools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.ui.theme.*

@Composable
fun DevScoreBanner(score: ProjectHealthScore) {
    val pDead = String.format("%.1f", score.deadButtonsCount * score.penaltyPerDeadButton)
    val pShallow = String.format("%.1f", score.shallowFeaturesCount * score.penaltyPerShallow)
    val pBug = String.format("%.1f", score.bugPenaltiesCount * score.penaltyPerBug)
    val pProg = String.format("%.1f", score.progressPenaltiesCount * score.penaltyPerProgressGap)

    DrgLuminousGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = devGlassBg(),
        borderHighlightColor = devGlassBorder(),
        contentPadding = 14.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = "Skor Kesehatan: ${String.format("%.1f", score.finalScore)}% (${score.grade})",
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DrgTextDark
                )
                Text(
                    text = "Penalti: Dead (-${pDead}%), Dangkal (-${pShallow}%), Cacat (-${pBug}%), Gap progres (-${pProg}%)",
                    fontSize = 10.sp,
                    color = DrgTextSecondary
                )
                Text(
                    text = "Heuristik penalti — BUKAN progres fitur. Progres fitur ada di tab Tree Progress.",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DrgWarningTextDark
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(DrgRedPanic)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "-${String.format("%.1f", score.totalPenalty)}%",
                    fontFamily = SpaceGroteskFontFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
