package com.example.modules.profile.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun CommunityActivityPointsList(
    activities: List<ActivityPointSource>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Sumber Poin Kegiatan Komunitas",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = DrgTextSecondary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            activities.take(2).forEach { activity ->
                ActivityPointTile(activity = activity, modifier = Modifier.weight(1f))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            activities.drop(2).take(2).forEach { activity ->
                ActivityPointTile(activity = activity, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ActivityPointTile(
    activity: ActivityPointSource,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        modifier = modifier.border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(DrgGreenContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(text = activity.iconEmoji, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = activity.title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgTextPrimary
                )
                Text(
                    text = activity.rateDesc,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgGreenPrimary
                )
            }
        }
    }
}
