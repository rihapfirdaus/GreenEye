package com.rose.greeneye.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rose.greeneye.data.local.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY created_at DESC")
    suspend fun getAllHistory(): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToHistory(historyEntity: HistoryEntity)

    @Query("DELETE FROM history")
    suspend fun deleteAllHistory()
}
