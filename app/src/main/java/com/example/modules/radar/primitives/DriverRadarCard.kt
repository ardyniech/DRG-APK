package com.example.modules.radar.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.atoms.RoleBadge
import com.example.shared.models.DriverMember
import com.example.shared.utils.DriverContactLauncher
import com.example.ui.theme.*

@Composable
fun DriverRadarCard(
    driver: DriverMember,
    onCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val theme = LocalAppVisualTheme.current
    var showOptionsModal by remember { mutableStateOf(false) }

    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 14.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(driver.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DrgTextPrimary)
                    Spacer(Modifier.width(6.dp))
                    RoleBadge(role = driver.role)
                }
                Spacer(Modifier.height(2.dp))
                Text("${driver.motorcycleModel} • ${driver.motorcyclePlate}", fontSize = 11.sp, color = DrgTextSecondary)
                Text(
                    "Status: ${driver.currentStatus} • ⭐ ${driver.rating} • ${driver.baseArea}",
                    fontFamily = SpaceGroteskFontFamily,
                    fontSize = 11.sp,
                    color = theme.primaryColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilledTonalButton(
                    onClick = { showOptionsModal = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = theme.pillBg),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "WA", modifier = Modifier.size(15.dp), tint = theme.primaryColor)
                    Spacer(Modifier.width(4.dp))
                    Text("WA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
                }
                Button(
                    onClick = { DriverContactLauncher.openDriverDialer(context, driver) },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                ) { Icon(Icons.Default.Phone, contentDescription = "Telepon", modifier = Modifier.size(15.dp)) }
            }
        }
    }

    if (showOptionsModal) {
        DriverContactOptionsModal(driver = driver, onDismiss = { showOptionsModal = false })
    }
}
