package com.example.ai

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Manages the background download, local caching, and lifecycle of the on-device LLM model.
 * Model URL: https://github.com/decoelectroclima-ctrl/Atalaya-/releases/download/Gemma/gemma3-270m-it-q8.task
 */
object OnDeviceModelManager {
    const val MODEL_URL = "https://github.com/decoelectroclima-ctrl/Atalaya-/releases/download/Gemma/gemma3-270m-it-q8.task"
    const val MODEL_FILE_NAME = "gemma3-270m-it-q8.task"

    sealed class ModelState {
        data object NotDownloaded : ModelState()
        data class Downloading(val progress: Float) : ModelState()
        data class Ready(val modelFile: File) : ModelState()
        data class Error(val message: String) : ModelState()
    }

    private val _modelState = MutableStateFlow<ModelState>(ModelState.NotDownloaded)
    val modelState: StateFlow<ModelState> = _modelState.asStateFlow()

    private var isDownloading = false

    fun init(context: Context) {
        val modelFile = File(context.filesDir, MODEL_FILE_NAME)
        if (modelFile.exists() && modelFile.length() > 1024 * 1024) {
            _modelState.value = ModelState.Ready(modelFile)
            OnDeviceLlmEngine.setModelReady(true)
        } else {
            _modelState.value = ModelState.NotDownloaded
            // Start background download automatically
            startDownloadInBackground(context)
        }
    }

    fun startDownloadInBackground(context: Context) {
        if (isDownloading) return
        val modelFile = File(context.filesDir, MODEL_FILE_NAME)
        if (modelFile.exists() && modelFile.length() > 1024 * 1024) {
            _modelState.value = ModelState.Ready(modelFile)
            OnDeviceLlmEngine.setModelReady(true)
            return
        }

        isDownloading = true
        CoroutineScope(Dispatchers.IO).launch {
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
                            _modelState.value = ModelState.Ready(modelFile)
                            OnDeviceLlmEngine.setModelReady(true)
                        } else {
                            _modelState.value = ModelState.Error("No se pudo renombrar el archivo del modelo.")
                        }
                    } finally {
                        input?.close()
                        output?.close()
                        connection.disconnect()
                    }
                } else {
                    _modelState.value = ModelState.Error("HTTP Error: ${connection.responseCode}")
                }
            } catch (e: Exception) {
                _modelState.value = ModelState.Error(e.localizedMessage ?: "Error de descarga del modelo")
            } finally {
                isDownloading = false
            }
        }
    }
}
