package com.example.modules.radar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DrgSurface,
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Tandai Area Rawan / Bahaya Jalanan", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DrgTextPrimary)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Peringatan Bahaya", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("Pilih Kategori Risiko:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextSecondary)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                    HazardType.values().forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text("${type.emoji} ${type.label.take(10)}..", fontSize = 10.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = locationName,
                    onValueChange = { locationName = it },
                    label = { Text("Nama Lokasi / Jalan", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Keterangan & Imbauan bagi Driver", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Batal") }
                    Button(
                        onClick = {
                            if (title.isNotBlank() && locationName.isNotBlank()) {
                                onSubmit(title, selectedType, locationName, description, -6.1700, 106.7500)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DrgRedPanic),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Tandai di Radar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
