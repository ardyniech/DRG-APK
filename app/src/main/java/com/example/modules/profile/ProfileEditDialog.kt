package com.example.modules.profile

import androidx.compose.foundation.layout.*
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
    onSave: (String, String, String, String, String) -> Unit
) {
    var phone by remember { mutableStateOf(member.phone) }
    var model by remember { mutableStateOf(member.motorcycleModel) }
    var plate by remember { mutableStateOf(member.motorcyclePlate) }
    var selectedPhoto by remember { mutableStateOf(member.profilePhotoUrl) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit Profil Driver DRG",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DrgGreenDark
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Pilih Avatar Foto Profil:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                ProfileAvatarOptionSelector(selectedPhoto = selectedPhoto, onSelectPhoto = { selectedPhoto = it })
                Spacer(modifier = Modifier.height(2.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("No HP/WA", fontSize = 11.sp) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Model Motor", fontSize = 11.sp) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = plate,
                    onValueChange = { plate = it },
                    label = { Text("Plat Nomor", fontSize = 11.sp) },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(phone, member.baseArea, model, plate, selectedPhoto) },
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
