package com.softbite.buildabunny.ui.creator

import com.softbite.buildabunny.data.model.CharacterConfig
import com.softbite.buildabunny.data.model.CustomizationCategory

data class CreatorUiState(
    val config: CharacterConfig = CharacterConfig(),
    val activeCategory: CustomizationCategory = CustomizationCategory.BODY_SHAPE,
    val isSaved: Boolean = false,
    val snackbarMessage: String? = null,
)
