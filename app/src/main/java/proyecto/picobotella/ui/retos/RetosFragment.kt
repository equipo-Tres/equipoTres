package proyecto.picobotella.ui.retos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import proyecto.picobotella.PicoBotellaApplication
import proyecto.picobotella.R
import kotlin.getValue

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
        return inflater.inflate(R.layout.fragment_retos, container, false)
    }

    override fun onResume(){
        super.onResume()
        viewModel.onRetosVisible()
    }
}
