package com.example.modules.treasury.primitives

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.ui.theme.DrgRedPanic
import com.example.ui.theme.SpaceGroteskFontFamily
import java.text.NumberFormat

@Composable
fun KasSummaryHeader(
    totalIncome: Long,
    totalExpense: Long,
    netBalance: Long,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppVisualTheme.current
    val gradient = Brush.linearGradient(listOf(theme.secondaryColor, theme.primaryColor))

    DrgLuminousGlassCard(
        modifier = modifier.fillMaxWidth().testTag("kas_balance_summary"),
        shape = RoundedCornerShape(24.dp),
        backgroundColor = Color.Transparent,
        borderHighlightColor = theme.glassBorderColor,
        contentPadding = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(gradient)
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Kas", tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("KAS TRANSPARAN DRG MALANG RAYA", color = Color.White.copy(alpha = 0.92f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Color.White.copy(alpha = 0.2f)).padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Transparan", tint = Color.White, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Terverifikasi", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = "Rp ${NumberFormat.getInstance().format(netBalance)}",
                fontFamily = SpaceGroteskFontFamily,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.18f)).padding(horizontal = 10.dp, vertical = 6.dp)) {
                    Text("↗ Masuk: Rp ${NumberFormat.getInstance().format(totalIncome)}", fontFamily = SpaceGroteskFontFamily, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(DrgRedPanic.copy(alpha = 0.28f)).padding(horizontal = 10.dp, vertical = 6.dp)) {
                    Text("↘ Keluar: Rp ${NumberFormat.getInstance().format(totalExpense)}", fontFamily = SpaceGroteskFontFamily, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
