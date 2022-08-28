package com.thakurnitin2684.mytasks.ui.viewmodel



import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.thakurnitin2684.mytasks.data.db.entity.Task
import com.thakurnitin2684.mytasks.data.repository.TaskRepositoryImpl
import com.thakurnitin2684.mytasks.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val TAG = "TasksViewModel"

@HiltViewModel
class TasksViewModel @Inject constructor(private val repository: TaskRepositoryImpl) : ViewModel() {

    private val mytasks = MutableLiveData<Resource<List<Task>>>()

    suspend fun insertTask(task: Task) = repository.insertTask(task)

    suspend fun updateTask(task: Task) = repository.updateTask(task)

    suspend fun deleteTask(task: Task) = repository.deleteTask(task)

    suspend fun deleteTaskById(id: Int) = repository.deleteTaskById(id)

    suspend fun clearTask() = repository.clearTask()

    fun fetchAllTasks() {
        mytasks.postValue(Resource.loading(null))
        GlobalScope.launch {
            repository.getAllTasks().also {
                mytasks.postValue(Resource.success(it))

            }
        }
    }
    fun getAllTasks(): LiveData<Resource<List<Task>>> {
        return mytasks
    }

}
