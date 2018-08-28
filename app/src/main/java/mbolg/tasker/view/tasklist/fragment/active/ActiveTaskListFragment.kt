package mbolg.tasker.view.tasklist.fragment.active

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_task_list.view.*
import kotlinx.android.synthetic.main.recognition_indicator.*
import kotlinx.android.synthetic.main.recognition_indicator.view.*
import mbolg.tasker.R
import mbolg.tasker.data.model.Task
import mbolg.tasker.recognizer.SpeechRecognizer
import mbolg.tasker.recognizer.SpeechRecorder
import mbolg.tasker.utils.AlertUtils
import mbolg.tasker.utils.NetworkUtils
import mbolg.tasker.utils.PermissionsUtils
import mbolg.tasker.view.task.ActiveTaskActivity
import mbolg.tasker.view.tasklist.fragment.*
import javax.inject.Inject

class ActiveTaskListFragment : TaskListFragment() {

    @Inject
    lateinit var recorder: SpeechRecorder

    @Inject
    lateinit var recognizer: SpeechRecognizer

    private var recordingStartedAt: Long = 0L
    private val MIN_HOLD_TO_RECORD_DURATION = 200L
    private var isRecordingInterrupted = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        view?.let { enableSwipeToDone(it) }
        view?.let { enableSwipeToArchive(it) }

        recorder = taskListFragmentComponent?.getSpeechRecorder()!!
        recognizer = taskListFragmentComponent?.getSpeechRecognizer()!!


        recorder.setOnRecordDoneListener(object : SpeechRecorder.RecordDoneListener {
            override fun onRecordDone() {

                if(isRecordingInterrupted)
                    return

                recordingIndicator.startRecognition()
                recognizer.recognize(recorder.getFile(), object : SpeechRecognizer.RecognitionListener {
                    override fun onSuccess(text: String) {
                        recordingIndicator.stopRecognition()
                        if (text.isEmpty()) {
                            AlertUtils.showNotRecognizedAlert(activity!!)
                            return
                        }
                        showCreatedTask(text)
                    }

                    override fun onFail() {
                        AlertUtils.showRecognitionErrorAlert(activity!!)
                        recordingIndicator.stopRecognition()
                    }
                })
            }
        })

        view?.btnAddTask?.show()
        view?.btnRecordTask?.show()

        view?.btnAddTask?.setOnClickListener {
            showCreatedTask()
        }

        view?.btnRecordTask?.setOnTouchListener { _, event ->
            if(NetworkUtils.isOnline(activity!!))
            when (event.action) {
                ACTION_DOWN -> {
                    if (PermissionsUtils.isAudioPermissionGranted(activity!!)) {
                        recordingStartedAt = System.currentTimeMillis()
                        isRecordingInterrupted = false
                        recorder.startRecording()
                        view.recordingIndicator.startRecord(view.btnRecordTask)
                    } else PermissionsUtils.requestAudioPermission(activity!!)
                }
                ACTION_UP -> {
                    if (System.currentTimeMillis() - recordingStartedAt < MIN_HOLD_TO_RECORD_DURATION) {
                        AlertUtils.showRecordingTipAlert(activity!!)
                        recordingIndicator.cancel()
                        isRecordingInterrupted = true

                    } else {
                        view.recordingIndicator.stopRecord()
                    }
                    recorder.stopRecording()
                }
            }
            else
                if (event.action == MotionEvent.ACTION_DOWN)
                    AlertUtils.showVoiceRecognitionRequireNetworkAlert(activity!!)
            true
        }


        return view
    }


    private fun showCreatedTask(title: String = "") {
        (viewModel as ActiveTaskListViewModel).stopSync()

        val createdTaskId = (viewModel as ActiveTaskListViewModel).createLocalTask()

        val intent = Intent(activity, ActiveTaskActivity::class.java)
        intent.putExtra("task_id", createdTaskId)
        intent.putExtra("recognized_title", title)
        intent.putExtra("created_task", true)

        startActivity(intent)
    }

    override fun getViewModelClass(): Class<out TaskListFragmentViewModel> = ActiveTaskListViewModel::class.java


    override fun getTaskType(): Task.TaskType = Task.TaskType.ACTIVE


    fun enableSwipeToDone(view: View) {
        val swipeToDoneCallback = object : SwipeToDoneCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val taskViewHolder = viewHolder as TaskViewHolder
                val task = taskViewHolder.task ?: return

                viewModel.updateTaskType(task, Task.TaskType.DONE)

                val snackbar = Snackbar.make(view!!, resources.getString(R.string.moved_to_done), Snackbar.LENGTH_LONG)
                snackbar.setAction(resources.getString(R.string.cancel)) {
                    viewModel.updateTaskType(task, Task.TaskType.ACTIVE)
                }

                snackbar.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDoneCallback)
        itemTouchHelper.attachToRecyclerView(view.rvTaskList)
    }

    fun enableSwipeToArchive(view: View) {
        val swipeToArchiveCallback = object : SwipeToArchiveCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val taskViewHolder = viewHolder as TaskViewHolder
                val task = taskViewHolder.task ?: return

                viewModel.updateTaskType(task, Task.TaskType.ARCHIVED)

                val snackbar = Snackbar.make(this@ActiveTaskListFragment.view!!, resources.getString(R.string.moved_to_archive), Snackbar.LENGTH_LONG)
                snackbar.setAction(resources.getString(R.string.cancel)) {
                    viewModel.updateTaskType(task, Task.TaskType.ACTIVE)
                }

                snackbar.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToArchiveCallback)
        itemTouchHelper.attachToRecyclerView(view.rvTaskList)
    }


}