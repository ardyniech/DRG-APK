package com.example.modules.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.profile.primitives.ProfileAddressRow
import com.example.modules.profile.primitives.ProfileEmergencyRow
import com.example.modules.profile.primitives.ProfilePhoneRow
import com.example.shared.models.DriverMember
import com.example.shared.utils.WhatsAppLauncher
import com.example.ui.theme.*

@Composable
fun VehicleInfoCard(member: DriverMember?, onEditClick: () -> Unit) {
    val context = LocalContext.current
    val cardShape = RoundedCornerShape(22.dp)
    val highlightBrush = Brush.linearGradient(
        listOf(Color.White, Color.White.copy(alpha = 0.5f), DrgGreenPrimary.copy(alpha = 0.2f))
    )

    Surface(
        shape = cardShape,
        color = Color.White.copy(alpha = 0.85f),
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, cardShape, ambientColor = Color(0x14003617), spotColor = Color(0x0E003617))
            .border(1.2.dp, highlightBrush, cardShape)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Biodata & Navigasi Driver", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DrgTextPrimary)
                member?.bloodType?.takeIf { it.isNotEmpty() }?.let { blood ->
                    Box(modifier = Modifier
                        .background(DrgRedDanger.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .border(1.dp, DrgRedDanger.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(text = "Gol. Darah: $blood", color = DrgRedDanger, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
            HorizontalDivider(color = DrgOutline.copy(alpha = 0.4f), thickness = 0.8.dp)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ProfileAddressRow(address = member?.address ?: "-", onNavigate = { WhatsAppLauncher.openGoogleMapsAddress(context, it) })
                ProfilePhoneRow(phone = member?.phone ?: "-", onChatWa = { WhatsAppLauncher.openChat(context, it) })
                ProfileEmergencyRow(contactName = member?.emergencyContact ?: "-", phone = member?.emergencyPhone ?: "-", onContact = { WhatsAppLauncher.openChat(context, it, "Halo, saya menghubungi dari sistem DRG Driver Malang") })
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Model & Plat Motor", fontSize = 11.sp, color = DrgTextSecondary, fontWeight = FontWeight.Medium)
                    Text(text = "${member?.motorcycleModel ?: "-"} • ${member?.motorcyclePlate ?: "-"}", fontSize = 12.sp, color = DrgTextPrimary, fontWeight = FontWeight.SemiBold)
                }
            }

            Button(
                onClick = onEditClick,
                colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary, contentColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Perbarui Biodata & Alamat", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
