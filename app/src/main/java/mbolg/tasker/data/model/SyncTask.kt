package mbolg.tasker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

@Entity
class SyncTask : Task() {

    @SerializedName("id")
    @Expose
    @PrimaryKey()
    override var id: Int = 0

}