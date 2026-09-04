package com.example.ai

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Manages the background download, local caching, and lifecycle of the on-device Gemma 3 LLM model.
 * Model URL: https://github.com/decoelectroclima-ctrl/Atalaya-/releases/download/Gemma/gemma3-270m-it-q8.task
 *
 * Polished lifecycle:
 * - Installs by default on first run (not optional).
 * - Can be disconnected (desactivado temporalmente) without deleting the file.
 * - Can be deleted (eliminado del dispositivo para liberar espacio) from user Profile.
 * - Can be re-connected or re-installed anytime.
 */
object OnDeviceModelManager {
    const val MODEL_URL = "https://github.com/decoelectroclima-ctrl/Atalaya-/releases/download/Gemma/gemma3-270m-it-q8.task"
    const val MODEL_FILE_NAME = "gemma3-270m-it-q8.task"

    private const val PREFS_NAME = "atalaya_gemma_prefs"
    private const val KEY_MODEL_ENABLED = "key_model_enabled"
    private const val KEY_EXPLICITLY_DELETED = "key_model_explicitly_deleted"

    sealed class ModelState {
        data class NotDownloaded(val isExplicitlyDeleted: Boolean = false) : ModelState()
        data class Downloading(val progress: Float) : ModelState()
        data class Ready(val modelFile: File) : ModelState()
        data class Disconnected(val modelFile: File) : ModelState()
        data class Error(val message: String) : ModelState()
    }

    private val _modelState = MutableStateFlow<ModelState>(ModelState.NotDownloaded())
    val modelState: StateFlow<ModelState> = _modelState.asStateFlow()

    private var downloadJob: Job? = null
    private var isDownloading = false

    private fun getPrefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun init(context: Context) {
        val prefs = getPrefs(context)
        val isExplicitlyDeleted = prefs.getBoolean(KEY_EXPLICITLY_DELETED, false)
        val isEnabled = prefs.getBoolean(KEY_MODEL_ENABLED, true)
        val modelFile = File(context.filesDir, MODEL_FILE_NAME)

        if (modelFile.exists() && modelFile.length() > 1024 * 1024) {
            if (!isEnabled) {
                _modelState.value = ModelState.Disconnected(modelFile)
                OnDeviceLlmEngine.setModelReady(false)
            } else {
                _modelState.value = ModelState.Ready(modelFile)
                OnDeviceLlmEngine.setModelReady(true)
            }
        } else {
            // Por defecto no es opcional: se instala automáticamente si no se ha eliminado explícitamente
            if (!isExplicitlyDeleted) {
                _modelState.value = ModelState.NotDownloaded(isExplicitlyDeleted = false)
                startDownloadInBackground(context)
            } else {
                _modelState.value = ModelState.NotDownloaded(isExplicitlyDeleted = true)
                OnDeviceLlmEngine.setModelReady(false)
            }
        }
    }

    fun startDownloadInBackground(context: Context) {
        if (isDownloading) return
        val modelFile = File(context.filesDir, MODEL_FILE_NAME)
        val prefs = getPrefs(context)
        val isEnabled = prefs.getBoolean(KEY_MODEL_ENABLED, true)

        if (modelFile.exists() && modelFile.length() > 1024 * 1024) {
            if (isEnabled) {
                _modelState.value = ModelState.Ready(modelFile)
                OnDeviceLlmEngine.setModelReady(true)
            } else {
                _modelState.value = ModelState.Disconnected(modelFile)
                OnDeviceLlmEngine.setModelReady(false)
            }
            return
        }

        isDownloading = true
        downloadJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                _modelState.value = ModelState.Downloading(0f)
                val tempFile = File(context.filesDir, "$MODEL_FILE_NAME.tmp")
                val url = URL(MODEL_URL)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 15000
                connection.readTimeout = 30000
                connection.instanceFollowRedirects = true
                connection.connect()

                if (connection.responseCode in 200..299) {
                    val fileLength = connection.contentLength
                    var input: InputStream? = null
                    var output: FileOutputStream? = null
                    try {
                        input = connection.inputStream
                        output = FileOutputStream(tempFile)

                        val data = ByteArray(8192)
                        var total: Long = 0
                        var count: Int

                        while (input.read(data).also { count = it } != -1) {
                            total += count.toLong()
                            if (fileLength > 0) {
                                val progress = (total.toFloat() / fileLength.toFloat()).coerceIn(0f, 1f)
                                _modelState.value = ModelState.Downloading(progress)
                            }
                            output.write(data, 0, count)
                        }

                        output.flush()
                        if (tempFile.renameTo(modelFile)) {
                            prefs.edit()
                                .putBoolean(KEY_EXPLICITLY_DELETED, false)
                                .putBoolean(KEY_MODEL_ENABLED, true)
                                .apply()

                            _modelState.value = ModelState.Ready(modelFile)
                            OnDeviceLlmEngine.setModelReady(true)
                            try {
                                com.example.notifications.SoltarNotificationHelper.showAppReadyNotification(context)
                            } catch (_: Exception) {}
                        } else {
                            val modelFileFallback = File(context.filesDir, MODEL_FILE_NAME)
                            _modelState.value = ModelState.Disconnected(modelFileFallback)
                            OnDeviceLlmEngine.setModelReady(false)
                        }
                    } finally {
                        input?.close()
                        output?.close()
                        connection.disconnect()
                    }
                } else {
                    val modelFileFallback = File(context.filesDir, MODEL_FILE_NAME)
                    _modelState.value = ModelState.Disconnected(modelFileFallback)
                    OnDeviceLlmEngine.setModelReady(false)
                }
            } catch (_: Exception) {
                val modelFileFallback = File(context.filesDir, MODEL_FILE_NAME)
                _modelState.value = ModelState.Disconnected(modelFileFallback)
                OnDeviceLlmEngine.setModelReady(false)
            } finally {
                isDownloading = false
            }
        }
    }

    /**
     * Desconecta el modelo sin borrar el archivo local del dispositivo.
     * La app pasa a usar los generadores clínicos deterministas de contingencia.
     */
    fun disconnectModel(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit().putBoolean(KEY_MODEL_ENABLED, false).apply()

        val modelFile = File(context.filesDir, MODEL_FILE_NAME)
        if (modelFile.exists() && modelFile.length() > 1024 * 1024) {
            _modelState.value = ModelState.Disconnected(modelFile)
        } else {
            downloadJob?.cancel()
            isDownloading = false
            _modelState.value = ModelState.NotDownloaded(isExplicitlyDeleted = false)
        }
        OnDeviceLlmEngine.setModelReady(false)
    }

    /**
     * Vuelve a conectar y activar el modelo Gemma 3 para inferencia local.
     */
    fun connectModel(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit()
            .putBoolean(KEY_MODEL_ENABLED, true)
            .putBoolean(KEY_EXPLICITLY_DELETED, false)
            .apply()

        val modelFile = File(context.filesDir, MODEL_FILE_NAME)
        if (modelFile.exists() && modelFile.length() > 1024 * 1024) {
            _modelState.value = ModelState.Ready(modelFile)
            OnDeviceLlmEngine.setModelReady(true)
        } else {
            startDownloadInBackground(context)
        }
    }

    /**
     * Elimina el archivo del modelo del almacenamiento local para liberar espacio.
     */
    fun deleteModel(context: Context) {
        downloadJob?.cancel()
        isDownloading = false

        val prefs = getPrefs(context)
        prefs.edit()
            .putBoolean(KEY_EXPLICITLY_DELETED, true)
            .putBoolean(KEY_MODEL_ENABLED, false)
            .apply()

        val modelFile = File(context.filesDir, MODEL_FILE_NAME)
        if (modelFile.exists()) {
            modelFile.delete()
        }
        val tempFile = File(context.filesDir, "$MODEL_FILE_NAME.tmp")
        if (tempFile.exists()) {
            tempFile.delete()
        }

        _modelState.value = ModelState.NotDownloaded(isExplicitlyDeleted = true)
        OnDeviceLlmEngine.setModelReady(false)
    }

    /**
     * Vuelve a instalar el modelo tras haber sido eliminado.
     */
    fun reinstallModel(context: Context) {
        val prefs = getPrefs(context)
        prefs.edit()
            .putBoolean(KEY_EXPLICITLY_DELETED, false)
            .putBoolean(KEY_MODEL_ENABLED, true)
            .apply()
        startDownloadInBackground(context)
    }

    fun isModelConnected(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_MODEL_ENABLED, true)
    }
}
