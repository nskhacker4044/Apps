package com.example.viewmodel

import android.app.Application
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.SoundManager
import com.example.data.AppDatabase
import com.example.data.GameRepository
import com.example.data.LevelDefinitions
import com.example.data.entity.GameStats
import com.example.data.entity.LevelProgress
import com.example.model.Arrow
import com.example.model.GameDifficulty
import com.example.model.LevelConfig
import com.example.model.LevelSolver
import com.example.model.PaletteTheme
import com.example.ui.components.FlyingTokenEvent
import com.example.ui.components.TokenType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenState {
    SPLASH,
    LEVEL_SELECT,
    LEVEL_LOADING,
    PLAYING,
    LEVEL_COMPLETE
}

data class VictoryStats(
    val levelNumber: Int,
    val stars: Int,
    val score: Int,
    val timeTakenSeconds: Int,
    val timeRemainingSeconds: Int,
    val bonusSpeedText: String,
    val levelConfig: LevelConfig
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GameRepository
    val soundManager: SoundManager

    init {
        val db = AppDatabase.getDatabase(application)
        repository = GameRepository(db.levelDao())
        soundManager = SoundManager(application)

        viewModelScope.launch {
            repository.initializeDefaultsIfNeeded(LevelDefinitions.levels.size)
        }
    }

    val allLevelProgress: StateFlow<List<LevelProgress>> = repository.allLevelProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val gameStats: StateFlow<GameStats?> = repository.gameStats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // UI States
    private val _screenState = MutableStateFlow(ScreenState.SPLASH)
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()

    private val _currentLevelNumber = MutableStateFlow(1)
    val currentLevelNumber: StateFlow<Int> = _currentLevelNumber.asStateFlow()

    private val _currentLevelConfig = MutableStateFlow(LevelDefinitions.getLevel(1))
    val currentLevelConfig: StateFlow<LevelConfig> = _currentLevelConfig.asStateFlow()

    private val _activeArrows = MutableStateFlow<List<Arrow>>(emptyList())
    val activeArrows: StateFlow<List<Arrow>> = _activeArrows.asStateFlow()

    private val _lives = MutableStateFlow(3)
    val lives: StateFlow<Int> = _lives.asStateFlow()

    private val _timeRemainingSeconds = MutableStateFlow(60)
    val timeRemainingSeconds: StateFlow<Int> = _timeRemainingSeconds.asStateFlow()

    private val _elapsedSeconds = MutableStateFlow(0)
    val elapsedSeconds: StateFlow<Int> = _elapsedSeconds.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _hintArrowId = MutableStateFlow<Int?>(null)
    val hintArrowId: StateFlow<Int?> = _hintArrowId.asStateFlow()

    private val _blockedArrowId = MutableStateFlow<Int?>(null)
    val blockedArrowId: StateFlow<Int?> = _blockedArrowId.asStateFlow()

    private val _isMagnifierActive = MutableStateFlow(false)
    val isMagnifierActive: StateFlow<Boolean> = _isMagnifierActive.asStateFlow()

    private val _isPaletteMenuOpen = MutableStateFlow(false)
    val isPaletteMenuOpen: StateFlow<Boolean> = _isPaletteMenuOpen.asStateFlow()

    private val _selectedPalette = MutableStateFlow(PaletteTheme.ClassicIndigo)
    val selectedPalette: StateFlow<PaletteTheme> = _selectedPalette.asStateFlow()

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver: StateFlow<Boolean> = _isGameOver.asStateFlow()

    private val _gameOverReason = MutableStateFlow("")
    val gameOverReason: StateFlow<String> = _gameOverReason.asStateFlow()

    private val _victoryStats = MutableStateFlow<VictoryStats?>(null)
    val victoryStats: StateFlow<VictoryStats?> = _victoryStats.asStateFlow()

    private val _showSettingsDialog = MutableStateFlow(false)
    val showSettingsDialog: StateFlow<Boolean> = _showSettingsDialog.asStateFlow()

    private val _flyingTokens = MutableStateFlow<List<FlyingTokenEvent>>(emptyList())
    val flyingTokens: StateFlow<List<FlyingTokenEvent>> = _flyingTokens.asStateFlow()

    private var scoreBadgePosition: Offset = Offset(500f, 120f)
    private var timerJob: Job? = null

    fun setScoreBadgePosition(pos: Offset) {
        scoreBadgePosition = pos
    }

    fun navigateToLevelSelect() {
        timerJob?.cancel()
        _screenState.value = ScreenState.LEVEL_SELECT
    }

    fun startLevel(levelNum: Int) {
        timerJob?.cancel()
        val config = LevelDefinitions.getLevel(levelNum)
        _currentLevelNumber.value = levelNum
        _currentLevelConfig.value = config
        _selectedPalette.value = config.defaultPalette
        _screenState.value = ScreenState.LEVEL_LOADING

        viewModelScope.launch {
            delay(900) // Animated transition loading
            initLevelGameplay(config)
            _screenState.value = ScreenState.PLAYING
        }
    }

    private fun initLevelGameplay(config: LevelConfig) {
        _activeArrows.value = config.initialArrows
        _lives.value = 3
        _timeRemainingSeconds.value = config.timeLimitSeconds
        _elapsedSeconds.value = 0
        _score.value = 0
        _hintArrowId.value = null
        _blockedArrowId.value = null
        _isGameOver.value = false
        _gameOverReason.value = ""
        _flyingTokens.value = emptyList()

        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_timeRemainingSeconds.value > 0 && !_isGameOver.value && _screenState.value == ScreenState.PLAYING) {
                delay(1000)
                _timeRemainingSeconds.value = _timeRemainingSeconds.value - 1
                _elapsedSeconds.value = _elapsedSeconds.value + 1

                if (_timeRemainingSeconds.value <= 0) {
                    triggerGameOver("Time's Up! ⏰")
                }
            }
        }
    }

    fun onArrowTapped(arrow: Arrow, screenPos: Offset) {
        if (_isGameOver.value || _screenState.value != ScreenState.PLAYING) return
        val currentArrows = _activeArrows.value
        val config = _currentLevelConfig.value

        val isFree = LevelSolver.isArrowFree(arrow, currentArrows, config.gridWidth, config.gridHeight)

        if (isFree) {
            // Correct Free Arrow Tap!
            soundManager.playSwoosh()
            soundManager.playCoinChime()
            soundManager.triggerSuccessHaptic()

            // Spawn golden coin trajectory
            val token = FlyingTokenEvent(
                type = TokenType.GOLDEN_COIN,
                startOffset = screenPos,
                targetOffset = scoreBadgePosition,
                valueTag = "+10"
            )
            _flyingTokens.value = _flyingTokens.value + token
            _score.value = _score.value + 10

            // If this arrow was hinted, clear hint
            if (_hintArrowId.value == arrow.id) {
                _hintArrowId.value = null
            }

            // Remove arrow from active board
            val updated = currentArrows.filter { it.id != arrow.id }
            _activeArrows.value = updated

            // Check if level completed!
            if (updated.isEmpty()) {
                handleLevelCompleted()
            }
        } else {
            // Blocked Arrow Tap!
            soundManager.playPenaltyBuzzer()
            soundManager.triggerErrorHaptic()

            // Red collision flash and shake
            _blockedArrowId.value = arrow.id

            // Penalty token trajectory
            val token = FlyingTokenEvent(
                type = TokenType.ANGRY_PENALTY,
                startOffset = screenPos,
                targetOffset = scoreBadgePosition,
                valueTag = "-10"
            )
            _flyingTokens.value = _flyingTokens.value + token
            _score.value = (_score.value - 10).coerceAtLeast(0)

            // Deduct life
            val newLives = _lives.value - 1
            _lives.value = newLives
            if (newLives <= 0) {
                triggerGameOver("Out of Lives! 💔")
            }
        }
    }

    fun onTokenAnimationCompleted(event: FlyingTokenEvent) {
        _flyingTokens.value = _flyingTokens.value.filter { it.id != event.id }
    }

    private fun handleLevelCompleted() {
        timerJob?.cancel()
        soundManager.playVictoryFanfare()

        val totalTime = _elapsedSeconds.value
        val timeLimit = _currentLevelConfig.value.timeLimitSeconds
        val savedSec = (timeLimit - totalTime).coerceAtLeast(0)

        // Star rating calculation
        val stars = when {
            _lives.value == 3 && savedSec >= 20 -> 3
            _lives.value >= 2 -> 2
            else -> 1
        }

        val bonusSpeedCallout = "Completed ${savedSec}s before 1 min limit! ⚡"
        val nextLvl = if (_currentLevelNumber.value < LevelDefinitions.levels.size) {
            _currentLevelNumber.value + 1
        } else null

        val finalScore = _score.value + (stars * 50) + (savedSec * 5)
        _score.value = finalScore

        viewModelScope.launch {
            repository.saveLevelCompletion(
                levelNumber = _currentLevelNumber.value,
                stars = stars,
                score = finalScore,
                timeSeconds = totalTime,
                nextLevelNumber = nextLvl
            )
            repository.addCoins(finalScore)
        }

        _victoryStats.value = VictoryStats(
            levelNumber = _currentLevelNumber.value,
            stars = stars,
            score = finalScore,
            timeTakenSeconds = totalTime,
            timeRemainingSeconds = _timeRemainingSeconds.value,
            bonusSpeedText = bonusSpeedCallout,
            levelConfig = _currentLevelConfig.value
        )

        _screenState.value = ScreenState.LEVEL_COMPLETE
    }

    private fun triggerGameOver(reason: String) {
        timerJob?.cancel()
        _isGameOver.value = true
        _gameOverReason.value = reason
        soundManager.playPenaltyBuzzer()
    }

    fun restartCurrentLevel() {
        startLevel(_currentLevelNumber.value)
    }

    fun nextLevel() {
        val next = _currentLevelNumber.value + 1
        if (next <= LevelDefinitions.levels.size) {
            startLevel(next)
        } else {
            navigateToLevelSelect()
        }
    }

    fun continueGameOver(extraHearts: Boolean = true) {
        if (extraHearts) {
            _lives.value = 3
        }
        if (_timeRemainingSeconds.value <= 0) {
            _timeRemainingSeconds.value = 30
        }
        _isGameOver.value = false
        _gameOverReason.value = ""
        startTimer()
    }

    fun useHint() {
        val freeArrow = LevelSolver.findAvailableFreeArrow(
            _activeArrows.value,
            _currentLevelConfig.value.gridWidth,
            _currentLevelConfig.value.gridHeight
        )
        if (freeArrow != null) {
            viewModelScope.launch {
                val stats = gameStats.value
                val count = stats?.hintsRemaining ?: 5
                if (count > 0) {
                    repository.useHint()
                    _hintArrowId.value = freeArrow.id
                    soundManager.playHintTone()
                } else {
                    // Give free hint for fun!
                    _hintArrowId.value = freeArrow.id
                    soundManager.playHintTone()
                }
            }
        }
    }

    fun toggleMagnifier() {
        _isMagnifierActive.value = !_isMagnifierActive.value
        soundManager.playClick()
    }

    fun togglePaletteMenu() {
        _isPaletteMenuOpen.value = !_isPaletteMenuOpen.value
        soundManager.playClick()
    }

    fun selectPalette(palette: PaletteTheme) {
        _selectedPalette.value = palette
        _isPaletteMenuOpen.value = false
        soundManager.playClick()
    }

    fun toggleSettingsDialog(show: Boolean) {
        _showSettingsDialog.value = show
        soundManager.playClick()
    }

    fun updateSoundSettings(soundEnabled: Boolean, hapticsEnabled: Boolean, volume: Float) {
        soundManager.isSoundEnabled = soundEnabled
        soundManager.isHapticsEnabled = hapticsEnabled
        soundManager.sfxVolume = volume

        viewModelScope.launch {
            val stats = gameStats.value ?: GameStats()
            repository.updateStats(
                stats.copy(
                    soundEnabled = soundEnabled,
                    hapticsEnabled = hapticsEnabled,
                    sfxVolume = volume
                )
            )
        }
    }

    fun resetAllGameProgress() {
        viewModelScope.launch {
            repository.resetAll(LevelDefinitions.levels.size)
            navigateToLevelSelect()
        }
    }
}
