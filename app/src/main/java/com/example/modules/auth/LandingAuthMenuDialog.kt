package com.example.modules.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextSecondary

@Composable
fun LandingAuthMenuDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onQuickDemo: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onAbout: () -> Unit
) {
    val theme = LocalAppVisualTheme.current
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x660F172A))
                .clickable { onDismiss() }
                .padding(horizontal = 20.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            DrgLuminousGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp)
                    .clickable(enabled = false) {}
                    .testTag("landing_auth_menu_dialog"),
                shape = RoundedCornerShape(26.dp),
                backgroundColor = Color(0xF5FFFFFF),
                borderHighlightColor = theme.glassBorderColor,
                contentPadding = 20.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(theme.pillBg)
                                    .border(1.dp, theme.primaryColor.copy(alpha = 0.35f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = theme.primaryColor, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text("DRG Driver • Masuk", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DrgTextDark)
                                Text("Portal Komunitas Malang Raya", fontSize = 11.sp, color = DrgTextSecondary)
                            }
                        }
                        IconButton(onClick = onDismiss, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Tutup", tint = DrgTextDark)
                        }
                    }

                    HorizontalDivider(color = theme.glassBorderColor.copy(alpha = 0.45f))

                    LandingAuthMenuButtons(
                        onDismiss = onDismiss,
                        onQuickDemo = onQuickDemo,
                        onLogin = onLogin,
                        onRegister = onRegister,
                        onAbout = onAbout
                    )
                }
            }
        }
    }
}
