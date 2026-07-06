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

    private val _addRetoError = MutableLiveData<String?>()
    val addRetoError: LiveData<String?> = _addRetoError

    private val _isEditRetoValid = MutableLiveData(false)
    val isEditRetoValid: LiveData<Boolean> = _isEditRetoValid

    private val _editRetoSavedEvent = MutableLiveData(false)
    val editRetoSavedEvent: LiveData<Boolean> = _editRetoSavedEvent

    private val _editRetoError = MutableLiveData<String?>()
    val editRetoError: LiveData<String?> = _editRetoError

    fun onAddRetoTextChanged(description: String) {
        _isAddRetoValid.value = description.trim().isNotEmpty()
        _addRetoError.value = null
    }

    fun onEditRetoTextChanged(originalDescription: String, editedDescription: String) {
        val normalizedOriginal = originalDescription.trim()
        val normalizedEdited = editedDescription.trim()
        _isEditRetoValid.value = normalizedEdited.isNotEmpty() && normalizedEdited != normalizedOriginal
        _editRetoError.value = null
    }

    fun addReto(description: String) {
        val normalizedDescription = description.trim()
        if (normalizedDescription.isEmpty()) return

        viewModelScope.launch {
            if (retoRepository.existsByDescription(normalizedDescription)) {
                _addRetoError.postValue("Ya existe un reto con ese texto")
                return@launch
            }

            retoRepository.insert(RetoEntity(description = normalizedDescription))
            _addRetoSavedEvent.postValue(true)
            _isAddRetoValid.postValue(false)
            _addRetoError.postValue(null)
        }
    }

    fun onAddRetoSavedHandled() {
        _addRetoSavedEvent.value = false
    }

    fun updateReto(reto: RetoEntity, description: String) {
        val normalizedDescription = description.trim()
        if (normalizedDescription.isEmpty() || normalizedDescription == reto.description.trim()) return

        viewModelScope.launch {
            if (retoRepository.existsByDescriptionExceptId(normalizedDescription, reto.id)) {
                _editRetoError.postValue("Ya existe un reto con ese texto")
                return@launch
            }

            retoRepository.update(reto.copy(description = normalizedDescription))
            _editRetoSavedEvent.postValue(true)
            _isEditRetoValid.postValue(false)
            _editRetoError.postValue(null)
        }
    }

    fun onEditRetoSavedHandled() {
        _editRetoSavedEvent.value = false
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
