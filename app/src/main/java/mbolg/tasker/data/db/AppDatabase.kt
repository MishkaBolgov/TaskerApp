package mbolg.tasker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mbolg.tasker.data.model.LocalTask
import mbolg.tasker.data.model.SyncTask

@Database(entities = [SyncTask::class, LocalTask::class], version = 1)
@TypeConverters(TaskConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): SyncTaskDao
    abstract fun localTaskDao(): LocalTaskDao
}