package com.example.modules.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.DriverMember
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

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
    val theme = LocalAppVisualTheme.current

    DrgLuminousGlassModal(
        title = "Edit Biodata & Kontak Driver",
        subtitle = "${member.name} • ${member.driverId}",
        icon = Icons.Default.Edit,
        accentColor = theme.primaryColor,
        onDismiss = onDismiss,
        footer = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) {
                    Text("Batal", color = DrgTextSecondary)
                }
                Button(
                    onClick = { onSave(phone, member.baseArea, model, plate, selectedPhoto, address, bloodType, emergencyContact, emergencyPhone, "", "") },
                    colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) { Text("Simpan", fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        Text("Pilih Avatar Foto Profil:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
        ProfileAvatarOptionSelector(selectedPhoto = selectedPhoto, onSelectPhoto = { selectedPhoto = it })
        Spacer(Modifier.height(2.dp))
        EditField(phone, { phone = it }, "No HP / WhatsApp Aktif")
        EditField(address, { address = it }, "Alamat Domisili (Bisa dibuka di Maps)", isSingleLine = false)
        EditField(bloodType, { bloodType = it }, "Golongan Darah (A/B/AB/O)")
        EditField(emergencyContact, { emergencyContact = it }, "Nama Kontak Darurat (Keluarga/Istri)")
        EditField(emergencyPhone, { emergencyPhone = it }, "No HP Kontak Darurat (Untuk SOS)")
        EditField(model, { model = it }, "Model Kendaraan Motor")
        EditField(plate, { plate = it }, "Nomor Plat Motor")
    }
}

@Composable
private fun EditField(value: String, onValueChange: (String) -> Unit, label: String, isSingleLine: Boolean = true) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(label, fontSize = 11.sp) },
        singleLine = isSingleLine, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()
    )
}
