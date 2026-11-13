package com.example.modules.profile.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun KtaFooterRow(member: DriverMember?) {
    val theme = LocalAppVisualTheme.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(theme.pillBg)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocationOn, contentDescription = "Area", tint = theme.primaryColor, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(4.dp))
            Text("DRG Malang Raya", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
        }
        Text("Bergabung: ${member?.joinedDate ?: "Jan 2024"}", fontFamily = SpaceGroteskFontFamily, color = DrgTextMuted, fontSize = 11.sp)
    }
}
