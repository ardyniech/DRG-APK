package com.example.modules.services

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ProtectionHeaderCard(modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = DrgRedContainer.copy(alpha = 0.6f),
        modifier = modifier.fillMaxWidth().padding(bottom = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Proteksi",
                tint = DrgRedDanger,
                modifier = Modifier.size(28.dp)
            )
            Column {
                Text(
                    text = "Dana Talangan & DRG Protection",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DrgRedDanger
                )
                Text(
                    text = "Perlindungan darurat dari iuran kas kolektif driver.",
                    fontSize = 11.sp,
                    color = DrgTextPrimary
                )
            }
        }
    }
}
