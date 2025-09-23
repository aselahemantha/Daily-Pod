package com.example.dailypod.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailypod.data.entity.HabitProgressEntity
import com.example.dailypod.data.entity.HabitRecordEntity
import com.example.dailypod.data.entity.HabitTemplateEntity
import com.example.dailypod.data.enums.TargetFrequency
import com.example.dailypod.repository.HabitRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HabitViewModel(
    private val repository: HabitRepository,
) : ViewModel() {
    val habits: StateFlow<List<HabitTemplateEntity>> =
        repository
            .getAllHabits()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

    val todayEntries: StateFlow<List<HabitRecordEntity>> =
        repository
            .getTodayEntries()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList(),
            )

    fun addHabit(
        name: String,
        description: String?,
        color: String,
        icon: String,
        targetFrequency: TargetFrequency,
        targetCount: Int,
    ) {
        viewModelScope.launch {
            repository.addHabit(name, description, color, icon, targetFrequency, targetCount)
        }
    }

    fun updateHabit(habitTemplateEntity: HabitTemplateEntity) {
        viewModelScope.launch {
            repository.updateHabit(habitTemplateEntity)
        }
    }

    fun deleteHabit(habitTemplateEntity: HabitTemplateEntity) {
        viewModelScope.launch {
            repository.deleteHabit(habitTemplateEntity)
        }
    }

    fun toggleHabitCompletion(habitId: String) {
        viewModelScope.launch {
            repository.toggleHabitCompletion(habitId)
        }
    }

    suspend fun getHabitStats(habitId: String): HabitProgressEntity = repository.getHabitStats(habitId)

    fun isHabitCompletedToday(habitId: String): Boolean = todayEntries.value.any { it.habitId == habitId && it.completed }
}
