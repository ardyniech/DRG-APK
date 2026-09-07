package com.example.modules.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.shared.models.EmergencyType
import com.example.ui.theme.*

@Composable
fun EmergencyTriggerDialog(
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
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                            text = "Pancarkan SOS",
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
                            text = if (isSosAlarmEnabled) "🔊 Sirine 100 dB" else "🔇 Senyap",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSosAlarmEnabled) DrgRedDanger else DrgTextSecondary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                Text(
                    text = "Pilih kategori situasi darurat untuk disiarkan ke Satgas & driver sekitar:",
                    fontSize = 11.sp,
                    color = DrgTextSecondary
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    EmergencyType.values().forEach { type ->
                        val isSelected = selectedType == type
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) DrgRedContainer else DrgSurfaceVariant.copy(alpha = 0.5f))
                                .border(
                                    1.dp,
                                    if (isSelected) DrgRedDanger else DrgOutline.copy(alpha = 0.5f),
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedType = type }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedType = type },
                                colors = RadioButtonDefaults.colors(selectedColor = DrgRedDanger)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = type.label,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) DrgRedDanger else DrgTextPrimary
                                )
                                Text(
                                    text = "Prioritas: ${type.severity}",
                                    fontSize = 10.sp,
                                    color = DrgTextSecondary
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = locationNote,
                    onValueChange = { locationNote = it },
                    label = { Text("Patokan / Nama Jalan Terdekat", fontSize = 11.sp) },
                    placeholder = { Text("Contoh: Depan Halte Flyover Kuningan", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = detailMessage,
                    onValueChange = { detailMessage = it },
                    label = { Text("Keterangan Tambahan", fontSize = 11.sp) },
                    placeholder = { Text("Contoh: Butuh kunci pas 12 & dorongan", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Batal")
                    }
                    Button(
                        onClick = {
                            val loc = if (locationNote.isBlank()) "Lokasi Terkini GPS" else locationNote
                            val msg = if (detailMessage.isBlank()) "Memerlukan bantuan rekan terdekat." else detailMessage
                            onConfirm(selectedType, msg, loc)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DrgRedDanger),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Kirim SOS", color = Color.White)
                    }
                }
            }
        }
    }
}
