package mbolg.tasker.data.api.task

import retrofit2.Call
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import mbolg.tasker.data.model.SyncTask
import okhttp3.ResponseBody
import retrofit2.http.*


interface Api {
    @GET("/todo/api/tasks/")
    fun getTaskList(@Header("x-auth-token") token: String): Call<List<SyncTask>>

    @GET("/todo/api/tasks/{task_id}/")
    fun getTask(@Header("x-auth-token") token: String,
                @Path("task_id") task_id: Int): Call<SyncTask>

    @POST("/todo/api/tasks/")
    fun createTask(@Header("x-auth-token") token: String, @Body body: CreateTaskRequest): Call<SyncTask>

    @PATCH("/todo/api/tasks/{task_id}/")
    fun updateTask(@Header("x-auth-token") token: String,
                   @Path("task_id") task_id: Int,
                   @Body updateTaskRequest: UpdateTaskRequest): Call<SyncTask>
}

class UpdateTaskRequest(val task_id: Int, val title: String, val text: String, val status: String) {
    override fun toString(): String {
        return "UpdateTaskRequest(task_id=$task_id, title='$title', text='$text', status='$status')"
    }
}

class CreateTaskRequest(val title: String, val text: String)

class TaskListRequest(val text: String)

class TaskResponse {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("created")
    @Expose
    var created: String? = null
    @SerializedName("edited")
    @Expose
    var edited: String? = null
    @SerializedName("user_email")
    @Expose
    var userEmail: String? = null
    @SerializedName("status")
    @Expose
    var status: String? = null

    override fun toString(): String {
        return "TaskResponse#$id: $title|$text"
    }

}