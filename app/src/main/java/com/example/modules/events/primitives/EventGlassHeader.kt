package com.example.modules.events.primitives

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.models.AppVisualTheme
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.*

@Composable
fun EventGlassHeader(
    visualTheme: AppVisualTheme,
    totalEvents: Int,
    readyCount: Int,
    blockedTasksCount: Int,
    openFeedbackCount: Int,
    selectedFilter: Int,
    onSelectFilter: (Int) -> Unit,
    onBack: () -> Unit,
    onCreateEventClick: () -> Unit
) {
    val accent = visualTheme.primaryColor
    val filters = listOf("Semua", "Persiapan", "Rawan Molor", "Selesai")

    DrgLuminousGlassCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = visualTheme.glassBorderColor,
        contentPadding = 12.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(visualTheme.pillBg.copy(alpha = 0.85f)).border(1.dp, Color.White, RoundedCornerShape(14.dp)).testTag("events_back_button")
                ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = accent) }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Icon(Icons.Default.EventAvailable, contentDescription = null, tint = accent, modifier = Modifier.size(16.dp))
                        Text("Event & Panitia DRG", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                    }
                    Text("Transparansi Progres Tugas & Masukan Anggota", fontSize = 10.sp, color = DrgTextSecondary)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.testTag("btn_create_event_record").defaultMinSize(minHeight = 40.dp).clip(RoundedCornerShape(13.dp)).background(accent).bouncyClick { onCreateEventClick() }.padding(horizontal = 10.dp, vertical = 7.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Buat Event", tint = Color.White, modifier = Modifier.size(15.dp))
                    Text("Buat Event", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                EventHeaderStatPill("Event", "$totalEvents", accent, Modifier.weight(1f))
                EventHeaderStatPill("Siap", "$readyCount", DrgGreenPrimary, Modifier.weight(1f))
                EventHeaderStatPill("Kendala", "$blockedTasksCount", if (blockedTasksCount > 0) DrgRedPanic else DrgTextSecondary, Modifier.weight(1f))
                EventHeaderStatPill("Masukan", "$openFeedbackCount", if (openFeedbackCount > 0) Color(0xFFD97706) else DrgTextSecondary, Modifier.weight(1f))
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                itemsIndexed(filters) { idx, label ->
                    val active = idx == selectedFilter
                    val bg by animateColorAsState(if (active) accent else Color.White.copy(alpha = 0.68f), tween(180), label = "evFltBg")
                    val fg by animateColorAsState(if (active) Color.White else DrgTextSecondary, tween(180), label = "evFltFg")
                    Box(
                        modifier = Modifier.testTag("event_filter_$idx").heightIn(min = 34.dp).clip(RoundedCornerShape(12.dp)).background(bg).border(1.dp, if (active) accent else Color.White, RoundedCornerShape(12.dp)).bouncyClick { onSelectFilter(idx) }.padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = fg) }
                }
            }
        }
    }
}

@Composable
private fun EventHeaderStatPill(label: String, value: String, tint: Color, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.72f)).border(1.dp, Color.White, RoundedCornerShape(12.dp)).padding(vertical = 5.dp)
    ) {
        Text(value, fontFamily = SpaceGroteskFontFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = tint)
        Text(label, fontSize = 9.sp, color = DrgTextSecondary)
    }
}
