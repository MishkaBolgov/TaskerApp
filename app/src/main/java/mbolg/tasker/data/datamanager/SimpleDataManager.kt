package mbolg.tasker.data.datamanager

import android.content.Context
import android.os.AsyncTask
import android.os.Handler
import androidx.lifecycle.LiveData
import mbolg.tasker.data.db.SyncTaskDao
import mbolg.tasker.data.api.task.TaskApiHelper
import mbolg.tasker.data.db.AppDatabase
import mbolg.tasker.data.db.LocalTaskDao
import mbolg.tasker.data.model.*
import mbolg.tasker.utils.NetworkUtils
import javax.inject.Inject

class SimpleDataManager @Inject constructor(val taskDao: SyncTaskDao,
                                            val localTaskDao: LocalTaskDao,
                                            val taskApiHelper: TaskApiHelper,
                                            val database: AppDatabase,
                                            val context: Context) : DataManager {

    val SYNC_INTERVAL = 2000L

    val handler = Handler()

    val syncronizeTasks = object : Runnable {
        override fun run() {
            syncLocalDatabaseWithServer()
            handler.postDelayed(this, SYNC_INTERVAL)
        }
    }

    override fun findSyncTaskById(taskId: Long): LiveData<SyncTask> = taskDao.findTaskById(taskId)
    override fun findLocalTaskById(taskId: Long): LiveData<LocalTask> = localTaskDao.findTaskById(taskId)

    override fun getTaskListByType(type: Task.TaskType): LiveData<List<SyncTask>> = taskDao.findAllByType(type.toString())
    override fun getLocalTaskListByType(type: Task.TaskType): LiveData<List<LocalTask>> = localTaskDao.findAllByType(type.toString())

    override fun getTaskList(): LiveData<List<SyncTask>> = taskDao.allLive()

    override fun addTask(syncTask: SyncTask) = taskDao.insert(syncTask)

    private fun downloadTasksFromServer() {
        if (NetworkUtils.isOnline(context)) {
            val serverTasks = taskApiHelper.getTaskList()

            if (areLocalTasksOutdated(serverTasks)) {
                taskDao.deleteAll()
                taskDao.insertAll(serverTasks)
            }
        }
    }

    private fun areLocalTasksOutdated(serverTasks: List<SyncTask>): Boolean {
        val oldTasks = taskDao.allRaw()

        if (oldTasks.size != serverTasks.size)
            return true

        for (task in serverTasks) {
            val oldTask = oldTasks.find { it.id == task.id } ?: return true

            if (task.compareTo(oldTask) != 0)
                return true
        }

        return false
    }

    private fun uploadLocalTasksOnServer() {
            if (NetworkUtils.isOnline(context)) {
                val localTasks = localTaskDao.allRaw()

                for (localTask in localTasks) {
                    if (NetworkUtils.isOffline(context))
                        break
                    if (localTask.existsOnServer) {
                        val task = taskApiHelper.updateTaskOnServer(localTask)
                        task?.let {
                            localTaskDao.delete(localTask)
                        }

                    } else {
                        val task = taskApiHelper.createTaskOnServer(localTask)
                        task?.let {
                            localTaskDao.delete(localTask)
                        }
                    }
                }
            }

    }

    override fun syncLocalDatabaseWithServer() = AsyncTask.execute {
        uploadLocalTasksOnServer()
        downloadTasksFromServer()
    }

    override fun createLocalTask(title: String, text: String): Long {
        val newTask = LocalTask(title, text)
        return localTaskDao.insert(newTask)
    }


    override fun updateTaskType(task: Task, type: Task.TaskType) {

        task.setStatus(type)
        task.saveEditTime()

        when (task) {
            is SyncTask -> {
                if (NetworkUtils.isOnline(context)) {
                    AsyncTask.execute {
                        val localTask = LocalTask(task)
                        localTaskDao.update(localTask)
                        taskDao.delete(task)
                        taskApiHelper.updateTaskOnServer(localTask)
                    }
                } else {
//                    создаем измененный локал таск
                    localTaskDao.insert(LocalTask(task))
//                    удаляем синк таск
                    taskDao.delete(task)
                }
            }
            is LocalTask -> {
                localTaskDao.update(task)
            }
        }
    }

    override fun saveTask(task: Task, title: String, text: String) {


        task.title = title
        task.text = text

        task.saveEditTime()

        when (task) {
            is LocalTask -> localTaskDao.update(task)
            is SyncTask -> taskDao.update(task)
        }

        when (task) {
            is SyncTask -> {
                val newLocalTask = LocalTask(task)
                saveTask(newLocalTask)
            }
            is LocalTask -> {
                if (NetworkUtils.isOnline(context))
                    AsyncTask.execute {
                        if (task.existsOnServer) {
//                            Если задача существует на сервере - обновляем ее
                            val updatedSyncTask = taskApiHelper.updateTaskOnServer(task)
                            localTaskDao.update(task)

                        } else {
//                            Если задача только что создана и ранее не выгружалась на сервер - создаем ее на сервере
//                            и сохраняем id под которым она хранится на сервере
                            val createdSyncTask = taskApiHelper.createTaskOnServer(task)
                            createdSyncTask?.let {
                                task.serverId = createdSyncTask.id
                                task.existsOnServer = true
                                localTaskDao.update(task)
                            }
                        }
                    }
                else {

                    if (localTaskDao.getTaskById(task.id.toLong()) != null) {
                        localTaskDao.update(task)
                    } else {
                        taskDao.deleteById(task.serverId)
                        localTaskDao.insert(task)
                    }
                }
            }
        }
    }


    override fun startSync() {
        syncronizeTasks.run()
    }

    override fun stopSync() {
        handler.removeCallbacks(syncronizeTasks)
    }

    override fun clearDatabase() {
        database.clearAllTables()
    }
}
