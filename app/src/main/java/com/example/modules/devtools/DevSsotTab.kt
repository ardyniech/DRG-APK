package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.DrgWarningTextDark
import org.json.JSONObject

/** Baca minus dari SSOT asset (tanpa cross-module import AboutManifest). */
private fun readMinus(ctx: android.content.Context): List<Triple<String, String, String>> {
    return try {
        val json = ctx.assets.open("about_manifest.json").bufferedReader().use { it.readText() }
        val arr = JSONObject(json).optJSONArray("minus") ?: return emptyList()
        buildList {
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                add(Triple(o.optString("severity"), o.optString("category"), o.optString("detail") + " — " + o.optString("fix")))
            }
        }
    } catch (_: Exception) { emptyList() }
}

/**
 * Tab SSOT: tampilkan BLOCKER/MAJOR langsung di HP dari manifest.
 * Sumber: about_manifest.json minus (digenerate audit_minus.py). Tanpa dependency ke modul About.
 */
@Composable
fun DevSsotTab(headerContent: (@Composable () -> Unit)? = null) {
    val ctx = LocalContext.current
    val findings = remember { readMinus(ctx) }
    var filter by remember { mutableStateOf<String?>(null) }
    val shown = if (filter == null) findings else findings.filter { it.first == filter }
    DevTabShell(headerContent, Icons.Default.GpsFixed, "SSOT — Acuan AI Dev & DevTools", "Satu sumber kebenaran (docs/ABOUT.md). BLOCKER dilarang klaim multi-user/sinkron.", headerExtra = {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            FilterChip(selected = filter == null, onClick = { filter = null }, label = { Text("Semua (${findings.size})", fontSize = 10.sp) })
            listOf("BLOCKER", "MAJOR", "MINOR").forEach { s ->
                FilterChip(selected = filter == s, onClick = { filter = s }, label = { Text(s, fontSize = 10.sp) })
            }
        }
    }) {
        items(shown) { (sev, cat, text) ->
            val chipColor = if (sev == "BLOCKER") Color.Red else if (sev == "MAJOR") DrgWarningTextDark else Color.Gray
            Surface(shape = RoundedCornerShape(12.dp), color = Color.White, modifier = Modifier.fillMaxWidth().border(1.dp, chipColor.copy(0.35f), RoundedCornerShape(12.dp))) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(shape = RoundedCornerShape(6.dp), color = chipColor.copy(alpha = 0.14f)) {
                            Text(sev, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = chipColor, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                        Text(cat, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DrgTextPrimary)
                    }
                    Text(text, fontSize = 11.sp, color = DrgTextSecondary)
                }
            }
        }
    }
}
