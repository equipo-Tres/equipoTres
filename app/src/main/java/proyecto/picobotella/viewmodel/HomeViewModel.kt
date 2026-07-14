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
import proyecto.picobotella.repository.PokemonRepository
import proyecto.picobotella.repository.RateRepository
import proyecto.picobotella.repository.RetoRepository
import proyecto.picobotella.repository.SpinSoundRepository
import proyecto.picobotella.utils.Constants

private enum class GameState {
    IDLE,
    SPINNING,
    COUNTING
}

class HomeViewModel(
    private val rateRepository: RateRepository,
    private val audioRepository: AudioRepository,
    private val spinSoundRepository: SpinSoundRepository,
    private val pokemonRepository: PokemonRepository,
    private val retoRepository: RetoRepository
) : ViewModel() {

    private val _isMusicEnabled = MutableLiveData(audioRepository.isMusicEnabled())
    val isMusicEnabled: LiveData<Boolean> = _isMusicEnabled

    private val _openPlayStore = MutableLiveData<Intent?>()
    val openPlayStore: LiveData<Intent?> = _openPlayStore

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

    private val _isCounterVisible = MutableLiveData(true)
    val isCounterVisible: LiveData<Boolean> = _isCounterVisible

    private val _showRetoDialog = MutableLiveData(false)
    val showRetoDialog: LiveData<Boolean> = _showRetoDialog

    private val _spinTarget = MutableLiveData(0f)
    val spinTarget: LiveData<Float> = _spinTarget

    private val _pokemonImageUrl = MutableLiveData<String?>()
    val pokemonImageUrl: LiveData<String?> = _pokemonImageUrl

    private val _randomRetoDescription = MutableLiveData<String?>()
    val randomRetoDescription: LiveData<String?> = _randomRetoDescription

    private var accumulatedRotation: Float = 0f
    private var spinStartedAt: Long = 0L

    private var gameState = GameState.IDLE
    private var gameJob: Job? = null

    fun onHomeVisible() {
        if (gameState == GameState.IDLE) {
            audioRepository.startBackgroundMusic()
        }
        _isMusicEnabled.value = audioRepository.isMusicEnabled()
    }

    fun onHomeHidden() {
        audioRepository.pauseBackgroundMusic()
        cancelGame()
    }

    fun onSpinClicked() {
        if (gameState != GameState.IDLE) return

        val extraRotations = (2..4).random() * 360f
        val randomAngle = (0 until 360).random().toFloat()
        accumulatedRotation += extraRotations + randomAngle
        _spinTarget.value = accumulatedRotation
        spinStartedAt = System.currentTimeMillis()

        audioRepository.pauseBackgroundMusic()
        gameState = GameState.SPINNING
        _isSpinButtonVisible.value = false
        _isCounterVisible.value = false
        _isBottleSpinning.value = true
        spinSoundRepository.startSpinSound()
        startGame()
    }

    private fun startGame() {
        gameJob?.cancel()
        gameJob = viewModelScope.launch {
            delay(Constants.SPIN_DURATION_MS)

            _isBottleSpinning.value = false
            spinSoundRepository.stopSpinSound()
            gameState = GameState.COUNTING
            _isCounterVisible.value = true

            for (value in 3 downTo 0) {
                _counterValue.value = value
                delay(1000)
            }

            _pokemonImageUrl.value = pokemonRepository.getRandomPokemonImageUrl()
            _randomRetoDescription.value = retoRepository.getRandomRetoDescription()?.trim()
            onCountdownFinished()
        }
    }

    private fun onCountdownFinished() {
        _isCounterVisible.value = true
        gameState = GameState.IDLE
        _isSpinButtonVisible.value = true
        _counterValue.value = 3
        _showRetoDialog.value = true
    }

    fun onRetoDialogShown() {
        _showRetoDialog.value = false
    }

    fun onRetoDialogClosed() {
        audioRepository.resumeBackgroundMusic()
    }

    private fun cancelGame() {
        gameJob?.cancel()
        gameJob = null
        gameState = GameState.IDLE
        _isBottleSpinning.value = false
        _isCounterVisible.value = true
        spinSoundRepository.stopSpinSound()
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

    fun onPlayStoreHandled() {
        _openPlayStore.value = null
    }

    fun getRemainingSpinDurationMs(): Long {
        if (gameState != GameState.SPINNING) return 0L
        val elapsed = System.currentTimeMillis() - spinStartedAt
        return (Constants.SPIN_DURATION_MS - elapsed).coerceAtLeast(0L)
    }
}
