package mbolg.tasker.di.component

import android.content.Context
import dagger.Component
import mbolg.tasker.TaskerApplication
import mbolg.tasker.data.datamanager.DataManager
import mbolg.tasker.di.module.ApplicationModule
import mbolg.tasker.di.module.AuthModule
import mbolg.tasker.recognizer.SpeechRecognizer
import mbolg.tasker.recognizer.SpeechRecorder
import mbolg.tasker.view.auth.AuthActivity
import mbolg.tasker.vocalizer.Vocalizer
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, AuthModule::class])
interface ApplicationComponent {
    fun inject(application: TaskerApplication)
    fun getDataManager(): DataManager
    fun getRetrofit(): Retrofit
    fun getContext(): Context
    fun getVocalizer(): Vocalizer
    fun getSpeechRecorder(): SpeechRecorder
    fun getSpeechRecognizer(): SpeechRecognizer
}