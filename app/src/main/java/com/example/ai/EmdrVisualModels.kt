package com.example.ai

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class EmdrAnimacionConfig(
    val frecuencia_hz: Float = 1.0f,
    val velocidad_comercial: String = "MEDIO", // RAPIDO, MEDIO, LENTO
    val estilo_esfera: String = "LINEAL" // GLOW, LINEAL, FADE
) {
    companion object {
        fun fromJson(json: JSONObject?): EmdrAnimacionConfig {
            if (json == null) return EmdrAnimacionConfig()
            val hz = json.optDouble("frecuencia_hz", 1.0).toFloat()
            val vel = json.optString("velocidad_comercial", "MEDIO").uppercase()
            val estilo = json.optString("estilo_esfera", "LINEAL").uppercase().trim()
            return EmdrAnimacionConfig(
                frecuencia_hz = if (hz > 0f) hz else 1.0f,
                velocidad_comercial = if (vel.isNotBlank()) vel else "MEDIO",
                estilo_esfera = if (estilo.isNotBlank()) estilo else "LINEAL"
            )
        }
    }
}

@Serializable
data class EmdrPaletaColoresConfig(
    val color_esfera_hex: String = "#8E9290",
    val color_fondo_hex: String = "#0D0D0D",
    val opacidad_texto: Float = 0.85f
) {
    fun parseEsferaColor(defaultColor: Color = Color(0xFF8E9290)): Color {
        return parseColorSafely(color_esfera_hex, defaultColor)
    }

    fun parseFondoColor(defaultColor: Color = Color(0xFF0D0D0D)): Color {
        return parseColorSafely(color_fondo_hex, defaultColor)
    }

    private fun parseColorSafely(hexStr: String, fallback: Color): Color {
        return try {
            val clean = hexStr.trim().removePrefix("#").trim()
            when (clean.length) {
                6 -> {
                    val colorLong = ("FF$clean").toLong(16)
                    Color(colorLong)
                }
                8 -> {
                    val colorLong = clean.toLong(16)
                    Color(colorLong)
                }
                3 -> {
                    // RGB short form #abc -> #aabbcc
                    val r = clean[0]
                    val g = clean[1]
                    val b = clean[2]
                    val expanded = "FF$r$r$g$g$b$b"
                    Color(expanded.toLong(16))
                }
                else -> fallback
            }
        } catch (_: Exception) {
            fallback
        }
    }

    companion object {
        fun fromJson(json: JSONObject?): EmdrPaletaColoresConfig {
            if (json == null) return EmdrPaletaColoresConfig()
            val esfera = json.optString("color_esfera_hex", "#8E9290")
            val fondo = json.optString("color_fondo_hex", "#0D0D0D")
            val opacidad = json.optDouble("opacidad_texto", 0.85).toFloat()
            return EmdrPaletaColoresConfig(
                color_esfera_hex = if (esfera.isNotBlank()) esfera else "#8E9290",
                color_fondo_hex = if (fondo.isNotBlank()) fondo else "#0D0D0D",
                opacidad_texto = opacidad.coerceIn(0.2f, 1.0f)
            )
        }
    }
}

@Serializable
data class EmdrInstruccionesConfig(
    val guia_visual: String = "Fije la mirada en la esfera a ritmo constante. Mantenga la cabeza quieta.",
    val alerta_audio: String = "Sincronice el sonido alterno en sus oídos izquierdo y derecho."
) {
    companion object {
        fun fromJson(json: JSONObject?): EmdrInstruccionesConfig {
            if (json == null) return EmdrInstruccionesConfig()
            val guia = json.optString("guia_visual", "Fije la mirada en la esfera a ritmo constante.")
            val audio = json.optString("alerta_audio", "Sincronice el sonido alterno bilateral.")
            return EmdrInstruccionesConfig(
                guia_visual = guia,
                alerta_audio = audio
            )
        }
    }
}

@Serializable
data class EmdrContenidoConfig(
    val texto_procesado: String = "[Firmeza interior. Movimiento bilateral] Dejo que la ola de ansiedad baje en calma."
) {
    companion object {
        fun fromJson(json: JSONObject?): EmdrContenidoConfig {
            if (json == null) return EmdrContenidoConfig()
            val texto = json.optString("texto_procesado", "")
            return EmdrContenidoConfig(texto_procesado = texto)
        }
    }
}

@Serializable
data class EmdrVisualConfig(
    val animacion: EmdrAnimacionConfig = EmdrAnimacionConfig(),
    val paleta_colores: EmdrPaletaColoresConfig = EmdrPaletaColoresConfig(),
    val instrucciones: EmdrInstruccionesConfig = EmdrInstruccionesConfig(),
    val contenido: EmdrContenidoConfig = EmdrContenidoConfig()
) {
    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("animacion", JSONObject().apply {
                put("frecuencia_hz", animacion.frecuencia_hz.toDouble())
                put("velocidad_comercial", animacion.velocidad_comercial)
                put("estilo_esfera", animacion.estilo_esfera)
            })
            put("paleta_colores", JSONObject().apply {
                put("color_esfera_hex", paleta_colores.color_esfera_hex)
                put("color_fondo_hex", paleta_colores.color_fondo_hex)
                put("opacidad_texto", paleta_colores.opacidad_texto.toDouble())
            })
            put("instrucciones", JSONObject().apply {
                put("guia_visual", instrucciones.guia_visual)
                put("alerta_audio", instrucciones.alerta_audio)
            })
            put("contenido", JSONObject().apply {
                put("texto_procesado", contenido.texto_procesado)
            })
        }
    }

    companion object {
        fun fromJson(json: JSONObject): EmdrVisualConfig {
            return EmdrVisualConfig(
                animacion = EmdrAnimacionConfig.fromJson(json.optJSONObject("animacion")),
                paleta_colores = EmdrPaletaColoresConfig.fromJson(json.optJSONObject("paleta_colores")),
                instrucciones = EmdrInstruccionesConfig.fromJson(json.optJSONObject("instrucciones")),
                contenido = EmdrContenidoConfig.fromJson(json.optJSONObject("contenido"))
            )
        }

        fun fromJsonString(rawJson: String): EmdrVisualConfig? {
            return try {
                val clean = rawJson.trim()
                    .removePrefix("```json")
                    .removePrefix("```")
                    .removeSuffix("```")
                    .trim()
                val jsonObject = JSONObject(clean)
                fromJson(jsonObject)
            } catch (_: Exception) {
                null
            }
        }
    }
}
