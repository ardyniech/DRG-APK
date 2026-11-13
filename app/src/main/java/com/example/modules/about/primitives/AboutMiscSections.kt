package com.example.modules.about.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.about.AboutBuild
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.SpaceGroteskFontFamily

@Composable
fun SectionTitle(title: String) {
    Text(
        title,
        fontSize = 14.sp,
        fontWeight = FontWeight.ExtraBold,
        color = DrgTextPrimary,
        modifier = Modifier.padding(top = 2.dp)
    )
}

@Composable
fun AboutErrorState(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("⚠️", fontSize = 40.sp)
        Text("Gagal Memuat Data Fitur", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
        Text(message, fontSize = 11.sp, color = DrgTextSecondary, textAlign = TextAlign.Center)
        Text(
            "File manifest tentang di-generate otomatis oleh pipeline. Coba buka lagi setelah build terbaru.",
            fontSize = 10.sp, color = DrgTextMuted, textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AboutFooterCard(build: AboutBuild) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        shape = RoundedCornerShape(18.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 14.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Terbentuk dari pipeline otomatis", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
            Text(
                "Status fitur & persentase dihitung langsung dari kode sumber, test suite, dan roadmap — bukan ditulis manual.",
                fontSize = 10.sp, color = DrgTextSecondary
            )
            Text("Skema ${build.version} · ${build.commit}", fontFamily = SpaceGroteskFontFamily, fontSize = 9.5.sp, color = DrgTextMuted)
        }
    }
}