package com.example.modules.forum_workshop

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
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
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
    val tierBonus = when (currentMember?.loyaltyTier) { "Kapten", "Leader" -> 5; "Pejuang" -> 3; else -> 0 }
    val totalDiscount = workshop.discountPercent + tierBonus
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassModal(
        title = "Booking Servis Rekanan",
        subtitle = workshop.name,
        icon = Icons.Default.Build,
        accentColor = theme.primaryColor,
        modifier = Modifier.testTag("booking_workshop_dialog"),
        onDismiss = onDismiss,
        footer = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f).height(42.dp), shape = RoundedCornerShape(12.dp)) { Text("Batal", fontSize = 12.sp) }
                Button(
                    onClick = {
                        val msg = "Halo ${workshop.name}, saya ${currentMember?.name ?: "Anggota DRG"} (${currentMember?.driverId ?: "DRG"}) " +
                            "ingin booking servis:\n- Layanan: $selectedService\n- Klaim Diskon: $totalDiscount%\n- Catatan: ${notes.ifBlank { "Standar" }}"
                        onConfirmBooking(msg)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                    modifier = Modifier.weight(1.5f).height(42.dp), shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Kirim Booking", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    ) {
        DrgLuminousGlassCard(shape = RoundedCornerShape(12.dp), backgroundColor = theme.pillBg, borderHighlightColor = theme.glassBorderColor, contentPadding = 10.dp, modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Diskon Anggota DRG:", fontSize = 11.sp, color = DrgTextPrimary)
                Text("${workshop.discountPercent}% + Tier ${tierBonus}% = $totalDiscount%", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = theme.primaryColor)
            }
        }
        Text("Pilih Jenis Layanan:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = DrgTextPrimary)
        services.chunked(2).forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                row.forEach { srv ->
                    FilterChip(
                        selected = selectedService == srv, onClick = { selectedService = srv },
                        label = { Text(srv, fontSize = 10.sp) }, modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = theme.primaryColor, selectedLabelColor = Color.White)
                    )
                }
            }
        }
        OutlinedTextField(
            value = notes, onValueChange = { notes = it },
            label = { Text("Catatan Motor / Keluhan (opsional)", fontSize = 11.sp) },
            placeholder = { Text("Misal: Vario 160 N 1234 XX, tarikan gredek", fontSize = 10.sp) },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), maxLines = 2
        )
    }
}
