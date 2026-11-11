package com.example.modules.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.TwoWheeler
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
import com.example.shared.atoms.DrgGlassCard
import com.example.ui.theme.DrgDialogDarkBg
import com.example.ui.theme.DrgGlassBorderBrush
import com.example.ui.theme.DrgGrabGreenPrimary

@Composable
fun LandingAuthMenuDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onQuickDemo: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onAbout: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .clickable { onDismiss() }
                .padding(horizontal = 20.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            DrgGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = false) {}
                    .testTag("landing_auth_menu_dialog"),
                shape = RoundedCornerShape(26.dp),
                backgroundColor = DrgDialogDarkBg.copy(alpha = 0.95f),
                borderBrush = DrgGlassBorderBrush,
                elevation = 16.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(34.dp).clip(CircleShape).background(DrgGrabGreenPrimary.copy(alpha = 0.20f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.TwoWheeler, contentDescription = null, tint = DrgGrabGreenPrimary, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("DRG Driver • Masuk", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                        }
                        IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Tutup", tint = Color.White.copy(alpha = 0.7f))
                        }
                    }

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
