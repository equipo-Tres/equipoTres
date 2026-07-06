package proyecto.picobotella.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import proyecto.picobotella.model.RetoEntity
import proyecto.picobotella.repository.AudioRepository
import proyecto.picobotella.repository.RetoRepository

class RetosViewModel(
    private val retoRepository: RetoRepository,
    private val audioRepository: AudioRepository
) : ViewModel() {

    val allRetos: LiveData<List<RetoEntity>> = retoRepository.allRetos

    private val _isAddRetoValid = MutableLiveData(false)
    val isAddRetoValid: LiveData<Boolean> = _isAddRetoValid

    private val _addRetoSavedEvent = MutableLiveData(false)
    val addRetoSavedEvent: LiveData<Boolean> = _addRetoSavedEvent

    fun onAddRetoTextChanged(description: String) {
        _isAddRetoValid.value = description.trim().isNotEmpty()
    }

    fun addReto(description: String) {
        val normalizedDescription = description.trim()
        if (normalizedDescription.isEmpty()) return

        viewModelScope.launch {
            retoRepository.insert(RetoEntity(description = normalizedDescription))
            _addRetoSavedEvent.postValue(true)
            _isAddRetoValid.postValue(false)
        }
    }

    fun onAddRetoSavedHandled() {
        _addRetoSavedEvent.value = false
    }

    fun deleteReto(reto: RetoEntity) {
        viewModelScope.launch {
            retoRepository.delete(reto)
        }
    }

    fun onRetosVisible() {
        if (audioRepository.isMusicEnabled()) {
            audioRepository.pauseBackgroundMusic()
        }
    }

    fun onRetosHidden() {
        if (audioRepository.isMusicEnabled()) {
            audioRepository.resumeBackgroundMusic()
        }
    }
}
