package com.example.shared.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Shared icon box dipakai semua tile/card agar tidak duplikat.
 * Kanonis: 1 tempat, semua QuickTile/StatCard/LauncherTile wajib pakai ini.
 *
 * @param icon gambar ikon.
 * @param tint warna ikon & wash background.
 * @param size ukuran box (default 44dp).
 * @param iconSize ukuran ikon di dalam box.
 * @param modifier modifier tambahan.
 */
@Composable
fun DrgIconTileBox(
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    iconSize: Dp = 24.dp,
    contentDescription: String? = null
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(14.dp))
            .background(tint.copy(alpha = 0.14f))
            .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = contentDescription, tint = tint, modifier = Modifier.size(iconSize))
    }
}
