package mbolg.tasker.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsUtils {
    companion object {
        val AUDIO_PERMISSION_CODE = 1

        fun requestAudioPermission(activity: Activity) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECORD_AUDIO), AUDIO_PERMISSION_CODE)

        }

        fun isAudioPermissionGranted(activity: Activity): Boolean {
            return (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
        }
    }
}