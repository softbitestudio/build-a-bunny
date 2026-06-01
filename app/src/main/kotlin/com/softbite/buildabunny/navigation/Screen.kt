package com.softbite.buildabunny.navigation

sealed class Screen(val route: String) {
    // Bunny creator section
    data object Creator : Screen("creator/{characterId}") {
        fun withId(characterId: String? = null) =
            "creator/${characterId ?: "new"}"
    }
    data object Gallery : Screen("gallery")

    // Receipts section
    data object ReceiptsTimeline : Screen("receipts_timeline")
    data object DropReceipt : Screen("drop_receipt")
    data object ArchetypeReport : Screen("archetype_report")
}

object NavSection {
    const val BUNNIES = "bunnies"
    const val RECEIPTS = "receipts"
}
