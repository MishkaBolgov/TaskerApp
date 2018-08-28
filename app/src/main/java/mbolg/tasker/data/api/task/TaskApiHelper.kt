package mbolg.tasker.data.api.task

import mbolg.tasker.data.model.LocalTask
import mbolg.tasker.data.model.SyncTask
import mbolg.tasker.data.model.Task

interface TaskApiHelper {
    fun getTaskList(): List<SyncTask>

    fun createTaskOnServer(task: LocalTask): SyncTask?
    fun updateTaskOnServer(task: LocalTask): SyncTask?
}