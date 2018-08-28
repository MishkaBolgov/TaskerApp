package mbolg.tasker.anim

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.fragment_task_list.view.*
import kotlinx.android.synthetic.main.recognition_indicator.view.*
import mbolg.tasker.R

class RecordingIndicatorLayout(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    private val ANIMATION_DURATION = 1000L
    private var recordAnimationEnabled = false

    fun startRecord(recordingTrigger: View) {
        waveView.x = recordingTrigger.x
        waveView.y = recordingTrigger.y

        visibility = View.VISIBLE
        waveView.visibility = View.VISIBLE

        tvText.text = context.resources.getString(R.string.in_recording)

        recordAnimationEnabled = true
        startRecordingAnimation()
    }


    private fun startRecordingAnimation() {
        waveView.startAnimation(recordingAnimation())
    }

    private fun recordingAnimation(): AnimationSet{
        val recordingAnimationSet = AnimationSet(false)

        recordingAnimationSet.addAnimation(scaleAnimation())
        recordingAnimationSet.addAnimation(alphaAnimation())
        recordingAnimationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                if(recordAnimationEnabled)
                    waveView.startAnimation(recordingAnimationSet)
            }

            override fun onAnimationStart(p0: Animation?) {

            }
        })

        return recordingAnimationSet
    }
    private fun scaleAnimation(): Animation {
        val scaleAnimation = ScaleAnimation(1f, 10f, 1f, 10f, waveView.x + waveView.height / 2, waveView.y + waveView.width / 2)
        scaleAnimation.duration = ANIMATION_DURATION
        return scaleAnimation
    }
    private fun alphaAnimation(): Animation {
        val alphaAnimation = AlphaAnimation(1f, 0f)
        alphaAnimation.duration = ANIMATION_DURATION
        return alphaAnimation
    }


    fun stopRecord() {
        waveView.visibility = View.INVISIBLE
        recordAnimationEnabled = false
    }

    fun startRecognition() {
        tvText.text = context.resources.getString(R.string.recognition)
        progressBar.visibility = View.VISIBLE

    }

    fun stopRecognition() {
        visibility = View.INVISIBLE
        progressBar.visibility = View.GONE
    }

    fun cancel(){
        waveView.visibility = View.INVISIBLE
        recordAnimationEnabled = false
        visibility = View.INVISIBLE
        progressBar.visibility = View.GONE
    }
}