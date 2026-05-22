package com.softbite.buildabunny.navigation

sealed class Screen(val route: String) {
    data object Creator : Screen("creator/{characterId}") {
        fun withId(characterId: String? = null) =
            "creator/${characterId ?: "new"}"
    }
    data object Gallery : Screen("gallery")
}
