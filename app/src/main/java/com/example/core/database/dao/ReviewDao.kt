package com.example.core.database.dao

import androidx.room.*
import com.example.shared.models.DriverReview
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("SELECT * FROM driver_reviews WHERE targetDriverId = :driverId ORDER BY timestamp DESC")
    fun getReviewsForDriver(driverId: String): Flow<List<DriverReview>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<DriverReview>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: DriverReview)
}
