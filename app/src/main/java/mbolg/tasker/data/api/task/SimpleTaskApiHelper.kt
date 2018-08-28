package mbolg.tasker.data.api.task

import mbolg.tasker.data.api.auth.Authenticator
import mbolg.tasker.data.model.LocalTask
import mbolg.tasker.data.model.SyncTask
import mbolg.tasker.data.model.Task
import mbolg.tasker.utils.log
import retrofit2.Response
import retrofit2.Retrofit
import java.net.ConnectException
import javax.inject.Inject

class SimpleTaskApiHelper @Inject constructor(val retrofit: Retrofit, val authenticator: Authenticator) : TaskApiHelper {


    override fun getTaskList(): List<SyncTask> {
        try {
            val token = authenticator.getToken()
            val request = retrofit.create(Api::class.java)
            val response = request.getTaskList(token).execute()
            return response.body() ?: ArrayList()
        } catch (ex: ConnectException) {
        }
        return ArrayList()
    }

    override fun createTaskOnServer(task: LocalTask): SyncTask? {
        try {
            val token = authenticator.getToken()
            val request = retrofit.create(Api::class.java)
            val responseBody = request.createTask(token, CreateTaskRequest(task.title, task.text)).execute().body()
            if (responseBody != null) {
                when (task.getStatus()) {
                    Task.TaskType.ARCHIVED -> {
                        responseBody.setStatus(task.getStatus())
                        return updateTaskOnServer(LocalTask(responseBody))
                    }
                    Task.TaskType.DONE -> {
                        responseBody.setStatus(task.getStatus())
                        return updateTaskOnServer(LocalTask(responseBody))
                    }
                }
            }
            return responseBody
        } catch (ex: ConnectException) {
            return null
        }
    }

    override fun updateTaskOnServer(task: LocalTask): SyncTask? {
        try {
            val token = authenticator.getToken()
            val testRequest = retrofit.create(Api::class.java)
            val testTask = testRequest.getTask(token, task.serverId).execute().body()

            if (testTask != null) {
                if (testTask.getStatus() == Task.TaskType.ARCHIVED && task.getStatus() == Task.TaskType.DONE) {
                    val request = retrofit.create(Api::class.java)
                    val updateRequest = UpdateTaskRequest(task.serverId, task.title, task.text, "active")
                    request.updateTask(token, task.serverId, updateRequest).execute()
                }
            }
            val request = retrofit.create(Api::class.java)
            val updateRequest = UpdateTaskRequest(task.serverId, task.title, task.text, task.status)
            var response: Response<SyncTask>? = null
            response = request.updateTask(token, task.serverId, updateRequest).execute()

            return response?.body()
        } catch (ex: ConnectException) {
            return null
        }
    }

}