package mbolg.tasker.view.tasklist.fragment.done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_task_list.view.*
import mbolg.tasker.data.model.SyncTask
import mbolg.tasker.data.model.Task
import mbolg.tasker.view.tasklist.fragment.TaskListFragment
import mbolg.tasker.view.tasklist.fragment.TaskListFragmentViewModel
import mbolg.tasker.view.tasklist.fragment.TaskViewHolder
import mbolg.tasker.view.tasklist.fragment.active.SwipeToDoneCallback

class DoneTaskListFragment : TaskListFragment() {
    override fun getViewModelClass(): Class<out TaskListFragmentViewModel> = DoneTaskListViewModel::class.java

    override fun getTaskType(): Task.TaskType = Task.TaskType.DONE
}