package proyecto.picobotella.view.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputEditText
import proyecto.picobotella.PicoBotellaApplication
import proyecto.picobotella.R
import proyecto.picobotella.model.RetoEntity
import proyecto.picobotella.view.adapter.RetoAdapter
import proyecto.picobotella.viewmodel.RetosViewModel
import proyecto.picobotella.viewmodel.RetosViewModelFactory

class RetosFragment : Fragment() {

    private var addRetoDialog: Dialog? = null
    private var addRetoSaveButton: MaterialButton? = null
    private var addRetoInputLayout: TextInputLayout? = null
    private var editRetoDialog: Dialog? = null
    private var editRetoSaveButton: MaterialButton? = null
    private var editRetoInputLayout: TextInputLayout? = null

    private val viewModel: RetosViewModel by viewModels {
        val app = requireActivity().application as PicoBotellaApplication
        RetosViewModelFactory(app.retoRepository, app.audioRepository)
    }

    private val adapter = RetoAdapter(
        { reto -> showEditRetoDialog(reto) },
        { reto -> showDeleteRetoDialog(reto) }
    )

    private fun showAddRetoDialog() {
        if (addRetoDialog?.isShowing == true) return

        val dialogView = layoutInflater.inflate(R.layout.dialog_add_reto, null)
        val inputLayoutAddReto = dialogView.findViewById<TextInputLayout>(R.id.inputLayoutAddReto)
        val edtAddReto = dialogView.findViewById<TextInputEditText>(R.id.edtAddReto)
        val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btnCancelAddReto)
        val btnSave = dialogView.findViewById<MaterialButton>(R.id.btnSaveAddReto)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        addRetoDialog = dialog
        addRetoSaveButton = btnSave
        addRetoInputLayout = inputLayoutAddReto

        edtAddReto.doAfterTextChanged { editable ->
            viewModel.onAddRetoTextChanged(editable?.toString().orEmpty())
        }

        viewModel.onAddRetoTextChanged(edtAddReto.text?.toString().orEmpty())

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            viewModel.addReto(edtAddReto.text?.toString().orEmpty())
        }

        dialog.setOnDismissListener {
            addRetoDialog = null
            addRetoSaveButton = null
            addRetoInputLayout = null
            viewModel.onAddRetoTextChanged("")
        }

        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun renderAddRetoSaveState(isEnabled: Boolean) {
        addRetoSaveButton?.let { button ->
            button.isEnabled = isEnabled
            button.backgroundTintList = ContextCompat.getColorStateList(
                requireContext(),
                if (isEnabled) R.color.orange else R.color.button_disabled
            )
        }
    }

    private fun showEditRetoDialog(reto: RetoEntity) {
        if (editRetoDialog?.isShowing == true) return

        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_reto, null)

        val inputLayoutEditReto = dialogView.findViewById<TextInputLayout>(R.id.inputLayoutEditReto)
        val edtEditReto = dialogView.findViewById<TextInputEditText>(R.id.edtEditReto)
        val btnCancel = dialogView.findViewById<MaterialButton>(R.id.btnCancelEditReto)
        val btnSave = dialogView.findViewById<MaterialButton>(R.id.btnSaveEditReto)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        editRetoDialog = dialog
        editRetoSaveButton = btnSave
        editRetoInputLayout = inputLayoutEditReto

        edtEditReto.setText(reto.description)

        edtEditReto.doAfterTextChanged { editable ->
            viewModel.onEditRetoTextChanged(
                reto.description,
                editable?.toString().orEmpty()
            )
        }

        viewModel.onEditRetoTextChanged(
            reto.description,
            edtEditReto.text?.toString().orEmpty()
        )

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            viewModel.updateReto(reto, edtEditReto.text?.toString().orEmpty())
        }

        dialog.setOnDismissListener {
            editRetoDialog = null
            editRetoSaveButton = null
            editRetoInputLayout = null
            viewModel.onEditRetoTextChanged(reto.description, "")
        }

        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
    }

    private fun renderEditRetoSaveState(isEnabled: Boolean) {
        editRetoSaveButton?.let { button ->
            button.isEnabled = isEnabled
            button.backgroundTintList = ContextCompat.getColorStateList(
                requireContext(),
                if (isEnabled) R.color.orange else R.color.button_disabled
            )
        }
    }

    private fun renderAddRetoError(error: String?) {
        addRetoInputLayout?.error = error
    }

    private fun renderEditRetoError(error: String?) {
        editRetoInputLayout?.error = error
    }

    private fun showDeleteRetoDialog(reto: RetoEntity) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_reto, null)

        dialogView.findViewById<TextView>(R.id.txtDeleteDescription).text = reto.description

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        dialogView.findViewById<TextView>(R.id.txtNo).setOnClickListener {
            dialog.dismiss()
        }

        dialogView.findViewById<TextView>(R.id.txtSi).setOnClickListener {
            viewModel.deleteReto(reto)
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
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

        val fabAddReto = view.findViewById<FloatingActionButton>(R.id.fabAddReto)
        fabAddReto.setOnClickListener {
            showAddRetoDialog()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerRetos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val txtEmpty = view.findViewById<TextView>(R.id.txtEmptyRetos)

        viewModel.allRetos.observe(viewLifecycleOwner) { retos ->
            adapter.submitList(retos)
            txtEmpty.visibility = if (retos.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isAddRetoValid.observe(viewLifecycleOwner) { isValid ->
            renderAddRetoSaveState(isValid)
        }

        viewModel.addRetoSavedEvent.observe(viewLifecycleOwner) { shouldDismiss ->
            if (shouldDismiss) {
                addRetoDialog?.dismiss()
                viewModel.onAddRetoSavedHandled()
            }
        }

        viewModel.addRetoError.observe(viewLifecycleOwner) { error ->
            renderAddRetoError(error)
        }

        viewModel.isEditRetoValid.observe(viewLifecycleOwner) { isValid ->
            renderEditRetoSaveState(isValid)
        }

        viewModel.editRetoSavedEvent.observe(viewLifecycleOwner) { shouldDismiss ->
            if (shouldDismiss) {
                editRetoDialog?.dismiss()
                viewModel.onEditRetoSavedHandled()
            }
        }

        viewModel.editRetoError.observe(viewLifecycleOwner) { error ->
            renderEditRetoError(error)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.onRetosVisible()
    }
}
