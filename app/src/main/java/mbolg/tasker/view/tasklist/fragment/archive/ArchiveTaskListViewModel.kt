package mbolg.tasker.view.tasklist.fragment.archive

import mbolg.tasker.data.model.Task
import mbolg.tasker.view.tasklist.fragment.TaskListFragmentViewModel

class ArchiveTaskListViewModel: TaskListFragmentViewModel() {
    override fun getTaskType(): Task.TaskType = Task.TaskType.ARCHIVED
}