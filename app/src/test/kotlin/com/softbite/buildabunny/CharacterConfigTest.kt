package com.softbite.buildabunny

import com.softbite.buildabunny.data.model.CharacterConfig
import com.softbite.buildabunny.data.model.CustomizationCategory
import org.junit.Assert.assertEquals
import org.junit.Test

class CharacterConfigTest {

    @Test
    fun `withOption updates the correct field`() {
        val config = CharacterConfig()
        val updated = config.withOption(CustomizationCategory.FUR_COLOR, "brown")
        assertEquals("brown", updated.furColor)
        assertEquals(config.bodyShape, updated.bodyShape)
    }

    @Test
    fun `selectedOptionFor returns current value for category`() {
        val config = CharacterConfig(earStyle = "floppy")
        assertEquals("floppy", config.selectedOptionFor(CustomizationCategory.EAR_STYLE))
    }

    @Test
    fun `withOption chaining applies all changes`() {
        val config = CharacterConfig()
            .withOption(CustomizationCategory.FUR_COLOR, "black")
            .withOption(CustomizationCategory.ACCESSORY, "top_hat")
            .withOption(CustomizationCategory.BACKGROUND, "stars")

        assertEquals("black", config.furColor)
        assertEquals("top_hat", config.accessory)
        assertEquals("stars", config.background)
    }
}
