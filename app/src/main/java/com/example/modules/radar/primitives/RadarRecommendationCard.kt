package com.example.modules.radar.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun RadarRecommendationCard(
    isConsentOn: Boolean,
    isVerified: Boolean,
    modifier: Modifier = Modifier
) {
    val pendingTodo = when {
        !isConsentOn -> Pair(
            "Aktifkan Izin Lokasi Live",
            "Lokasi live Anda mati. Aktifkan demi fitur radar keselamatan & pemantauan Satgas Korlap."
        )
        !isVerified -> Pair(
            "Verifikasi Akun Pengemudi",
            "Profil Anda belum diverifikasi Ketua DRG. Segera hubungi pengurus pada Kopdar terdekat!"
        )
        else -> Pair(
            "Absen Kehadiran Kopdar DRG",
            "Saling sapa anggota ojol Malang Raya hari ini dan amankan tambahan +50 Poin!"
        )
    }

    Surface(
        color = DrgAmberSecondary.copy(alpha = 0.08f),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, DrgAmberSecondary.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DrgAmberSecondary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.TaskAlt, contentDescription = null, tint = DrgAmberSecondary, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Rekomendasi Otoritas", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgAmberSecondary)
                Text(pendingTodo.first, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = DrgTextPrimary)
                Text(pendingTodo.second, fontSize = 9.5.sp, color = DrgTextSecondary)
            }
        }
    }
}

@Composable
fun RadarConsentAndHazardBar(
    isConsentOn: Boolean,
    onToggleConsent: (Boolean) -> Unit,
    onAddHazardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = DrgSurface,
        tonalElevation = 1.dp,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Izin Lokasi Live Komunitas", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                Text("Privasi: Lokasi disiarkan di Radar", fontSize = 9.sp, color = DrgTextMuted)
            }
            Switch(
                checked = isConsentOn,
                onCheckedChange = onToggleConsent,
                modifier = Modifier.scale(0.8f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onAddHazardClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DrgRedPanic.copy(alpha = 0.12f),
                    contentColor = DrgRedPanic
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Icon(Icons.Default.AddLocationAlt, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Bahaya", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
