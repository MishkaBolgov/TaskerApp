package mbolg.tasker.view.tasklist.fragment

import mbolg.tasker.R
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_item_view.view.*
import mbolg.tasker.data.datamanager.DataManager
import mbolg.tasker.data.model.LocalTask
import mbolg.tasker.data.model.SyncTask
import mbolg.tasker.data.model.Task
import mbolg.tasker.utils.AlertUtils
import mbolg.tasker.utils.NetworkUtils
import mbolg.tasker.utils.log
import mbolg.tasker.view.task.ActiveTaskActivity
import mbolg.tasker.view.task.TaskActivity
import mbolg.tasker.vocalizer.Vocalizer
import java.io.IOException
import javax.inject.Inject

class TaskListAdapter @Inject constructor(val vocalizer: Vocalizer, val dataManager: DataManager) : RecyclerView.Adapter<TaskViewHolder>() {
    var syncTaskList: List<Task> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
        get() {
            return field.filter { it.title.contains(filterString, true) || it.text.contains(filterString, true) }
        }

    var localTaskList: List<Task> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
        get() {
            return field.filter { it.title.contains(filterString, true) || it.text.contains(filterString, true) }
        }

    private fun getMergedSortedTaskList(): List<Task> {
        val mergedList = ArrayList<Task>()

        for (syncTask in syncTaskList)
            mergedList.add(syncTask)

        for (localTask in localTaskList)
            mergedList.add(localTask)


        val sortedList = mergedList.sortedWith(Comparator<Task> { p0, p1 ->

            val task1Edited = p0!!.getEditedMillis()
            val task2Edited = p1!!.getEditedMillis()

            when {
                task1Edited < task2Edited -> -1
                task1Edited > task2Edited -> 1
                else -> 0
            }
        })


        return sortedList
    }


    var filterString: String = ""
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item_view, parent, false)

        return TaskViewHolder(view, vocalizer, dataManager)
    }

    override fun getItemCount(): Int = syncTaskList.size + localTaskList.size


    override fun onBindViewHolder(viewHolder: TaskViewHolder, position: Int) {
        viewHolder.task = getMergedSortedTaskList()[position]
    }
}


class TaskViewHolder(val view: View, val vocalizer: Vocalizer, val dataManager: DataManager) : RecyclerView.ViewHolder(view) {
    var task: Task? = null
        set(value) {
            field = value
            view.tvTaskTitle.text = value?.title
            view.tvTaskUpdatedAt.text = value?.getFormatUpdatedAt()

            when (field) {
                is SyncTask -> {
                    view.syncIndicator.visibility = INVISIBLE
                }
                is LocalTask -> {
                    view.syncIndicator.visibility = VISIBLE
                }
            }

            view.setOnClickListener {
                val context = view.context
                val taskActivityClass = if (value?.getStatus() == Task.TaskType.ACTIVE) ActiveTaskActivity::class.java else TaskActivity::class.java
                val intent = Intent(context, taskActivityClass)

                when (field) {
                    is SyncTask -> {
                        intent.putExtra("is_sync", true)
                    }
                    is LocalTask -> {
                        intent.putExtra("is_sync", false)
                    }
                }
                intent.putExtra("task_id", field?.id?.toLong())

                context.startActivity(intent)
            }

            view.btnVocalize.setOnClickListener {
                if (NetworkUtils.isOnline(view.context)) {
                    view.progressBar.visibility = VISIBLE
                    view.btnVocalize.visibility = INVISIBLE

                    try {

                        vocalizer.vocalize(field?.title ?: "", field?.text ?: "") {
                            view.progressBar.visibility = INVISIBLE
                            view.btnVocalize.visibility = VISIBLE
                        }
                    } catch (ex: IOException) {
                        AlertUtils.showVocalizationErrorAlert(view.context)
                        view.progressBar.visibility = INVISIBLE
                        view.btnVocalize.visibility = VISIBLE
                    }
                } else {
                    AlertUtils.showVocalizationRequireNetworkAlert(view.context)
                }
            }
        }

    fun getForeground(): View {
        return itemView.findViewById<View>(R.id.foreground)
    }

    fun showDoneBackground() {
        itemView.findViewById<View>(R.id.doneBackground).visibility = VISIBLE
    }

    fun showArchiveBackground() {
        itemView.findViewById<View>(R.id.archiveBackground).visibility = VISIBLE
    }

    fun showActiveBackground() {
        itemView.findViewById<View>(R.id.activeBackground).visibility = VISIBLE
    }

    fun hideBackgrounds() {
        itemView.findViewById<View>(R.id.doneBackground).visibility = INVISIBLE
        itemView.findViewById<View>(R.id.archiveBackground).visibility = INVISIBLE
        itemView.findViewById<View>(R.id.activeBackground).visibility = INVISIBLE
    }
}