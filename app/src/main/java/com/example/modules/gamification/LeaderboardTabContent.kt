package com.example.modules.gamification

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.atoms.RoleBadge
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun LeaderboardTabContent(members: List<DriverMember>, onAwardPointsClick: () -> Unit) {
    val sortedMembers = members.sortedByDescending { it.loyaltyPoints }
    val theme = LocalAppVisualTheme.current

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = onAwardPointsClick,
            colors = ButtonDefaults.buttonColors(containerColor = DrgAmberSecondary),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(44.dp)
        ) {
            Icon(Icons.Default.VolunteerActivism, contentDescription = "Beri Poin", modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Beri Poin Apresiasi / Bantuan Rekan", fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
            itemsIndexed(sortedMembers) { index, m ->
                DrgLuminousGlassCard(
                    shape = RoundedCornerShape(18.dp),
                    backgroundColor = theme.glassBg,
                    borderHighlightColor = if (index < 3) DrgAmberSecondary.copy(alpha = 0.6f) else theme.glassBorderColor,
                    contentPadding = 12.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("#${index + 1}", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Black, fontSize = 14.sp, color = if (index < 3) DrgAmberDark else DrgTextMuted)
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(m.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                                    Spacer(Modifier.width(6.dp))
                                    RoleBadge(role = m.role)
                                }
                                Text(m.motorcycleModel, fontSize = 11.sp, color = DrgTextSecondary)
                            }
                        }
                        Text("⭐ ${m.loyaltyPoints} XP", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgAmberSecondary)
                    }
                }
            }
        }
    }
}
