package com.example.modules.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.ui.theme.*
import java.io.ByteArrayOutputStream

@Composable
fun ProfileAvatarOptionSelector(
    selectedPhoto: String,
    onSelectPhoto: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream)
            val byteArray = stream.toByteArray()
            val base64String = "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.NO_WRAP)
            onSelectPhoto(base64String)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch()
        }
    }

    val photoOptions = listOf(
        "" to "Standar",
        "satgas" to "Satgas",
        "captain" to "Captain",
        "ojol" to "Driver Ojol"
    )

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            photoOptions.forEach { (key, label) ->
                val isSelected = selectedPhoto == key
                Surface(
                    onClick = { onSelectPhoto(key) },
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) DrgGreenContainer else DrgSurfaceVariant,
                    border = BorderStroke(1.dp, if (isSelected) DrgGreenPrimary else Color.Transparent),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val icon = when (key) {
                            "satgas" -> Icons.Default.Shield
                            "captain" -> Icons.Default.MilitaryTech
                            "ojol" -> Icons.Default.TwoWheeler
                            else -> Icons.Default.Person
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isSelected) DrgGreenPrimary else DrgTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = label,
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) DrgGreenPrimary else DrgTextSecondary
                        )
                    }
                }
            }
        }

        val isCameraPhoto = selectedPhoto.startsWith("data:image/")
        Button(
            onClick = { permissionLauncher.launch(android.Manifest.permission.CAMERA) },
            colors = ButtonDefaults.buttonColors(containerColor = if (isCameraPhoto) DrgGreenPrimary else DrgGrabGreenPrimary),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().height(38.dp)
        ) {
            Icon(Icons.Default.PhotoCamera, contentDescription = "Ambil Foto Kamera", tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(if (isCameraPhoto) "✓ Foto Kamera Tersimpan (Tap untuk Foto Ulang)" else "📷 Ambil Foto via Kamera HP", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
