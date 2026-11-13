package com.example.modules.radar.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Power
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.SpaceGroteskFontFamily

@Composable
fun ShelterRadarCard(
    name: String,
    distanceKm: Double,
    availableSlots: Int,
    hasCoffee: Boolean = true,
    hasPowerOutlet: Boolean = true,
    onNavigateClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 16.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DrgTextDark)
                    Text(
                        text = "Jarak: %.1f km • %d driver siaga".format(distanceKm, availableSlots),
                        fontFamily = SpaceGroteskFontFamily,
                        fontSize = 11.sp,
                        color = DrgTextSecondary
                    )
                }
                Button(
                    onClick = onNavigateClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = "Navigasi", modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Rute", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            if (hasCoffee || hasPowerOutlet) {
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (hasCoffee) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Coffee, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Kopi Gratis", fontSize = 11.sp, color = DrgTextSecondary)
                        }
                    }
                    if (hasPowerOutlet) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Power, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Colokan Cas", fontSize = 11.sp, color = DrgTextSecondary)
                        }
                    }
                }
            }
        }
    }
}
