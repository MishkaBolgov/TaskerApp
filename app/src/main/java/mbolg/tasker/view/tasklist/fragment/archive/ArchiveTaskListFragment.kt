package mbolg.tasker.view.tasklist.fragment.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_task_list.view.*
import mbolg.tasker.R
import mbolg.tasker.data.model.Task
import mbolg.tasker.view.tasklist.fragment.*

class ArchiveTaskListFragment: TaskListFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        view?.let { enableSwipeToActive(it) }

        return view
    }
    override fun getViewModelClass(): Class<out TaskListFragmentViewModel> = ArchiveTaskListViewModel::class.java


    override fun getTaskType(): Task.TaskType = Task.TaskType.ARCHIVED


    fun enableSwipeToActive(view: View) {
        val swipeToDoneCallback = object : SwipeToActiveCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val taskViewHolder = viewHolder as TaskViewHolder
                val task = taskViewHolder.task ?: return

                viewModel.updateTaskType(task, Task.TaskType.ACTIVE)

                val snackbar = Snackbar.make(view!!, resources.getString(R.string.moved_to_active), Snackbar.LENGTH_LONG)
                snackbar.setAction(resources.getString(R.string.cancel)) {
                    viewModel.updateTaskType(task, Task.TaskType.ARCHIVED)
                }

                snackbar.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDoneCallback)
        itemTouchHelper.attachToRecyclerView(view.rvTaskList)
    }

}