package com.example.modules.treasury

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.RoleManager
import com.example.modules.treasury.primitives.KasBalanceCard
import com.example.shared.atoms.EmptyStateOrganicGraphic
import com.example.shared.models.AccessLevel
import com.example.shared.models.DriverMember
import com.example.shared.models.KasTransaction
import com.example.shared.models.TransactionType
import com.example.ui.theme.*

@Composable
fun TreasuryScreen(
    currentMember: DriverMember?,
    transactions: List<KasTransaction>,
    onAddTransactionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalBalance = transactions.sumOf { if (it.type == TransactionType.INCOME) it.amount else -it.amount }
    val userRole = currentMember?.role?.let { RoleManager.getUserRoleForMemberRole(it) }
    val isTreasurerOrAdmin = userRole != null && (userRole.accessLevel == AccessLevel.ADMIN || userRole.accessLevel == AccessLevel.PENGURUS)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DrgBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        // Balance Header Card
        item {
            KasBalanceCard(totalBalance = totalBalance)
        }

        if (isTreasurerOrAdmin) {
            item {
                Button(
                    onClick = onAddTransactionClick,
                    colors = ButtonDefaults.buttonColors(containerColor = DrgGreenPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah", tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Catat Transaksi Kas Baru (Pengurus)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        item {
            TreasuryHealthCard()
        }

        item {
            Text(
                text = "Riwayat Mutasi Kas Komunitas",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DrgTextPrimary
            )
        }

        if (transactions.isEmpty()) {
            item {
                EmptyStateOrganicGraphic(
                    title = "Belum Ada Mutasi Kas",
                    message = "Seluruh catatan iuran kas anggota dan pengeluaran darurat akan tercatat secara transparan di sini."
                )
            }
        } else {
            items(transactions) { tx ->
                KasTransactionCard(transaction = tx)
            }
        }
    }
}
