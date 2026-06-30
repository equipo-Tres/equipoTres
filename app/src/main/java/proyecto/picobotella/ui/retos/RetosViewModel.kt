package proyecto.picobotella.ui.retos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import proyecto.picobotella.data.local.RetoEntity
import proyecto.picobotella.data.repository.RetoRepository
import proyecto.picobotella.data.repository.AudioRepository

class RetosViewModel(private val retoRepository: RetoRepository, private val audioRepository: AudioRepository) : ViewModel() {

    val allRetos: LiveData<List<RetoEntity>> = retoRepository.allRetos

    fun addReto(description: String) {
        viewModelScope.launch {
            retoRepository.insert(RetoEntity(description = description))
        }
    }

    fun deleteReto(reto: RetoEntity) {
        viewModelScope.launch {
            retoRepository.delete(reto)
        }
    }

    fun onRetosVisible() {
        if (audioRepository.isMusicEnabled()){
            audioRepository.pauseBackgroundMusic()
        }
    }
    fun onRetosHidden() {
        if (audioRepository.isMusicEnabled()){
            audioRepository.resumeBackgroundMusic()
        }
    }
}
