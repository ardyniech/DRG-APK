package com.example.modules.profile

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

@Composable
fun ProfileAvatarOptionSelector(
    selectedPhoto: String,
    onSelectPhoto: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val photoOptions = listOf(
        "" to "Standar",
        "satgas" to "Satgas",
        "captain" to "Captain",
        "ojol" to "Driver Ojol"
    )

    Row(
        modifier = modifier.fillMaxWidth(),
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
                        modifier = Modifier.size(20.dp)
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
}
