package com.softbite.buildabunny.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterConfig(
    val id: String = generateId(),
    val name: String = "My Bunny",
    val bodyShape: String = "round",
    val furColor: String = "white",
    val earStyle: String = "upright",
    val eyeStyle: String = "round",
    val eyeColor: String = "brown",
    val noseColor: String = "pink",
    val mouthStyle: String = "smile",
    val accessory: String = "none",
    val background: String = "plain_white",
    val createdAt: Long = System.currentTimeMillis(),
) {
    fun withOption(category: CustomizationCategory, optionId: String): CharacterConfig = when (category) {
        CustomizationCategory.BODY_SHAPE -> copy(bodyShape = optionId)
        CustomizationCategory.FUR_COLOR -> copy(furColor = optionId)
        CustomizationCategory.EAR_STYLE -> copy(earStyle = optionId)
        CustomizationCategory.EYE_STYLE -> copy(eyeStyle = optionId)
        CustomizationCategory.EYE_COLOR -> copy(eyeColor = optionId)
        CustomizationCategory.NOSE -> copy(noseColor = optionId)
        CustomizationCategory.MOUTH -> copy(mouthStyle = optionId)
        CustomizationCategory.ACCESSORY -> copy(accessory = optionId)
        CustomizationCategory.BACKGROUND -> copy(background = optionId)
    }

    fun selectedOptionFor(category: CustomizationCategory): String = when (category) {
        CustomizationCategory.BODY_SHAPE -> bodyShape
        CustomizationCategory.FUR_COLOR -> furColor
        CustomizationCategory.EAR_STYLE -> earStyle
        CustomizationCategory.EYE_STYLE -> eyeStyle
        CustomizationCategory.EYE_COLOR -> eyeColor
        CustomizationCategory.NOSE -> noseColor
        CustomizationCategory.MOUTH -> mouthStyle
        CustomizationCategory.ACCESSORY -> accessory
        CustomizationCategory.BACKGROUND -> background
    }
}

private fun generateId(): String =
    (System.currentTimeMillis().toString(36) + (0..999999).random().toString(36)).uppercase()
