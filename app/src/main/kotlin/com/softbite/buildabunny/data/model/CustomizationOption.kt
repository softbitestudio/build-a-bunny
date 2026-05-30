package com.softbite.buildabunny.data.model

import androidx.compose.ui.graphics.Color

data class CustomizationOption(
    val id: String,
    val label: String,
    val category: CustomizationCategory,
    val swatch: Color? = null,
    val description: String = label,
)

object CustomizationOptions {

    val bodyShapes = listOf(
        CustomizationOption("round", "Round", CustomizationCategory.BODY_SHAPE, description = "Chubby round body"),
        CustomizationOption("slim", "Slim", CustomizationCategory.BODY_SHAPE, description = "Slim elegant body"),
        CustomizationOption("chubby", "Chubby", CustomizationCategory.BODY_SHAPE, description = "Extra large body"),
    )

    val furColors = listOf(
        CustomizationOption("white", "White", CustomizationCategory.FUR_COLOR, Color(0xFFF5F5F0)),
        CustomizationOption("cream", "Cream", CustomizationCategory.FUR_COLOR, Color(0xFFFFF0CC)),
        CustomizationOption("brown", "Brown", CustomizationCategory.FUR_COLOR, Color(0xFF8B6914)),
        CustomizationOption("grey", "Grey", CustomizationCategory.FUR_COLOR, Color(0xFFAAAAAA)),
        CustomizationOption("black", "Black", CustomizationCategory.FUR_COLOR, Color(0xFF333333)),
        CustomizationOption("spotted", "Spotted", CustomizationCategory.FUR_COLOR, Color(0xFFF5F5F0), "Spotted white & brown"),
    )

    val earStyles = listOf(
        CustomizationOption("upright", "Standing", CustomizationCategory.EAR_STYLE, description = "Tall standing ears"),
        CustomizationOption("floppy", "Floppy", CustomizationCategory.EAR_STYLE, description = "Both ears flopped down"),
        CustomizationOption("lop", "Lop", CustomizationCategory.EAR_STYLE, description = "One ear up, one flopped"),
    )

    val eyeStyles = listOf(
        CustomizationOption("round", "Round", CustomizationCategory.EYE_STYLE, description = "Classic round eyes"),
        CustomizationOption("sleepy", "Sleepy", CustomizationCategory.EYE_STYLE, description = "Half-closed sleepy eyes"),
        CustomizationOption("wide", "Wide", CustomizationCategory.EYE_STYLE, description = "Big wide eyes"),
        CustomizationOption("sparkle", "Sparkle", CustomizationCategory.EYE_STYLE, description = "Sparkling eyes"),
    )

    val eyeColors = listOf(
        CustomizationOption("brown", "Brown", CustomizationCategory.EYE_COLOR, Color(0xFF6B3A2A)),
        CustomizationOption("blue", "Blue", CustomizationCategory.EYE_COLOR, Color(0xFF4A90D9)),
        CustomizationOption("green", "Green", CustomizationCategory.EYE_COLOR, Color(0xFF4CAF50)),
        CustomizationOption("pink", "Pink", CustomizationCategory.EYE_COLOR, Color(0xFFE91E8C)),
        CustomizationOption("black", "Black", CustomizationCategory.EYE_COLOR, Color(0xFF111111)),
    )

    val noseColors = listOf(
        CustomizationOption("pink", "Pink", CustomizationCategory.NOSE, Color(0xFFFF9EB5)),
        CustomizationOption("brown", "Brown", CustomizationCategory.NOSE, Color(0xFF8B6914)),
        CustomizationOption("black", "Black", CustomizationCategory.NOSE, Color(0xFF333333)),
    )

    val mouthStyles = listOf(
        CustomizationOption("smile", "Smile", CustomizationCategory.MOUTH, description = "Happy smile"),
        CustomizationOption("neutral", "Neutral", CustomizationCategory.MOUTH, description = "Calm expression"),
        CustomizationOption("silly", "Silly", CustomizationCategory.MOUTH, description = "Silly grin"),
        CustomizationOption("surprised", "Surprised", CustomizationCategory.MOUTH, description = "Surprised O shape"),
    )

    val accessories = listOf(
        CustomizationOption("none", "None", CustomizationCategory.ACCESSORY, description = "No accessory"),
        CustomizationOption("bow_tie", "Bow Tie", CustomizationCategory.ACCESSORY, Color(0xFFE91E63)),
        CustomizationOption("top_hat", "Top Hat", CustomizationCategory.ACCESSORY, Color(0xFF212121)),
        CustomizationOption("flower", "Flower", CustomizationCategory.ACCESSORY, Color(0xFFFF9800)),
        CustomizationOption("glasses", "Glasses", CustomizationCategory.ACCESSORY, Color(0xFF795548)),
        CustomizationOption("scarf", "Scarf", CustomizationCategory.ACCESSORY, Color(0xFF2196F3)),
    )

    val backgrounds = listOf(
        CustomizationOption("plain_white", "White", CustomizationCategory.BACKGROUND, Color(0xFFFFFFFF)),
        CustomizationOption("plain_pink", "Pink", CustomizationCategory.BACKGROUND, Color(0xFFFCE4EC)),
        CustomizationOption("meadow", "Meadow", CustomizationCategory.BACKGROUND, Color(0xFF81C784), "Green meadow"),
        CustomizationOption("sky", "Sky", CustomizationCategory.BACKGROUND, Color(0xFF64B5F6), "Blue sky"),
        CustomizationOption("stars", "Stars", CustomizationCategory.BACKGROUND, Color(0xFF1A237E), "Starry night"),
        CustomizationOption("rainbow", "Rainbow", CustomizationCategory.BACKGROUND, Color(0xFFFFF9C4), "Rainbow gradient"),
    )

    fun forCategory(category: CustomizationCategory): List<CustomizationOption> = when (category) {
        CustomizationCategory.BODY_SHAPE -> bodyShapes
        CustomizationCategory.FUR_COLOR -> furColors
        CustomizationCategory.EAR_STYLE -> earStyles
        CustomizationCategory.EYE_STYLE -> eyeStyles
        CustomizationCategory.EYE_COLOR -> eyeColors
        CustomizationCategory.NOSE -> noseColors
        CustomizationCategory.MOUTH -> mouthStyles
        CustomizationCategory.ACCESSORY -> accessories
        CustomizationCategory.BACKGROUND -> backgrounds
    }
}
