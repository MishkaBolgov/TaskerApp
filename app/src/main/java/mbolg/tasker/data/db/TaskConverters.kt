package mbolg.tasker.data.db

import androidx.room.TypeConverter
import mbolg.tasker.data.model.SyncTask
import mbolg.tasker.data.model.Task
import org.joda.time.Instant

class TaskConverters {
    @TypeConverter
    fun fromTaskTypeToString(syncTaskType: Task.TaskType): String = syncTaskType.name

    @TypeConverter
    fun fromStringToTaskType(string: String): Task.TaskType = Task.TaskType.valueOf(string)

    @TypeConverter
    fun fromInstantToLong(instant: Instant): Long {
        return instant.millis
    }

    @TypeConverter
    fun fromLongToInstant(millis: Long): Instant {
        return Instant(millis)
    }
}