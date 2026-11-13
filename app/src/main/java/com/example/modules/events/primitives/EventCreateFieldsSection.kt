package com.example.modules.events.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.*

@Composable
fun EventCreateFieldsSection(
    accent: Color,
    title: String, onTitleChange: (String) -> Unit,
    category: String, onCategoryChange: (String) -> Unit,
    location: String, onLocationChange: (String) -> Unit,
    dateText: String, onDateTextChange: (String) -> Unit,
    daysAhead: Int, onDaysAheadChange: (Int) -> Unit,
    coordinatorName: String, onCoordinatorNameChange: (String) -> Unit,
    targetAttendeesText: String, onTargetAttendeesChange: (String) -> Unit,
    budgetText: String, onBudgetChange: (String) -> Unit,
    collectedText: String, onCollectedChange: (String) -> Unit,
    description: String, onDescriptionChange: (String) -> Unit,
    readinessNotes: String, onReadinessNotesChange: (String) -> Unit
) {
    val categories = listOf("Kopdar", "Baksos", "Touring", "Safety Clinic", "Rapat")
    val dayPresets = listOf(3 to "H+3", 7 to "H+7", 14 to "H+14", 30 to "H+30")

    OutlinedTextField(value = title, onValueChange = onTitleChange, label = { Text("Nama Event Komunitas *") }, singleLine = true, modifier = Modifier.fillMaxWidth().testTag("input_event_title"))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items(categories) { cat ->
            val active = cat == category
            Box(modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(if (active) accent else DrgSlateBgSoftAlt).bouncyClick { onCategoryChange(cat) }.padding(horizontal = 10.dp, vertical = 6.dp)) {
                Text(cat, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else DrgTextDark)
            }
        }
    }
    OutlinedTextField(value = location, onValueChange = onLocationChange, label = { Text("Lokasi & Titik Kumpul *") }, singleLine = true, modifier = Modifier.fillMaxWidth().testTag("input_event_location"))
    OutlinedTextField(value = dateText, onValueChange = onDateTextChange, label = { Text("Jadwal Target Pelaksanaan") }, singleLine = true, modifier = Modifier.fillMaxWidth())
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("Deadline:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextSecondary)
        dayPresets.forEach { (d, lbl) ->
            val active = daysAhead == d
            Box(modifier = Modifier.clip(RoundedCornerShape(9.dp)).background(if (active) accent else DrgSlateBgSoftAlt).border(1.dp, if (active) accent else Color.Transparent, RoundedCornerShape(9.dp)).bouncyClick { onDaysAheadChange(d) }.padding(horizontal = 9.dp, vertical = 5.dp)) {
                Text(lbl, fontFamily = SpaceGroteskFontFamily, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else DrgTextDark)
            }
        }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        OutlinedTextField(value = coordinatorName, onValueChange = onCoordinatorNameChange, label = { Text("Ketua Panitia / PJ") }, singleLine = true, modifier = Modifier.weight(1.2f))
        OutlinedTextField(value = targetAttendeesText, onValueChange = { onTargetAttendeesChange(it.filter { c -> c.isDigit() }) }, label = { Text("Target Peserta") }, singleLine = true, modifier = Modifier.weight(0.8f))
    }
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        OutlinedTextField(value = budgetText, onValueChange = { onBudgetChange(it.filter { c -> c.isDigit() }) }, label = { Text("Estimasi RAB (Rp)") }, singleLine = true, modifier = Modifier.weight(1f).testTag("input_event_budget"))
        OutlinedTextField(value = collectedText, onValueChange = { onCollectedChange(it.filter { c -> c.isDigit() }) }, label = { Text("Dana Masuk (Rp)") }, singleLine = true, modifier = Modifier.weight(1f))
    }
    OutlinedTextField(value = description, onValueChange = onDescriptionChange, label = { Text("Tujuan & Rincian Kegiatan") }, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(value = readinessNotes, onValueChange = onReadinessNotesChange, label = { Text("Catatan Risiko / Kesiapan Awal") }, modifier = Modifier.fillMaxWidth())
}
