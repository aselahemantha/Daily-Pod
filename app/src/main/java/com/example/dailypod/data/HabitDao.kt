package com.example.dailypod.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: String): Habit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habit_entries WHERE habitId = :habitId ORDER BY date DESC")
    fun getEntriesByHabitId(habitId: String): Flow<List<HabitEntry>>

    @Query("SELECT * FROM habit_entries WHERE habitId = :habitId ORDER BY date DESC")
    suspend fun getEntriesByHabitIdSync(habitId: String): List<HabitEntry>

    @Query("SELECT * FROM habit_entries WHERE habitId = :habitId AND date = :date")
    suspend fun getEntryByHabitIdAndDate(habitId: String, date: String): HabitEntry?

    @Query("SELECT * FROM habit_entries WHERE date = :date")
    fun getTodayEntries(date: String): Flow<List<HabitEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: HabitEntry)

    @Update
    suspend fun updateEntry(entry: HabitEntry)

    @Delete
    suspend fun deleteEntry(entry: HabitEntry)

    @Query("DELETE FROM habit_entries WHERE habitId = :habitId")
    suspend fun deleteEntriesByHabitId(habitId: String)
}
