package mbolg.tasker.data.api.auth

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase.deleteDatabase
import android.os.AsyncTask
import com.google.gson.GsonBuilder
import mbolg.tasker.di.AuthSharedPrefs
import mbolg.tasker.utils.log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class SimpleAuthenticator @Inject constructor(val retrofit: Retrofit,
                                              @AuthSharedPrefs val authSharedPrefs: SharedPreferences,
                                              val context: Context) : Authenticator {

    private val TOKEN_KEY = "token"

    override fun signUp(credentials: Credentials, listener: AuthListener) {
        object : AsyncTask<Void, Void, String?>() {
            override fun doInBackground(vararg p0: Void?): String? {
                val api = retrofit.create(AuthApi::class.java)

                val createUser = api.createUser(AuthRequest(credentials.email, credentials.password))

                val token = createUser.execute().body()?.token

                return token
            }

            override fun onPostExecute(token: String?) {
                super.onPostExecute(token)
                if (token != null) {
                    saveToken(token)
                    listener.signInSuccess()
                } else listener.signInFailed()
            }
        }.execute()
    }

    override fun signOut() {
        authSharedPrefs.edit().remove(TOKEN_KEY).commit()
        context.deleteDatabase("database")
    }

    override fun signIn(credentials: Credentials, listener: AuthListener) {
        object : AsyncTask<Void, Void, String?>() {
            override fun doInBackground(vararg p0: Void?): String? {
                val api = retrofit.create(AuthApi::class.java)
                val authRequest = AuthRequest(credentials.email, credentials.password)

                val getToken = api.getToken(authRequest)

                val token = getToken.execute().body()?.token
                return token
            }

            override fun onPostExecute(token: String?) {
                super.onPostExecute(token)
                if (token != null) {
                    saveToken(token)
                    listener.signInSuccess()
                } else listener.signInFailed()
            }
        }.execute()

    }


    override fun isUserSignedIn() = authSharedPrefs.contains(TOKEN_KEY)

    override fun getToken(): String = authSharedPrefs.getString(TOKEN_KEY, "")

    private fun saveToken(token: String) = authSharedPrefs.edit().putString(TOKEN_KEY, token).commit()

}

