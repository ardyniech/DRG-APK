package com.example.modules.devtools

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DevScoreBanner(score: ProjectHealthScore) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(10.dp),
        color = DrgAmberContainer
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "🎯 Skor Proyek: ${String.format("%.1f", score.finalScore)}% (${score.grade})",
                    fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgOnAmberContainer
                )
                val pDead = String.format("%.1f", score.deadButtonsCount * score.penaltyPerDeadButton)
                val pShallow = String.format("%.1f", score.shallowFeaturesCount * score.penaltyPerShallow)
                val pBug = String.format("%.1f", score.bugPenaltiesCount * score.penaltyPerBug)
                val pProg = String.format("%.1f", score.progressPenaltiesCount * score.penaltyPerProgressGap)
                Text(
                    "Penalti: Dead (-${pDead}%), Dangkal (-${pShallow}%), Cacat (-${pBug}%), Progres (-${pProg}%)",
                    fontSize = 10.sp, color = DrgTextSecondary
                )
            }
            Surface(shape = RoundedCornerShape(6.dp), color = DrgRedDanger) {
                Text(
                    "Penalti -${String.format("%.1f", score.totalPenalty)}%",
                    fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}
