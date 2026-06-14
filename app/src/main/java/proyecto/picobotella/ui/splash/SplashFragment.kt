package proyecto.picobotella.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import proyecto.picobotella.R

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.fragment_splash,
            container,
            false
        )

        val bottle = view.findViewById<ImageView>(R.id.imgBottle)

        val animation = AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.bottle_animation
        )

        bottle.startAnimation(animation)

        viewModel.navigateToHome.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().navigate(
                    R.id.homeFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.splashFragment, true)
                        .build()
                )
            }
        }

        return view
    }
}