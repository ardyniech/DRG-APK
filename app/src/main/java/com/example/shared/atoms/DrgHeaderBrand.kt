package com.example.shared.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.shared.models.DriverMember
import com.example.ui.theme.*

@Composable
fun DrgHeaderBrand(
    member: DriverMember? = null,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                .background(androidx.compose.ui.graphics.Color.Black)
                .border(1.5.dp, DrgGreenPrimary, androidx.compose.foundation.shape.RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.drg_app_icon),
                contentDescription = "DRG Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
            )
        }
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "DRG",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = DrgGreenPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "MALANG",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DrgTextPrimary
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (member?.isOnline != false) DrgGreenPrimary else DrgTextMuted)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = member?.name?.split(" ")?.firstOrNull() ?: "Satgas Radar Siaga",
                    fontSize = 11.sp,
                    color = DrgTextSecondary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
