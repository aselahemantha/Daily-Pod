package com.example.dailypod.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dailypod.data.dao.HabitDao
import com.example.dailypod.data.entity.HabitRecordEntity
import com.example.dailypod.data.entity.HabitTemplateEntity

@Database(
    entities = [HabitTemplateEntity::class, HabitRecordEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        @Volatile
        private var INSTANCE: HabitDatabase? = null

        fun getDatabase(context: Context): HabitDatabase =
            INSTANCE ?: synchronized(this) {
                val instance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            HabitDatabase::class.java,
                            "habit_database",
                        ).build()
                INSTANCE = instance
                instance
            }
    }
}
