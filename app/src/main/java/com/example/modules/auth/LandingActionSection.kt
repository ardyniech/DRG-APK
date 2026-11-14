package com.example.modules.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGlassSurfaceBright
import com.example.ui.theme.DrgGoldReward
import com.example.ui.theme.DrgGrabGreenDark
import com.example.ui.theme.DrgGrabGreenPrimary
import com.example.ui.theme.DrgTextDark

@Composable
fun LandingActionSection(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onQuickDemoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Primary Button: Masuk Driver
        Button(
            onClick = onLoginClick,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(DrgGrabGreenPrimary, DrgGrabGreenDark)),
                    shape = RoundedCornerShape(16.dp)
                )
                .testTag("btn_landing_login")
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Login, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Masuk Portal Driver", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        // Secondary & Quick Demo Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onRegisterClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = DrgGlassSurfaceBright),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .border(1.dp, DrgGrabGreenPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .testTag("btn_landing_register")
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, tint = DrgGrabGreenPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Daftar Driver", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
            }

            OutlinedButton(
                onClick = onQuickDemoClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = DrgGlassSurfaceBright),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .border(1.dp, DrgGoldReward.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .testTag("btn_landing_quick_demo")
            ) {
                Icon(Icons.Default.Bolt, contentDescription = null, tint = DrgGoldReward, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Demo Cepat", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
            }
        }
    }
}
