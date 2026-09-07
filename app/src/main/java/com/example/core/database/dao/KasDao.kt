package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.KasTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface KasDao {
    @Query("SELECT * FROM kas_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<KasTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: KasTransaction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<KasTransaction>)

    @Query("DELETE FROM kas_transactions WHERE id = :id")
    suspend fun deleteTransaction(id: String)
}
