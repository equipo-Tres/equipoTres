package proyecto.picobotella.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import proyecto.picobotella.repository.AudioRepository
import proyecto.picobotella.repository.RateRepository

private enum class GameState {
    IDLE,
    SPINNING,
    COUNTING
}

private const val SPIN_DURATION_MS = 4000L

class HomeViewModel(
    private val rateRepository: RateRepository,
    private val audioRepository: AudioRepository
) : ViewModel() {

    private val _isMusicEnabled = MutableLiveData(audioRepository.isMusicEnabled())
    val isMusicEnabled: LiveData<Boolean> = _isMusicEnabled

    private val _openPlayStore = MutableLiveData<Intent>()
    val openPlayStore: LiveData<Intent> = _openPlayStore

    private val _navigateToInstructions = MutableLiveData<Boolean>()
    val navigateToInstructions: LiveData<Boolean> = _navigateToInstructions

    private val _navigateToRetos = MutableLiveData<Boolean>()
    val navigateToRetos: LiveData<Boolean> = _navigateToRetos

    private val _shareAppEvent = MutableLiveData<Boolean>()
    val shareAppEvent: LiveData<Boolean> = _shareAppEvent

    private val _counterValue = MutableLiveData(3)
    val counterValue: LiveData<Int> = _counterValue

    private val _isSpinButtonVisible = MutableLiveData(true)
    val isSpinButtonVisible: LiveData<Boolean> = _isSpinButtonVisible

    private val _isBottleSpinning = MutableLiveData(false)
    val isBottleSpinning: LiveData<Boolean> = _isBottleSpinning

    private var gameState = GameState.IDLE
    private var gameJob: Job? = null

    fun onHomeVisible() {
        audioRepository.startBackgroundMusic()
        _isMusicEnabled.value = audioRepository.isMusicEnabled()
    }

    fun onHomeHidden() {
        audioRepository.pauseBackgroundMusic()
        cancelGame()
    }

    fun onSpinClicked() {
        if (gameState != GameState.IDLE) return

        gameState = GameState.SPINNING
        _isSpinButtonVisible.value = false
        _isBottleSpinning.value = true
        startGame()
    }

    private fun startGame() {
        gameJob?.cancel()
        gameJob = viewModelScope.launch {
            delay(SPIN_DURATION_MS)

            _isBottleSpinning.value = false
            gameState = GameState.COUNTING

            for (value in 3 downTo 0) {
                _counterValue.value = value
                delay(1000)
            }

            onCountdownFinished()
        }
    }

    private fun onCountdownFinished() {
        gameState = GameState.IDLE
        _isSpinButtonVisible.value = true
        _counterValue.value = 3
    }

    private fun cancelGame() {
        gameJob?.cancel()
        gameJob = null
        gameState = GameState.IDLE
        _isBottleSpinning.value = false
        _counterValue.value = 3
        _isSpinButtonVisible.value = true
    }

    fun onAudioClicked() {
        val enabled = audioRepository.toggleBackgroundMusic()
        _isMusicEnabled.value = enabled
    }

    fun onInfoClicked() {
        _navigateToInstructions.value = true
    }

    fun onStarClicked() {
        _openPlayStore.value = rateRepository.createPlayStoreIntent()
    }

    fun onShareClicked() {
        _shareAppEvent.value = true
    }

    fun getWebFallbackIntent(): Intent {
        return rateRepository.createWebFallbackIntent()
    }

    fun onInstructionsNavigated() {
        _navigateToInstructions.value = false
    }

    fun onRetosClicked() {
        _navigateToRetos.value = true
    }

    fun onRetosNavigated() {
        _navigateToRetos.value = false
    }

    fun onShareAppHandled() {
        _shareAppEvent.value = false
    }
}
