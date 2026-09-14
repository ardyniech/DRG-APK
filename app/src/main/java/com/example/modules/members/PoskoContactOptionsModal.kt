package com.example.modules.members

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.PoskoLocation
import com.example.shared.utils.PoskoContactLauncher
import com.example.shared.utils.PoskoCoordinationReason
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoskoContactOptionsModal(
    posko: PoskoLocation,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DrgSurface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hubungi ${posko.name}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                    Text("Pj: ${posko.coordinatorName} • ${posko.area}", fontSize = 11.sp, color = DrgTextSecondary)
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Tutup", tint = DrgTextSecondary)
                }
            }

            Text("Pilih Template Koordinasi Posko:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DrgGreenPrimary)

            PoskoCoordinationReason.values().forEach { reason ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = DrgBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            PoskoContactLauncher.openPoskoWhatsApp(context, posko, reason)
                            onDismiss()
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = DrgGreenPrimary, modifier = Modifier.size(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(reason.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DrgTextPrimary)
                            Text(reason.template(posko), fontSize = 10.sp, color = DrgTextSecondary, maxLines = 1)
                        }
                    }
                }
            }

            Button(
                onClick = {
                    PoskoContactLauncher.openPoskoDialer(context, posko)
                    onDismiss()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Panggil Pengurus via Telepon", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}
