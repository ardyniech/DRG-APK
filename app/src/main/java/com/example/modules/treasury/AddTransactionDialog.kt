package com.example.modules.treasury

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shared.atoms.DrgLuminousGlassModal
import com.example.shared.atoms.LocalAppVisualTheme
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
    var validationError by remember { mutableStateOf<String?>(null) }
    val theme = LocalAppVisualTheme.current
    val accent = if (txType == TransactionType.INCOME) theme.primaryColor else DrgRedDanger

    DrgLuminousGlassModal(
        title = "Catat Transaksi Kas DRG",
        subtitle = "Pembukuan transparan real-time untuk seluruh anggota",
        icon = Icons.Default.AccountBalanceWallet,
        accentColor = accent,
        validationError = validationError,
        onDismiss = onDismiss,
        footer = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Batal") }
                Button(
                    onClick = {
                        val amount = amountStr.toLongOrNull() ?: 0L
                        when {
                            title.isBlank() -> validationError = "Judul transaksi wajib diisi."
                            amount <= 0L -> validationError = "Nominal transaksi harus lebih dari Rp 0."
                            else -> { onConfirm(title.trim(), amount, txType, category, description.trim()); onDismiss() }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = accent),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).testTag("btn_submit_kas")
                ) { Text("Simpan Kas", color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = txType == TransactionType.INCOME, onClick = { txType = TransactionType.INCOME },
                label = { Text("Pemasukan (+)") }, modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = theme.primaryColor, selectedLabelColor = Color.White)
            )
            FilterChip(
                selected = txType == TransactionType.EXPENSE, onClick = { txType = TransactionType.EXPENSE },
                label = { Text("Pengeluaran (-)") }, modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DrgRedDanger, selectedLabelColor = Color.White)
            )
        }
        Text("Kategori Transaksi:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DrgTextSecondary)
        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            KasCategory.entries.forEach { cat ->
                FilterChip(
                    selected = category == cat, onClick = { category = cat },
                    label = { Text(cat.label, fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = accent, selectedLabelColor = Color.White)
                )
            }
        }
        OutlinedTextField(
            value = title, onValueChange = { title = it; validationError = null },
            label = { Text("Judul Transaksi", fontSize = 11.sp) }, placeholder = { Text("Contoh: Iuran Kas 10 Anggota", fontSize = 11.sp) },
            modifier = Modifier.fillMaxWidth().testTag("input_kas_title"), singleLine = true, shape = RoundedCornerShape(12.dp)
        )
        OutlinedTextField(
            value = amountStr, onValueChange = { amountStr = it.filter { c -> c.isDigit() }; validationError = null },
            label = { Text("Nominal (Rp)", fontSize = 11.sp) }, placeholder = { Text("Contoh: 200000", fontSize = 11.sp) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().testTag("input_kas_amount"), singleLine = true, shape = RoundedCornerShape(12.dp),
            textStyle = LocalTextStyle.current.copy(fontFamily = SpaceGroteskFontFamily)
        )
        OutlinedTextField(
            value = description, onValueChange = { description = it },
            label = { Text("Catatan / Keterangan", fontSize = 11.sp) }, placeholder = { Text("Keterangan pembukuan kas...", fontSize = 11.sp) },
            modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp)
        )
    }
}
