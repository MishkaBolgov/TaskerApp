package mbolg.tasker.data.datamanager

import androidx.lifecycle.LiveData
import mbolg.tasker.data.api.auth.Authenticator
import mbolg.tasker.data.model.LocalTask
import mbolg.tasker.data.model.SyncTask
import mbolg.tasker.data.model.Task

interface DataManager {
    fun getTaskList(): LiveData<List<SyncTask>>
    fun getTaskListByType(type: Task.TaskType): LiveData<List<SyncTask>>
    fun getLocalTaskListByType(type: Task.TaskType): LiveData<List<LocalTask>>

    fun addTask(syncTask: SyncTask): Long

    fun findSyncTaskById(taskId: Long): LiveData<SyncTask>
    fun findLocalTaskById(taskId: Long): LiveData<LocalTask>

    fun createLocalTask(title: String = "", text: String = ""): Long

    fun syncLocalDatabaseWithServer()

    fun updateTaskType(task: Task, type: Task.TaskType)

    fun startSync()
    fun stopSync()

    fun saveTask(task: Task, title: String = task.title, text: String = task.text)

    fun clearDatabase()
}