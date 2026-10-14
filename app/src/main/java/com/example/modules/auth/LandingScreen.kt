package com.example.modules.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.*
import com.example.ui.theme.*

@Composable
fun LandingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onQuickDemoLogin: () -> Unit = onNavigateToLogin
) {
    DrgScreenContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .testTag("landing_screen"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LandingHeroCard(modifier = Modifier.padding(top = 6.dp))
            LandingFeatureAutoScrollRow()
            LandingAboutCard(onClick = onNavigateToAbout)

        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Quick One-Tap Demo Access Button
            DrgGlassCard(
                modifier = Modifier.fillMaxWidth().testTag("btn_quick_demo"),
                shape = RoundedCornerShape(16.dp),
                backgroundColor = DrgWarmGradientStart.copy(alpha = 0.90f),
                borderBrush = Brush.horizontalGradient(listOf(DrgGrabGreenPrimary, DrgGoldReward)),
                borderWidth = 1.5.dp,
                elevation = 6.dp,
                onClick = onQuickDemoLogin
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = DrgGrabGreenDark, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("⚡ Masuk Cepat (Akun Demo Ketua DRG)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DrgOnGrabGreenContainer)
                }
            }

            DrgGlassPrimaryButton(
                text = "Masuk Anggota Terdaftar",
                icon = Icons.Default.Login,
                onClick = onNavigateToLogin,
                modifier = Modifier.testTag("btn_to_login")
            )

            DrgGlassSecondaryButton(
                text = "Daftar Driver Baru",
                icon = Icons.Default.PersonAdd,
                onClick = onNavigateToRegister,
                modifier = Modifier.testTag("btn_to_register")
            )
        }
    }
}
}

