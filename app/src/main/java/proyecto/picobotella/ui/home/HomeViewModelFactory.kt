package proyecto.picobotella.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import proyecto.picobotella.data.repository.RateRepository

class HomeViewModelFactory(
    private val rateRepository: RateRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(rateRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}