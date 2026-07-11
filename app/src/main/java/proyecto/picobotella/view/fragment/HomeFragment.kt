package proyecto.picobotella.view.fragment

import android.animation.ObjectAnimator
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import proyecto.picobotella.PicoBotellaApplication
import proyecto.picobotella.R
import proyecto.picobotella.viewmodel.HomeViewModel
import proyecto.picobotella.viewmodel.HomeViewModelFactory

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        val app = requireActivity().application as PicoBotellaApplication
        HomeViewModelFactory(app.rateRepository, app.audioRepository)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onHomeVisible()
    }

    override fun onPause() {
        viewModel.onHomeHidden()
        super.onPause()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.fragment_home,
            container,
            false
        )

        val imgBottle = view.findViewById<ImageView>(R.id.imgBottle)
        val btnSpin = view.findViewById<ImageView>(R.id.btnSpin)
        val btnSpinContainer = view.findViewById<View>(R.id.btnSpinContainer)
        val txtCounter = view.findViewById<TextView>(R.id.txtCounter)

        val pulseAnimation = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.button_pulse
        )

        var bottleSpinAnimator: ObjectAnimator? = null

        btnSpin.startAnimation(pulseAnimation)

        btnSpin.setOnClickListener {
            viewModel.onSpinClicked()
        }

        viewModel.isBottleSpinning.observe(viewLifecycleOwner) { spinning ->
            if (spinning) {
                bottleSpinAnimator = ObjectAnimator.ofFloat(imgBottle, "rotation", 0f, 360f).apply {
                    duration = 500L
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.RESTART
                    interpolator = LinearInterpolator()
                    start()
                }
            } else {
                bottleSpinAnimator?.cancel()
                bottleSpinAnimator = null
            }
        }

        viewModel.counterValue.observe(viewLifecycleOwner) { value ->
            txtCounter.text = value.toString()
        }

        viewModel.isSpinButtonVisible.observe(viewLifecycleOwner) { visible ->
            btnSpinContainer.visibility = if (visible) View.VISIBLE else View.INVISIBLE
            if (visible) {
                btnSpin.startAnimation(pulseAnimation)
            } else {
                btnSpin.clearAnimation()
            }
        }

        val btnStar = view.findViewById<ImageButton>(R.id.btnStar)
        btnStar.setOnClickListener {
            viewModel.onStarClicked()
        }

        val btnAudio = view.findViewById<ImageButton>(R.id.btnAudio)
        btnAudio.setOnClickListener {
            viewModel.onAudioClicked()
        }

        viewModel.isMusicEnabled.observe(viewLifecycleOwner) { isEnable ->
            btnAudio.setImageResource(
                if (isEnable) R.drawable.ic_sound_on else R.drawable.ic_sound_off
            )
        }

        viewModel.openPlayStore.observe(viewLifecycleOwner) { intent ->
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                startActivity(viewModel.getWebFallbackIntent())
            }
        }

        val btnInfo = view.findViewById<ImageButton>(R.id.btnInfo)
        btnInfo.setOnClickListener {
            viewModel.onInfoClicked()
        }

        viewModel.navigateToInstructions.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().navigate(R.id.action_home_to_instructions)
                viewModel.onInstructionsNavigated()
            }
        }

        val btnRetos = view.findViewById<ImageButton>(R.id.btnRetos)
        btnRetos.setOnClickListener {
            viewModel.onRetosClicked()
        }

        val btnShare = view.findViewById<ImageButton>(R.id.btnShare)
        btnShare.setOnClickListener {
            viewModel.onShareClicked()
        }

        viewModel.navigateToRetos.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().navigate(R.id.action_home_to_retos)
                viewModel.onRetosNavigated()
            }
        }

        viewModel.shareAppEvent.observe(viewLifecycleOwner) { shouldShare ->
            if (shouldShare) {
                shareApp()
                viewModel.onShareAppHandled()
            }
        }

        return view
    }

    private fun shareApp() {
        val appLink = getString(R.string.share_app_url)
        val shareText = getString(R.string.share_app_message, appLink)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        startActivity(
            Intent.createChooser(
                shareIntent,
                getString(R.string.share_app_chooser_title)
            )
        )
    }
}
