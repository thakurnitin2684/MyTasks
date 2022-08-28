package com.thakurnitin2684.mytasks.data.repository

import com.thakurnitin2684.mytasks.data.db.TaskDao
import com.thakurnitin2684.mytasks.data.db.entity.Task
import javax.inject.Inject


class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {

    override fun getAllTasks() = taskDao.getAllTasks()

    override suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    override suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    override suspend fun deleteTaskById(id: Int) = taskDao.deleteTaskById(id)

    override suspend fun clearTask() = taskDao.clearTask()
}
