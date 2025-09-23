package com.example.dailypod.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "habits")
@Parcelize
data class Habit(
    @PrimaryKey val id: String,
    val name: String,
    val description: String? = null,
    val color: String,
    val icon: String,
    val targetFrequency: TargetFrequency,
    val targetCount: Int,
    val createdAt: Long,
    val updatedAt: Long
) : Parcelable

@Entity(tableName = "habit_entries")
@Parcelize
data class HabitEntry(
    @PrimaryKey val id: String,
    val habitId: String,
    val date: String, // YYYY-MM-DD format
    val count: Int,
    val completed: Boolean,
    val createdAt: Long
) : Parcelable

@Parcelize
enum class TargetFrequency : Parcelable {
    DAILY, WEEKLY
}

data class HabitStats(
    val currentStreak: Int,
    val longestStreak: Int,
    val totalCompletions: Int,
    val completionRate: Int,
    val weeklyProgress: Int
)
