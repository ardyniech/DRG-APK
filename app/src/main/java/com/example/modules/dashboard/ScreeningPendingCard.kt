package com.example.modules.dashboard

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.HourglassTop, contentDescription = null, tint = DrgAmberDark, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Status KTA: Menunggu Screening", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DrgAmberDark)
                }
                Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(DrgAmberSecondary.copy(alpha = 0.2f)).padding(horizontal = 8.dp, vertical = 2.dp)) {
                    Text("Tahap 1 dari 2", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DrgAmberDark)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StepItem(stepNum = "1", title = "Berkas KTA", isDone = true)
                Box(modifier = Modifier.width(28.dp).height(2.dp).background(DrgAmberSecondary))
                StepItem(stepNum = "2", title = "Cek Fisik Satgas", isDone = false)
                Box(modifier = Modifier.width(28.dp).height(2.dp).background(DrgAmberSecondary.copy(alpha = 0.4f)))
                StepItem(stepNum = "3", title = "KTA Aktif", isDone = false)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { Toast.makeText(context, "Membuka WhatsApp Satgas DRG Malang...", Toast.LENGTH_SHORT).show() },
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGrabGreenPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1.5f).height(38.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.SupportAgent, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Chat Satgas", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
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

@Composable
private fun StepItem(stepNum: String, title: String, isDone: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(24.dp).clip(CircleShape).background(if (isDone) DrgGreenPrimary else DrgAmberSecondary.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            } else {
                Text(text = stepNum, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgAmberDark)
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = title, fontSize = 9.sp, fontWeight = FontWeight.SemiBold, color = DrgTextDark)
    }
}
