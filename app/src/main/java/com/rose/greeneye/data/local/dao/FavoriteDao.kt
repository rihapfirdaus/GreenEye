package com.rose.greeneye.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rose.greeneye.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite ORDER BY created_at DESC")
    suspend fun getAllFavorite(): List<FavoriteEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite WHERE science_name = :scienceName LIMIT 1)")
    suspend fun isFavorite(scienceName: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavorite(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favorite WHERE science_name = :scienceName")
    suspend fun deleteFavorite(scienceName: String)
}
