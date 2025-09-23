package com.example.dailypod.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dailypod.data.TargetFrequency
import kotlinx.parcelize.Parcelize

@Entity(tableName = "habits")
@Parcelize
data class HabitTemplateEntity(
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
