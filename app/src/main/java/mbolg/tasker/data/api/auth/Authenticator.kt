package mbolg.tasker.data.api.auth


interface Authenticator {
    fun signUp(credentials: Credentials, listener: AuthListener)
    fun signIn(credentials: Credentials, listener: AuthListener)
    fun signOut()
    fun isUserSignedIn(): Boolean
    fun getToken(): String
}

interface AuthListener{
    fun signInSuccess()
    fun signInFailed()
    fun signUpSuccess()
    fun signUpFailed()
}