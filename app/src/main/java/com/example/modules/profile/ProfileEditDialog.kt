package com.example.modules.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun EditProfileDialog(
    member: DriverMember,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String, String, String, String, String, String, String) -> Unit
) {
    var phone by remember { mutableStateOf(member.phone) }
    var model by remember { mutableStateOf(member.motorcycleModel) }
    var plate by remember { mutableStateOf(member.motorcyclePlate) }
    var selectedPhoto by remember { mutableStateOf(member.profilePhotoUrl) }
    var address by remember { mutableStateOf(member.address) }
    var bloodType by remember { mutableStateOf(member.bloodType) }
    var emergencyContact by remember { mutableStateOf(member.emergencyContact) }
    var emergencyPhone by remember { mutableStateOf(member.emergencyPhone) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Biodata & Kontak Driver", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = DrgGreenDark) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Pilih Avatar Foto Profil:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                ProfileAvatarOptionSelector(selectedPhoto = selectedPhoto, onSelectPhoto = { selectedPhoto = it })
                Spacer(modifier = Modifier.height(2.dp))

                EditField(phone, { phone = it }, "No HP / WhatsApp Aktif")
                EditField(address, { address = it }, "Alamat Domisili (Bisa dibuka di Maps)", isSingleLine = false)
                EditField(bloodType, { bloodType = it }, "Golongan Darah (A/B/AB/O)")
                EditField(emergencyContact, { emergencyContact = it }, "Nama Kontak Darurat (Keluarga/Istri)")
                EditField(emergencyPhone, { emergencyPhone = it }, "No HP Kontak Darurat (Untuk SOS)")
                EditField(model, { model = it }, "Model Kendaraan Motor")
                EditField(plate, { plate = it }, "Nomor Plat Motor")
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(phone, member.baseArea, model, plate, selectedPhoto, address, bloodType, emergencyContact, emergencyPhone, "", "") },
                colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary, contentColor = Color.White)
            ) {
                Text("Simpan", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Batal", color = DrgTextSecondary)
            }
        }
    )
}

@Composable
private fun EditField(value: String, onValueChange: (String) -> Unit, label: String, isSingleLine: Boolean = true) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 11.sp) },
        singleLine = isSingleLine,
        modifier = Modifier.fillMaxWidth()
    )
}
