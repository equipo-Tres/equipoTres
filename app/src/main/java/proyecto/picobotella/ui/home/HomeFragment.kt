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

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        val app = requireActivity().application as PicoBotellaApplication
        HomeViewModelFactory(app.rateRepository)
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

        viewModel.openPlayStore.observe(viewLifecycleOwner) { intent ->
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                startActivity(viewModel.getWebFallbackIntent())
            }
        }

        return view
    }
}