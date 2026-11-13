package com.example.modules.profile

import android.graphics.Bitmap
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*
import java.io.ByteArrayOutputStream

@Composable
fun ProfileAvatarOptionSelector(
    selectedPhoto: String,
    onSelectPhoto: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
            onSelectPhoto("data:image/jpeg;base64," + Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP))
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { if (it) cameraLauncher.launch() }
    val photoOptions = listOf("" to "Standar", "satgas" to "Satgas", "captain" to "Captain", "ojol" to "Driver Ojol")

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            photoOptions.forEach { (key, label) ->
                val isSelected = selectedPhoto == key
                Surface(
                    onClick = { onSelectPhoto(key) },
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) theme.pillBg else DrgGlassSurfaceSoft,
                    border = BorderStroke(1.dp, if (isSelected) theme.primaryColor else theme.glassBorderColor.copy(alpha = 0.6f)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(vertical = 7.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        val icon = when (key) {
                            "satgas" -> Icons.Default.Shield
                            "captain" -> Icons.Default.MilitaryTech
                            "ojol" -> Icons.Default.TwoWheeler
                            else -> Icons.Default.Person
                        }
                        Icon(icon, contentDescription = label, tint = if (isSelected) theme.primaryColor else DrgTextSecondary, modifier = Modifier.size(18.dp))
                        Text(label, fontSize = 9.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, color = if (isSelected) theme.primaryColor else DrgTextSecondary)
                    }
                }
            }
        }

        val isCameraPhoto = selectedPhoto.startsWith("data:image/")
        Button(
            onClick = { permissionLauncher.launch(android.Manifest.permission.CAMERA) },
            colors = ButtonDefaults.buttonColors(containerColor = theme.primaryColor),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(42.dp)
        ) {
            Icon(Icons.Default.PhotoCamera, contentDescription = "Ambil Foto Kamera", tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (isCameraPhoto) "✓ Foto Kamera Tersimpan (Tap Ulang)" else "Ambil Foto via Kamera HP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
