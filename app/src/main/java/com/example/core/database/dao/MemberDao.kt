package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.DriverMember
import com.example.shared.models.VerificationStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members ORDER BY name ASC")
    fun getAllMembers(): Flow<List<DriverMember>>

    @Query("SELECT COUNT(*) FROM members")
    suspend fun getCount(): Int

    @Query("SELECT * FROM members WHERE verificationStatus = :status")
    fun getMembersByStatus(status: VerificationStatus): Flow<List<DriverMember>>

    @Query("SELECT * FROM members WHERE id = :id")
    suspend fun getMemberById(id: String): DriverMember?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(members: List<DriverMember>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: DriverMember)

    @Update
    suspend fun updateMember(member: DriverMember)

    @Query("UPDATE members SET verificationStatus = :status, screeningNotes = :notes WHERE id = :id")
    suspend fun updateScreeningStatus(id: String, status: VerificationStatus, notes: String)

    @Query("UPDATE members SET currentLat = :lat, currentLng = :lng, currentStatus = :status WHERE id = :id")
    suspend fun updateLocationAndStatus(id: String, lat: Double, lng: Double, status: String)

    @Query("UPDATE members SET loyaltyPoints = loyaltyPoints + :points WHERE id = :id")
    suspend fun addLoyaltyPoints(id: String, points: Int)
}
