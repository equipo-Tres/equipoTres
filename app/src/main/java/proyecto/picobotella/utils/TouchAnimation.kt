package proyecto.picobotella.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import proyecto.picobotella.R

object TouchAnimation {

    fun play(view: View, onComplete: () -> Unit) {
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.touch_animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                onComplete()
            }
        })
        view.startAnimation(animation)
    }
}
