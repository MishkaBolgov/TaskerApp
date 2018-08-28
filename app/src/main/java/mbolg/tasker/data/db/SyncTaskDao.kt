package mbolg.tasker.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import mbolg.tasker.data.model.SyncTask
import mbolg.tasker.data.model.Task

@Dao
interface SyncTaskDao {
    @Query("SELECT * FROM synctask")
    fun allLive(): LiveData<List<SyncTask>>

    @Query("SELECT * FROM synctask")
    fun allRaw(): List<SyncTask>


    @Query("SELECT * FROM synctask WHERE status=:type")
    fun findAllByType(type: String): LiveData<List<SyncTask>>

    @Insert
    fun insert(syncTask: SyncTask): Long

    @Insert
    fun insertAll(syncTask: List<SyncTask>)

    @Query("SELECT * FROM synctask WHERE id=:taskId")
    fun findTaskById(taskId: Long): LiveData<SyncTask>

    @Update
    fun update(syncTask: SyncTask)

    @Query("DELETE FROM synctask")
    fun deleteAll()


    @Query("DELETE FROM synctask WHERE id=:id")
    fun deleteById(id: Int)

    @Delete
    fun delete(task: SyncTask)

}