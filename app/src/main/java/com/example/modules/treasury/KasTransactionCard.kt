package com.example.modules.treasury

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.modules.treasury.primitives.KasTransactionStatusBadge
import com.example.shared.atoms.DrgLuminousGlassCard
import com.example.shared.atoms.LocalAppVisualTheme
import com.example.shared.models.KasTransaction
import com.example.shared.models.TransactionType
import com.example.ui.theme.DrgRedDanger
import com.example.ui.theme.DrgTextMuted
import com.example.ui.theme.DrgTextPrimary
import com.example.ui.theme.DrgTextSecondary
import com.example.ui.theme.SpaceGroteskFontFamily
import java.text.NumberFormat

@Composable
fun KasTransactionCard(
    transaction: KasTransaction,
    modifier: Modifier = Modifier
) {
    val isIncome = transaction.type == TransactionType.INCOME
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val theme = LocalAppVisualTheme.current
    val accent = if (isIncome) theme.primaryColor else DrgRedDanger

    DrgLuminousGlassCard(
        shape = RoundedCornerShape(18.dp),
        backgroundColor = theme.glassBg,
        borderHighlightColor = accent,
        contentPadding = 14.dp,
        modifier = modifier.fillMaxWidth().testTag("kas_transaction_card")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                    fontFamily = SpaceGroteskFontFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = accent
                )
                IconButton(
                    onClick = {
                        val text = "KUITANSI DRG [${transaction.id}]\n${transaction.title} : Rp ${NumberFormat.getInstance().format(transaction.amount)} (${if (isIncome) "MASUK" else "KELUAR"})"
                        clipboardManager.setText(AnnotatedString(text))
                        Toast.makeText(context, "Kuitansi disalin!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = "Salin", tint = accent, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
