package com.example.modules.services

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun MarketHeaderCard(modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DrgAmberContainer,
        modifier = modifier.fillMaxWidth().padding(bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = "Koperasi",
                tint = DrgAmberDark,
                modifier = Modifier.size(28.dp)
            )
            Column {
                Text(
                    text = "Koperasi & Atribut DRG",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DrgAmberDark
                )
                Text(
                    text = "Harga khusus driver bersubsidi dari Kas Komunitas.",
                    fontSize = 11.sp,
                    color = DrgTextPrimary
                )
            }
        }
    }
}
