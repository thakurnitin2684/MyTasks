package com.thakurnitin2684.mytasks.di


import com.thakurnitin2684.mytasks.data.db.TaskDao
import com.thakurnitin2684.mytasks.data.repository.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
object RepositoryModule {

    @Provides
    fun providesTaskRepository(taskDao: TaskDao) = TaskRepositoryImpl(taskDao)
}