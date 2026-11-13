package com.example.modules.radar

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.models.HazardType
import com.example.ui.theme.*

@Composable
fun AddHazardDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, HazardType, String, String, Double, Double) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(HazardType.BEGAL_RISK) }
    var locationName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    DrgLuminousGlassModal(
        title = "Tandai Area Rawan Jalanan",
        subtitle = "Peringatan langsung ke seluruh Radar Driver DRG",
        icon = Icons.Default.AddLocationAlt,
        accentColor = DrgRedPanic,
        validationError = validationError,
        onDismiss = onDismiss,
        footer = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Batal") }
                Button(
                    onClick = {
                        when {
                            title.isBlank() -> validationError = "Judul peringatan bahaya wajib diisi."
                            locationName.isBlank() -> validationError = "Nama lokasi atau ruas jalan wajib diisi."
                            else -> onSubmit(title.trim(), selectedType, locationName.trim(), description.trim(), -7.9778, 112.6336)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DrgRedPanic),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).testTag("btn_submit_hazard")
                ) { Text("Tandai di Radar", color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        OutlinedTextField(
            value = title, onValueChange = { title = it; validationError = null },
            label = { Text("Judul Peringatan Bahaya", fontSize = 11.sp) },
            modifier = Modifier.fillMaxWidth().testTag("input_hazard_title"), singleLine = true, shape = RoundedCornerShape(12.dp)
        )
        Text("Pilih Kategori Risiko:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextSecondary)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
            HazardType.entries.forEach { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { selectedType = type },
                    label = { Text("${type.emoji} ${type.label}", fontSize = 10.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(type.colorHex), selectedLabelColor = Color.White)
                )
            }
        }
        OutlinedTextField(
            value = locationName, onValueChange = { locationName = it; validationError = null },
            label = { Text("Nama Lokasi / Jalan", fontSize = 11.sp) },
            modifier = Modifier.fillMaxWidth().testTag("input_hazard_location"), singleLine = true, shape = RoundedCornerShape(12.dp)
        )
        OutlinedTextField(
            value = description, onValueChange = { description = it },
            label = { Text("Keterangan & Imbauan bagi Driver", fontSize = 11.sp) },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
        )
    }
}
