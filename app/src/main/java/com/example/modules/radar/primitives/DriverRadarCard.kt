package com.example.modules.radar.primitives

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var showOptionsModal by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(16.dp), color = DrgSurface, tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth().border(1.dp, DrgOutline.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = driver.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DrgTextPrimary)
                    Spacer(modifier = Modifier.width(6.dp))
                    RoleBadge(role = driver.role)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "${driver.motorcycleModel} • ${driver.motorcyclePlate}", fontSize = 11.sp, color = DrgTextSecondary)
                Text(
                    text = "Status: ${driver.currentStatus} • ⭐ ${driver.rating} • ${driver.baseArea}",
                    fontSize = 11.sp, color = DrgGreenPrimary, fontWeight = FontWeight.Medium
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilledTonalButton(
                    onClick = { showOptionsModal = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = DrgGreenPrimary.copy(alpha = 0.15f)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = "WA", modifier = Modifier.size(15.dp), tint = DrgGreenPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WA", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
                }

                Button(
                    onClick = { DriverContactLauncher.openDriverDialer(context, driver) },
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Phone, contentDescription = "Telepon", modifier = Modifier.size(15.dp))
                }
            }
        }
    }

    if (showOptionsModal) {
        DriverContactOptionsModal(driver = driver, onDismiss = { showOptionsModal = false })
    }
}
