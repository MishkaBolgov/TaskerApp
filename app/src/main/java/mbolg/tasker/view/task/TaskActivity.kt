package mbolg.tasker.view.task

import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_task.*
import mbolg.tasker.R
import mbolg.tasker.data.model.SyncTask
import mbolg.tasker.view.BaseActivity
import javax.inject.Inject
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.task_item_view.*
import kotlinx.android.synthetic.main.task_item_view.view.*
import kotlinx.android.synthetic.main.vocalize_icon.*
import mbolg.tasker.data.model.LocalTask
import mbolg.tasker.data.model.Task
import mbolg.tasker.di.component.DaggerActivityComponent
import mbolg.tasker.di.module.ActivityModule
import mbolg.tasker.recognizer.SpeechRecognizer
import mbolg.tasker.recognizer.SpeechRecorder
import mbolg.tasker.utils.AlertUtils
import mbolg.tasker.utils.NetworkUtils
import mbolg.tasker.utils.log
import mbolg.tasker.vocalizer.Vocalizer
import java.io.IOException


open class TaskActivity : BaseActivity() {
    @Inject
    lateinit var viewModel: TaskViewModel

    @Inject
    lateinit var vocalizer: Vocalizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        val taskActivityComponent = DaggerActivityComponent.builder().activityModule(ActivityModule(this)).applicationComponent(getApplicationComponent()).build()
        taskActivityComponent.inject(this)


        val taskId = intent.getLongExtra("task_id", -1)
        val isSync = intent.getBooleanExtra("is_sync", false)

        if (isSync) {
            viewModel.getSyncTask(taskId).observe(this, Observer<SyncTask> {
                it?.let {
                    viewModel.task = it
                    fillFields(it)
                }
            })
        } else {
            viewModel.getLocalTask(taskId).observe(this, Observer<LocalTask> {
                it?.let {
                    viewModel.task = it
                    fillFields(it)
                }
            })
        }
    }

    private fun fillFields(task: Task) {

        if (task.title.isNotEmpty())
            etTitle.setText(task.title)

        if (task.text.isNotEmpty())
            etBody.setText(task.text)

        tvUpdatedAt.text = task.getFormatUpdatedAt()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private var btnVocalize: Button? = null
    private var progressBar: ProgressBar? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.task_menu, menu)

        menu?.let {
            val vocalizeItem = it.findItem(R.id.vocalize)
            btnVocalize = vocalizeItem.actionView.findViewById(R.id.btnVocalize)
            progressBar = vocalizeItem.actionView.findViewById(R.id.progressBar)


            btnVocalize?.setOnClickListener {
                if (NetworkUtils.isOnline(this)) {
                    btnVocalize?.visibility = View.INVISIBLE
                    progressBar?.visibility = View.VISIBLE
                    try{
                    vocalizer.vocalize(etTitle.text.toString(), etBody.text.toString()) {
                        btnVocalize?.visibility = View.VISIBLE
                        progressBar?.visibility = View.INVISIBLE
                    }

                } catch (ex: IOException) {
                AlertUtils.showVocalizationErrorAlert(this)
                        btnVocalize?.visibility = View.VISIBLE
                        progressBar?.visibility = View.INVISIBLE
            }
            } else {
            AlertUtils.showVocalizationRequireNetworkAlert(this)
        }
        }
    }

    return super.onCreateOptionsMenu(menu)
}

}
