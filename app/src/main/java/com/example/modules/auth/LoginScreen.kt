package com.example.modules.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.shared.models.DriverMember
import com.example.shared.utils.SecurityUtils
import com.example.ui.theme.*

@Composable
fun LoginScreen(
    members: List<DriverMember>,
    onBack: () -> Unit,
    onLogin: (String) -> Unit
) {
    var inputId by remember { mutableStateOf("") }
    var inputPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showHelpDialog by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("login_back_button")) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
            }
            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(Color.Black).border(1.5.dp, DrgGrabGreenPrimary, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(painter = painterResource(id = R.drawable.drg_app_icon), contentDescription = "Logo DRG", contentScale = ContentScale.Fit, modifier = Modifier.fillMaxSize().padding(2.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Autentikasi Anggota DRG", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
        Text("Masukkan ID KTA, No. HP, atau Plat Motor dan PIN.", fontSize = 13.sp, color = DrgTextMuted)

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = inputId, onValueChange = { inputId = it; errorMessage = null },
            label = { Text("No. HP / KTA ID / Plat Motor") }, placeholder = { Text("DRG-001, 0812..., atau N 1234 XX") },
            modifier = Modifier.fillMaxWidth().testTag("login_input_id"), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            isError = errorMessage != null, shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = inputPin, onValueChange = { if (it.length <= 4) { inputPin = it; errorMessage = null } },
            label = { Text("4-Digit PIN Keamanan") }, placeholder = { Text("Masukkan 4 digit PIN") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) }, visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().testTag("login_input_pin"), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errorMessage != null, shape = RoundedCornerShape(12.dp)
        )

        if (errorMessage != null) {
            Text(errorMessage!!, color = MaterialTheme.colorScheme.error, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { showHelpDialog = true }, modifier = Modifier.align(Alignment.CenterHorizontally).testTag("forgot_id_button")) {
            Text("Lupa PIN / KTA ID Anggota? Hubungi Pengurus", color = DrgGrabGreenPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                if (inputPin.length < 4) {
                    errorMessage = "PIN keamanan harus 4 digit angka."
                    return@Button
                }
                val (success, result) = LoginAuthValidator.authenticate(context, members, inputId, inputPin)
                if (success && result != null) {
                    onLogin(result)
                } else {
                    errorMessage = result
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp).testTag("login_submit_button"),
            shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = DrgGrabGreenPrimary)
        ) {
            Text("Masuk Terverifikasi", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }

    if (showHelpDialog) { LoginHelpDialog(onDismiss = { showHelpDialog = false }) }
}
