package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.FlyingTokenItem
import com.example.ui.components.GameHUD
import com.example.ui.components.GameOverDialog
import com.example.ui.components.SettingsDialog
import com.example.ui.components.ToolButtons
import com.example.ui.game.GameBoardView
import com.example.viewmodel.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val levelNumber by viewModel.currentLevelNumber.collectAsStateWithLifecycle()
    val levelConfig by viewModel.currentLevelConfig.collectAsStateWithLifecycle()
    val activeArrows by viewModel.activeArrows.collectAsStateWithLifecycle()
    val lives by viewModel.lives.collectAsStateWithLifecycle()
    val timeRemaining by viewModel.timeRemainingSeconds.collectAsStateWithLifecycle()
    val score by viewModel.score.collectAsStateWithLifecycle()
    val hintArrowId by viewModel.hintArrowId.collectAsStateWithLifecycle()
    val blockedArrowId by viewModel.blockedArrowId.collectAsStateWithLifecycle()
    val isMagnifierActive by viewModel.isMagnifierActive.collectAsStateWithLifecycle()
    val isPaletteMenuOpen by viewModel.isPaletteMenuOpen.collectAsStateWithLifecycle()
    val selectedPalette by viewModel.selectedPalette.collectAsStateWithLifecycle()
    val isGameOver by viewModel.isGameOver.collectAsStateWithLifecycle()
    val gameOverReason by viewModel.gameOverReason.collectAsStateWithLifecycle()
    val showSettings by viewModel.showSettingsDialog.collectAsStateWithLifecycle()
    val flyingTokens by viewModel.flyingTokens.collectAsStateWithLifecycle()
    val stats by viewModel.gameStats.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = selectedPalette.backgroundGradient
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Top Stats HUD
            GameHUD(
                levelNumber = levelNumber,
                levelTitle = levelConfig.title,
                activeArrowsCount = activeArrows.size,
                timeRemainingSeconds = timeRemaining,
                livesRemaining = lives,
                difficulty = levelConfig.difficulty,
                score = score,
                onScoreBadgePositioned = { viewModel.setScoreBadgePosition(it) },
                onBackClick = { viewModel.navigateToLevelSelect() },
                onRestartClick = { viewModel.restartCurrentLevel() },
                onSettingsClick = { viewModel.toggleSettingsDialog(true) }
            )

            // 2. Center Interactive Puzzle Board
            GameBoardView(
                gridWidth = levelConfig.gridWidth,
                gridHeight = levelConfig.gridHeight,
                activeArrows = activeArrows,
                palette = selectedPalette,
                hintArrowId = hintArrowId,
                blockedArrowId = blockedArrowId,
                isMagnifierActive = isMagnifierActive,
                onArrowTapped = { arrow, pos -> viewModel.onArrowTapped(arrow, pos) },
                onArrowExitCompleted = { /* Handled in VM */ },
                modifier = Modifier.weight(1f, fill = false)
            )

            // 3. Floating Bottom Tool Buttons
            ToolButtons(
                hintsCount = stats?.hintsRemaining ?: 5,
                isMagnifierActive = isMagnifierActive,
                isPaletteMenuOpen = isPaletteMenuOpen,
                selectedPalette = selectedPalette,
                onHintClick = { viewModel.useHint() },
                onMagnifierToggle = { viewModel.toggleMagnifier() },
                onPaletteToggle = { viewModel.togglePaletteMenu() },
                onPaletteSelect = { viewModel.selectPalette(it) }
            )
        }

        // 4. Flying Coins & Penalty Tokens Trajectory Layer
        flyingTokens.forEach { tokenEvent ->
            FlyingTokenItem(
                event = tokenEvent,
                onCompleted = { viewModel.onTokenAnimationCompleted(it) }
            )
        }

        // 5. Game Over Modal Dialog
        if (isGameOver) {
            GameOverDialog(
                reason = gameOverReason,
                onRetry = { viewModel.restartCurrentLevel() },
                onContinue = { viewModel.continueGameOver(extraHearts = true) },
                onMainMenu = { viewModel.navigateToLevelSelect() }
            )
        }

        // 6. Settings Modal Dialog
        if (showSettings) {
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
}
