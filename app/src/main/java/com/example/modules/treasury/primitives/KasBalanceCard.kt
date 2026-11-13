package com.example.modules.treasury.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.SpaceGroteskFontFamily
import java.text.NumberFormat

@Composable
fun KasBalanceCard(
    totalBalance: Long,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    val shape = RoundedCornerShape(22.dp)
    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = shape,
        backgroundColor = Color.Transparent,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(Brush.linearGradient(listOf(theme.secondaryColor, theme.primaryColor)))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Kas",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "KAS TRANSPARAN KOMUNITAS DRG",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Rp ${NumberFormat.getInstance().format(totalBalance)}",
                fontFamily = SpaceGroteskFontFamily,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Ringkasan saldo dari transaksi yang tercatat di aplikasi (lokal-first).",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp
            )
        }
    }
}
