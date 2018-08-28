package mbolg.tasker.view

import androidx.appcompat.app.AppCompatActivity
import mbolg.tasker.TaskerApplication
import mbolg.tasker.di.component.ApplicationComponent

open class BaseActivity : AppCompatActivity() {

    fun getApplicationComponent(): ApplicationComponent {
        val application = application as TaskerApplication
        return application.applicationComponent
    }

}
