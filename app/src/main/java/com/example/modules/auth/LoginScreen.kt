package com.example.modules.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun LoginScreen(
    members: List<DriverMember>,
    onBack: () -> Unit,
    onLogin: (String) -> Unit
) {
    var inputId by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showHelpDialog by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        IconButton(onClick = onBack, modifier = Modifier.padding(top = 16.dp).testTag("login_back_button")) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Selamat Datang", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
        Text("Masukkan ID Anggota / No. HP untuk melanjutkan.", fontSize = 13.sp, color = DrgTextMuted)

        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = inputId,
            onValueChange = { inputId = it; errorMessage = null },
            label = { Text("No. HP atau KTA ID") },
            placeholder = { Text("Contoh: 081298765431 atau DRG-001") },
            modifier = Modifier.fillMaxWidth().testTag("login_input_id"),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            isError = errorMessage != null,
            shape = RoundedCornerShape(12.dp)
        )
        if (errorMessage != null) {
            Text(errorMessage!!, color = MaterialTheme.colorScheme.error, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
        LoginQuickDemoRow(members = members, selectedId = inputId, onSelectMember = { inputId = it })

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = { showHelpDialog = true }, modifier = Modifier.align(Alignment.CenterHorizontally).testTag("forgot_id_button")) {
            Text("Lupa ID Anggota DRG? Tanya Pengurus", color = DrgGrabGreenPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                val target = members.find { it.id.equals(inputId, true) || it.phone == inputId }
                if (target != null) {
                    onLogin(target.id)
                } else {
                    errorMessage = "ID atau No. HP tidak terdaftar di database DRG Malang."
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp).testTag("login_submit_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DrgGrabGreenPrimary)
        ) {
            Text("Masuk", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }

    if (showHelpDialog) {
        LoginHelpDialog(onDismiss = { showHelpDialog = false })
    }
}
