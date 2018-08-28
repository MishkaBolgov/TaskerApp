package mbolg.tasker.view.task

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.recognition_indicator.*
import mbolg.tasker.R
import mbolg.tasker.di.component.DaggerActivityComponent
import mbolg.tasker.di.module.ActivityModule
import mbolg.tasker.recognizer.SpeechRecognizer
import mbolg.tasker.recognizer.SpeechRecorder
import mbolg.tasker.utils.AlertUtils
import mbolg.tasker.utils.NetworkUtils
import mbolg.tasker.utils.PermissionsUtils
import javax.inject.Inject

class ActiveTaskActivity : TaskActivity() {

    @Inject
    lateinit var recognizer: SpeechRecognizer

    @Inject
    lateinit var recorder: SpeechRecorder


    private var recognitionType = RecognitionType.TITLE_RECORD

    enum class RecognitionType {
        TITLE_RECORD, TEXT_RECORD
    }

    private var recordingStartedAt: Long = 0L
    private val MIN_HOLD_TO_RECORD_DURATION = 200L
    private var isRecordingInterrupted = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskActivityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .applicationComponent(getApplicationComponent())
                .build()

        taskActivityComponent.inject(this)

        val isTaskNew = intent.getBooleanExtra("created_task", false)
        if (isTaskNew) {
            val recognizedTitle = intent.getStringExtra("recognized_title")
            etTitle.setText(recognizedTitle)
        }

        recorder.setOnRecordDoneListener(object : SpeechRecorder.RecordDoneListener {
            override fun onRecordDone() {
                if (isRecordingInterrupted)
                    return
                recordingIndicator.startRecognition()
                recognizer.recognize(recorder.getFile(), object : SpeechRecognizer.RecognitionListener {
                    override fun onSuccess(text: String) {
                        recordingIndicator.stopRecognition()

                        if (text.isEmpty()) {
                            AlertUtils.showNotRecognizedAlert(this@ActiveTaskActivity)
                            return
                        }

                        when (recognitionType) {
                            RecognitionType.TITLE_RECORD -> etTitle.setText(text)
                            RecognitionType.TEXT_RECORD -> etBody.setText(text)
                        }

                        onOptionsItemSelected(editMenuItem)
                    }

                    override fun onFail() {
                        AlertUtils.showNotRecognizedAlert(this@ActiveTaskActivity)
                        recordingIndicator.stopRecognition()
                    }
                })
            }
        })

        btnRecordTitle.visibility = View.VISIBLE
        btnRecordText.visibility = View.VISIBLE

        btnRecordTitle.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            if (NetworkUtils.isOnline(this)) {


                recognitionType = RecognitionType.TITLE_RECORD
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (PermissionsUtils.isAudioPermissionGranted(this@ActiveTaskActivity)) {
                            recordingIndicator.startRecord(btnRecordTitle)
                            onRecordButtonDown()
                        } else PermissionsUtils.requestAudioPermission(this@ActiveTaskActivity)
                    }
                    MotionEvent.ACTION_UP -> onRecordButtonUp()
                }
            } else {
                if (motionEvent.action == MotionEvent.ACTION_DOWN)
                    AlertUtils.showVoiceRecognitionRequireNetworkAlert(this)
            }
            true
        }
        btnRecordText.setOnTouchListener { view: View, motionEvent: MotionEvent ->

            if (NetworkUtils.isOnline(this)) {


                recognitionType = RecognitionType.TEXT_RECORD
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (PermissionsUtils.isAudioPermissionGranted(this@ActiveTaskActivity)) {
                            recordingIndicator.startRecord(btnRecordText)
                            onRecordButtonDown()

                        } else PermissionsUtils.requestAudioPermission(this@ActiveTaskActivity)
                    }
                    MotionEvent.ACTION_UP -> onRecordButtonUp()
                }
            } else {
                if (motionEvent.action == MotionEvent.ACTION_DOWN)
                    AlertUtils.showVoiceRecognitionRequireNetworkAlert(this)
            }
            true
        }


    }


    private fun onRecordButtonDown() {
        recordingStartedAt = System.currentTimeMillis()
        isRecordingInterrupted = false
        recorder.startRecording()
    }

    private fun onRecordButtonUp() {
        if (System.currentTimeMillis() - recordingStartedAt < MIN_HOLD_TO_RECORD_DURATION) {
            AlertUtils.showRecordingTipAlert(this)
            recordingIndicator.cancel()
            isRecordingInterrupted = true
        } else {
            recordingIndicator.stopRecord()
        }
        recorder.stopRecording()
    }


    var editMenuItem: MenuItem? = null
    var saveMenuItem: MenuItem? = null


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.save -> {
                toShowMode()

                viewModel.saveTask(etTitle.text.toString(), etBody.text.toString())
            }
            R.id.edit -> {
                toEditMode()

                etTitle.setSelection(etTitle.text.length)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun toEditMode() {
        etTitle.isEnabled = true
        etBody.isEnabled = true

        editMenuItem?.isVisible = false
        saveMenuItem?.isVisible = true
    }

    private fun toShowMode() {
        etTitle.isEnabled = false
        etBody.isEnabled = false

        editMenuItem?.isVisible = true
        saveMenuItem?.isVisible = false

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val res = super.onCreateOptionsMenu(menu)

        menu?.let {
            editMenuItem = it.findItem(R.id.edit)
            saveMenuItem = it.findItem(R.id.save)

            if (intent.getBooleanExtra("created_task", false))
                toEditMode()
            else toShowMode()
        }

        return res
    }

    override fun onBackPressed() {
        super.onBackPressed()
        onOptionsItemSelected(saveMenuItem)
    }

}