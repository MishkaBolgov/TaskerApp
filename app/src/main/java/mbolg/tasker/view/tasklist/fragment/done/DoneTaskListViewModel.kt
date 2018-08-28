package mbolg.tasker.view.tasklist.fragment.done

import androidx.lifecycle.LiveData
import mbolg.tasker.data.model.SyncTask
import mbolg.tasker.data.model.Task
import mbolg.tasker.view.tasklist.fragment.TaskListFragmentViewModel

class DoneTaskListViewModel: TaskListFragmentViewModel() {
    override fun getTaskType(): Task.TaskType = Task.TaskType.DONE

}