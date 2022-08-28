package com.thakurnitin2684.mytasks.data.repository


import androidx.lifecycle.LiveData
import com.thakurnitin2684.mytasks.data.db.entity.Task


interface TaskRepository {
    fun getAllTasks(): List<Task>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteTaskById(id: Int)
    suspend fun clearTask()
}