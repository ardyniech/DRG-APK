package com.example.modules.emergency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.shared.models.EmergencyType
import com.example.ui.theme.*

@Composable
fun CommunityEmergencyModal(
    onDismiss: () -> Unit,
    onConfirm: (EmergencyType, String, String) -> Unit,
    isSosAlarmEnabled: Boolean = true
) {
    var selectedType by remember { mutableStateOf(EmergencyType.MOGOK) }
    var locationNote by remember { mutableStateOf("") }
    var detailMessage by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DrgSurface,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .testTag("community_emergency_modal")
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Peringatan",
                            tint = DrgRedDanger,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Lapor Darurat Komunitas",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DrgRedDanger
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSosAlarmEnabled) DrgRedContainer else DrgSurfaceVariant
                    ) {
                        Text(
                            text = if (isSosAlarmEnabled) "🔊 Sirine Siaga" else "🔇 Senyap",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSosAlarmEnabled) DrgRedDanger else DrgTextSecondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                Text(
                    text = "Pilih kategori situasi untuk disiarkan ke Satgas & driver sekitar:",
                    fontSize = 11.sp,
                    color = DrgTextSecondary
                )

                EmergencyTypeSelectorList(
                    selectedType = selectedType,
                    onSelectType = { selectedType = it }
                )

                OutlinedTextField(
                    value = locationNote,
                    onValueChange = { locationNote = it },
                    label = { Text("Patokan / Nama Jalan Terdekat", fontSize = 11.sp) },
                    placeholder = { Text("Contoh: Depan Halte Flyover", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("emergency_location_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = detailMessage,
                    onValueChange = { detailMessage = it },
                    label = { Text("Keterangan Tambahan", fontSize = 11.sp) },
                    placeholder = { Text("Contoh: Mogok butuh dorongan & kunci 12", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth().testTag("emergency_message_input"),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).testTag("emergency_cancel_button")
                    ) {
                        Text("Batal")
                    }
                    Button(
                        onClick = {
                            val loc = if (locationNote.isBlank()) "Lokasi Terkini GPS" else locationNote.trim()
                            val msg = if (detailMessage.isBlank()) "Memerlukan bantuan rekan (${selectedType.label})" else detailMessage.trim()
                            onConfirm(selectedType, msg, loc)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DrgRedDanger),
                        modifier = Modifier.weight(1f).testTag("emergency_submit_button")
                    ) {
                        Text("Pancarkan SOS", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
