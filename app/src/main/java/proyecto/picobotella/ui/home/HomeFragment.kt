package proyecto.picobotella.ui.home

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import proyecto.picobotella.PicoBotellaApplication
import proyecto.picobotella.R
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        val app = requireActivity().application as PicoBotellaApplication
        HomeViewModelFactory(app.rateRepository, app.audioRepository)
    }
    //musica de fondo
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

        val btnSpin = view.findViewById<ImageView>(R.id.btnSpin)

        val pulseAnimation = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.button_pulse
        )

        btnSpin.startAnimation(pulseAnimation)

        val btnStar = view.findViewById<ImageButton>(R.id.btnStar)
        btnStar.setOnClickListener {
            viewModel.onStarClicked()
        }

        //boton del audio - criterio 3 hu 3
        val btnAudio = view.findViewById<ImageButton>(R.id.btnAudio)
        btnAudio.setOnClickListener {
            viewModel.onAudioClicked() //llamado de la funcion que esta declarada en HomeViewModel
        }

        viewModel.isMusicEnabled.observe(viewLifecycleOwner){ isEnable ->
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

        //boton para ir a instrucciones
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

        // HU 6.0 - Retos
        val btnRetos = view.findViewById<ImageButton>(R.id.btnRetos)
        btnRetos.setOnClickListener {
            viewModel.onRetosClicked()
        }

        viewModel.navigateToRetos.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().navigate(R.id.action_home_to_retos)
                viewModel.onRetosNavigated()
            }
        }

        return view
    }
}