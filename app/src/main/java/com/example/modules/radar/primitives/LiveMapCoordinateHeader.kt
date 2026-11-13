package com.example.modules.radar.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.radar.logic.CoordinateUtils
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@Composable
fun LiveMapCoordinateHeader(
    centerLat: Double,
    centerLng: Double,
    nearbyCount: Int,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = Color(0xEBFFFFFF),
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.MyLocation, contentDescription = "Pusat GPS", tint = theme.primaryColor, modifier = Modifier.size(18.dp))
                Column {
                    Text("Area Pantau GPS", fontSize = 10.sp, fontWeight = FontWeight.Medium, color = DrgTextSecondary)
                    Text(
                        text = CoordinateUtils.formatCoordinates(centerLat, centerLng),
                        fontSize = 11.5.sp,
                        fontFamily = SpaceGroteskFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = DrgTextDark
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(theme.pillBg)
                    .border(0.8.dp, theme.primaryColor.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 9.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "$nearbyCount Driver di Layar",
                    fontSize = 10.sp,
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = theme.primaryColor
                )
            }
        }
    }
}
