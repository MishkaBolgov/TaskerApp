package mbolg.tasker.view.tasklist.fragment.active

import androidx.lifecycle.LiveData
import mbolg.tasker.data.model.SyncTask
import mbolg.tasker.data.model.Task
import mbolg.tasker.view.tasklist.fragment.TaskListFragmentViewModel

class ActiveTaskListViewModel: TaskListFragmentViewModel() {
    override fun getTaskType(): Task.TaskType = Task.TaskType.ACTIVE

    fun stopSync() {
        dataManager.stopSync()
    }

    fun createLocalTask() = dataManager.createLocalTask()
}