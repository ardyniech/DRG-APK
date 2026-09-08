package com.example.modules.services

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AiAssistantCard(onOpenAi: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DrgSurface),
        border = BorderStroke(1.dp, DrgAmberSecondary.copy(alpha = 0.5f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = "AI Assistant",
                    tint = DrgAmberSecondary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Asisten Driver Cerdas DRG AI",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DrgTextPrimary
                )
            }
            Text(
                text = "💡 Prediksi Gacor Hari Ini:\n• Pukul 18:30 - 21:00: Stasiun Malang (Kota Baru) & Jl. Soekarno-Hatta diprediksi lonjakan orderan +45%.\n• Jalur Galunggung rawan genangan banjir ringan saat hujan deras.",
                fontSize = 12.sp,
                color = DrgTextSecondary
            )
            Button(
                onClick = onOpenAi,
                colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tanya Rute / Spot Teraman", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
