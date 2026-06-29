package proyecto.picobotella.ui.retos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import proyecto.picobotella.data.repository.RetoRepository

class RetosViewModelFactory(
    private val retoRepository: RetoRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RetosViewModel::class.java)) {
            return RetosViewModel(retoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
