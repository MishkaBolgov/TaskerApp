package mbolg.tasker.di.module

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import mbolg.tasker.data.api.task.SimpleTaskApiHelper
import mbolg.tasker.data.api.task.TaskApiHelper
import mbolg.tasker.data.datamanager.DataManager
import mbolg.tasker.data.datamanager.SimpleDataManager
import mbolg.tasker.data.db.AppDatabase
import mbolg.tasker.di.BaseApiUrl
import mbolg.tasker.di.DatabaseName
import mbolg.tasker.recognizer.*
import mbolg.tasker.vocalizer.Vocalizer
import mbolg.tasker.vocalizer.YandexVocalizer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(val context: Context) {


    @Provides
    fun provideContext() = context

    @Provides
    @Singleton
    fun provideDataManager(database: AppDatabase, taskApiHelper: TaskApiHelper): DataManager {
        return SimpleDataManager(database.taskDao(), database.localTaskDao(), taskApiHelper, database, context)
    }

    @Provides
    @Singleton
    fun provideDatabase(@DatabaseName databaseName: String): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, databaseName).allowMainThreadQueries().build()

    @Provides
    @Singleton
    fun provideRetrofit(@BaseApiUrl baseApiUrl: String): Retrofit {
        val gson = GsonBuilder().setLenient().create()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
                .baseUrl(baseApiUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()

        return retrofit
    }

    @Provides
    fun provideVocalizer(yandexVocalizer: YandexVocalizer): Vocalizer {
        return yandexVocalizer
    }

    @Provides
    fun provideSpeechRecorder(speechRecorder: SimpleSpeechRecorder): SpeechRecorder = speechRecorder

    @Provides
    fun provideSpeechRecognizer(speechRecognizer: YandexSpeechRecognizer): SpeechRecognizer = speechRecognizer

    @Provides
    @Singleton
    fun provideTaskApiHelper(helper: SimpleTaskApiHelper): TaskApiHelper = helper

    @Provides
    @BaseApiUrl
    fun provideBaseApiUrl(): String = "https://atg.appvelox.ru/"

    @Provides
    @DatabaseName
    fun provideDatabaseName(): String = "database"
}