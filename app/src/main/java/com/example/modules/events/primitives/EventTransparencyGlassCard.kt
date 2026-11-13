package com.example.modules.events.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.events.logic.EventReadinessReport
import com.example.modules.events.logic.EventRiskLevel
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.AppVisualTheme
import com.example.shared.models.CommunityEventEntity
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EventTransparencyGlassCard(
    event: CommunityEventEntity,
    report: EventReadinessReport,
    visualTheme: AppVisualTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val riskColor = when (report.riskLevel) {
        EventRiskLevel.AMAN -> DrgGreenPrimary
        EventRiskLevel.WASPADA -> Color(0xFFD97706)
        EventRiskLevel.KRITIS_RAWAN_GAGAL -> DrgRedPanic
    }
    val rupiahFmt = NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID"))

    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth().testTag("event_card_${event.id}"),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = if (report.riskLevel == EventRiskLevel.KRITIS_RAWAN_GAGAL) DrgRedPanic.copy(alpha = 0.65f) else visualTheme.glassBorderColor,
        contentPadding = 14.dp,
        onClick = onClick
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    EventPillTag(event.category, visualTheme.primaryColor)
                    EventPillTag(if (report.daysRemaining >= 0) "H-${report.daysRemaining}" else "Lewat", Color(0xFF4F46E5))
                }
                EventPillTag(report.riskLevel.label, riskColor)
            }

            Text(event.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DrgTextDark, maxLines = 2, overflow = TextOverflow.Ellipsis)

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = visualTheme.primaryColor, modifier = Modifier.size(13.dp))
                Text(event.targetDateText, fontSize = 11.sp, color = DrgTextSecondary)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(13.dp))
                Text("${event.locationName} • PJ: ${event.coordinatorName}", fontSize = 11.sp, color = DrgTextSecondary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Kesiapan Panitia (${event.stage.label})", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextDark)
                    Text("${report.overallReadinessPct}%", fontFamily = SpaceGroteskFontFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = riskColor)
                }
                LinearProgressIndicator(
                    progress = { report.overallReadinessPct / 100f },
                    modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(4.dp)),
                    color = riskColor,
                    trackColor = riskColor.copy(alpha = 0.16f)
                )
            }

            if (report.riskLevel != EventRiskLevel.AMAN) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(riskColor.copy(alpha = 0.11f)).padding(horizontal = 9.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = riskColor, modifier = Modifier.size(14.dp))
                    Text(report.riskHeadline, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = riskColor)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tugas: ${report.completedTasks}/${report.totalTasks} selesai", fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                Text("Dana: Rp ${rupiahFmt.format(event.collectedFundsRupiah)} / ${rupiahFmt.format(event.estimatedBudgetRupiah)}", fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                Text("${report.unresolvedFeedback} masukan", fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, color = if (report.criticalFeedback > 0) DrgRedPanic else DrgTextSecondary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun EventPillTag(text: String, color: Color) {
    Box(modifier = Modifier.clip(RoundedCornerShape(9.dp)).background(color.copy(alpha = 0.14f)).border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(9.dp)).padding(horizontal = 8.dp, vertical = 3.dp)) {
        Text(text, fontFamily = SpaceGroteskFontFamily, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = color)
    }
}
