package com.example.modules.devtools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Unified Frosted Glass Scaffold shared across all 18 Super-Admin DevTools audit tabs.
 */
@Composable
fun DevTabShell(
    headerContent: (@Composable () -> Unit)?,
    headerIcon: ImageVector?,
    headerTitle: String,
    headerDesc: String,
    modifier: Modifier = Modifier,
    headerBackground: Color = devGlassBg(),
    headerBorderColor: Color = devGlassBorder(),
    titleColor: Color = DrgTextDark,
    headerExtra: (@Composable () -> Unit)? = null,
    headerTrailing: (@Composable RowScope.() -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {
    val cardShape = RoundedCornerShape(22.dp)
    val glassBg = Brush.verticalGradient(listOf(headerBackground.copy(alpha = 0.92f), headerBackground.copy(alpha = 0.76f)))
    val glassRim = Brush.linearGradient(listOf(Color.White, headerBorderColor.copy(alpha = 0.55f)))

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 104.dp)
    ) {
        if (headerContent != null) item { headerContent() }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, cardShape, ambientColor = headerBorderColor.copy(alpha = 0.25f))
                    .clip(cardShape)
                    .background(glassBg)
                    .border(1.2.dp, glassRim, cardShape)
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                            if (headerIcon != null) Icon(headerIcon, contentDescription = null, tint = devAccent())
                            Text(headerTitle, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = titleColor)
                        }
                        if (headerTrailing != null) headerTrailing()
                    }
                    Text(headerDesc, fontSize = 11.sp, color = DrgTextSecondary)
                    if (headerExtra != null) headerExtra()
                }
            }
        }
        content()
    }
}

@Composable
fun DevMetricRow(label: String, value: String, valueColor: Color = DrgTextDark) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 11.sp, color = DrgTextSecondary)
        Text(value, fontFamily = SpaceGroteskFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}
