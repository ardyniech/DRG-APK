package com.example.modules.radar.primitives

import androidx.compose.foundation.background
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
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.*

@Composable
fun RadarRecommendationCard(
    isConsentOn: Boolean,
    isVerified: Boolean,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    val pendingTodo = when {
        !isConsentOn -> Pair("Aktifkan Izin Lokasi Live", "Lokasi live Anda mati. Aktifkan demi fitur radar keselamatan & pemantauan Satgas Korlap.")
        !isVerified -> Pair("Verifikasi Akun Pengemudi", "Profil Anda belum diverifikasi Ketua DRG. Hubungi pengurus pada Kopdar terdekat!")
        else -> Pair("Absen Kehadiran Kopdar DRG", "Saling sapa anggota ojol Malang Raya hari ini dan amankan tambahan +50 Poin!")
    }

    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 12.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(theme.pillBg),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Default.TaskAlt, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(20.dp)) }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Rekomendasi Satgas", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = theme.primaryColor)
                Text(pendingTodo.first, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                Text(pendingTodo.second, fontSize = 10.sp, color = DrgTextSecondary)
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
    val theme = LocalAppVisualTheme.current
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 10.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Izin Lokasi Live Komunitas", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextPrimary)
                Text("Privasi: Lokasi disiarkan di Radar", fontSize = 9.5.sp, color = DrgTextMuted)
            }
            Switch(
                checked = isConsentOn,
                onCheckedChange = onToggleConsent,
                modifier = Modifier.scale(0.8f),
                colors = SwitchDefaults.colors(checkedThumbColor = theme.primaryColor, checkedTrackColor = theme.primaryColor.copy(alpha = 0.35f))
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = onAddHazardClick,
                colors = ButtonDefaults.buttonColors(containerColor = DrgRedPanic.copy(alpha = 0.14f), contentColor = DrgRedPanic),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Icon(Icons.Default.AddLocationAlt, contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(4.dp))
                Text("Bahaya", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
