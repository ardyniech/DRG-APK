package com.example.modules.members

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun PoskoAddDialog(
    userLat: Double, userLng: Double, onDismiss: () -> Unit,
    onSave: (String, String, String, Double, Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var latStr by remember { mutableStateOf(userLat.toString()) }
    var lngStr by remember { mutableStateOf(userLng.toString()) }
    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Daftarkan Posko Baru", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DrgTextPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = name, onValueChange = { name = it; nameError = false }, isError = nameError,
                    label = { Text("Nama Posko", fontSize = 11.sp) }, placeholder = { Text("Contoh: Posko Sukun Guyub", fontSize = 11.sp) },
                    singleLine = true, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = area, onValueChange = { area = it },
                    label = { Text("Area / Kecamatan", fontSize = 11.sp) }, placeholder = { Text("Contoh: Sukun, Malang", fontSize = 11.sp) },
                    singleLine = true, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone, onValueChange = { phone = it; phoneError = false }, isError = phoneError,
                    label = { Text("Nomor Telepon", fontSize = 11.sp) }, placeholder = { Text("Contoh: 0812345678", fontSize = 11.sp) },
                    singleLine = true, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(value = latStr, onValueChange = { latStr = it }, label = { Text("Lat (Y)", fontSize = 11.sp) }, singleLine = true, shape = RoundedCornerShape(10.dp), modifier = Modifier.weight(1f))
                    OutlinedTextField(value = lngStr, onValueChange = { lngStr = it }, label = { Text("Lng (X)", fontSize = 11.sp) }, singleLine = true, shape = RoundedCornerShape(10.dp), modifier = Modifier.weight(1f))
                }
                Button(
                    onClick = { latStr = userLat.toString(); lngStr = userLng.toString() },
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenContainer),
                    shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Gunakan GPS HP Saat Ini", color = DrgGreenPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) { nameError = true; return@Button }
                    if (phone.isBlank()) { phoneError = true; return@Button }
                    onSave(name, area, phone, latStr.toDoubleOrNull() ?: userLat, lngStr.toDoubleOrNull() ?: userLng)
                },
                colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary), shape = RoundedCornerShape(10.dp)
            ) {
                Text("Simpan", color = MaterialTheme.colorScheme.onPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal", color = DrgTextSecondary, fontSize = 12.sp) } },
        containerColor = DrgSurface, shape = RoundedCornerShape(16.dp)
    )
}
