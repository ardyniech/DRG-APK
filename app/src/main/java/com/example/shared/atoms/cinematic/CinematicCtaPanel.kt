package com.example.shared.atoms.cinematic

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgGlassCard
import com.example.shared.atoms.DrgGlassPrimaryButton
import com.example.shared.atoms.DrgGlassSecondaryButton
import com.example.ui.theme.DrgGoldReward
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextMuted

@Composable
fun CinematicCtaPanel(
    visible: Boolean,
    onQuickDemo: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val reveal by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "cta-reveal"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .alpha(reveal.coerceIn(0f, 1f))
            .testTag("landing_cta_panel"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DrgGlassCard(
            modifier = Modifier.fillMaxWidth().testTag("btn_quick_demo"),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = DrgGrabGreenPrimary.copy(alpha = 0.18f),
            borderBrush = Brush.horizontalGradient(listOf(DrgGrabGreenPrimary, DrgGoldReward)),
            borderWidth = 1.5.dp,
            elevation = 6.dp,
            onClick = onQuickDemo
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Bolt, contentDescription = null, tint = DrgGrabGreenPrimary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Masuk Cepat • Akun Demo Ketua",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        DrgGlassPrimaryButton(
            text = "Masuk Anggota Terdaftar",
            icon = Icons.Default.Login,
            onClick = onLogin,
            modifier = Modifier.testTag("btn_to_login")
        )
        DrgGlassSecondaryButton(
            text = "Daftar Driver Baru",
            icon = Icons.Default.PersonAdd,
            onClick = onRegister,
            modifier = Modifier.testTag("btn_to_register")
        )
        DrgGlassSecondaryButton(
            text = "Tentang Fitur & Roadmap",
            icon = Icons.Default.Info,
            onClick = onAbout,
            modifier = Modifier.testTag("btn_to_about")
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            "Nol server • Nol biaya bulanan • Data tetap di perangkat",
            fontSize = 10.sp,
            color = DrgTextMuted
        )
    }
}