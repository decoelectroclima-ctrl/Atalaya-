package com.example

import com.example.ai.EmdrVisualConfig
import com.example.ai.SoltarAiEngine
import com.example.data.SoltarFramework
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class EmdrVisualEngineTest {

    @Test
    fun testCatolicoVisualMatrixParameters() {
        val config = SoltarAiEngine.calculateLocalEmdrVisualConfig(
            textoBase = "Entrego en oración a [Nombre] y cuido mi paz.",
            framework = SoltarFramework.CATOLICO,
            nombreEx = "Sofía"
        )

        assertEquals(0.8f, config.animacion.frecuencia_hz, 0.01f)
        assertEquals("LENTO", config.animacion.velocidad_comercial)
        assertEquals("FADE", config.animacion.estilo_esfera)
        assertEquals("#D4AF37", config.paleta_colores.color_esfera_hex)
        assertEquals("#111116", config.paleta_colores.color_fondo_hex)
        assertTrue(config.contenido.texto_procesado.contains("Sofía"))
        assertFalse(config.contenido.texto_procesado.contains("[Nombre]"))
    }

    @Test
    fun testEstoicoVisualMatrixParameters() {
        val config = SoltarAiEngine.calculateLocalEmdrVisualConfig(
            textoBase = "Lo que [Nombre] haga o piense no está bajo mi gobierno.",
            framework = SoltarFramework.ESTOICO,
            nombreEx = "Marcos"
        )

        assertEquals(1.0f, config.animacion.frecuencia_hz, 0.01f)
        assertEquals("MEDIO", config.animacion.velocidad_comercial)
        assertEquals("LINEAL", config.animacion.estilo_esfera)
        assertEquals("#8E9290", config.paleta_colores.color_esfera_hex)
        assertEquals("#0D0D0D", config.paleta_colores.color_fondo_hex)
        assertTrue(config.contenido.texto_procesado.contains("Marcos"))
    }

    @Test
    fun testPsicologiaModernaVisualMatrixParameters() {
        val config = SoltarAiEngine.calculateLocalEmdrVisualConfig(
            textoBase = "La urgencia de escribir a [Nombre] es solo abstinencia.",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            nombreEx = "Carlos"
        )

        assertEquals(1.5f, config.animacion.frecuencia_hz, 0.01f)
        assertEquals("RAPIDO", config.animacion.velocidad_comercial)
        assertEquals("GLOW", config.animacion.estilo_esfera)
        assertEquals("#4A90E2", config.paleta_colores.color_esfera_hex)
        assertEquals("#0A0E17", config.paleta_colores.color_fondo_hex)
        assertTrue(config.contenido.texto_procesado.contains("Carlos"))
    }

    @Test
    fun testStrictJsonParsingWithoutMarkdown() {
        val jsonString = """
        {
          "animacion": {
            "frecuencia_hz": 1.5,
            "velocidad_comercial": "RAPIDO",
            "estilo_esfera": "GLOW"
          },
          "paleta_colores": {
            "color_esfera_hex": "#4A90E2",
            "color_fondo_hex": "#0A0E17",
            "opacidad_texto": 0.90
          },
          "instrucciones": {
            "guia_visual": "Siga la esfera azul rápidamente con los ojos.",
            "alerta_audio": "Sincronice el sonido alterno en sus oídos."
          },
          "contenido": {
            "texto_procesado": "[Fije la mirada] Dejo que la ola baje."
          }
        }
        """.trimIndent()

        val parsed = EmdrVisualConfig.fromJsonString(jsonString)
        assertNotNull(parsed)
        assertEquals(1.5f, parsed!!.animacion.frecuencia_hz, 0.01f)
        assertEquals("RAPIDO", parsed.animacion.velocidad_comercial)
        assertEquals("GLOW", parsed.animacion.estilo_esfera)
        assertEquals("#4A90E2", parsed.paleta_colores.color_esfera_hex)
        assertEquals(0.90f, parsed.paleta_colores.opacidad_texto, 0.01f)
    }

    @Test
    fun testSystemInstructionsContainEmdrKeywords() {
        val instructions = SoltarAiEngine.SYSTEM_INSTRUCTIONS_EMDR
        assertTrue(instructions.contains("EMDR VISUAL INTEGRATION ENGINE"))
        assertTrue(instructions.contains("Perfil CATÓLICO"))
        assertTrue(instructions.contains("Perfil ESTOICO"))
        assertTrue(instructions.contains("Perfil PSICOLOGÍA MODERNA"))
        assertTrue(instructions.contains("color_esfera_hex"))
        assertTrue(instructions.contains("frecuencia_hz"))
    }
}
