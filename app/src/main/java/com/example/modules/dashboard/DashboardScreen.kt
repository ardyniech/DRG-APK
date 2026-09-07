package com.example.modules.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.viewmodel.MainNavTab
import com.example.shared.models.*
import com.example.ui.theme.*
import com.example.R

@Composable
fun DashboardScreen(
    currentMember: DriverMember?,
    members: List<DriverMember>,
    activeAlerts: List<EmergencyAlert>,
    transactions: List<KasTransaction>,
    notifications: List<CommunityNotification>,
    onNavigateTab: (MainNavTab) -> Unit,
    onTriggerEmergency: () -> Unit,
    onApproveSelf: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val activeMemberCount = members.filter { it.isOnline }.size
    val totalBalance = transactions.sumOf { if (it.type == TransactionType.INCOME) it.amount else -it.amount }
    val totalKasFormatted = java.text.NumberFormat.getInstance().format(totalBalance)
    val poskoCount = 4 // standard default

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 24.dp)
    ) {
        if (currentMember?.verificationStatus == VerificationStatus.PENDING_SCREENING) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DrgAmberContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, DrgAmberSecondary)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text("⏳", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Pendaftaran Dalam Screening",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = DrgAmberDark
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Halo ${currentMember.name}, selamat bergabung di DRG Malang Raya! Akun Anda saat ini sedang dikaji oleh Dewan Etika & Pengurus.",
                            fontSize = 12.sp,
                            color = DrgTextDark
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Langkah Berikutnya untuk Aktivasi Akun:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = DrgTextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("1. Hubungi Satgas DRG Malang untuk verifikasi fisik (KTP/SIM/STNK).", fontSize = 11.sp, color = DrgTextSlate)
                        Text("2. Hadir di Kopdar mingguan untuk perkenalan solidaritas Arema.", fontSize = 11.sp, color = DrgTextSlate)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    android.widget.Toast.makeText(context, "Membuka WhatsApp Satgas DRG Malang Raya (Mock)...", android.widget.Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = DrgGrabGreenPrimary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1.5f).height(38.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp)
                            ) {
                                Text("Hubungi Satgas", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            if (onApproveSelf != null) {
                                OutlinedButton(
                                    onClick = onApproveSelf,
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgAmberDark),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, DrgAmberDark),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.weight(2f).height(38.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp)
                                ) {
                                    Text("Verifikasi Instan (Demo)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            ShiftStatusBanner(
                member = currentMember
            )
        }

        val isConsentOn = currentMember?.isLocationSharingConsent ?: true
        val isVerified = currentMember?.verificationStatus == VerificationStatus.VERIFIED

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DrgSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DrgOutline, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "📋 DAFTAR REKOMENDASI TUGAS PENDING",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = DrgAmberSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Task 1: GPS Permission
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Nyalakan Uplink GPS Live Radar",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = DrgTextPrimary
                            )
                            Text(
                                text = "Penting untuk keselamatan berkendara & koordinasi Satgas",
                                fontSize = 10.sp,
                                color = DrgTextSecondary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isConsentOn) DrgGreenContainer else DrgRedContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isConsentOn) "SELESAI" else "PENDING",
                                color = if (isConsentOn) DrgGreenPrimary else DrgRedDanger,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    HorizontalDivider(color = DrgOutline.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 6.dp))

                    // Task 2: Attendance / Kopdar Action
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Absen Kehadiran Kopdar DRG",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = DrgTextPrimary
                            )
                            Text(
                                text = "Saling sapa anggota ojol Malang Raya & raih +50 Poin",
                                fontSize = 10.sp,
                                color = DrgTextSecondary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DrgGreenContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "READY",
                                color = DrgGreenPrimary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    HorizontalDivider(color = DrgOutline.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 6.dp))

                    // Task 3: Verifikasi Profil
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Verifikasi Status Keanggotaan KTA",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = DrgTextPrimary
                            )
                            Text(
                                text = "Verifikasi KTP/SIM/STNK oleh Satgas & Ketua DRG",
                                fontSize = 10.sp,
                                color = DrgTextSecondary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isVerified) DrgGreenContainer else DrgAmberContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isVerified) "VERIFIED" else "SCREENING",
                                color = if (isVerified) DrgGreenPrimary else DrgAmberDark,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Custom Community Hero Banner Image
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.drg_hero_banner),
                        contentDescription = "Komunitas DRG Driver Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Dark elegant gradient scrim
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.75f)
                                    )
                                )
                            )
                    )
                    // Banner caption overlay
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            text = "Solidaritas Tanpa Batas",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Keluarga Besar Driver Riang Gembira Malang Raya",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        val activeSos = activeAlerts.firstOrNull { it.isActive }
        if (activeSos != null) {
            item {
                ActiveAlertBanner(
                    alert = activeSos,
                    onViewDetail = { onNavigateTab(MainNavTab.EMERGENCY) }
                )
            }
        }

        item {
            Text(
                text = "Ringkasan Ekosistem Komunitas DRG",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DrgTextPrimary
            )
        }

        item {
            SummaryStatsGrid(
                activeMemberCount = activeMemberCount,
                activeHazardCount = 0, // derived from system hazards or similar. Let's pass 0 or activeAlerts.size for simplicity
                kasFormatted = totalKasFormatted,
                poskoCount = poskoCount,
                onNavigateToTab = { index ->
                    when (index) {
                        1 -> onNavigateTab(MainNavTab.RADAR)
                        2 -> onNavigateTab(MainNavTab.MEMBERS)
                        4 -> onNavigateTab(MainNavTab.KAS)
                        else -> onNavigateTab(MainNavTab.DASHBOARD)
                    }
                }
            )
        }

        item {
            Text(
                text = "Pintas Tindakan Cepat (Quick Actions)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = DrgTextPrimary
            )
        }

        item {
            QuickActionGrid(
                onNavigate = onNavigateTab,
                onQuickEmergency = onTriggerEmergency
            )
        }
    }
}
