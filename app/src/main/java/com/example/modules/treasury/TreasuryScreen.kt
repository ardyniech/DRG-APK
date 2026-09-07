package com.example.modules.treasury

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.RoleManager
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
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = DrgGreenDark,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DrgGreenPrimary, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = "Kas",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "KAS TRANSPARAN KOMUNITAS DRG",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Rp ${java.text.NumberFormat.getInstance().format(totalBalance)}",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Laporan Keuangan Real-Time Terbuka Bagi Seluruh Anggota.",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 11.sp
                    )
                }
            }
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

        items(transactions) { tx ->
            KasTransactionCard(transaction = tx)
        }
    }
}
