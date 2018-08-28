package mbolg.tasker.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import mbolg.tasker.data.model.LocalTask
import mbolg.tasker.data.model.Task

@Dao
interface LocalTaskDao {
    @Query("SELECT * FROM localtask")
    fun allLive(): LiveData<List<LocalTask>>

    @Query("SELECT * FROM localtask")
    fun allRaw(): List<LocalTask>



    @Query("SELECT * FROM localtask WHERE status=:type")
    fun findAllByType(type: String): LiveData<List<LocalTask>>

    @Insert
    fun insert(task: LocalTask): Long

    @Insert
    fun insertAll(task: List<LocalTask>)

    @Query("SELECT * FROM localtask WHERE id=:taskId")
    fun findTaskById(taskId: Long): LiveData<LocalTask>

    @Update
    fun update(task: LocalTask)

    @Query("DELETE FROM localtask")
    fun deleteAll()

    @Delete
    fun delete(localTask: LocalTask)

    @Query("SELECT * FROM localtask WHERE id=:id")
    fun getTaskById(id: Long): LocalTask?
}