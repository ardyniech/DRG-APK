package com.example.modules.treasury.primitives

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.TransactionType
import com.example.ui.theme.*

@Composable
fun KasTransactionStatusBadge(
    type: TransactionType,
    modifier: Modifier = Modifier
) {
    val isIncome = type == TransactionType.INCOME
    val badgeBg = if (isIncome) DrgGreenContainer else Color(0xFFFFEBEE)
    val badgeFg = if (isIncome) DrgGreenPrimary else DrgRedDanger
    val icon = if (isIncome) Icons.Default.TrendingUp else Icons.Default.TrendingDown
    val label = if (isIncome) "PEMASUKAN" else "PENGELUARAN"

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = badgeBg,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = badgeFg,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = badgeFg
            )
        }
    }
}
