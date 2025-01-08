package com.example.kalogatia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.kalogatia.data.entities.HistorySet
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorySetDao {
    @Upsert
    suspend fun upsertHistorySet(historySet: HistorySet)

    @Delete
    suspend fun deleteHistorySet(historySet: HistorySet)

    @Query("SELECT * FROM historySet")
    fun selectAllHistorySet(): Flow<List<HistorySet>>
}