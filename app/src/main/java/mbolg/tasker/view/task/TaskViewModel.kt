package mbolg.tasker.view.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mbolg.tasker.data.datamanager.DataManager
import mbolg.tasker.data.model.LocalTask
import mbolg.tasker.data.model.SyncTask
import mbolg.tasker.data.model.Task
import javax.inject.Inject

class TaskViewModel(val dataManager: DataManager) : ViewModel() {
    lateinit var task: Task

    fun getLocalTask(taskId: Long): LiveData<LocalTask> = dataManager.findLocalTaskById(taskId)
    fun getSyncTask(taskId: Long): LiveData<SyncTask> = dataManager.findSyncTaskById(taskId)

    fun saveTask(title: String, text: String) {
        dataManager.saveTask(task, title, text)
    }
}

class TaskViewModelFactory @Inject constructor(val dataManager: DataManager) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TaskViewModel(dataManager) as T
    }
}