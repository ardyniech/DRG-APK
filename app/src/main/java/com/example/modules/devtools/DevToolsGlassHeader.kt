package com.example.modules.devtools

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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
fun DevToolsGlassHeader(
    tabs: List<String>,
    selectedTab: Int,
    onSelectTab: (Int) -> Unit,
    isSuperAdmin: Boolean,
    score: ProjectHealthScore,
    visualTheme: AppVisualTheme,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = visualTheme.primaryColor

    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = visualTheme.glassBg,
        borderHighlightColor = visualTheme.glassBorderColor,
        contentPadding = 12.dp
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(visualTheme.pillBg.copy(alpha = 0.85f)).border(1.dp, Color.White, RoundedCornerShape(14.dp)).testTag("devtools_back_button")
                ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = accent) }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Icon(Icons.Default.Terminal, contentDescription = null, tint = accent, modifier = Modifier.size(16.dp))
                        Text("DevTools Enterprise", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                    }
                    Text("Observabilitas CI/CD, Code Integrity & Security", fontSize = 10.sp, color = DrgTextSecondary)
                }

                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(if (isSuperAdmin) accent.copy(alpha = 0.16f) else DrgRedPanic.copy(alpha = 0.14f)).padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Icon(if (isSuperAdmin) Icons.Default.VerifiedUser else Icons.Default.Lock, contentDescription = null, tint = if (isSuperAdmin) accent else DrgRedPanic, modifier = Modifier.size(11.dp))
                        Text(if (isSuperAdmin) "SUPER ADMIN" else "TERKUNCI", fontFamily = SpaceGroteskFontFamily, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = if (isSuperAdmin) accent else DrgRedPanic)
                    }
                    if (isSuperAdmin) {
                        Text("${String.format("%.1f", score.finalScore)}% • ${score.grade}", fontFamily = SpaceGroteskFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
                    }
                }
            }

            if (isSuperAdmin) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    itemsIndexed(tabs) { idx, title ->
                        val active = idx == selectedTab
                        val bg by animateColorAsState(if (active) accent else Color.White.copy(alpha = 0.65f), tween(200), label = "dtTabBg")
                        val tc by animateColorAsState(if (active) Color.White else DrgTextSecondary, tween(200), label = "dtTabFg")
                        Box(
                            modifier = Modifier.heightIn(min = 38.dp).clip(RoundedCornerShape(13.dp)).background(bg).border(1.dp, if (active) accent else Color.White, RoundedCornerShape(13.dp)).bouncyClick { onSelectTab(idx) }.padding(horizontal = 12.dp, vertical = 7.dp).testTag("devtools_tab_$idx"),
                            contentAlignment = Alignment.Center
                        ) { Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = tc) }
                    }
                }
            }
        }
    }
}

@Composable
fun DevToolsLockedGlassCard(roleName: String, visualTheme: AppVisualTheme, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().padding(20.dp).testTag("devtools_access_denied"), contentAlignment = Alignment.Center) {
        DrgLuminousGlassCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(26.dp), backgroundColor = visualTheme.glassBg, borderHighlightColor = DrgRedPanic.copy(alpha = 0.5f), contentPadding = 22.dp) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(18.dp)).background(DrgRedPanic.copy(alpha = 0.14f)).border(1.2.dp, Color.White, RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Lock, contentDescription = "Akses Dibatasi", tint = DrgRedPanic, modifier = Modifier.size(28.dp))
                }
                Text("Akses Khusus Super Admin", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DrgTextDark)
                Text("Peran Anda ($roleName) tidak memiliki otorisasi ke panel diagnostik & integritas enterprise DRG.", fontSize = 12.sp, color = DrgTextSecondary)
                Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = visualTheme.primaryColor), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                    Text("Kembali ke Profil", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
