package com.example.modules.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
    onSubmit: (name: String, phone: String, plate: String, model: String, area: String, pin: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("N ") }
    var model by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    val areaOptions = listOf("Klojen, Malang", "Lowokwaru, Malang", "Blimbing, Malang", "Sukun, Malang", "Kedungkandang, Malang", "Singosari, Malang", "Kota Batu")
    var selectedArea by remember { mutableStateOf(areaOptions[0]) }
    var nameTouched by remember { mutableStateOf(false) }
    var phoneTouched by remember { mutableStateOf(false) }
    var plateTouched by remember { mutableStateOf(false) }
    var modelTouched by remember { mutableStateOf(false) }
    var pinTouched by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val theme = LocalAppVisualTheme.current
    val isValid = name.isNotBlank() && phone.length >= 10 && plate.trim().length > 2 && model.isNotBlank() && pin.length == 4

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 14.dp).verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("register_back_button")) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali", tint = DrgTextDark) }
            Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(Color.Black).border(1.5.dp, theme.primaryColor, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                Image(painter = painterResource(id = R.drawable.drg_app_icon), "Logo DRG", contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize().padding(2.dp))
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Daftar Anggota DRG", fontSize = 22.sp, fontWeight = FontWeight.Black, color = DrgTextDark)
        Text("Bergabung dengan solidaritas driver Arema Malang Raya.", fontSize = 12.sp, color = DrgTextMuted)
        Spacer(Modifier.height(14.dp))

        DrgLuminousGlassCard(shape = RoundedCornerShape(24.dp), backgroundColor = theme.glassBg, borderHighlightColor = theme.glassBorderColor, contentPadding = 16.dp, modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name, onValueChange = { name = it; nameTouched = true }, label = { Text("Nama Lengkap") },
                    isError = nameTouched && name.isBlank(), supportingText = { if (nameTouched && name.isBlank()) Text("Nama tidak boleh kosong", color = DrgRedPanic) },
                    modifier = Modifier.fillMaxWidth().testTag("reg_name"), shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = phone, onValueChange = { phone = it; phoneTouched = true }, label = { Text("Nomor HP Aktif") },
                    isError = phoneTouched && phone.length < 10, supportingText = { if (phoneTouched && phone.length < 10) Text("Nomor HP minimal 10 digit", color = DrgRedPanic) },
                    modifier = Modifier.fillMaxWidth().testTag("reg_phone"), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = plate, onValueChange = { var c = it.uppercase(); if (!c.startsWith("N")) c = "N " + c.trim(); plate = c; plateTouched = true },
                    label = { Text("Nomor Pelat Motor (Jatim Malang)") }, isError = plateTouched && plate.trim().length <= 2,
                    supportingText = { if (plateTouched && plate.trim().length <= 2) Text("Pelat motor wajib valid", color = DrgRedPanic) },
                    modifier = Modifier.fillMaxWidth().testTag("reg_plate"), keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters), shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = model, onValueChange = { model = it; modelTouched = true }, label = { Text("Model Motor") },
                    isError = modelTouched && model.isBlank(), supportingText = { if (modelTouched && model.isBlank()) Text("Model motor wajib diisi", color = DrgRedPanic) },
                    placeholder = { Text("Contoh: Honda Vario 160") }, modifier = Modifier.fillMaxWidth().testTag("reg_motor"), shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = pin, onValueChange = { if (it.length <= 4) { pin = it; pinTouched = true } }, label = { Text("Setel 4-Digit PIN Baru") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }, isError = pinTouched && pin.length < 4,
                    supportingText = { if (pinTouched && pin.length < 4) Text("PIN wajib 4 digit angka", color = DrgRedPanic) else Text("PIN ini akan digunakan untuk login anggota.") },
                    modifier = Modifier.fillMaxWidth().testTag("reg_pin"), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(12.dp)
                )
                RegisterAreaDropdown(selectedArea = selectedArea, onAreaSelected = { selectedArea = it }, areaOptions = areaOptions)
            }
        }
        Spacer(Modifier.height(14.dp))
        Button(
            onClick = { if (isValid) { haptic.performHapticFeedback(HapticFeedbackType.LongPress); onSubmit(name, phone, plate, model, selectedArea, pin) } },
            enabled = isValid, modifier = Modifier.fillMaxWidth().height(52.dp).testTag("reg_submit_button"), shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor)
        ) { Text("Kirim Pendaftaran Terverifikasi", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White) }
        Spacer(Modifier.height(12.dp))
    }
}
