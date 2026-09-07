package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.MainNavTab
import com.example.ui.theme.*

@Composable
fun QuickActionGrid(
    onNavigate: (MainNavTab) -> Unit,
    onQuickEmergency: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Big Road Emergency SOS Action Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(DrgRedDanger)
                .clickable { onQuickEmergency() }
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "SOS",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "TOMBOL DARURAT & PANTAUAN",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Mogok, Kecelakaan, Jalur Rawan / Begal",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.95f)
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Buka SOS",
                    tint = Color.White
                )
            }
        }

        // 4 Fast Access Tiles
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickTile(
                title = "Live Radar",
                desc = "Posisi Driver & Posko",
                icon = Icons.Default.GpsFixed,
                color = DrgGreenPrimary,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(MainNavTab.RADAR) }
            )
            QuickTile(
                title = "Kas Transparan",
                desc = "Laporan Keuangan",
                icon = Icons.Default.AccountBalanceWallet,
                color = DrgAmberSecondary,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(MainNavTab.KAS) }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickTile(
                title = "Bengkel Mitra",
                desc = "Diskon Khusus DRG",
                icon = Icons.Default.Build,
                color = DrgGrabGreenPrimary,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(MainNavTab.COMMUNITY) }
            )
            QuickTile(
                title = "Anggota & Posko",
                desc = "Screening & Absensi",
                icon = Icons.Default.People,
                color = DrgGrabGreenDark,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(MainNavTab.COMMUNITY) }
            )
        }
    }
}

@Composable
private fun QuickTile(
    title: String,
    desc: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DrgSurface)
            .border(1.dp, DrgOutline.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DrgTextPrimary
                )
                Text(
                    text = desc,
                    fontSize = 11.sp,
                    color = DrgTextSecondary
                )
            }
        }
    }
}
