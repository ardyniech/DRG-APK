package com.example.modules.services

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.InsuranceClaimItem
import com.example.ui.theme.*

@Composable
fun ProtectionHeaderCard() {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DrgRedContainer.copy(alpha = 0.6f),
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Proteksi",
                tint = DrgRedDanger,
                modifier = Modifier.size(28.dp)
            )
            Column {
                Text(
                    text = "Dana Talangan & DRG Protection",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DrgRedDanger
                )
                Text(
                    text = "Perlindungan darurat dari iuran kas kolektif driver.",
                    fontSize = 11.sp,
                    color = DrgTextPrimary
                )
            }
        }
    }
}

@Composable
fun ClaimItemCard(claim: InsuranceClaimItem, onClaim: (InsuranceClaimItem) -> Unit) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DrgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = claim.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DrgTextPrimary
                )
                Text(
                    text = "Maksimal Perbaikan: Rp ${String.format("%,d", claim.maxCoverageRp).replace(',', '.')}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgRedDanger
                )
                Text(
                    text = "${claim.category} • ${claim.status}",
                    fontSize = 10.sp,
                    color = DrgTextMuted
                )
            }
            OutlinedButton(
                onClick = { onClaim(claim) },
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DrgRedDanger),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(text = "Klaim", fontSize = 11.sp, color = DrgRedDanger, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AiAssistantCard(onOpenAi: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DrgSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, DrgAmberSecondary.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
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

@Composable
fun EscortSatgasCard(onOpenPtt: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = DrgSurface,
            contentColor = DrgTextPrimary,
            disabledContainerColor = DrgSurface,
            disabledContentColor = DrgTextMuted
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, DrgGrabGreenContainer),
        modifier = Modifier.fillMaxWidth()
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
                    imageVector = Icons.Default.Radio,
                    contentDescription = "Escort Walkie-Talkie",
                    tint = DrgGrabGreenPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Konvoi & PTT Walkie-Talkie Satgas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DrgTextPrimary
                )
            }
            Text(
                text = "Fitur komunikasi radio PTT terhubung langsung antar-driver dan rombongan touring komunitas tanpa pulsa.",
                fontSize = 12.sp,
                color = DrgTextSecondary
            )
            OutlinedButton(
                onClick = onOpenPtt,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buka Mode Radio PTT (Komunikasi)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
