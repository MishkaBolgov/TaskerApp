package mbolg.tasker.di.module

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import mbolg.tasker.data.api.auth.Authenticator
import mbolg.tasker.data.api.auth.SimpleAuthenticator
import mbolg.tasker.di.AuthSharedPrefs
import mbolg.tasker.di.AuthSharedPrefsName

@Module
class AuthModule {
    @Provides
    fun provideAuthenticator(authenticator: SimpleAuthenticator): Authenticator = authenticator


    @Provides
    @AuthSharedPrefs
    fun provideAuthPrefs(context: Context, @AuthSharedPrefsName name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)


    @Provides
    @AuthSharedPrefsName
    fun provideAuthPrefsName(): String = "auth_prefs"
}