package mbolg.tasker.recognizer
import java.io.File

interface SpeechRecognizer {
    fun recognize(file: File, listener: RecognitionListener)

    interface RecognitionListener{
        fun onSuccess(text: String)
        fun onFail()
    }
}

