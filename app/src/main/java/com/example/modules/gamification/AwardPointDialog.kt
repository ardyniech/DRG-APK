package com.example.modules.gamification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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

    val calculatedWeight = if (isBeneficiary) 2 else when (currentGiverRole) {
        MemberRole.DEWAN_ETIKA -> 5
        MemberRole.KETUA, MemberRole.WAKIL_KETUA -> 4
        MemberRole.SEKRETARIS, MemberRole.BENDAHARA, MemberRole.SATGAS -> 3
        MemberRole.ANGGOTA -> 1
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DrgSurface,
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Beri Apresiasi Poin Loyalitas", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DrgTextPrimary)
                Text("Penerima: ${targetDriver.name} (${targetDriver.motorcyclePlate})", fontSize = 12.sp, color = DrgTextSecondary)

                Card(
                    colors = CardDefaults.cardColors(containerColor = DrgAmberContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Perhitungan Bobot Poin Role Anda:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgOnAmberContainer)
                        Text("• Role Anda (${currentGiverRole.title}): +$calculatedWeight XP", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgAmberDark)
                        Text("(Aturan: Dewan Etika=5, Ketua/Waket=4, Pengurus=3, Beneficiary/Korban=2, Anggota=1)", fontSize = 9.sp, color = DrgTextMuted)
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isBeneficiary, onCheckedChange = { isBeneficiary = it })
                    Text("Saya merasakan bantuan/pertolongan langsung dr driver ini (Beneficiary = 2 Poin)", fontSize = 11.sp)
                }

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    placeholder = { Text("Contoh: Mendorong motor saat mogok di Daan Mogot", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Batal") }
                    Button(
                        onClick = { if (reason.isNotBlank()) onAward(isBeneficiary, reason) },
                        colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Kirim +$calculatedWeight XP", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
