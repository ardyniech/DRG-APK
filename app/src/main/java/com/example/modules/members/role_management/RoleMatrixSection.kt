package com.example.modules.members.role_management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.MemberRole
import com.example.ui.theme.*

@Composable
fun RoleMatrixSection(modifier: Modifier = Modifier) {
    val theme = LocalAppVisualTheme.current
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DrgLuminousGlassCard(shape = RoundedCornerShape(16.dp), backgroundColor = theme.glassBg, borderHighlightColor = theme.glassBorderColor, contentPadding = 12.dp, modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("Gunakan matriks ini sebagai acuan pelatihan struktural anggota DRG Malang Raya.", fontSize = 11.sp, color = DrgTextSecondary)
            }
        }

        MemberRole.values().forEach { role ->
            val roleColor = Color(role.badgeColorHex)
            DrgLuminousGlassCard(
                shape = RoundedCornerShape(18.dp),
                backgroundColor = theme.glassBg,
                borderHighlightColor = roleColor.copy(alpha = 0.5f),
                contentPadding = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(role.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = roleColor)
                        Text(role.shortName, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = DrgTextSecondary)
                    }
                    Text(role.description, fontSize = 11.sp, color = DrgTextMuted)
                    HorizontalDivider(color = theme.glassBorderColor.copy(alpha = 0.4f), modifier = Modifier.padding(vertical = 2.dp))
                    Text("Hak Akses Sistem Bawaan:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgTextSecondary)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        val perms = when (role) {
                            MemberRole.SUPER_ADMIN -> listOf("Semua Izin", "Akses Dev", "Bypass Validasi")
                            MemberRole.KETUA -> listOf("Kelola Kas", "Verifikasi", "SOS", "Kelola Posko", "Kelola Peran")
                            MemberRole.WAKIL_KETUA -> listOf("SOS", "Kelola Posko", "Lihat Kas")
                            MemberRole.SEKRETARIS -> listOf("Verifikasi", "Kelola Peran", "Lihat Anggota")
                            MemberRole.BENDAHARA -> listOf("Kelola Kas", "Catat Transaksi", "Laporan Kas")
                            MemberRole.SATGAS -> listOf("Tanggap SOS", "Kelola Posko", "Kawal Jalur")
                            MemberRole.DEWAN_ETIKA -> listOf("Verifikasi", "Sidang Kode Etik")
                            MemberRole.ANGGOTA -> listOf("Gunakan Fitur Dasar", "Pancar SOS", "Absensi")
                        }
                        perms.forEach { perm ->
                            Box(modifier = Modifier.background(theme.pillBg, RoundedCornerShape(6.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                Text(perm, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = theme.primaryColor)
                            }
                        }
                    }
                }
            }
        }
    }
}
