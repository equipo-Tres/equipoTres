package proyecto.picobotella.view.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import proyecto.picobotella.R

class InstructionsFragment : Fragment() {

    private var victoryAnimationCompleted = false
    private var victoryAnimationProgress = 0f
    private var lottieVictory: LottieAnimationView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_instructions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            victoryAnimationCompleted = savedInstanceState.getBoolean(KEY_VICTORY_ANIMATION_DONE, false)
            victoryAnimationProgress = savedInstanceState.getFloat(KEY_VICTORY_ANIMATION_PROGRESS, 0f)
        }

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }

        val lottieVictory = view.findViewById<LottieAnimationView>(R.id.imgVictory)
        this.lottieVictory = lottieVictory
        lottieVictory.repeatCount = 0

        when {
            victoryAnimationCompleted -> lottieVictory.progress = 1f
            victoryAnimationProgress > 0f -> {
                lottieVictory.progress = victoryAnimationProgress
                lottieVictory.resumeAnimation()
                lottieVictory.addAnimatorListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        victoryAnimationCompleted = true
                        lottieVictory.progress = 1f
                    }
                })
            }
            else -> {
                lottieVictory.playAnimation()
                lottieVictory.addAnimatorListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        victoryAnimationCompleted = true
                        lottieVictory.progress = 1f
                    }
                })
            }
        }
    }

    override fun onDestroyView() {
        victoryAnimationProgress = lottieVictory?.progress ?: 0f
        lottieVictory = null
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_VICTORY_ANIMATION_DONE, victoryAnimationCompleted)
        outState.putFloat(KEY_VICTORY_ANIMATION_PROGRESS, lottieVictory?.progress ?: victoryAnimationProgress)
    }

    companion object {
        private const val KEY_VICTORY_ANIMATION_DONE = "victory_animation_done"
        private const val KEY_VICTORY_ANIMATION_PROGRESS = "victory_animation_progress"
    }
}
