package mbolg.tasker.utils

import java.util.*

class LangUtils {
    companion object {
        fun currentLang(): String {
            val systemLang = Locale.getDefault().language
            return when (systemLang) {
                "en" -> "en-US"
                else -> "ru-RU"
            }
        }

        fun isCurrentLangEn() = Locale.getDefault().language == "en"
    }
}
