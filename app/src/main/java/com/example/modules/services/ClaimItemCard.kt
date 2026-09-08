package com.example.modules.services

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.InsuranceClaimItem
import com.example.ui.theme.*
import java.text.NumberFormat

@Composable
fun ClaimItemCard(
    claim: InsuranceClaimItem,
    onClaim: (InsuranceClaimItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DrgSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = claim.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DrgTextPrimary
                )
                Text(
                    text = "Maksimal Perbaikan: Rp ${NumberFormat.getInstance().format(claim.maxCoverageRp)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgRedDanger
                )
                Text(
                    text = "${claim.category} • ${claim.status}",
                    fontSize = 10.sp,
                    color = DrgTextMuted
                )
            }
            OutlinedButton(
                onClick = { onClaim(claim) },
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, DrgRedDanger),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(text = "Klaim", fontSize = 11.sp, color = DrgRedDanger, fontWeight = FontWeight.Bold)
            }
        }
    }
}
