package mbolg.tasker.view.tasklist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import kotlinx.android.synthetic.main.activity_task_list.*
import mbolg.tasker.R
import mbolg.tasker.data.model.Task
import mbolg.tasker.di.component.DaggerActivityComponent
import mbolg.tasker.di.module.ActivityModule
import mbolg.tasker.view.BaseActivity
import mbolg.tasker.view.tasklist.fragment.TaskListFragment
import mbolg.tasker.view.tasklist.fragment.active.ActiveTaskListFragment
import mbolg.tasker.view.tasklist.fragment.archive.ArchiveTaskListFragment
import mbolg.tasker.view.tasklist.fragment.done.DoneTaskListFragment
import javax.inject.Inject

class TaskListActivity : BaseActivity() {

    lateinit var currentTaskListFragment: TaskListFragment

    @Inject
    lateinit var viewModel: TaskListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        val component = DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(ActivityModule(this))
                .build()

        component.inject(this)

        navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.archive -> showTaskListFragment(Task.TaskType.ARCHIVED)
                R.id.active -> showTaskListFragment(Task.TaskType.ACTIVE)
                R.id.done -> showTaskListFragment(Task.TaskType.DONE)
            }
            true
        }

        instantiateAllFragments()

        navigationView.selectedItemId = R.id.active

        setSupportActionBar(toolbar)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.task_list_menu, menu)
        menu?.let {
            val searchItem = it.findItem(R.id.search)
            val searchView = searchItem.actionView as SearchView
            searchView.setOnCloseListener {
                supportActionBar?.setDisplayShowTitleEnabled(true)
                false
            }
            searchView.setOnSearchClickListener {
                supportActionBar?.setDisplayShowTitleEnabled(false)
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    currentTaskListFragment.filterTaskList(newText ?: "")
                    return true
                }
            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.signOut) {
            viewModel.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun instantiateAllFragments() {
        val transaction = supportFragmentManager.beginTransaction()

        val activeTaskListFragment = ActiveTaskListFragment()
        val archiveTaskListFragment = ArchiveTaskListFragment()
        val doneTaskListFragment = DoneTaskListFragment()

        transaction.add(R.id.taskListContainer, activeTaskListFragment, Task.TaskType.ACTIVE.name)
        transaction.add(R.id.taskListContainer, archiveTaskListFragment, Task.TaskType.ARCHIVED.name)
        transaction.add(R.id.taskListContainer, doneTaskListFragment, Task.TaskType.DONE.name)

        transaction.hide(activeTaskListFragment)
        transaction.hide(archiveTaskListFragment)
        transaction.hide(doneTaskListFragment)

        transaction.commitNow()
    }

    private fun showTaskListFragment(syncTaskType: Task.TaskType) {
        val transaction = supportFragmentManager.beginTransaction()

        supportFragmentManager.fragments.forEach {
            if (it.tag != syncTaskType.name)
                transaction.hide(it)
        }

        currentTaskListFragment = supportFragmentManager.findFragmentByTag(syncTaskType.name)!! as TaskListFragment
        transaction.show(currentTaskListFragment)
        transaction.commit()

    }

    override fun onResume() {
        super.onResume()
        viewModel.startSync()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopSync()
    }
}
