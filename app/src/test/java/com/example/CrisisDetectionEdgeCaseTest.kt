package com.example

import com.example.ai.SoltarAiEngine
import org.junit.Assert.*
import org.junit.Test

/**
 * Tests de casos limite para checkSelfHarmTrigger() (deteccion por palabras clave).
 * Documenta tanto lo que SI detecta hoy como los huecos conocidos que requieren
 * la capa semantica adicional (checkSelfHarmTriggerSemantic en OnDeviceLlmEngine).
 * Si un test del bloque "DEBE detectar" falla, es una regresion real.
 * Si un test del bloque "hueco conocido" empieza a pasar, significa que el
 * keyword-matching mejoro y el comentario debe actualizarse (no es un fallo).
 */
class CrisisDetectionEdgeCaseTest {

    // ---- DEBE detectar: frases explicitas ya cubiertas ----

    @Test
    fun `detecta frase explicita de ideacion suicida`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("ya no quiero vivir, quiero morir"))
    }

    @Test
    fun `detecta frase explicita de autolesion`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("tengo ganas de cortarme otra vez"))
    }

    @Test
    fun `detecta frase en mayusculas`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("NO QUIERO VIVIR MAS"))
    }

    @Test
    fun `detecta frase clave rodeada de otro texto`() {
        assertTrue(SoltarAiEngine.checkSelfHarmTrigger("hoy fue un dia horrible y la verdad es que quiero morir, no aguanto mas"))
    }

    // ---- NO debe detectar: dolor emocional normal del duelo, sin riesgo ----

    @Test
    fun `NO detecta tristeza normal del duelo`() {
        assertFalse(SoltarAiEngine.checkSelfHarmTrigger("estoy muy triste, lo extraño mucho y me cuesta dormir"))
    }

    @Test
    fun `NO detecta frustracion sin referencia a la propia vida`() {
        assertFalse(SoltarAiEngine.checkSelfHarmTrigger("no puedo mas con esta situacion, estoy agotada de tanto pensar en el"))
    }

    @Test
    fun `NO detecta enfado hacia la ex pareja`() {
        assertFalse(SoltarAiEngine.checkSelfHarmTrigger("lo odio, ojala le vaya mal, no se merece nada bueno"))
    }

    // ---- Huecos conocidos del keyword-matching (documentados, no arreglados aqui) ----
    // Estos tests reflejan el estado ACTUAL de la funcion. Estan aqui para dejar constancia
    // de que este tipo de frases requiere la capa semantica adicional para ser detectadas,
    // no para forzar un comportamiento que el keyword-matching no puede dar por diseno.

    @Test
    fun `hueco conocido - falta de ortografia en frase de riesgo`() {
        // Nota: "kiero morirm" contiene la subcadena "morir" y es detectado.
        // El verdadero hueco por falta de ortografía fonética ocurre cuando no coincide la subcadena:
        val result = SoltarAiEngine.checkSelfHarmTrigger("kiero moril ya no aguanto")
        assertFalse("Si esto empieza a pasar (true), es una mejora real del keyword-matching, actualiza este comentario", result)
    }

    @Test
    fun `hueco conocido - intencion indirecta sin palabra clave exacta`() {
        val result = SoltarAiEngine.checkSelfHarmTrigger("ya no quiero estar en este mundo, seria mas facil para todos si yo no estuviera")
        assertFalse("Frase claramente de riesgo pero sin coincidencia literal con la lista de keywords", result)
    }

    @Test
    fun `hueco conocido - metafora de desaparecer sin frase exacta`() {
        val result = SoltarAiEngine.checkSelfHarmTrigger("solo quiero cerrar los ojos y no tener que abrirlos de nuevo")
        assertFalse("Expresion indirecta de deseo de morir, sin match literal", result)
    }

    @Test
    fun `hueco conocido - jerga regional de autolesion`() {
        val result = SoltarAiEngine.checkSelfHarmTrigger("tengo ganas de lastimarme para sentir algo distinto al vacio")
        assertFalse("Usa 'lastimarme' en vez de las variantes exactas de la lista ('hacerme daño', 'cortarme')", result)
    }

    @Test
    fun `hueco conocido - frase partida en varias oraciones cortas`() {
        val result = SoltarAiEngine.checkSelfHarmTrigger("no doy mas. de verdad. esto se tiene que acabar de alguna forma")
        assertFalse("Ambigua por diseno: 'esto se tiene que acabar' puede referirse al dolor, no a la vida - caso genuinamente limite para cualquier metodo", result)
    }
}
