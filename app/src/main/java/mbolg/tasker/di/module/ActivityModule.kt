package mbolg.tasker.di.module

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mbolg.tasker.view.auth.AuthActivity
import mbolg.tasker.view.auth.AuthViewModel
import mbolg.tasker.view.auth.AuthViewModelFactory
import mbolg.tasker.view.task.TaskViewModel
import mbolg.tasker.view.task.TaskViewModelFactory
import mbolg.tasker.view.tasklist.TaskListViewModel
import mbolg.tasker.view.tasklist.TaskListViewModelFactory

@Module
class ActivityModule(val activity: AppCompatActivity) {

    @Provides
    fun provideAuthViewModule(authViewModelFactory: AuthViewModelFactory): AuthViewModel {
        return ViewModelProviders.of(activity, authViewModelFactory).get(AuthViewModel::class.java)
    }

    @Provides
    fun provideTaskViewModel(taskViewModelFactory: TaskViewModelFactory): TaskViewModel {
        return ViewModelProviders.of(activity, taskViewModelFactory).get(TaskViewModel::class.java)
    }

    @Provides
    fun provideTaskListViewModel(viewModelFactory: TaskListViewModelFactory): TaskListViewModel {
        return ViewModelProviders.of(activity, viewModelFactory).get(TaskListViewModel::class.java)
    }
}