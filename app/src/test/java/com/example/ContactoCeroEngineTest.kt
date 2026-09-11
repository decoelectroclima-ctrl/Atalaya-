package com.example

import com.example.contactocero.ContactoCeroContent
import com.example.contactocero.ContactoCeroEngine
import com.example.contactocero.MarcoAnclaje
import com.example.contactocero.ModuloAnclaje
import org.junit.Assert.*
import org.junit.Test
import java.util.Random

class ContactoCeroEngineTest {

    @Test
    fun todasLasEntradasTienenTokenCanonica() {
        for (anclaje in ContactoCeroContent.TODOS_LOS_ANCLAJES) {
            // Verificar que no use llave simple {ex_name} sin doble llave
            val contieneLlaveSimpleSola = anclaje.textoPlantilla.contains("{ex_name}") &&
                    !anclaje.textoPlantilla.contains("{{ex_name}}")
            assertFalse(
                "La entrada ${anclaje.id} contiene llave simple '{ex_name}' sin normalizar",
                contieneLlaveSimpleSola
            )
        }
    }

    @Test
    fun catTieneDosEntradasPorModulo() {
        val catEntries = ContactoCeroContent.TODOS_LOS_ANCLAJES.filter { it.marco == MarcoAnclaje.CATOLICO }
        assertEquals("Debe haber exactamente 20 entradas CAT", 20, catEntries.size)

        for (modulo in ModuloAnclaje.entries) {
            val count = catEntries.count { it.modulo == modulo }
            assertEquals("El módulo $modulo en CAT debe tener exactamente 2 entradas", 2, count)
        }
    }

    @Test
    fun hayCanonicosUniversal() {
        for (marco in MarcoAnclaje.entries) {
            val universales = ContactoCeroContent.TODOS_LOS_ANCLAJES.filter { it.marco == marco && it.universal }
            assertTrue("Debe existir al menos 1 entrada universal para el marco $marco", universales.isNotEmpty())
        }
    }

    @Test
    fun fallbackNuncaNulo() {
        val rng = Random(42)
        for (marco in MarcoAnclaje.entries) {
            for (modulo in ModuloAnclaje.entries) {
                val resultado = ContactoCeroEngine.seleccionar(marco, modulo, rng)
                assertNotNull("El anclaje seleccionado para $marco y $modulo no debe ser nulo", resultado)
                assertTrue("El anclaje seleccionado debe tener un id no vacío", resultado.id.isNotBlank())
                assertTrue("El anclaje seleccionado debe tener texto", resultado.textoPlantilla.isNotBlank())
            }
        }
    }

    @Test
    fun renderizadoSinPlaceholders() {
        val anclajeTest = ContactoCeroContent.TODOS_LOS_ANCLAJES.first()

        // Caso 1: Con exName personalizado
        val renderizadoConNombre = ContactoCeroEngine.renderizar(anclajeTest, "Alex")
        assertFalse(renderizadoConNombre.contains("{{ex_name}}"))
        assertFalse(renderizadoConNombre.contains("{ex_name}"))

        // Caso 2: Con exName vacío / en blanco
        val renderizadoVacio = ContactoCeroEngine.renderizar(anclajeTest, "")
        assertFalse(renderizadoVacio.contains("{{ex_name}}"))
        assertFalse(renderizadoVacio.contains("{ex_name}"))
        assertTrue(
            "Cuando exName está vacío debe reemplazarlo por 'esa persona'",
            renderizadoVacio.contains("esa persona") || !anclajeTest.textoPlantilla.contains("{{ex_name}}")
        )

        val renderizadoEspacios = ContactoCeroEngine.renderizar(anclajeTest, "   ")
        assertFalse(renderizadoEspacios.contains("{{ex_name}}"))
    }

    @Test
    fun sinDuplicados() {
        val textos = ContactoCeroContent.TODOS_LOS_ANCLAJES.map { it.textoPlantilla }
        val distinctTextos = textos.distinct()
        assertEquals(
            "No debe haber textos de plantilla duplicados entre los anclajes",
            textos.size,
            distinctTextos.size
        )
    }
}
