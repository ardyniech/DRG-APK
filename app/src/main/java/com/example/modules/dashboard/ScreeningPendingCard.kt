package com.example.modules.dashboard

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun ScreeningPendingCard(
    member: DriverMember,
    onApproveSelf: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DrgAmberContainer),
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp),
        border = BorderStroke(1.5.dp, DrgAmberSecondary)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                text = "Halo ${member.name}, selamat bergabung di DRG Malang Raya! Akun Anda saat ini sedang dikaji oleh Dewan Etika & Pengurus.",
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
                        Toast.makeText(context, "Membuka WhatsApp Satgas DRG Malang Raya (Mock)...", Toast.LENGTH_SHORT).show()
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
                        border = BorderStroke(1.dp, DrgAmberDark),
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
