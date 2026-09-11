package com.example.modules.auth

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.abs

@Composable
fun LandingFeatureAutoScrollRow(
    modifier: Modifier = Modifier
) {
    val features = remember { AppFeatureRepository.allFeatures }
    val pagerState = rememberPagerState(pageCount = { features.size })

    LaunchedEffect(pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            while (isActive) {
                delay(3200)
                if (!pagerState.isScrollInProgress) {
                    val next = (pagerState.currentPage + 1) % features.size
                    pagerState.animateScrollToPage(
                        page = next,
                        animationSpec = tween(durationMillis = 1600, easing = FastOutSlowInEasing)
                    )
                }
            }
        }
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "Features", tint = DrgGrabGreenPrimary, modifier = Modifier.size(16.dp))
                Text(text = "Fitur Unggulan Komunitas", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DrgTextDark)
            }
            Text(text = "${pagerState.currentPage + 1} / ${features.size}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DrgGrabGreenPrimary)
        }

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 20.dp),
            pageSpacing = 14.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).coerceIn(-1f, 1f)
            val absOffset = abs(pageOffset)
            val scale = 0.94f + (1f - absOffset) * 0.06f
            val alpha = 0.70f + (1f - absOffset) * 0.30f

            LandingFeatureThumbnailCard(
                feature = features[page],
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            features.indices.forEach { idx ->
                val isSelected = pagerState.currentPage == idx
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (isSelected) 18.dp else 6.dp, 6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(if (isSelected) DrgGrabGreenPrimary else DrgBorderLight)
                )
            }
        }
    }
}
