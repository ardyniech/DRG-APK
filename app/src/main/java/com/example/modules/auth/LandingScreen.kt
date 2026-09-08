package com.example.modules.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun LandingScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var promoIndex by remember { mutableStateOf(0) }
    val promoList = remember {
        listOf(
            PromoData("Solidaritas Tanpa Batas", "Saling bantu saat ban bocor, mogok, maupun musibah jalanan secara real-time.", "🤝"),
            PromoData("Radar Pantau Arema", "Pantau titik keramaian, spot gacor, hingga area rawan kejahatan di Malang.", "📡"),
            PromoData("Kas Transparan & BPJS", "Iuran sukarela yang dikelola terbuka penuh demi santunan rekan driver.", "🎟️")
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(4000)
            promoIndex = (promoIndex + 1) % promoList.size
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(DrgGrabGreenPrimary.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
                    .border(2.dp, DrgGrabGreenPrimary, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("DRG", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DrgGrabGreenPrimary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Driver Riang Gembira", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
            Text(text = "Kompak • Solid • Gacor di Malang Raya", fontSize = 13.sp, color = DrgTextMuted, modifier = Modifier.padding(top = 4.dp))
        }

        LandingPromoCarouselCard(promoList = promoList, promoIndex = promoIndex)

        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth().height(52.dp).testTag("btn_to_login"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DrgGrabGreenPrimary)
            ) {
                Text("Masuk Anggota", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            OutlinedButton(
                onClick = onNavigateToRegister,
                modifier = Modifier.fillMaxWidth().height(52.dp).testTag("btn_to_register"),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, DrgGrabGreenPrimary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DrgGrabGreenPrimary)
            ) {
                Text("Daftar Driver Baru", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
