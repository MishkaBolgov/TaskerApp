package mbolg.tasker.recognizer

import java.io.File

interface SpeechRecorder {
    fun startRecording()
    fun stopRecording()
    fun getFile(): File
    fun setOnRecordDoneListener(listener: RecordDoneListener)

    interface RecordDoneListener{
        fun onRecordDone()
    }
}