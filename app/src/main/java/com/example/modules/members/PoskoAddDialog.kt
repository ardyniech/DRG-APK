package com.example.modules.members

import androidx.compose.foundation.layout.*
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
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.SpaceGroteskFontFamily

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
    var validationError by remember { mutableStateOf<String?>(null) }
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassModal(
        title = "Daftarkan Posko Baru",
        subtitle = "Titik istirahat & siaga darurat anggota DRG",
        icon = Icons.Default.AddLocationAlt,
        accentColor = theme.primaryColor,
        validationError = validationError,
        onDismiss = onDismiss,
        footer = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Batal", fontSize = 12.sp) }
                Button(
                    onClick = {
                        when {
                            name.isBlank() -> validationError = "Nama posko wajib diisi."
                            phone.isBlank() -> validationError = "Nomor telepon koordinator posko wajib diisi."
                            else -> onSave(name.trim(), area.trim(), phone.trim(), latStr.toDoubleOrNull() ?: userLat, lngStr.toDoubleOrNull() ?: userLng)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).testTag("btn_submit_posko")
                ) { Text("Simpan", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        OutlinedTextField(
            value = name, onValueChange = { name = it; validationError = null },
            label = { Text("Nama Posko", fontSize = 11.sp) }, placeholder = { Text("Contoh: Posko Sukun Guyub", fontSize = 11.sp) },
            singleLine = true, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().testTag("input_posko_name")
        )
        OutlinedTextField(
            value = area, onValueChange = { area = it },
            label = { Text("Area / Kecamatan", fontSize = 11.sp) }, placeholder = { Text("Contoh: Sukun, Malang", fontSize = 11.sp) },
            singleLine = true, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = phone, onValueChange = { phone = it; validationError = null },
            label = { Text("Nomor Telepon", fontSize = 11.sp) }, placeholder = { Text("Contoh: 0812345678", fontSize = 11.sp) },
            singleLine = true, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().testTag("input_posko_phone")
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(value = latStr, onValueChange = { latStr = it }, label = { Text("Lat (Y)", fontSize = 11.sp) }, singleLine = true, shape = RoundedCornerShape(12.dp), textStyle = LocalTextStyle.current.copy(fontFamily = SpaceGroteskFontFamily), modifier = Modifier.weight(1f))
            OutlinedTextField(value = lngStr, onValueChange = { lngStr = it }, label = { Text("Lng (X)", fontSize = 11.sp) }, singleLine = true, shape = RoundedCornerShape(12.dp), textStyle = LocalTextStyle.current.copy(fontFamily = SpaceGroteskFontFamily), modifier = Modifier.weight(1f))
        }
        Button(
            onClick = { latStr = userLat.toString(); lngStr = userLng.toString() },
            colors = ButtonDefaults.buttonColors(containerColor = theme.pillBg),
            shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()
        ) { Text("Gunakan GPS HP Saat Ini", color = theme.primaryColor, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
    }
}
