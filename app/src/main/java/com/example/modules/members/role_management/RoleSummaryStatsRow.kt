package com.example.modules.members.role_management

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.ui.theme.*

@Composable
fun RoleSummaryStatsRow(members: List<DriverMember>, modifier: Modifier = Modifier) {
    val theme = LocalAppVisualTheme.current
    val pengurusCount = members.count { it.role in listOf(MemberRole.KETUA, MemberRole.WAKIL_KETUA, MemberRole.SEKRETARIS, MemberRole.BENDAHARA, MemberRole.DEWAN_ETIKA) }
    val satgasCount = members.count { it.role == MemberRole.SATGAS }
    val anggotaCount = members.count { it.role == MemberRole.ANGGOTA }

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        StatPill("Pengurus", pengurusCount, theme.primaryColor, Modifier.weight(1f))
        StatPill("Satgas TRC", satgasCount, DrgRedDanger, Modifier.weight(1f))
        StatPill("Anggota", anggotaCount, DrgBlueInfo, Modifier.weight(1f))
    }
}

@Composable
private fun StatPill(label: String, count: Int, color: Color, modifier: Modifier = Modifier) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        shape = RoundedCornerShape(14.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = color.copy(alpha = 0.5f),
        contentPadding = 8.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$count", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = color)
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = DrgTextDark)
        }
    }
}
