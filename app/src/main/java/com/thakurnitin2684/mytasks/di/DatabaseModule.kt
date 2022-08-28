package com.thakurnitin2684.mytasks.di

import com.thakurnitin2684.mytasks.data.db.TaskDatabase

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
        fun providesTaskDatabase(@ApplicationContext context: Context): TaskDatabase {
        return Room.databaseBuilder(context, TaskDatabase::class.java, TaskDatabase.DB_NAME).build()
    }

    @Singleton
    @Provides
    fun providesTaskDao(taskDatabase: TaskDatabase) = taskDatabase.getTaskDao()
}