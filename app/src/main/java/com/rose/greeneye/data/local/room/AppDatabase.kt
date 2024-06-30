package com.rose.greeneye.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rose.greeneye.data.local.dao.FavoriteDao
import com.rose.greeneye.data.local.dao.HistoryDao
import com.rose.greeneye.data.local.entity.FavoriteEntity
import com.rose.greeneye.data.local.entity.HistoryEntity

@Database(
    entities = [FavoriteEntity::class, HistoryEntity::class],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    abstract fun historyDao(): HistoryDao
}
