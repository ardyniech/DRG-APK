package com.example.shared.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "point_transactions")
data class PointTransaction(
    @PrimaryKey val id: String,
    val targetMemberId: String,
    val targetMemberName: String,
    val giverMemberId: String,
    val giverMemberName: String,
    val giverRole: MemberRole,
    val weightPoints: Int, // Dewan Etika=5, Ketua=4, Pengurus=3, Beneficiary/Victim=2, Anggota=1
    val isBeneficiaryDirect: Boolean = false,
    val reason: String,
    val timestamp: Long = System.currentTimeMillis()
)
