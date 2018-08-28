package mbolg.tasker.view.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mbolg.tasker.data.api.auth.Authenticator
import mbolg.tasker.data.datamanager.DataManager
import javax.inject.Inject

class TaskListViewModel(val dataManager: DataManager, val authenticator: Authenticator) : ViewModel() {
    fun signOut() {
        dataManager.clearDatabase()
        authenticator.signOut()
    }

    fun startSync() {
        dataManager.startSync()
    }

    fun stopSync() {
        dataManager.stopSync()
    }
}

class TaskListViewModelFactory @Inject constructor(val dataManager: DataManager, val authenticator: Authenticator) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TaskListViewModel(dataManager, authenticator) as T
    }
}