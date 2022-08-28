package com.thakurnitin2684.mytasks.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.thakurnitin2684.mytasks.data.db.entity.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //if some data is same/conflict, it'll be replace with new data.
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): List<Task>
    // why not use suspend ? because Room does not support LiveData with suspended functions.
    // LiveData already works on a background thread and should be used directly without using coroutines

    @Query("DELETE FROM tasks")
    suspend fun clearTask()

    @Query("DELETE FROM tasks WHERE task_id = :id") //you can use this too, for delete task by id.
    suspend fun deleteTaskById(id: Int)
}