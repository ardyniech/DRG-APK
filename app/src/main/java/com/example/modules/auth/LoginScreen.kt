package com.example.modules.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.ui.theme.*
import com.example.shared.models.DriverMember

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
    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.padding(top = 16.dp).testTag("login_back_button")
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Selamat Datang", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
        Text("Masukkan ID Anggota / No. HP untuk melanjutkan.", fontSize = 13.sp, color = DrgTextMuted)

        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = inputId,
            onValueChange = {
                inputId = it
                errorMessage = null
            },
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
        Text("Akses Cepat Pengurus / Anggota (Uji Demo):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextSecondary)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(members.take(5)) { member ->
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(
                            width = 1.dp,
                            color = if (inputId == member.id) DrgGrabGreenPrimary else DrgBorderLight,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            inputId = member.id
                        }
                        .padding(12.dp)
                ) {
                    Column {
                        Text(member.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1, color = DrgTextDark)
                        Text(member.role.shortName, fontSize = 10.sp, color = DrgGrabGreenPrimary, fontWeight = FontWeight.SemiBold)
                        Text(member.motorcyclePlate, fontSize = 10.sp, color = DrgTextMuted, maxLines = 1)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(
            onClick = { showHelpDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally).testTag("forgot_id_button")
        ) {
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
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("Bantuan Lupa ID / KTA", fontWeight = FontWeight.Bold, color = DrgTextDark) },
            text = {
                Column {
                    Text("Jangan khawatir, KTA digital Anda aman tersimpan di pangkalan data DRG Malang Raya.", fontSize = 13.sp, color = DrgTextSlate)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Untuk mengetahui ID Anda, Anda bisa:", fontSize = 13.sp, color = DrgTextSlate)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Menanyakan kepada Ketua Basis / Admin Grup WA Basis Anda.", fontSize = 12.sp, color = DrgTextMuted)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("• Menghubungi Satgas Pelayanan Keanggotaan DRG via WhatsApp.", fontSize = 12.sp, color = DrgTextMuted)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showHelpDialog = false
                        android.widget.Toast.makeText(context, "Membuka WA Admin DRG Malang (Mock)...", android.widget.Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGrabGreenPrimary)
                ) {
                    Text("Chat Admin WA", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("Tutup", color = DrgTextMuted)
                }
            }
        )
    }
}
