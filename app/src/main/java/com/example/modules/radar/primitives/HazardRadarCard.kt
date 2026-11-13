package com.example.modules.radar.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.HazardArea
import com.example.ui.theme.*

@Composable
fun HazardRadarCard(
    hazard: HazardArea,
    onConfirmHazard: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val hazardColor = Color(hazard.hazardType.colorHex)
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = hazardColor.copy(alpha = 0.65f),
        contentPadding = 14.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(12.dp), color = hazardColor.copy(alpha = 0.14f), modifier = Modifier.size(42.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = hazardColor, modifier = Modifier.size(22.dp))
                    }
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        text = "${hazard.hazardType.emoji} ${hazard.title.ifEmpty { hazard.hazardType.label }}",
                        fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextDark
                    )
                    Text(hazard.description.ifEmpty { hazard.locationName }, fontSize = 11.sp, color = DrgTextMuted, maxLines = 1)
                    Text(
                        text = "Dilaporkan: ${hazard.reportedBy} • ${hazard.confirmCount} Konfirmasi • ${hazard.timeAgo}",
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = hazardColor
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { onConfirmHazard(hazard.id) },
                colors = ButtonDefaults.buttonColors(containerColor = hazardColor),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                Spacer(Modifier.width(4.dp))
                Text("Valid (+5)", fontFamily = SpaceGroteskFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
