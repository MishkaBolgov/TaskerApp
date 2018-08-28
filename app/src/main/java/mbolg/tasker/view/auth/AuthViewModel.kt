package mbolg.tasker.view.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mbolg.tasker.data.api.auth.AuthListener
import mbolg.tasker.data.api.auth.Authenticator
import mbolg.tasker.data.api.auth.Credentials
import mbolg.tasker.data.datamanager.DataManager
import javax.inject.Inject

class AuthViewModel @Inject constructor(val authenticator: Authenticator, val dataManager: DataManager) : ViewModel() {
    fun signIn(credentials: Credentials, listener: AuthListener) {
        authenticator.signIn(credentials, listener)
    }

    fun signUp(credentials: Credentials, listener: AuthListener) {
        authenticator.signUp(credentials, listener)
    }

    fun isAuthed(): Boolean = authenticator.isUserSignedIn()
}

class AuthViewModelFactory @Inject constructor(val authenticator: Authenticator, val dataManager: DataManager) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(authenticator, dataManager) as T
    }
}