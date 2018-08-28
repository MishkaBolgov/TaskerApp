package mbolg.tasker.vocalizer

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.widget.Toast
import mbolg.tasker.recognizer.SpeechKitConfig
import mbolg.tasker.utils.LangUtils
import mbolg.tasker.utils.NetworkUtils
import mbolg.tasker.utils.log
import java.util.*
import javax.inject.Inject

class YandexVocalizer @Inject constructor(val context: Context) : Vocalizer {

    override fun vocalize(title: String, body: String, listener: () -> Unit) {

        if (NetworkUtils.isOffline(context)) {
            Toast.makeText(context, "Необходимо соединение с интернетом", Toast.LENGTH_SHORT)
            return
        }
        val key = SpeechKitConfig.SPEECHKIT_API_KEY
        val format = "mp3"
        val lang = LangUtils.currentLang()

        val speaker = "jane"
        val text = if (LangUtils.isCurrentLangEn())
            "Task name: $title, task description: $body"
        else "Название задачи: $title, описание задачи: $body"

        val uri = "https://tts.voicetech.yandex.net/generate?key=$key&text=$text&format=$format&lang=$lang&speaker=$speaker"

        AsyncTask.execute {
            val mediaPlayer = MediaPlayer()

            mediaPlayer.setDataSource(context, Uri.parse(uri))

            mediaPlayer.setOnCompletionListener {
                listener()
            }
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    }
}