package com.example.data

import com.example.ai.OnDeviceLlmEngine
import org.junit.Assert.*
import org.junit.Test

class ExerciseBankTest {

    @Test
    fun testExerciseBank_HasSufficientCuratedExercises() {
        val exercises = ExerciseBank.exercises
        assertTrue("El banco debe tener al menos 20 ejercicios", exercises.size >= 20)
        
        exercises.forEach { exercise ->
            assertTrue("ID no debe ser blanco: ${exercise.id}", exercise.id.isNotBlank())
            assertTrue("Título no debe ser blanco: ${exercise.id}", exercise.title.isNotBlank())
            assertTrue("Instrucciones no deben ser blancas: ${exercise.id}", exercise.instructions.isNotBlank())
            assertTrue("whyItHelps no debe ser blanco: ${exercise.id}", exercise.whyItHelps.isNotBlank())
            assertTrue("estimatedMinutes debe ser mayor a 0: ${exercise.id}", exercise.estimatedMinutes > 0)
        }
    }

    @Test
    fun testExerciseBank_AllCategoriesAndFrameworksRepresented() {
        for (category in ExerciseCategory.entries) {
            for (framework in SoltarFramework.entries) {
                val matches = ExerciseBank.exercises.filter { it.category == category && it.framework == framework }
                assertTrue("Debe haber al menos 2 ejercicios para $category + $framework", matches.size >= 2)
            }
        }
    }

    @Test
    fun testExerciseBank_SuggestRespectsIntensityAndExclusion() {
        val suaveOnly = ExerciseBank.suggest(
            category = ExerciseCategory.CUERPO,
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            maxIntensity = ExerciseIntensity.SUAVE,
            excludeIds = emptyList()
        )
        assertNotNull(suaveOnly)
        assertEquals(ExerciseIntensity.SUAVE, suaveOnly?.intensity)

        // Excluir un ID
        val excludedId = suaveOnly!!.id
        val nextSuggest = ExerciseBank.suggest(
            category = ExerciseCategory.CUERPO,
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            maxIntensity = ExerciseIntensity.SUAVE,
            excludeIds = listOf(excludedId)
        )
        assertNotNull(nextSuggest)
        assertNotEquals(excludedId, nextSuggest?.id)
    }

    @Test
    fun testPersonalizeExercisePresentation_OfflineFallback() {
        val exercise = ExerciseBank.exercises.first()
        val presentation = OnDeviceLlmEngine.personalizeExercisePresentation(exercise, "Me siento muy triste hoy")
        assertTrue(presentation.contains(exercise.title))
        assertTrue(presentation.contains(exercise.instructions))
        assertTrue(presentation.contains(exercise.whyItHelps))
    }
}
