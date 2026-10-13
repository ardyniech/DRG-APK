package com.example.modules.treasury

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.treasury.primitives.KasTransactionStatusBadge
import com.example.shared.models.KasTransaction
import com.example.shared.models.TransactionType
import com.example.ui.theme.*
import java.text.NumberFormat

@Composable
fun KasTransactionCard(
    transaction: KasTransaction,
    modifier: Modifier = Modifier
) {
    val isIncome = transaction.type == TransactionType.INCOME
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val cardShape = RoundedCornerShape(18.dp)
    val highlightBrush = Brush.linearGradient(
        listOf(Color.White, Color.White.copy(alpha = 0.5f), if (isIncome) DrgGreenPrimary.copy(alpha = 0.2f) else DrgRedDanger.copy(alpha = 0.2f))
    )

    Surface(
        shape = cardShape,
        color = Color.White.copy(alpha = 0.85f),
        tonalElevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .shadow(3.dp, cardShape, ambientColor = Color(0x12003617), spotColor = Color(0x0A003617))
            .border(1.2.dp, highlightBrush, cardShape)
            .testTag("kas_transaction_card")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    KasTransactionStatusBadge(type = transaction.type)
                    Text(text = transaction.category.label, fontSize = 10.sp, color = DrgTextMuted, fontWeight = FontWeight.Medium)
                }
                Text(text = transaction.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DrgTextPrimary)
                Text(text = "${transaction.dateString} • ${transaction.recordedBy}", fontSize = 11.sp, color = DrgTextSecondary)
            }

            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "${if (isIncome) "+" else "-"}Rp ${NumberFormat.getInstance().format(transaction.amount)}",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = if (isIncome) DrgGreenPrimary else DrgRedDanger
                )
                IconButton(
                    onClick = {
                        val text = "KUITANSI DRG [${transaction.id}]\n${transaction.title} : Rp ${NumberFormat.getInstance().format(transaction.amount)} (${if (isIncome) "MASUK" else "KELUAR"})"
                        clipboardManager.setText(AnnotatedString(text))
                        Toast.makeText(context, "Kuitansi disalin!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = "Salin", tint = DrgGreenPrimary, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
