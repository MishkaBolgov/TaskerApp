package mbolg.tasker.utils

import android.content.Context
import android.content.DialogInterface
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AlertDialog
import mbolg.tasker.R
import mbolg.tasker.view.auth.AuthActivity
import kotlin.coroutines.experimental.coroutineContext


class AlertUtils {
    companion object {

        private fun showAlert(context: Context, title: String, message: String? = null) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            if (message != null)
                builder.setMessage(message)
            builder.setPositiveButton("Ok", object: DialogInterface.OnClickListener{
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }
            })
            builder.create().show()
        }

        fun showSignInRequireNetworkAlert(context: Context) {
            showAlert(context,
                    context.resources.getString(R.string.network_connection_error_alert_title),
                    context.resources.getString(R.string.sign_in_require_network_alert_message))
        }

        fun showSignUpRequireNetworkAlert(context: Context) {
            showAlert(context,
                    context.resources.getString(R.string.network_connection_error_alert_title),
                    context.resources.getString(R.string.sign_up_require_network_alert_message))
        }


        fun showPasswordConfirmCheckAlert(context: Context) {
            showAlert(context,
                    context.resources.getString(R.string.password_confirm_error_title),
                    context.resources.getString(R.string.password_confirm_error_message))
        }

        fun showSignInError(context: Context) {
            showAlert(context,
                    context.resources.getString(R.string.sign_in_error_title),
                    context.resources.getString(R.string.sign_in_error_message))

        }

        fun showNotRecognizedAlert(context: Context) {
            showAlert(context,
                    context.resources.getString(R.string.not_recognized_alert_title),
                    context.resources.getString(R.string.not_recognized_alert_message))
        }

        fun showRecognitionErrorAlert(context: Context) {
            showAlert(context,
                    context.resources.getString(R.string.recognition_error_alert_title))
        }

        fun showRecordingTipAlert(context: Context) {
            showAlert(context,
                    context.resources.getString(R.string.recording_tip_title),
                    context.resources.getString(R.string.recording_tip_message))
        }

        fun showVoiceRecognitionRequireNetworkAlert(context: Context){
            showAlert(context,
                    context.resources.getString(R.string.recognition_require_network_alert_title),
                    context.resources.getString(R.string.recognition_require_network_alert_message))
        }

        fun showVocalizationRequireNetworkAlert(context: Context){
            showAlert(context,
                    context.resources.getString(R.string.vocalization_require_network_alert_title),
                    context.resources.getString(R.string.vocalization_require_network_alert_message))
        }

        fun showVocalizationErrorAlert(context: Context) {
            showAlert(context,
                    context.resources.getString(R.string.vocalization_error_alert_title))
        }

        fun showInvalidEmailAlert(context: Context) {
            showAlert(context,
                    context.resources.getString(R.string.invalid_email_alert_title))
        }

        fun showInvalidPasswordAlert(context: Context) {
            showAlert(context,
                    context.resources.getString(R.string.invalid_password_alert_title))
        }
    }
}