package mbolg.tasker.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import mbolg.tasker.utils.log
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Instant
import org.joda.time.format.DateTimeFormat

abstract class Task : Comparable<Task> {

    abstract var id: Int

    @SerializedName("title")
    @Expose
    var title: String = ""

    @SerializedName("text")
    @Expose
    var text: String = ""

    @SerializedName("created")
    @Expose
    var created: String = ""

    @SerializedName("edited")
    @Expose
    var edited: String = ""

    @SerializedName("user_email")
    @Expose
    var userEmail: String = ""

    @SerializedName("status")
    @Expose
    var status: String = "active"


    fun saveEditTime() {
        val time = DateTime.now(DateTimeZone.getDefault())
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
        edited = time.toString(formatter)

    }

    fun getStatus(): TaskType = when (status) {
        "archived" -> TaskType.ARCHIVED
        "active" -> TaskType.ACTIVE
        "done" -> TaskType.DONE
        else -> TaskType.ACTIVE
    }

    fun setStatus(taskType: TaskType) {
        status = taskType.toString()
    }


    enum class TaskType {
        ARCHIVED, ACTIVE, DONE;

        override fun toString() = when (this) {
            ARCHIVED -> "archived"
            ACTIVE -> "active"
            DONE -> "done"
        }
    }

    override fun compareTo(other: Task): Int {
        if (other.title == this.title
                && other.text == this.text
                && other.status == this.status
                && other.created == this.created)
            return 0

        return 1
    }

    fun getFormatUpdatedAt(): String {
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
        val date = DateTime.parse(edited, formatter)
        return date.toString("HH:mm  |  dd MMMM")
    }

    fun getEditedMillis(): Long{
        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
        val date = DateTime.parse(edited, formatter)
        return date.millis
    }

    override fun toString(): String {
        return "Task(id=$id, title='$title', text='$text', created='$created', edited='$edited', userEmail='$userEmail', status='$status')"
    }


}
