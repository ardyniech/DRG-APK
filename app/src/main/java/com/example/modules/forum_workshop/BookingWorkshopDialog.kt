package com.example.modules.forum_workshop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
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
import com.example.shared.models.DriverMember
import com.example.shared.models.WorkshopPartner
import com.example.ui.theme.*

@Composable
fun BookingWorkshopDialog(
    workshop: WorkshopPartner,
    currentMember: DriverMember?,
    onDismiss: () -> Unit,
    onConfirmBooking: (String) -> Unit
) {
    val services = listOf("Ganti Oli Cepat", "Servis CVT", "Tambal Tubeless", "Cek Kelistrikan & Aki")
    var selectedService by remember { mutableStateOf(services.first()) }
    var notes by remember { mutableStateOf("") }
    val tierBonus = when (currentMember?.loyaltyTier) {
        "Kapten", "Leader" -> 5
        "Pejuang" -> 3
        else -> 0
    }
    val totalDiscount = workshop.discountPercent + tierBonus

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(20.dp), color = Color.White, modifier = Modifier.fillMaxWidth().testTag("booking_workshop_dialog")) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Build, contentDescription = null, tint = DrgGreenPrimary)
                    Column {
                        Text("Booking Servis Rekanan", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DrgTextPrimary)
                        Text(workshop.name, fontSize = 11.sp, color = DrgTextSecondary)
                    }
                }

                Surface(shape = RoundedCornerShape(10.dp), color = DrgGreenLight) {
                    Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Diskon Anggota DRG:", fontSize = 11.sp, color = DrgTextPrimary)
                        Text("Diskon ${workshop.discountPercent}% + Tier ${tierBonus}% = Total $totalDiscount%", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DrgGreenPrimary)
                    }
                }

                Text("Pilih Jenis Layanan:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextPrimary)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    services.take(2).forEach { srv ->
                        FilterChip(
                            selected = selectedService == srv, onClick = { selectedService = srv },
                            label = { Text(srv, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgGreenPrimary, selectedLabelColor = Color.White)
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    services.drop(2).forEach { srv ->
                        FilterChip(
                            selected = selectedService == srv, onClick = { selectedService = srv },
                            label = { Text(srv, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgGreenPrimary, selectedLabelColor = Color.White)
                        )
                    }
                }

                OutlinedTextField(
                    value = notes, onValueChange = { notes = it },
                    label = { Text("Catatan Motor / Keluhan (opsional)", fontSize = 11.sp) },
                    placeholder = { Text("Misal: Vario 160 N 1234 XX, tarikan gredek", fontSize = 10.sp) },
                    modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), maxLines = 2
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f).height(40.dp), shape = RoundedCornerShape(10.dp)) {
                        Text("Batal", fontSize = 12.sp)
                    }
                    Button(
                        onClick = {
                            val msg = "Halo ${workshop.name}, saya ${currentMember?.name ?: "Anggota DRG"} (${currentMember?.driverId ?: "DRG"}) " +
                                "ingin booking servis:\n- Layanan: $selectedService\n- Klaim Diskon: $totalDiscount%\n- Catatan: ${notes.ifBlank { "Standar" }}"
                            onConfirmBooking(msg)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                        modifier = Modifier.weight(1.5f).height(40.dp), shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Kirim Booking", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
