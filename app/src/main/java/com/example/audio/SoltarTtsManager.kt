package com.example.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale

/**
 * TextToSpeech manager for ADRIANA's guided voice meditations and regulation practices.
 * Dynamically adjusts speech cadence and pitch based on user vulnerability.
 */
object SoltarTtsManager {

    private const val TAG = "SoltarTtsManager"
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var isSpeakingState = false

    var onSpeakingStateChanged: ((Boolean) -> Unit)? = null

    fun initialize(context: Context, onReady: (() -> Unit)? = null) {
        if (tts != null && isInitialized) {
            onReady?.invoke()
            return
        }

        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale("es", "ES"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Fallback to default Spanish or device default
                    tts?.setLanguage(Locale("es"))
                }
                isInitialized = true
                Log.d(TAG, "TTS initialized successfully")
                onReady?.invoke()
            } else {
                Log.e(TAG, "TTS initialization failed with status $status")
            }
        }

        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                isSpeakingState = true
                onSpeakingStateChanged?.invoke(true)
            }

            override fun onDone(utteranceId: String?) {
                isSpeakingState = false
                onSpeakingStateChanged?.invoke(false)
            }

            override fun onError(utteranceId: String?) {
                isSpeakingState = false
                onSpeakingStateChanged?.invoke(false)
            }
        })
    }

    /**
     * Speaks the meditation script, adjusting speech rate and pitch based on vulnerability:
     * - High vulnerability (>= 70): slower rate (0.82f), lower/calmer pitch (0.92f)
     * - Moderate vulnerability (35-69): balanced rate (0.92f), natural pitch (1.0f)
     * - Low vulnerability (< 35): energetic/fluid rate (1.05f), bright pitch (1.05f)
     */
    fun speakMeditation(
        text: String,
        vulnerabilityScore: Int,
        onDone: (() -> Unit)? = null
    ) {
        if (tts == null || !isInitialized) return

        val (rate, pitch) = when {
            vulnerabilityScore >= 70 -> Pair(0.82f, 0.92f) // slow and calm
            vulnerabilityScore >= 35 -> Pair(0.92f, 1.0f)  // centered and steady
            else -> Pair(1.02f, 1.04f)                     // active and empowering
        }

        tts?.setSpeechRate(rate)
        tts?.setPitch(pitch)

        val utteranceId = "meditation_${System.currentTimeMillis()}"
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun stop() {
        tts?.stop()
        isSpeakingState = false
        onSpeakingStateChanged?.invoke(false)
    }

    fun isSpeaking(): Boolean = isSpeakingState

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
        tts = null
        isInitialized = false
        isSpeakingState = false
    }
}
