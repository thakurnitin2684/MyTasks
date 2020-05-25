package com.thakurnitin2684.mytasks

import android.app.Application
import android.content.ContentValues
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "MyTasksViewModel"

class MyTasksViewModel(application: Application) : AndroidViewModel(application) {
    private val contentObserver = object : ContentObserver(Handler()) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            loadTask()
        }
    }
    private val databaseCursor = MutableLiveData<Cursor>()
    val cursor: LiveData<Cursor>
        get() = databaseCursor
    init {
        Log.d(TAG, "TaskTimerViewModel: created")
        getApplication<Application>().contentResolver.registerContentObserver(
            TasksTable.CONTENT_URI,
            true,
            contentObserver
        )
        loadTask()
    }
    private fun loadTask() {
        val projection = arrayOf(
            TasksTable.Columns.ID,
            TasksTable.Columns.TASK_NAME,
            TasksTable.Columns.TASK_DESCRIPTION,
            TasksTable.Columns.TASK_TIME
        )
                GlobalScope.launch {
            val cursor = getApplication<Application>().contentResolver.query(
                TasksTable.CONTENT_URI,
                projection, null, null,null
            )
            databaseCursor.postValue(cursor)
        }
    }
    fun saveTask(task: Task): Task {
        val values = ContentValues()
        if (task.name.isNotEmpty()) {
            values.put(TasksTable.Columns.TASK_NAME, task.name)
            values.put(TasksTable.Columns.TASK_DESCRIPTION, task.description)
            values.put(TasksTable.Columns.TASK_TIME, task.time)
            if (task.id == 0L) {
                GlobalScope.launch {
                    val uri = getApplication<Application>().contentResolver?.insert(
                        TasksTable.CONTENT_URI,
                        values
                    )
                    if (uri != null) {
                        task.id = TasksTable.getId(uri)
                    }
                }
            } else {
                GlobalScope.launch {
                    getApplication<Application>().contentResolver?.update(
                        TasksTable.buildUriFromId(
                            task.id
                        ), values, null, null
                    )
                }
            }
//           Log.d(TAG,"Task saved : ${task.name}")
        }
        return task
    }
    fun deleteTask(taskId: Long) {
        GlobalScope.launch {
            getApplication<Application>().contentResolver?.delete(
                TasksTable.buildUriFromId(
                    taskId
                ), null, null
            )
        }
    }
    override fun onCleared() {
        getApplication<Application>().contentResolver.unregisterContentObserver(contentObserver)
    }
}