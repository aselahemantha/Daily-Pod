package com.example.dailypod.repository

import com.example.dailypod.data.Habit
import com.example.dailypod.data.HabitDao
import com.example.dailypod.data.HabitEntry
import com.example.dailypod.data.HabitStats
import com.example.dailypod.data.TargetFrequency
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class HabitRepository(private val habitDao: HabitDao) {

    fun getAllHabits(): Flow<List<Habit>> = habitDao.getAllHabits()

    suspend fun getHabitById(id: String): Habit? = habitDao.getHabitById(id)

    suspend fun addHabit(
        name: String,
        description: String?,
        color: String,
        icon: String,
        targetFrequency: TargetFrequency,
        targetCount: Int
    ) {
        val habit = Habit(
            id = UUID.randomUUID().toString(),
            name = name,
            description = description,
            color = color,
            icon = icon,
            targetFrequency = targetFrequency,
            targetCount = targetCount,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        habitDao.insertHabit(habit)
    }

    suspend fun updateHabit(habit: Habit) {
        val updatedHabit = habit.copy(updatedAt = System.currentTimeMillis())
        habitDao.updateHabit(updatedHabit)
    }

    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit)
        habitDao.deleteEntriesByHabitId(habit.id)
    }

    fun getTodayEntries(): Flow<List<HabitEntry>> {
        val today = getCurrentDateString()
        return habitDao.getTodayEntries(today)
    }

    suspend fun toggleHabitCompletion(habitId: String, date: String = getCurrentDateString()) {
        val existingEntry = habitDao.getEntryByHabitIdAndDate(habitId, date)
        
        if (existingEntry != null) {
            val updatedEntry = existingEntry.copy(
                completed = !existingEntry.completed,
                count = if (!existingEntry.completed) 1 else 0
            )
            habitDao.updateEntry(updatedEntry)
        } else {
            val newEntry = HabitEntry(
                id = UUID.randomUUID().toString(),
                habitId = habitId,
                date = date,
                count = 1,
                completed = true,
                createdAt = System.currentTimeMillis()
            )
            habitDao.insertEntry(newEntry)
        }
    }

    suspend fun getHabitStats(habitId: String): HabitStats {
        // Get all entries for this habit synchronously
        val allEntries = habitDao.getEntriesByHabitIdSync(habitId)
        val habitEntries = allEntries.filter { it.completed }
            .sortedBy { it.date }

        if (habitEntries.isEmpty()) {
            return HabitStats(0, 0, 0, 0, 0)
        }

        // Calculate current streak
        var currentStreak = 0
        val today = getCurrentDateString()
        var checkDate = parseDateString(today)
        
        while (true) {
            val dateStr = formatDate(checkDate)
            val entry = habitEntries.find { it.date == dateStr }
            
            if (entry?.completed == true) {
                currentStreak++
                checkDate = Date(checkDate.time - 86400000) // Subtract 1 day
            } else {
                break
            }
        }

        // Calculate longest streak
        var longestStreak = 0
        var tempStreak = 0
        
        for (i in habitEntries.indices) {
            if (i == 0 || 
                parseDateString(habitEntries[i].date).time == 
                parseDateString(habitEntries[i-1].date).time + 86400000) {
                tempStreak++
                longestStreak = maxOf(longestStreak, tempStreak)
            } else {
                tempStreak = 1
            }
        }

        val totalCompletions = habitEntries.size
        
        // Calculate weekly progress (last 7 days)
        val weekAgo = Date(System.currentTimeMillis() - 7 * 86400000)
        val weeklyEntries = habitEntries.filter { 
            parseDateString(it.date) >= weekAgo 
        }
        val weeklyProgress = ((weeklyEntries.size.toFloat() / 7) * 100).toInt()

        // Calculate completion rate (last 30 days)
        val monthAgo = Date(System.currentTimeMillis() - 30 * 86400000)
        val monthlyEntries = habitEntries.filter { 
            parseDateString(it.date) >= monthAgo 
        }
        val completionRate = ((monthlyEntries.size.toFloat() / 30) * 100).toInt()

        return HabitStats(
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            totalCompletions = totalCompletions,
            completionRate = completionRate,
            weeklyProgress = weeklyProgress
        )
    }

    private fun getCurrentDateString(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(Date())
    }

    private fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(date)
    }

    private fun parseDateString(dateString: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.parse(dateString) ?: Date()
    }
}
