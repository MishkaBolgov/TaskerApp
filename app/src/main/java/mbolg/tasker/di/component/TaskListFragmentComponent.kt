package mbolg.tasker.di.component

import dagger.Component
import mbolg.tasker.di.ActivityScope
import mbolg.tasker.di.module.TaskListFragmentModule
import mbolg.tasker.recognizer.SpeechRecognizer
import mbolg.tasker.recognizer.SpeechRecorder
import mbolg.tasker.view.tasklist.fragment.TaskListFragment
import mbolg.tasker.view.tasklist.fragment.active.ActiveTaskListFragment

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [TaskListFragmentModule::class])
interface TaskListFragmentComponent {
    fun inject(taskListFragment: TaskListFragment)
    fun getSpeechRecorder(): SpeechRecorder
    fun getSpeechRecognizer(): SpeechRecognizer
}