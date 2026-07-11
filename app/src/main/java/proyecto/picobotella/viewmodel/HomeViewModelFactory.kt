package proyecto.picobotella.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import proyecto.picobotella.repository.AudioRepository
import proyecto.picobotella.repository.RateRepository
import proyecto.picobotella.repository.SpinSoundRepository

class HomeViewModelFactory(
    private val rateRepository: RateRepository,
    private val audioRepository: AudioRepository,
    private val spinSoundRepository: SpinSoundRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(rateRepository, audioRepository, spinSoundRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
