package com.example.core.repository

import androidx.room.withTransaction
import com.example.core.database.AppDatabase
import com.example.shared.models.DriverMember
import com.example.shared.models.PoskoCheckInEntity
import com.example.shared.models.PoskoLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PoskoCheckInRepository(private val db: AppDatabase) {

    val allCheckIns: Flow<List<PoskoCheckInEntity>> =
        db.poskoCheckInDao().getAllCheckIns()

    fun getMemberCheckIns(memberId: String): Flow<List<PoskoCheckInEntity>> =
        db.poskoCheckInDao().getCheckInsByMember(memberId)

    suspend fun recordCheckIn(
        member: DriverMember,
        posko: PoskoLocation,
        pointsAwarded: Int = 15,
        method: String = "GEOFENCE_AUTO"
    ): Result<PoskoCheckInEntity> = withContext(Dispatchers.IO) {
        val checkIn = PoskoCheckInEntity(
            id = "CHK-${System.currentTimeMillis()}-${member.id.takeLast(4)}",
            poskoId = posko.id,
            poskoName = posko.name,
            memberId = member.id,
            memberName = member.name,
            driverPlate = member.motorcyclePlate,
            checkInTimestamp = System.currentTimeMillis(),
            pointsAwarded = pointsAwarded,
            checkInMethod = method
        )

        db.withTransaction {
            db.poskoCheckInDao().insertCheckIn(checkIn)
            db.memberDao().addLoyaltyPoints(member.id, pointsAwarded)
        }

        Result.success(checkIn)
    }

    suspend fun getCheckInCount(memberId: String): Int = withContext(Dispatchers.IO) {
        db.poskoCheckInDao().getCheckInCountForMember(memberId)
    }
}
