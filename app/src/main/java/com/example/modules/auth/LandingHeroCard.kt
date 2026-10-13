package com.example.modules.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.shared.atoms.DrgGlassCard
import com.example.ui.theme.*

@Composable
fun LandingHeroCard(modifier: Modifier = Modifier) {
    DrgGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        backgroundColor = Color.White.copy(alpha = 0.88f),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color.Black)
                    .border(2.dp, DrgGrabGreenPrimary, RoundedCornerShape(22.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.drg_app_icon),
                    contentDescription = "Logo Resmi DRG",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize().padding(4.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(DrgGrabGreenContainer)
                    .border(1.dp, DrgGrabGreenPrimary.copy(alpha = 0.4f), CircleShape)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(Icons.Default.Verified, contentDescription = null, tint = DrgGrabGreenPrimary, modifier = Modifier.size(14.dp))
                Text("Official DRG Driver • Malang Raya", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgGrabGreenPrimary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Driver Riang Gembira", fontSize = 22.sp, fontWeight = FontWeight.Black, color = DrgTextDark)
            Text("Kompak • Solid • Gacor di Malang Raya", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = DrgTextMuted)
        }
    }
}
