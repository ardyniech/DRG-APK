package com.example.modules.events.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.*
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.*

@Composable
fun EventFeedbackPanel(
    feedbackList: List<EventMemberFeedbackEntity>,
    currentMember: DriverMember?,
    coordinatorId: String,
    visualTheme: AppVisualTheme,
    onSubmitFeedback: (String, String, String) -> Unit,
    onResolveFeedback: (String, String) -> Unit
) {
    val accent = visualTheme.primaryColor
    val categories = listOf("Kekurangan Perencanaan", "Kesiapan Panitia", "Risiko Keterlambatan", "Anggaran/RAB", "Saran Logistik")
    val severities = listOf("SARAN", "PERINGATAN", "KRITIS")
    val canRespond = currentMember != null && (currentMember.role == MemberRole.SUPER_ADMIN || currentMember.role.isLeadership || currentMember.id == coordinatorId)

    var category by remember { mutableStateOf(categories.first()) }
    var severity by remember { mutableStateOf("PERINGATAN") }
    var message by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        DrgLuminousGlassCard(shape = RoundedCornerShape(20.dp), backgroundColor = DrgGlassSurfaceStrong, borderHighlightColor = accent.copy(alpha = 0.6f), contentPadding = 12.dp, modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Beri Masukan / Koreksi Perencanaan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                Text("Semua anggota dapat melaporkan kekurangan panitia, kendala logistik, atau potensi acara telat.", fontSize = 10.sp, color = DrgTextSecondary)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        val active = cat == category
                        Box(modifier = Modifier.clip(RoundedCornerShape(9.dp)).background(if (active) accent else Color(0xFFF1F5F9)).bouncyClick { category = cat }.padding(horizontal = 9.dp, vertical = 5.dp)) {
                            Text(cat, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else DrgTextDark)
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    severities.forEach { sev ->
                        val sevColor = when (sev) { "KRITIS" -> DrgRedPanic; "PERINGATAN" -> Color(0xFFD97706); else -> Color(0xFF0284C7) }
                        val active = sev == severity
                        Box(modifier = Modifier.testTag("severity_chip_$sev").clip(RoundedCornerShape(9.dp)).background(if (active) sevColor else sevColor.copy(alpha = 0.12f)).bouncyClick { severity = sev }.padding(horizontal = 10.dp, vertical = 5.dp)) {
                            Text(sev, fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else sevColor)
                        }
                    }
                }
                OutlinedTextField(value = message, onValueChange = { message = it }, label = { Text("Tulis masukan konstruktif / temuan kekurangan...", fontSize = 10.sp) }, modifier = Modifier.fillMaxWidth().testTag("input_event_feedback_msg"))
                Button(
                    onClick = { if (message.isNotBlank()) { onSubmitFeedback(category, severity, message); message = "" } },
                    colors = ButtonDefaults.buttonColors(containerColor = accent), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().testTag("btn_submit_event_feedback")
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Kirim Masukan ke Panitia", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        feedbackList.forEach { fb ->
            val sevColor = when (fb.severityLevel) { "KRITIS" -> DrgRedPanic; "PERINGATAN" -> Color(0xFFD97706); else -> Color(0xFF0284C7) }
            var responseDraft by remember(fb.id, fb.committeeResponse) { mutableStateOf(fb.committeeResponse) }
            DrgLuminousGlassCard(shape = RoundedCornerShape(18.dp), backgroundColor = visualTheme.glassBg, borderHighlightColor = if (fb.isResolved) DrgGreenPrimary.copy(alpha = 0.55f) else sevColor.copy(alpha = 0.6f), contentPadding = 12.dp, modifier = Modifier.fillMaxWidth().testTag("feedback_card_${fb.id}")) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("${fb.authorName} (${fb.authorRole.shortName})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(fb.severityLevel, fontFamily = SpaceGroteskFontFamily, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = sevColor, modifier = Modifier.clip(RoundedCornerShape(7.dp)).background(sevColor.copy(alpha = 0.14f)).padding(horizontal = 6.dp, vertical = 2.dp))
                            if (fb.isResolved) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clip(RoundedCornerShape(7.dp)).background(DrgGreenPrimary.copy(alpha = 0.14f)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(10.dp))
                                    Spacer(Modifier.width(2.dp))
                                    Text("Selesai", fontFamily = SpaceGroteskFontFamily, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                                }
                            }
                        }
                    }
                    Text(fb.category, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = accent)
                    Text(fb.message, fontSize = 12.sp, color = DrgTextDark)
                    if (fb.committeeResponse.isNotBlank()) {
                        Text("Respon Panitia: ${fb.committeeResponse}", fontSize = 10.5.sp, fontWeight = FontWeight.Medium, color = DrgGreenPrimary, modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(DrgGreenPrimary.copy(alpha = 0.10f)).padding(8.dp))
                    }
                    if (canRespond && !fb.isResolved) {
                        OutlinedTextField(value = responseDraft, onValueChange = { responseDraft = it }, label = { Text("Respon Tindak Lanjut Panitia", fontSize = 9.5.sp) }, singleLine = true, modifier = Modifier.fillMaxWidth())
                        OutlinedButton(onClick = { onResolveFeedback(fb.id, responseDraft) }, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                            Text("Tandai Ditindaklanjuti", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                        }
                    }
                }
            }
        }
    }
}
