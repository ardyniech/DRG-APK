package com.example.modules.profile.primitives

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.RoleManager
import com.example.shared.atoms.RoleBadge
import com.example.shared.models.AccessLevel
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun KtaDriverAvatarAndDetails(member: DriverMember?, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DrgGreenContainer)
                .border(1.dp, DrgGreenPrimary, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            val avatarIcon = when (member?.profilePhotoUrl) {
                "satgas" -> Icons.Default.Shield
                "captain" -> Icons.Default.MilitaryTech
                "ojol" -> Icons.Default.TwoWheeler
                else -> Icons.Default.Person
            }
            Icon(imageVector = avatarIcon, contentDescription = "Foto Profil", tint = DrgGreenPrimary, modifier = Modifier.size(34.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(text = member?.name ?: "Nama Driver", color = DrgTextPrimary, fontWeight = FontWeight.Black, fontSize = 17.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                member?.role?.let { r ->
                    RoleBadge(role = r)
                    val userRole = RoleManager.getUserRoleForMemberRole(r)
                    val (bgColor, textColor, label) = when (userRole.accessLevel) {
                        AccessLevel.ADMIN -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "🔑 Admin")
                        AccessLevel.PENGURUS -> Triple(Color(0xFFE0F2F1), Color(0xFF00796B), "⚙️ Pengurus")
                        AccessLevel.MEMBER -> Triple(Color(0xFFECEFF1), Color(0xFF37474F), "👤 Member")
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(bgColor).padding(horizontal = 6.dp, vertical = 2.dp)) {
                        Text(text = label, color = textColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(DrgGreenContainer).clickable {
                        val idText = member?.id ?: "DRG-000"
                        clipboardManager.setText(AnnotatedString(idText))
                        Toast.makeText(context, "ID KTA $idText disalin!", Toast.LENGTH_SHORT).show()
                    }.padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = "• ID: ${member?.id ?: "DRG-000"}", color = DrgTextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Salin KTA ID", tint = DrgGreenPrimary, modifier = Modifier.size(11.dp))
                }
            }
            Text(text = "Motor: ${member?.motorcycleModel ?: "-"} (${member?.motorcyclePlate ?: "-"})", color = DrgTextSecondary, fontSize = 11.sp, modifier = Modifier.padding(top = 2.dp))
        }
    }
}
