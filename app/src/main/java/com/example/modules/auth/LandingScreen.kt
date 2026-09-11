package com.example.modules.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.drg_app_icon),
                contentDescription = "Logo Resmi DRG Driver Riang Gembira",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                    .border(2.dp, DrgGrabGreenPrimary, CircleShape)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Driver Riang Gembira", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
            Text(text = "Kompak • Solid • Gacor di Malang Raya", fontSize = 12.sp, color = DrgTextMuted, modifier = Modifier.padding(top = 2.dp))
        }

        LandingFeatureAutoScrollRow()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
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
