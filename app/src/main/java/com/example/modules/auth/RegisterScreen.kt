package com.example.modules.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onSubmit: (name: String, phone: String, plate: String, model: String, area: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("N ") }
    var model by remember { mutableStateOf("") }
    val areaOptions = listOf("Klojen, Malang", "Lowokwaru, Malang", "Blimbing, Malang", "Sukun, Malang", "Kedungkandang, Malang", "Singosari, Malang", "Kota Batu")
    var selectedArea by remember { mutableStateOf(areaOptions[0]) }
    var expandedAreaMenu by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
        IconButton(onClick = onBack, modifier = Modifier.padding(top = 16.dp).testTag("register_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text("Daftar Anggota DRG", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
        Text("Bergabung dengan solidaritas driver Arema Malang Raya.", fontSize = 13.sp, color = DrgTextMuted)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Lengkap") }, modifier = Modifier.fillMaxWidth().testTag("reg_name"), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = phone, onValueChange = { phone = it }, label = { Text("Nomor HP Aktif") },
            modifier = Modifier.fillMaxWidth().testTag("reg_phone"), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = plate, onValueChange = { var clean = it.uppercase(); if (!clean.startsWith("N")) { clean = "N " + clean.trim() }; plate = clean },
            label = { Text("Nomor Pelat Motor (Jatim Malang)") }, modifier = Modifier.fillMaxWidth().testTag("reg_plate"),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters), shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Model Motor") }, placeholder = { Text("Contoh: Honda Vario 160") }, modifier = Modifier.fillMaxWidth().testTag("reg_motor"), shape = RoundedCornerShape(12.dp))
        Spacer(modifier = Modifier.height(10.dp))

        ExposedDropdownMenuBox(expanded = expandedAreaMenu, onExpandedChange = { expandedAreaMenu = !expandedAreaMenu }, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedArea, onValueChange = {}, readOnly = true, label = { Text("Wilayah Operasional / Basukom") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAreaMenu) }, modifier = Modifier.menuAnchor().fillMaxWidth(), shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(expanded = expandedAreaMenu, onDismissRequest = { expandedAreaMenu = false }) {
                areaOptions.forEach { area -> DropdownMenuItem(text = { Text(area) }, onClick = { selectedArea = area; expandedAreaMenu = false }) }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if (name.isNotBlank() && phone.isNotBlank() && plate.length > 2 && model.isNotBlank()) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onSubmit(name, phone, plate, model, selectedArea)
                }
            },
            enabled = name.isNotBlank() && phone.isNotBlank() && plate.length > 2 && model.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(52.dp).testTag("reg_submit_button"), shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DrgGrabGreenPrimary)
        ) {
            Text("Kirim Formulir Pendaftaran", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
