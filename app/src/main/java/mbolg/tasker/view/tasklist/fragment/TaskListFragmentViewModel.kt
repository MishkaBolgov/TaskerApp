package mbolg.tasker.view.tasklist.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import mbolg.tasker.data.datamanager.DataManager
import mbolg.tasker.data.model.LocalTask
import mbolg.tasker.data.model.SyncTask
import mbolg.tasker.data.model.Task

abstract class TaskListFragmentViewModel : ViewModel() {
    lateinit var dataManager: DataManager

    fun getSyncTaskList(): LiveData<List<SyncTask>> = dataManager.getTaskListByType(getTaskType())
    fun getLocalTaskList(): LiveData<List<LocalTask>> = dataManager.getLocalTaskListByType(getTaskType())


    abstract fun getTaskType(): Task.TaskType

    fun updateTaskType(task: Task, type: Task.TaskType) = dataManager.updateTaskType(task, type)

}