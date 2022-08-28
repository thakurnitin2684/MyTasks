package com.thakurnitin2684.mytasks.data.db

import com.thakurnitin2684.mytasks.data.db.entity.Task


import androidx.room.Database
import androidx.room.RoomDatabase

private const val TAG = "AppDatabase"
private const val DATABASE_NAME = "mytasks.db"
private const val DATABASE_VERSION = 1

@Database(
    entities = [Task::class],
    version = DATABASE_VERSION,
    exportSchema = false
)

abstract class TaskDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao

    companion object {
        const val DB_NAME = DATABASE_NAME
    }
}