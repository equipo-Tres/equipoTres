package proyecto.picobotella.ui.retos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import proyecto.picobotella.PicoBotellaApplication
import proyecto.picobotella.R
import proyecto.picobotella.data.local.RetoEntity

class RetosFragment : Fragment() {

    private val viewModel: RetosViewModel by viewModels {
        val app = requireActivity().application as PicoBotellaApplication
        RetosViewModelFactory(app.retoRepository, app.audioRepository)
    }

    private val adapter = RetoAdapter({reto -> showEditRetoDialog(reto) /*HU-8*/},{reto -> showDeleteRetoDialog(reto) /*HU-9*/})

    private fun showEditRetoDialog(reto: RetoEntity) {
        // Crear un diálogo para editar el reto
        // Boton guardar
    }

    private fun showDeleteRetoDialog(reto: RetoEntity) {
        // Crear un diálogo para eliminar el reto
        // SI
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_retos, container, false)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            viewModel.onRetosHidden()
            findNavController().popBackStack()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerRetos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val txtEmpty = view.findViewById<TextView>(R.id.txtEmptyRetos)

        viewModel.allRetos.observe(viewLifecycleOwner){ retos ->
            adapter.submitList(retos)
            txtEmpty.visibility = if (retos.isEmpty()) View.VISIBLE else View.GONE
        }

        return view
    }

    override fun onResume(){
        super.onResume()
        viewModel.onRetosVisible()
    }
}
