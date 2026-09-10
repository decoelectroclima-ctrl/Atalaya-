package com.example.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CrisisResourcesTest {

    @Test
    fun testCountryDetectionSpain() {
        val resources = CrisisResources.forCountryCode("ES")
        assertEquals("España", resources.countryName)
        assertTrue(resources.lines.any { it.phone == "024" })
        assertTrue(resources.lines.any { it.phone == "717003717" })
        assertTrue(resources.lines.any { it.phone == "112" })
    }

    @Test
    fun testCountryDetectionMexico() {
        val resources = CrisisResources.forCountryCode("mx")
        assertEquals("México", resources.countryName)
        assertTrue(resources.lines.any { it.phone == "8009112000" })
        assertTrue(resources.lines.any { it.phone == "911" })
    }

    @Test
    fun testCountryDetectionColombia() {
        val resources = CrisisResources.forCountryCode("co")
        assertEquals("Colombia", resources.countryName)
        assertTrue(resources.lines.any { it.phone == "106" })
        assertTrue(resources.lines.any { it.phone == "192" })
    }

    @Test
    fun testCountryDetectionArgentina() {
        val resources = CrisisResources.forCountryCode("AR")
        assertEquals("Argentina", resources.countryName)
        assertTrue(resources.lines.any { it.phone == "135" })
        assertTrue(resources.lines.any { it.phone == "01152751135" })
    }

    @Test
    fun testCountryDetectionUnitedStates() {
        val resources = CrisisResources.forCountryCode("US")
        assertEquals("Estados Unidos", resources.countryName)
        assertTrue(resources.lines.any { it.phone == "988" })
        assertTrue(resources.lines.any { it.phone == "911" })
    }

    @Test
    fun testFallbackForUnknownOrNull() {
        assertEquals(CrisisResources.GENERICO_LATAM, CrisisResources.forCountryCode(null))
        assertEquals(CrisisResources.GENERICO_LATAM, CrisisResources.forCountryCode(""))
        assertEquals(CrisisResources.GENERICO_LATAM, CrisisResources.forCountryCode("  "))
        assertEquals(CrisisResources.GENERICO_LATAM, CrisisResources.forCountryCode("FR"))
        assertEquals(CrisisResources.GENERICO_LATAM, CrisisResources.forCountryCode("DE"))
    }
}
