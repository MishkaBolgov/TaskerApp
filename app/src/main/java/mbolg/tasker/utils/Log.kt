package mbolg.tasker.utils

import android.util.Log
import mbolg.tasker.data.model.Task

fun String.log(comment: String = ""){
    Log.d("mylog", "$comment $this")
}