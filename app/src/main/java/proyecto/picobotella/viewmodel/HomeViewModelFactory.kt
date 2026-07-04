package proyecto.picobotella.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import proyecto.picobotella.repository.AudioRepository
import proyecto.picobotella.repository.RateRepository

class HomeViewModelFactory(
    private val rateRepository: RateRepository,
    private val audioRepository: AudioRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(rateRepository, audioRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
