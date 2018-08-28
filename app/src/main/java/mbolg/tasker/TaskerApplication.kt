package mbolg.tasker

import android.app.Application
import mbolg.tasker.data.datamanager.SimpleDataManager
import mbolg.tasker.di.component.ApplicationComponent
import mbolg.tasker.di.component.DaggerApplicationComponent
import mbolg.tasker.di.module.ApplicationModule
import mbolg.tasker.utils.log

class TaskerApplication : Application() {
    lateinit var applicationComponent: ApplicationComponent



    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
        applicationComponent.inject(this)
    }
}
