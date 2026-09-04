package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * SoltarSoundManager
 * High-craft acoustic synthesizer for meditative, grounded audio feedback
 * (Tibetan singing bowls, warm resonant chimes, tactile haptics).
 * Generates pure harmonic PCM audio buffers on-demand without external dependencies.
 */
object SoltarSoundManager {

    var isSoundEnabled: Boolean = true
    private val scope = CoroutineScope(Dispatchers.Default)

    enum class SoundType {
        WARM_CHIME,
        TAP,
        URGE_ALERT,
        CALM_BELL,
        BREATH_IN,
        BREATH_OUT,
        HEARTBEAT
    }

    enum class SoundscapeType {
        KINTSUGI_RAIN,
        OCEAN_WAVES,
        FOREST_CALM,
        DEEP_SILENCE
    }

    private var activeSoundscapeTrack: AudioTrack? = null
    private var isSoundscapePlaying = false

    fun playSound(type: SoundType) {
        if (!isSoundEnabled) return
        scope.launch {
            try {
                when (type) {
                    SoundType.WARM_CHIME -> generateSingingBowl(baseFreq = 432.0, durationMs = 1800, harmonics = doubleArrayOf(1.0, 2.76, 5.4), weights = doubleArrayOf(0.6, 0.25, 0.1))
                    SoundType.TAP -> generateSoftClick(durationMs = 45, freq = 520.0)
                    SoundType.URGE_ALERT -> generateSingingBowl(baseFreq = 216.0, durationMs = 1200, harmonics = doubleArrayOf(1.0, 1.5, 2.0), weights = doubleArrayOf(0.7, 0.2, 0.1))
                    SoundType.CALM_BELL -> generateSingingBowl(baseFreq = 528.0, durationMs = 2200, harmonics = doubleArrayOf(1.0, 2.0, 3.0), weights = doubleArrayOf(0.6, 0.3, 0.1))
                    SoundType.BREATH_IN -> generateSwell(durationMs = 2500, startFreq = 220.0, endFreq = 440.0)
                    SoundType.BREATH_OUT -> generateSwell(durationMs = 2500, startFreq = 440.0, endFreq = 220.0)
                    SoundType.HEARTBEAT -> generateHeartbeat()
                }
            } catch (_: Exception) {
                // Audio track fallback gracefully ignores interruptions
            }
        }
    }

    fun startSoundscape(type: SoundscapeType) {
        if (!isSoundEnabled) return
        stopSoundscape()
        isSoundscapePlaying = true
        scope.launch {
            try {
                val sampleRate = 44100
                val durationSec = 6
                val numSamples = sampleRate * durationSec
                val buffer = ShortArray(numSamples)

                val random = java.util.Random(42)
                for (i in 0 until numSamples) {
                    val t = i.toDouble() / sampleRate
                    var sample = 0.0
                    when (type) {
                        SoundscapeType.KINTSUGI_RAIN -> {
                            val white = (random.nextDouble() * 2.0 - 1.0)
                            val lowPass = sin(2.0 * PI * 120.0 * t) * 0.3
                            sample = (white * 0.25 + lowPass * 0.2) * 12000.0
                        }
                        SoundscapeType.OCEAN_WAVES -> {
                            val swell = (sin(2.0 * PI * 0.15 * t) * 0.5 + 0.5)
                            val white = (random.nextDouble() * 2.0 - 1.0)
                            sample = white * swell * 10000.0
                        }
                        SoundscapeType.FOREST_CALM -> {
                            val bowl = sin(2.0 * PI * 432.0 * t) * 0.4 + sin(2.0 * PI * 864.0 * t) * 0.1
                            val breeze = (random.nextDouble() * 2.0 - 1.0) * 0.15
                            sample = (bowl + breeze) * 14000.0
                        }
                        SoundscapeType.DEEP_SILENCE -> {
                            val freq = 528.0
                            sample = (sin(2.0 * PI * freq * t) * 0.5 + sin(2.0 * PI * (freq * 1.5) * t) * 0.15) * 12000.0
                        }
                    }
                    buffer[i] = sample.coerceIn(-32767.0, 32767.0).toInt().toShort()
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
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
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(buffer, 0, buffer.size)
                audioTrack.setLoopPoints(0, buffer.size, -1)
                audioTrack.play()
                activeSoundscapeTrack = audioTrack
            } catch (_: Exception) {}
        }
    }

    fun stopSoundscape() {
        isSoundscapePlaying = false
        try {
            activeSoundscapeTrack?.stop()
            activeSoundscapeTrack?.release()
            activeSoundscapeTrack = null
        } catch (_: Exception) {}
    }

    private fun generateSingingBowl(
        baseFreq: Double,
        durationMs: Int,
        harmonics: DoubleArray,
        weights: DoubleArray
    ) {
        val sampleRate = 44100
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val decay = exp(-3.2 * t)
            var sample = 0.0

            for (h in harmonics.indices) {
                val f = baseFreq * harmonics[h]
                val w = weights[h]
                sample += w * sin(2.0 * PI * f * t)
            }

            // Apply soft attack envelope to eliminate clicking
            val attack = if (t < 0.03) (t / 0.03) else 1.0
            val finalVal = (sample * decay * attack * 18000.0).coerceIn(-32767.0, 32767.0)
            buffer[i] = finalVal.toInt().toShort()
        }

        playPcmBuffer(buffer, sampleRate)
    }

    private fun generateSoftClick(durationMs: Int, freq: Double) {
        val sampleRate = 44100
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val decay = exp(-50.0 * t)
            val sample = sin(2.0 * PI * freq * t) * decay * 14000.0
            buffer[i] = sample.coerceIn(-32767.0, 32767.0).toInt().toShort()
        }

        playPcmBuffer(buffer, sampleRate)
    }

    private fun generateSwell(durationMs: Int, startFreq: Double, endFreq: Double) {
        val sampleRate = 44100
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val t = i.toDouble() / sampleRate
            val currentFreq = startFreq + (endFreq - startFreq) * progress
            // Sine envelope for gentle swell and fade
            val envelope = sin(PI * progress)
            val sample = sin(2.0 * PI * currentFreq * t) * envelope * 12000.0
            buffer[i] = sample.coerceIn(-32767.0, 32767.0).toInt().toShort()
        }

        playPcmBuffer(buffer, sampleRate)
    }

    private fun generateHeartbeat() {
        val sampleRate = 44100
        val durationMs = 500
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)

        val thud1End = (sampleRate * 0.14).toInt()
        val gapEnd = (sampleRate * 0.20).toInt()
        val thud2End = (sampleRate * 0.38).toInt()

        for (i in 0 until numSamples) {
            var sample = 0.0

            if (i < thud1End) {
                val t1 = i.toDouble() / sampleRate
                val decay = exp(-35.0 * t1)
                sample += (sin(2.0 * PI * 68.0 * t1) * 0.8 + sin(2.0 * PI * 136.0 * t1) * 0.3) * decay * 31000.0
            } else if (i >= gapEnd && i < thud2End) {
                val t2 = (i - gapEnd).toDouble() / sampleRate
                val decay = exp(-45.0 * t2)
                sample += (sin(2.0 * PI * 85.0 * t2) * 0.7 + sin(2.0 * PI * 170.0 * t2) * 0.25) * decay * 28000.0
            }

            buffer[i] = sample.coerceIn(-32767.0, 32767.0).toInt().toShort()
        }

        playPcmBuffer(buffer, sampleRate)
    }

    private fun playPcmBuffer(buffer: ShortArray, sampleRate: Int) {
        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
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
            .setBufferSizeInBytes(buffer.size * 2)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        audioTrack.write(buffer, 0, buffer.size)
        audioTrack.play()
        audioTrack.setNotificationMarkerPosition(buffer.size)
        audioTrack.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
            override fun onMarkerReached(track: AudioTrack?) {
                try {
                    track?.stop()
                    track?.release()
                } catch (_: Exception) {}
            }
            override fun onPeriodicNotification(track: AudioTrack?) {}
        })
    }
}
