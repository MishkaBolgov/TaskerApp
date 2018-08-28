package mbolg.tasker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

@Entity
class LocalTask(): Task() {

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0

    constructor(task: SyncTask) : this() {
        title = task.title
        text = task.text
        status = task.status
        created = task.created
        edited = task.edited
        serverId = task.id
        existsOnServer = true
    }

    constructor(title: String, text: String) : this(){
        this.title = title
        this.text = text
        saveEditTime()
    }


    var existsOnServer = false
    var serverId: Int = 0
}
