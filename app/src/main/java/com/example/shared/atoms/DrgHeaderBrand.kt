package com.example.shared.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DrgHeaderBrand(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DrgGreenPrimary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.TwoWheeler,
                contentDescription = "DRG Logo",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "DRG",
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    color = DrgGreenPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "DRIVER ONLINE",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DrgTextPrimary
                )
            }
            Text(
                text = "Driver Riang Gembira • Lokal 🟢",
                fontSize = 11.sp,
                color = DrgTextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
