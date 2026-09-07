package com.example.modules.profile

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.RoleManager
import com.example.shared.atoms.RoleBadge
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun KtaDigitalCard(member: DriverMember?) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DrgSurface,
        tonalElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, DrgGreenPrimary, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(DrgGreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TwoWheeler,
                            contentDescription = "DRG",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "KARTU TANDA ANGGOTA (KTA)",
                            color = DrgGreenDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "KOMUNITAS DRIVER RIANG GEMBIRA",
                            color = DrgTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(DrgGreenContainer)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "VERIFIED ✅",
                        color = DrgGreenPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Divider(color = DrgOutline.copy(alpha = 0.5f))

            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    Icon(
                        imageVector = avatarIcon,
                        contentDescription = "Foto Profil",
                        tint = DrgGreenPrimary,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = member?.name ?: "Nama Driver",
                        color = DrgTextPrimary,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        member?.role?.let { r ->
                            RoleBadge(role = r)
                            
                            val userRole = RoleManager.getUserRoleForMemberRole(r)
                            val (bgColor, textColor, label) = when (userRole.accessLevel) {
                                com.example.shared.models.AccessLevel.ADMIN -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "🔑 Admin")
                                com.example.shared.models.AccessLevel.PENGURUS -> Triple(Color(0xFFE0F2F1), Color(0xFF00796B), "⚙️ Pengurus")
                                com.example.shared.models.AccessLevel.MEMBER -> Triple(Color(0xFFECEFF1), Color(0xFF37474F), "👤 Member")
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(bgColor)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = label,
                                    color = textColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DrgGreenContainer)
                                .clickable {
                                    val idText = member?.id ?: "DRG-000"
                                    clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(idText))
                                    android.widget.Toast.makeText(context, "ID KTA $idText disalin!", android.widget.Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "• ID: ${member?.id ?: "DRG-000"}",
                                color = DrgTextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Salin KTA ID",
                                tint = DrgGreenPrimary,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }
                    Text(
                        text = "Motor: ${member?.motorcycleModel ?: "-"} (${member?.motorcyclePlate ?: "-"})",
                        color = DrgTextSecondary,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(DrgBackground)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Area",
                        tint = DrgGreenPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "DRG Malang Raya",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DrgTextPrimary
                    )
                }
                Text(
                    text = "Bergabung: ${member?.joinedDate ?: "Jan 2024"}",
                    color = DrgTextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}
