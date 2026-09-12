package com.example.modules.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*

@Composable
fun LandingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = Color.Black,
                shadowElevation = 8.dp,
                border = BorderStroke(2.dp, DrgGrabGreenPrimary),
                modifier = Modifier.size(112.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.drg_app_icon),
                    contentDescription = "Logo Resmi DRG Driver Riang Gembira",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DrgGrabGreenPrimary.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, DrgGrabGreenPrimary.copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Verified, contentDescription = null, tint = DrgGrabGreenPrimary, modifier = Modifier.size(13.dp))
                    Text("Official Community App", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGrabGreenPrimary)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Driver Riang Gembira", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = DrgTextDark)
            Text(text = "Kompak • Solid • Gacor di Malang Raya", fontSize = 12.sp, color = DrgTextMuted, modifier = Modifier.padding(top = 2.dp))
        }

        LandingFeatureAutoScrollRow()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("btn_to_login"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DrgGrabGreenPrimary)
            ) {
                Text("Masuk Anggota", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            OutlinedButton(
                onClick = onNavigateToRegister,
                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("btn_to_register"),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, DrgGrabGreenPrimary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgGrabGreenPrimary)
            ) {
                Text("Daftar Driver Baru", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
