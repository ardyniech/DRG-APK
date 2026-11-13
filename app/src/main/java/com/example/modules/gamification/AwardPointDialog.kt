package com.example.modules.gamification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.models.DriverMember
import com.example.shared.models.MemberRole
import com.example.ui.theme.*

@Composable
fun AwardPointDialog(
    targetDriver: DriverMember,
    currentGiverRole: MemberRole,
    onDismiss: () -> Unit,
    onAward: (Boolean, String) -> Unit
) {
    var isBeneficiary by remember { mutableStateOf(false) }
    var reason by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    val calculatedWeight = if (isBeneficiary) 2 else when (currentGiverRole) {
        MemberRole.SUPER_ADMIN, MemberRole.DEWAN_ETIKA -> 5
        MemberRole.KETUA, MemberRole.WAKIL_KETUA -> 4
        MemberRole.SEKRETARIS, MemberRole.BENDAHARA, MemberRole.SATGAS -> 3
        MemberRole.ANGGOTA -> 1
    }

    DrgLuminousGlassModal(
        title = "Beri Apresiasi Poin Loyalitas",
        subtitle = "Penerima: ${targetDriver.name} (${targetDriver.motorcyclePlate})",
        icon = Icons.Default.VolunteerActivism,
        accentColor = DrgAmberSecondary,
        validationError = validationError,
        onDismiss = onDismiss,
        footer = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Batal") }
                Button(
                    onClick = {
                        if (reason.isBlank()) {
                            validationError = "Alasan apresiasi poin wajib diisi."
                        } else {
                            onAward(isBeneficiary, reason.trim())
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).testTag("btn_submit_award_point")
                ) { Text("Kirim +$calculatedWeight XP", fontFamily = SpaceGroteskFontFamily, color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        DrgLuminousGlassCard(shape = RoundedCornerShape(14.dp), backgroundColor = DrgAmberContainer, borderHighlightColor = DrgAmberSecondary.copy(alpha = 0.5f), contentPadding = 12.dp, modifier = Modifier.fillMaxWidth()) {
            Column {
                Text("Perhitungan Bobot Poin Role Anda:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgOnAmberContainer)
                Text("• Role Anda (${currentGiverRole.title}): +$calculatedWeight XP", fontFamily = SpaceGroteskFontFamily, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgAmberDark)
                Text("(Aturan: Dewan Etika=5, Ketua/Waket=4, Pengurus=3, Beneficiary=2, Anggota=1)", fontSize = 9.sp, color = DrgTextMuted)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isBeneficiary, onCheckedChange = { isBeneficiary = it })
            Text("Saya merasakan bantuan/pertolongan langsung dr driver ini (Beneficiary = 2 Poin)", fontSize = 11.sp)
        }
        OutlinedTextField(
            value = reason, onValueChange = { reason = it; validationError = null },
            label = { Text("Alasan Apresiasi", fontSize = 11.sp) },
            placeholder = { Text("Contoh: Mendorong motor saat mogok di Jl. Ijen", fontSize = 11.sp) },
            modifier = Modifier.fillMaxWidth().testTag("input_award_reason"), singleLine = true, shape = RoundedCornerShape(12.dp)
        )
    }
}
