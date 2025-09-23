package com.example.dailypod.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dailypod.data.entity.HabitRecordEntity
import com.example.dailypod.data.entity.HabitTemplateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<HabitTemplateEntity>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: String): HabitTemplateEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertHabit(habitTemplateEntity: HabitTemplateEntity)

    @Update
    suspend fun updateHabit(habitTemplateEntity: HabitTemplateEntity)

    @Delete
    suspend fun deleteHabit(habitTemplateEntity: HabitTemplateEntity)

    @Query("SELECT * FROM habit_entries WHERE habitId = :habitId ORDER BY date DESC")
    fun getEntriesByHabitId(habitId: String): Flow<List<HabitRecordEntity>>

    @Query("SELECT * FROM habit_entries WHERE habitId = :habitId ORDER BY date DESC")
    suspend fun getEntriesByHabitIdSync(habitId: String): List<HabitRecordEntity>

    @Query("SELECT * FROM habit_entries WHERE habitId = :habitId AND date = :date")
    suspend fun getEntryByHabitIdAndDate(
        habitId: String,
        date: String,
    ): HabitRecordEntity?

    @Query("SELECT * FROM habit_entries WHERE date = :date")
    fun getTodayEntries(date: String): Flow<List<HabitRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertEntry(entry: HabitRecordEntity)

    @Update
    suspend fun updateEntry(entry: HabitRecordEntity)

    @Delete
    suspend fun deleteEntry(entry: HabitRecordEntity)

    @Query("DELETE FROM habit_entries WHERE habitId = :habitId")
    suspend fun deleteEntriesByHabitId(habitId: String)
}
