package com.example.modules.devtools

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DrgGreenPrimary
import com.example.ui.theme.DrgOutline
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary

/**
 * Scaffold kanonis semua Dev*Tab — hilangkan copy-paste LazyColumn + header.
 * Kanonis: 1 tempat; tab hanya membawa scanner + items() unik.
 * Jaga kompatibilitas: 17 layout lama (kondisional warna/badge/refresh/filter)
 * diekspresikan lewat param bg/border/title/trailing/extra tanpa ubah visual inti.
 */
@Composable
fun DevTabShell(
    headerContent: (@Composable () -> Unit)?,
    headerIcon: ImageVector?,
    headerTitle: String,
    headerDesc: String,
    modifier: Modifier = Modifier,
    headerBackground: Color = Color.White,
    headerBorderColor: Color = DrgOutline.copy(alpha = 0.5f),
    titleColor: Color = DrgTextPrimary,
    headerExtra: (@Composable () -> Unit)? = null,
    headerTrailing: (@Composable RowScope.() -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        if (headerContent != null) item { headerContent() }
        item {
            Surface(
                shape = RoundedCornerShape(12.dp), color = headerBackground,
                modifier = Modifier.fillMaxWidth().border(1.dp, headerBorderColor, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                            if (headerIcon != null) Icon(headerIcon, contentDescription = null, tint = DrgGreenPrimary)
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

/** Baris metrik label-nilai dukuan untuk semua tab DevTools. */
@Composable
fun DevMetricRow(label: String, value: String, valueColor: Color = DrgTextPrimary) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 11.sp, color = DrgTextSecondary)
        Text(value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}
