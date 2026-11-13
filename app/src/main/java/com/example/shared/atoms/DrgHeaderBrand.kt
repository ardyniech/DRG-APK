package com.example.shared.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.shared.models.DriverMember
import com.example.ui.theme.DrgTextDark
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.SpaceGroteskFontFamily

@Composable
fun DrgHeaderBrand(
    member: DriverMember? = null,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    val accent = theme.primaryColor

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black)
                .border(1.5.dp, accent, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.drg_app_icon),
                contentDescription = "DRG Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize().padding(2.dp)
            )
        }
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("DRG", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = accent)
                Spacer(modifier = Modifier.width(4.dp))
                Text("MALANG", fontFamily = SpaceGroteskFontFamily, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextDark)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(if (member?.isOnline != false) accent else DrgTextMuted))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = member?.name?.split(" ")?.firstOrNull() ?: "Satgas Radar Siaga",
                    fontSize = 11.sp, color = DrgTextSecondary, fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
