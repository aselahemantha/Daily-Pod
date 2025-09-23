package com.example.dailypod.data.entity

data class HabitProgressEntity(
    val currentStreak: Int,
    val longestStreak: Int,
    val totalCompletions: Int,
    val completionRate: Int,
    val weeklyProgress: Int,
)
