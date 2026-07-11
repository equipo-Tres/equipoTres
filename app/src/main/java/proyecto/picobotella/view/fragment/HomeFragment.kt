package proyecto.picobotella.view.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import proyecto.picobotella.PicoBotellaApplication
import proyecto.picobotella.R
import proyecto.picobotella.utils.Constants
import proyecto.picobotella.viewmodel.HomeViewModel
import proyecto.picobotella.viewmodel.HomeViewModelFactory

class HomeFragment : Fragment() {

    private var retoDialog: Dialog? = null

    private val viewModel: HomeViewModel by viewModels {
        val app = requireActivity().application as PicoBotellaApplication
        HomeViewModelFactory(app.rateRepository, app.audioRepository, app.spinSoundRepository)
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
        val spinContainer = view.findViewById<View>(R.id.spinContainer)
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
                val target = viewModel.spinTarget.value ?: imgBottle.rotation
                bottleSpinAnimator = ObjectAnimator.ofFloat(imgBottle, "rotation", imgBottle.rotation, target).apply {
                    duration = Constants.SPIN_DURATION_MS
                    interpolator = DecelerateInterpolator(2f)
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            imgBottle.rotation = target
                        }
                        override fun onAnimationCancel(animation: Animator) {
                            imgBottle.rotation = target
                        }
                    })
                    start()
                }
            } else {
                bottleSpinAnimator?.cancel()
                bottleSpinAnimator = null
            }
        }

        viewModel.isCounterVisible.observe(viewLifecycleOwner) { visible ->
            txtCounter.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        }

        viewModel.counterValue.observe(viewLifecycleOwner) { value ->
            txtCounter.text = value.toString()
        }

        viewModel.isSpinButtonVisible.observe(viewLifecycleOwner) { visible ->
            spinContainer.visibility = if (visible) View.VISIBLE else View.INVISIBLE
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

        viewModel.showRetoDialog.observe(viewLifecycleOwner) { shouldShow ->
            if (shouldShow) {
                showRetoAleatorioDialog()
                viewModel.onRetoDialogShown()
            }
        }

        return view
    }

    private fun showRetoAleatorioDialog() {
        if (retoDialog?.isShowing == true) return

        val dialogView = layoutInflater.inflate(R.layout.dialog_reto_aleatorio, null)
        val btnClose = dialogView.findViewById<MaterialButton>(R.id.btnCloseRetoDialog)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        retoDialog = dialog

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            retoDialog = null
            viewModel.onRetoDialogClosed()
        }

        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
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
