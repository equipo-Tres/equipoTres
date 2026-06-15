package proyecto.picobotella.ui.home

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import proyecto.picobotella.data.repository.AudioRepository
import proyecto.picobotella.data.repository.RateRepository

class HomeViewModel(
    private val rateRepository: RateRepository,
    private val audioRepository: AudioRepository
) : ViewModel() {

    fun onHomeVisible() {
        audioRepository.startBackgroundMusic()
    }

    fun onHomeHidden(){
        audioRepository.pauseBackgroundMusic()
    }
    private val _openPlayStore = MutableLiveData<Intent>()
    val openPlayStore: LiveData<Intent> = _openPlayStore

    fun onStarClicked() {
        _openPlayStore.value = rateRepository.createPlayStoreIntent()
    }

    fun getWebFallbackIntent(): Intent {
        return rateRepository.createWebFallbackIntent()
    }
}