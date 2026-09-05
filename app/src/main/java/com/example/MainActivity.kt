package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.SettingsDialog
import com.example.ui.screens.GameScreen
import com.example.ui.screens.LevelCompleteScreen
import com.example.ui.screens.LevelLoadingScreen
import com.example.ui.screens.LevelSelectScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GameViewModel
import com.example.viewmodel.ScreenState

class MainActivity : ComponentActivity() {

    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    TapAwayArrowsApp(viewModel = gameViewModel)
                }
            }
        }
    }
}

@Composable
fun TapAwayArrowsApp(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val progressList by viewModel.allLevelProgress.collectAsStateWithLifecycle()
    val stats by viewModel.gameStats.collectAsStateWithLifecycle()
    val currentLevelConfig by viewModel.currentLevelConfig.collectAsStateWithLifecycle()
    val victoryStats by viewModel.victoryStats.collectAsStateWithLifecycle()
    val showSettings by viewModel.showSettingsDialog.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = screenState,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "screen_transition",
        modifier = modifier.fillMaxSize()
    ) { targetScreen ->
        when (targetScreen) {
            ScreenState.SPLASH -> {
                SplashScreen(
                    onStartGame = { viewModel.navigateToLevelSelect() }
                )
            }

            ScreenState.LEVEL_SELECT -> {
                LevelSelectScreen(
                    progressList = progressList,
                    stats = stats,
                    onLevelSelected = { levelNum -> viewModel.startLevel(levelNum) },
                    onOpenSettings = { viewModel.toggleSettingsDialog(true) }
                )
            }

            ScreenState.LEVEL_LOADING -> {
                LevelLoadingScreen(levelConfig = currentLevelConfig)
            }

            ScreenState.PLAYING -> {
                GameScreen(viewModel = viewModel)
            }

            ScreenState.LEVEL_COMPLETE -> {
                if (victoryStats != null) {
                    LevelCompleteScreen(
                        stats = victoryStats!!,
                        onNextLevel = { viewModel.nextLevel() },
                        onMainSelect = { viewModel.navigateToLevelSelect() }
                    )
                } else {
                    viewModel.navigateToLevelSelect()
                }
            }
        }
    }

    if (showSettings && screenState != ScreenState.PLAYING) {
        SettingsDialog(
            isSoundEnabled = viewModel.soundManager.isSoundEnabled,
            isHapticsEnabled = viewModel.soundManager.isHapticsEnabled,
            sfxVolume = viewModel.soundManager.sfxVolume,
            onSaveSettings = { snd, hapt, vol ->
                viewModel.updateSoundSettings(snd, hapt, vol)
            },
            onResetProgress = { viewModel.resetAllGameProgress() },
            onDismiss = { viewModel.toggleSettingsDialog(false) }
        )
    }
}
