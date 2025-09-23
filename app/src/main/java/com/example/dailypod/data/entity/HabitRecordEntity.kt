package com.example.dailypod.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "habit_entries")
@Parcelize
data class HabitRecordEntity(
    @PrimaryKey val id: String,
    val habitId: String,
    val date: String,
    val count: Int,
    val completed: Boolean,
    val createdAt: Long,
) : Parcelable
