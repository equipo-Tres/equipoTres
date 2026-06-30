package proyecto.picobotella.ui.retos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import proyecto.picobotella.PicoBotellaApplication
import proyecto.picobotella.R

class RetosFragment : Fragment() {

    private val viewModel: RetosViewModel by viewModels {
        val app = requireActivity().application as PicoBotellaApplication
        RetosViewModelFactory(app.retoRepository, app.audioRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.fragment_retos, container, false)

        val view = inflater.inflate(R.layout.fragment_retos, container, false)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            viewModel.onRetosHidden()
            findNavController().popBackStack()
        }

        return view
    }

    override fun onResume(){
        super.onResume()
        viewModel.onRetosVisible()
    }
}
