package com.example.core.repository

import androidx.room.withTransaction
import com.example.core.database.AppDatabase
import com.example.shared.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MemberRepository(private val db: AppDatabase) {
    val allMembers: Flow<List<DriverMember>> = db.memberDao().getAllMembers()

    fun getReviewsForDriver(driverId: String): Flow<List<DriverReview>> =
        db.reviewDao().getReviewsForDriver(driverId)

    suspend fun getMemberCount(): Int = withContext(Dispatchers.IO) {
        db.memberDao().getCount()
    }

    suspend fun updateScreening(memberId: String, status: VerificationStatus, notes: String) = withContext(Dispatchers.IO) {
        db.memberDao().updateScreeningStatus(memberId, status, notes)
    }

    suspend fun updateMember(member: DriverMember) = withContext(Dispatchers.IO) {
        db.memberDao().updateMember(member)
    }

    suspend fun updateMemberLocation(id: String, lat: Double, lng: Double, status: String = "Online") = withContext(Dispatchers.IO) {
        db.memberDao().updateLocationAndStatus(id, lat, lng, status)
    }

    suspend fun registerMember(member: DriverMember) = withContext(Dispatchers.IO) {
        db.memberDao().insertMember(member)
    }

    suspend fun checkInKopdar(eventId: String, memberId: String, points: Int) = withContext(Dispatchers.IO) {
        db.withTransaction {
            db.attendanceDao().checkIn(eventId)
            db.memberDao().addLoyaltyPoints(memberId, points)
        }
    }

    suspend fun addReview(review: DriverReview) = withContext(Dispatchers.IO) {
        db.reviewDao().insertReview(review)
    }
}
