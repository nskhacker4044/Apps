package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

/**
 * Real-time Audio Synthesizer and Haptics Manager.
 * Uses low-latency PCM synthesis via AudioTrack so all sound effects play
 * reliably on any Android device without requiring external audio assets.
 */
class SoundManager(private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.Default)

    var isSoundEnabled: Boolean = true
    var isHapticsEnabled: Boolean = true
    var sfxVolume: Float = 0.8f

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    private fun playPcmTone(
        sampleRate: Int = 22050,
        samples: ShortArray
    ) {
        if (!isSoundEnabled || sfxVolume <= 0.01f) return
        scope.launch {
            try {
                val bufferSize = samples.size * 2
                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.setVolume(sfxVolume)
                audioTrack.write(samples, 0, samples.size)
                audioTrack.play()
                // Wait for playback and release
                val durationMs = (samples.size * 1000L) / sampleRate + 50L
                kotlinx.coroutines.delay(durationMs)
                audioTrack.stop()
                audioTrack.release()
            } catch (_: Exception) {
                // Ignore audio track fallback
            }
        }
    }

    /**
     * Crisp swoosh tone for an escaping arrow
     */
    fun playSwoosh() {
        if (!isSoundEnabled) return
        val sampleRate = 22050
        val durationMs = 180
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val samples = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            // Frequency sweep 300Hz -> 850Hz with noise modulation
            val freq = 300.0 + progress * 550.0
            val envelope = (1.0 - progress) * progress * 4.0 // parabolic envelope
            val wave = sin(2.0 * PI * freq * t)
            val noise = (Math.random() * 2.0 - 1.0) * 0.25 * (1.0 - progress)
            val value = ((wave * 0.75 + noise) * envelope * 0.9 * Short.MAX_VALUE).toInt()
            samples[i] = value.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        playPcmTone(sampleRate, samples)
    }

    /**
     * Golden coin chime (+10 points) - sparkling high two-tone chord
     */
    fun playCoinChime() {
        if (!isSoundEnabled) return
        val sampleRate = 22050
        val durationMs = 280
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val samples = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            val envelope = Math.exp(-progress * 5.0) // exponential decay
            val f1 = 1318.51 // E6
            val f2 = 1760.00 // A6
            val f3 = 2637.02 // E7
            val wave = sin(2.0 * PI * f1 * t) * 0.4 +
                    sin(2.0 * PI * f2 * t) * 0.35 +
                    sin(2.0 * PI * f3 * t) * 0.25
            val value = (wave * envelope * 0.85 * Short.MAX_VALUE).toInt()
            samples[i] = value.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        playPcmTone(sampleRate, samples)
    }

    /**
     * Angry penalty buzzer (-10 points and blocked collision)
     */
    fun playPenaltyBuzzer() {
        if (!isSoundEnabled) return
        val sampleRate = 22050
        val durationMs = 240
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val samples = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            val envelope = (1.0 - progress)
            // Low dissonant saw/square wave around 140Hz and 175Hz
            val f1 = 140.0
            val f2 = 185.0
            val wave1 = if (sin(2.0 * PI * f1 * t) >= 0) 0.5 else -0.5
            val wave2 = sin(2.0 * PI * f2 * t) * 0.5
            val value = ((wave1 + wave2) * envelope * 0.8 * Short.MAX_VALUE).toInt()
            samples[i] = value.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        playPcmTone(sampleRate, samples)
    }

    /**
     * Hint tone - ascending melodic sparkle
     */
    fun playHintTone() {
        if (!isSoundEnabled) return
        val sampleRate = 22050
        val durationMs = 320
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val samples = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            val stage = (progress * 4).toInt()
            val freq = when (stage) {
                0 -> 659.25  // E5
                1 -> 830.61  // G#5
                2 -> 987.77  // B5
                else -> 1318.51 // E6
            }
            val localProgress = (progress * 4) - stage
            val localEnv = (1.0 - localProgress * 0.7)
            val wave = sin(2.0 * PI * freq * t) * 0.7 + sin(4.0 * PI * freq * t) * 0.3
            val value = (wave * localEnv * 0.8 * Short.MAX_VALUE).toInt()
            samples[i] = value.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        playPcmTone(sampleRate, samples)
    }

    /**
     * Victory fanfare for completing a level
     */
    fun playVictoryFanfare() {
        if (!isSoundEnabled) return
        val sampleRate = 22050
        val durationMs = 600
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val samples = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            val stage = (progress * 5).toInt()
            val freq = when (stage) {
                0 -> 523.25 // C5
                1 -> 659.25 // E5
                2 -> 783.99 // G5
                3 -> 1046.50 // C6
                else -> 1318.51 // E6 triumphant chord
            }
            val env = Math.exp(-((progress * 5) % 1.0) * 2.0)
            val wave = sin(2.0 * PI * freq * t) * 0.6 +
                    sin(2.0 * PI * (freq * 1.5) * t) * 0.25 +
                    sin(2.0 * PI * (freq * 2.0) * t) * 0.15
            val value = (wave * env * 0.9 * Short.MAX_VALUE).toInt()
            samples[i] = value.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        playPcmTone(sampleRate, samples)
    }

    /**
     * Click / UI button feedback
     */
    fun playClick() {
        if (!isSoundEnabled) return
        val sampleRate = 22050
        val durationMs = 40
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val samples = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            val env = 1.0 - progress
            val wave = sin(2.0 * PI * 800.0 * t) * env
            val value = (wave * 0.7 * Short.MAX_VALUE).toInt()
            samples[i] = value.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        playPcmTone(sampleRate, samples)
    }

    /**
     * Haptic feedback: light click or error rattle
     */
    fun triggerTapHaptic() {
        if (!isHapticsEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(20)
            }
        } catch (_: Exception) {}
    }

    fun triggerErrorHaptic() {
        if (!isHapticsEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 40, 50, 40)
                val amplitudes = intArrayOf(0, 200, 0, 255)
                vibrator?.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(100)
            }
        } catch (_: Exception) {}
    }

    fun triggerSuccessHaptic() {
        if (!isHapticsEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 30, 40, 50)
                val amplitudes = intArrayOf(0, 150, 0, 220)
                vibrator?.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(60)
            }
        } catch (_: Exception) {}
    }
}
