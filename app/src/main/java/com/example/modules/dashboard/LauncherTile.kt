package com.example.modules.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgIconTileBox
import com.example.shared.utils.bouncyClick
import com.example.ui.theme.DrgTextPrimary

/**
 * Single launcher tile — ikon via DrgIconTileBox (kanonis, ganti duplikat Box manual).
 */
@Composable
internal fun LauncherTile(
    app: DesktopAppItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .bouncyClick { onClick() }
            .padding(4.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            DrgIconTileBox(
                icon = app.icon, tint = app.color,
                size = 46.dp, iconSize = 24.dp, contentDescription = app.title,
                modifier = Modifier
                    .shadow(3.dp, RoundedCornerShape(14.dp), ambientColor = app.color.copy(alpha = 0.3f))
            )
            if (app.isHot) {
                Box(
                    modifier = Modifier
                        .offset(x = 2.dp, y = (-2).dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(app.color)
                        .border(1.5.dp, Color.White, CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = app.title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
            color = DrgTextPrimary, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis
        )
    }
}
