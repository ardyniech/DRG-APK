package com.example.modules.treasury

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.shared.models.KasCategory
import com.example.shared.models.TransactionType
import com.example.ui.theme.*

@Composable
fun AddTransactionDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Long, TransactionType, KasCategory, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amountStr by remember { mutableStateOf("") }
    var txType by remember { mutableStateOf(TransactionType.INCOME) }
    var category by remember { mutableStateOf(KasCategory.IURAN_BULANAN) }
    var description by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DrgSurface,
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Catat Transaksi Kas DRG",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DrgTextPrimary
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    FilterChip(
                        selected = txType == TransactionType.INCOME,
                        onClick = { txType = TransactionType.INCOME },
                        label = { Text("Pemasukan (+)") },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgGreenPrimary, selectedLabelColor = Color.White)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = txType == TransactionType.EXPENSE,
                        onClick = { txType = TransactionType.EXPENSE },
                        label = { Text("Pengeluaran (-)") },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgRedDanger, selectedLabelColor = Color.White)
                    )
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Transaksi", fontSize = 11.sp) },
                    placeholder = { Text("Contoh: Iuran Kas 10 Anggota", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it.filter { char -> char.isDigit() } },
                    label = { Text("Nominal (Rp)", fontSize = 11.sp) },
                    placeholder = { Text("Contoh: 200000", fontSize = 11.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Catatan / Keterangan", fontSize = 11.sp) },
                    placeholder = { Text("Keterangan pembukuan kas...", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Batal")
                    }
                    Button(
                        onClick = {
                            val amount = amountStr.toLongOrNull() ?: 0L
                            if (title.isNotBlank() && amount > 0) {
                                onConfirm(title, amount, txType, category, description)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Simpan Kas", color = Color.White)
                    }
                }
            }
        }
    }
}
