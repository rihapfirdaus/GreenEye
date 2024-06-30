package com.rose.greeneye.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "history", indices = [Index(value = ["science_name"], unique = true)])
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo("science_name") val scienceName: String,
    @ColumnInfo("en_name") val enName: String,
    @ColumnInfo("id_name") val idName: String,
    @ColumnInfo("created_at") val createdAt: Long,
    val index: Int,
    val imageUrl: String,
    val benefits: String,
    val effects: String,
    val tips: String,
) : Parcelable
