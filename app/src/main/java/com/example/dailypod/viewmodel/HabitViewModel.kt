package com.example.dailypod.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailypod.data.Habit
import com.example.dailypod.data.HabitEntry
import com.example.dailypod.data.HabitStats
import com.example.dailypod.data.TargetFrequency
import com.example.dailypod.repository.HabitRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HabitViewModel(private val repository: HabitRepository) : ViewModel() {

    val habits: StateFlow<List<Habit>> = repository.getAllHabits()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val todayEntries: StateFlow<List<HabitEntry>> = repository.getTodayEntries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addHabit(
        name: String,
        description: String?,
        color: String,
        icon: String,
        targetFrequency: TargetFrequency,
        targetCount: Int
    ) {
        viewModelScope.launch {
            repository.addHabit(name, description, color, icon, targetFrequency, targetCount)
        }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            repository.updateHabit(habit)
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }

    fun toggleHabitCompletion(habitId: String) {
        viewModelScope.launch {
            repository.toggleHabitCompletion(habitId)
        }
    }

    suspend fun getHabitStats(habitId: String): HabitStats {
        return repository.getHabitStats(habitId)
    }

    fun isHabitCompletedToday(habitId: String): Boolean {
        return todayEntries.value.any { it.habitId == habitId && it.completed }
    }
}
