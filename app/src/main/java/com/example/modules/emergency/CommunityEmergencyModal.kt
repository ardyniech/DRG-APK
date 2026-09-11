package com.example.modules.emergency

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
            modifier = Modifier.fillMaxWidth().wrapContentHeight().testTag("community_emergency_modal")
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CommunityEmergencyModalHeader(isSosAlarmEnabled = isSosAlarmEnabled)

                Text(
                    text = "Pilih kategori situasi untuk disiarkan ke Satgas & driver sekitar:",
                    fontSize = 11.sp,
                    color = DrgTextSecondary
                )

                EmergencyTypeSelectorList(selectedType = selectedType, onSelectType = { selectedType = it })

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
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f).testTag("emergency_cancel_button")) {
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

                val context = androidx.compose.ui.platform.LocalContext.current
                OutlinedButton(
                    onClick = {
                        val loc = if (locationNote.isBlank()) "Lokasi Terkini GPS" else locationNote.trim()
                        val smsText = com.example.core.sync.EmergencySmsFallbackHelper.formatEmergencySms(
                            driverName = "Driver DRG",
                            plateNumber = "Motor",
                            type = selectedType,
                            locationNote = loc,
                            lat = 0.0,
                            lng = 0.0
                        )
                        com.example.core.sync.EmergencySmsFallbackHelper.dispatchSmsFallback(
                            context = context,
                            phoneNumber = com.example.core.sync.EmergencySmsFallbackHelper.DEFAULT_HOTLINE_TRC,
                            message = smsText
                        )
                        onDismiss()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgAmberSecondary),
                    modifier = Modifier.fillMaxWidth().height(42.dp)
                ) {
                    Text("Kirim via SMS Darurat (Offline / Tanpa Kuota)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
