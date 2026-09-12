package com.example.modules.profile.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ProfileAddressRow(address: String, onNavigate: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Alamat Domisili", fontSize = 11.sp, color = DrgTextSecondary, fontWeight = FontWeight.Medium)
            Text(address.ifBlank { "-" }, fontSize = 12.sp, color = DrgTextPrimary, fontWeight = FontWeight.SemiBold)
        }
        if (address.isNotBlank() && address != "-") {
            FilledTonalButton(
                onClick = { onNavigate(address) },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Icon(Icons.Default.Navigation, contentDescription = "Buka Maps", modifier = Modifier.size(13.dp), tint = DrgGreenPrimary)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Maps", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
            }
        }
    }
}

@Composable
fun ProfilePhoneRow(phone: String, onChatWa: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("No HP / WhatsApp", fontSize = 11.sp, color = DrgTextSecondary, fontWeight = FontWeight.Medium)
            Text(phone.ifBlank { "-" }, fontSize = 12.sp, color = DrgTextPrimary, fontWeight = FontWeight.SemiBold)
        }
        if (phone.isNotBlank() && phone != "-") {
            FilledTonalButton(
                onClick = { onChatWa(phone) },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = DrgGreenPrimary.copy(alpha = 0.15f)),
                modifier = Modifier.height(30.dp)
            ) {
                Icon(Icons.Default.Chat, contentDescription = "Chat WA", modifier = Modifier.size(13.dp), tint = DrgGreenPrimary)
                Spacer(modifier = Modifier.width(4.dp))
                Text("WA", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGreenPrimary)
            }
        }
    }
}

@Composable
fun ProfileEmergencyRow(contactName: String, phone: String, onContact: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Kontak Darurat", fontSize = 11.sp, color = DrgTextSecondary, fontWeight = FontWeight.Medium)
            Text("${contactName.ifBlank { "-" }} (${phone.ifBlank { "-" }})", fontSize = 12.sp, color = DrgTextPrimary, fontWeight = FontWeight.SemiBold)
        }
        if (phone.isNotBlank() && phone != "-") {
            FilledTonalButton(
                onClick = { onContact(phone) },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.filledTonalButtonColors(containerColor = DrgRedDanger.copy(alpha = 0.12f)),
                modifier = Modifier.height(30.dp)
            ) {
                Icon(Icons.Default.Call, contentDescription = "Hubungi SOS", modifier = Modifier.size(13.dp), tint = DrgRedDanger)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Hubungi", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgRedDanger)
            }
        }
    }
}
