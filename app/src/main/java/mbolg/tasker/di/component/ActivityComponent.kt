package mbolg.tasker.di.component

import dagger.Component
import mbolg.tasker.di.ActivityScope
import mbolg.tasker.di.module.ActivityModule
import mbolg.tasker.di.module.AuthModule
import mbolg.tasker.view.auth.AuthActivity
import mbolg.tasker.view.task.ActiveTaskActivity
import mbolg.tasker.view.task.TaskActivity
import mbolg.tasker.view.tasklist.TaskListActivity

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class, AuthModule::class])
interface ActivityComponent {
    fun inject(activity: AuthActivity)
    fun inject(activity: TaskActivity)
    fun inject(activity: ActiveTaskActivity)
    fun inject(activity: TaskListActivity)
}