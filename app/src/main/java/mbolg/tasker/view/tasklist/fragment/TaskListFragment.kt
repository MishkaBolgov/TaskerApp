package mbolg.tasker.view.tasklist.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_task_list.view.*

import mbolg.tasker.R
import mbolg.tasker.data.model.LocalTask
import mbolg.tasker.data.model.SyncTask
import mbolg.tasker.data.model.Task
import mbolg.tasker.di.component.DaggerTaskListFragmentComponent
import mbolg.tasker.di.component.TaskListFragmentComponent
import mbolg.tasker.di.module.TaskListFragmentModule
import mbolg.tasker.utils.log
import mbolg.tasker.view.BaseActivity
import javax.inject.Inject

abstract class TaskListFragment : Fragment() {

    @Inject
    lateinit var adapter: TaskListAdapter

    @Inject
    lateinit var viewModel: TaskListFragmentViewModel

    var taskListFragmentComponent: TaskListFragmentComponent? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)


        taskListFragmentComponent = DaggerTaskListFragmentComponent.builder()
                .taskListFragmentModule(TaskListFragmentModule(this))
                .applicationComponent((activity as BaseActivity).getApplicationComponent())
                .build()

        taskListFragmentComponent?.inject(this)

        view.rvTaskList.layoutManager = LinearLayoutManager(activity)
        view.rvTaskList.adapter = adapter

        viewModel.getSyncTaskList().observe(this, Observer<List<SyncTask>> {
            adapter.syncTaskList = it
        })


        viewModel.getLocalTaskList().observe(this, Observer<List<LocalTask>> {
            adapter.localTaskList = it
        })

        return view
    }

    abstract fun getViewModelClass(): Class<out TaskListFragmentViewModel>

    abstract fun getTaskType(): Task.TaskType

    fun filterTaskList(searchText: String) {
        adapter.filterString = searchText
    }


}
