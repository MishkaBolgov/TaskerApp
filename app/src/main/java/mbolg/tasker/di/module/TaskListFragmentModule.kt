package mbolg.tasker.di.module

import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import mbolg.tasker.data.datamanager.DataManager
import mbolg.tasker.di.ActivityScope
import mbolg.tasker.view.tasklist.fragment.TaskListFragment
import mbolg.tasker.view.tasklist.fragment.TaskListFragmentViewModel

@Module
class TaskListFragmentModule(val taskListFragment: TaskListFragment) {
    @Provides
    @ActivityScope
    fun provideTaskListViewModel(dataManager: DataManager): TaskListFragmentViewModel {
        val viewModel = ViewModelProviders.of(taskListFragment).get(taskListFragment.getViewModelClass())
        viewModel.dataManager = dataManager
        return viewModel
    }

}