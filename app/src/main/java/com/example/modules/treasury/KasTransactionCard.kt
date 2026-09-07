package com.example.modules.treasury

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.models.KasTransaction
import com.example.shared.models.TransactionType
import com.example.ui.theme.*

@Composable
fun KasTransactionCard(transaction: KasTransaction) {
    val isIncome = transaction.type == TransactionType.INCOME
    val context = androidx.compose.ui.platform.LocalContext.current
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = DrgSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DrgOutline.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = DrgTextPrimary
                )
                Text(
                    text = "${transaction.dateString} • ${transaction.recordedBy}",
                    fontSize = 11.sp,
                    color = DrgTextSecondary
                )
                Text(
                    text = "Kategori: ${transaction.category.label}",
                    fontSize = 10.sp,
                    color = DrgTextMuted
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${if (isIncome) "+" else "-"}Rp ${java.text.NumberFormat.getInstance().format(transaction.amount)}",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = if (isIncome) DrgGreenPrimary else DrgRedDanger
                )
                IconButton(
                    onClick = {
                        val receiptText = """
                            ===================================
                               KUITANSI DIGITAL RESMI DRG
                            ===================================
                            ID Transaksi : ${transaction.id}
                            Waktu        : ${transaction.dateString}
                            Judul        : ${transaction.title}
                            Nominal      : Rp ${java.text.NumberFormat.getInstance().format(transaction.amount)}
                            Tipe         : ${if (isIncome) "PEMASUKAN" else "PENGELUARAN"}
                            Kategori     : ${transaction.category.label}
                            Pencatat     : ${transaction.recordedBy}
                            Status       : SAH & TRANSPARAN ✅
                            ===================================
                            *Kuitansi ini diterbitkan secara otomatis dan sah oleh sistem keuangan terbuka DRG Malang Raya*
                        """.trimIndent()
                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(receiptText))
                        android.widget.Toast.makeText(context, "Bukti kuitansi disalin!", android.widget.Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = "Salin Kuitansi",
                        tint = DrgGreenPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
