package com.softbite.buildabunny.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.softbite.buildabunny.data.repository.CharacterRepository
import com.softbite.buildabunny.ui.creator.CreatorScreen
import com.softbite.buildabunny.ui.creator.CreatorViewModel
import com.softbite.buildabunny.ui.creator.CreatorViewModelFactory
import com.softbite.buildabunny.ui.gallery.GalleryScreen
import com.softbite.buildabunny.ui.gallery.GalleryViewModel
import com.softbite.buildabunny.ui.gallery.GalleryViewModelFactory

@Composable
fun AppNavGraph(
    repository: CharacterRepository,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Gallery.route,
        modifier = modifier,
    ) {
        composable(Screen.Gallery.route) {
            val vm: GalleryViewModel = viewModel(factory = GalleryViewModelFactory(repository))
            GalleryScreen(
                viewModel = vm,
                onCreateNew = { navController.navigate(Screen.Creator.withId()) },
                onEditCharacter = { id -> navController.navigate(Screen.Creator.withId(id)) },
            )
        }

        composable(
            route = Screen.Creator.route,
            arguments = listOf(navArgument("characterId") { type = NavType.StringType }),
        ) { backStack ->
            val characterId = backStack.arguments?.getString("characterId")
                .takeUnless { it == "new" }
            val vm: CreatorViewModel = viewModel(
                factory = CreatorViewModelFactory(repository, characterId),
            )
            CreatorScreen(
                viewModel = vm,
                onNavigateToGallery = {
                    navController.navigate(Screen.Gallery.route) {
                        popUpTo(Screen.Gallery.route) { inclusive = true }
                    }
                },
            )
        }
    }
}
