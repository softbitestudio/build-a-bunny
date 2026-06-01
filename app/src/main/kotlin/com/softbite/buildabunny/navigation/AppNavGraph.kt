package com.softbite.buildabunny.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.softbite.buildabunny.data.repository.CharacterRepository
import com.softbite.buildabunny.receipts.data.repository.ReceiptRepository
import com.softbite.buildabunny.receipts.ui.drop.DropReceiptScreen
import com.softbite.buildabunny.receipts.ui.drop.DropReceiptViewModel
import com.softbite.buildabunny.receipts.ui.drop.DropReceiptViewModelFactory
import com.softbite.buildabunny.receipts.ui.report.ArchetypeReportScreen
import com.softbite.buildabunny.receipts.ui.report.ArchetypeReportViewModel
import com.softbite.buildabunny.receipts.ui.report.ArchetypeReportViewModelFactory
import com.softbite.buildabunny.receipts.ui.timeline.ReceiptsTimelineScreen
import com.softbite.buildabunny.receipts.ui.timeline.ReceiptsTimelineViewModel
import com.softbite.buildabunny.receipts.ui.timeline.ReceiptsTimelineViewModelFactory
import com.softbite.buildabunny.ui.creator.CreatorScreen
import com.softbite.buildabunny.ui.creator.CreatorViewModel
import com.softbite.buildabunny.ui.creator.CreatorViewModelFactory
import com.softbite.buildabunny.ui.gallery.GalleryScreen
import com.softbite.buildabunny.ui.gallery.GalleryViewModel
import com.softbite.buildabunny.ui.gallery.GalleryViewModelFactory

private val MistyPurple = Color(0xFF8B5CF6)

@Composable
fun AppNavGraph(
    characterRepository: CharacterRepository,
    receiptRepository: ReceiptRepository,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            NavigationBar {
                listOf(NavSection.BUNNIES, NavSection.RECEIPTS).forEach { section ->
                    val inSection = currentDestination?.hierarchy?.any { it.route == section } == true
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (section == NavSection.BUNNIES) Icons.Default.Pets else Icons.Default.Receipt,
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text(if (section == NavSection.BUNNIES) "My Bunnies" else "Receipts")
                        },
                        selected = inSection,
                        onClick = {
                            navController.navigate(section) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MistyPurple,
                            selectedTextColor = MistyPurple,
                            indicatorColor = MistyPurple.copy(alpha = 0.12f),
                        ),
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavSection.BUNNIES,
            modifier = Modifier.padding(innerPadding),
        ) {
            // ── Bunny Creator section ──────────────────────────────────────────
            navigation(startDestination = Screen.Gallery.route, route = NavSection.BUNNIES) {
                composable(Screen.Gallery.route) {
                    val vm: GalleryViewModel = viewModel(
                        factory = GalleryViewModelFactory(characterRepository),
                    )
                    GalleryScreen(
                        viewModel = vm,
                        onCreateNew = { navController.navigate(Screen.Creator.withId()) },
                        onEditCharacter = { id -> navController.navigate(Screen.Creator.withId(id)) },
                        modifier = Modifier,
                    )
                }
                composable(
                    route = Screen.Creator.route,
                    arguments = listOf(navArgument("characterId") { type = NavType.StringType }),
                ) { backStack ->
                    val characterId = backStack.arguments?.getString("characterId")
                        .takeUnless { it == "new" }
                    val vm: CreatorViewModel = viewModel(
                        factory = CreatorViewModelFactory(characterRepository, characterId),
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

            // ── Receipts section ───────────────────────────────────────────────
            navigation(
                startDestination = Screen.ReceiptsTimeline.route,
                route = NavSection.RECEIPTS,
            ) {
                composable(Screen.ReceiptsTimeline.route) {
                    val vm: ReceiptsTimelineViewModel = viewModel(
                        factory = ReceiptsTimelineViewModelFactory(receiptRepository),
                    )
                    ReceiptsTimelineScreen(
                        viewModel = vm,
                        onDropReceipt = { navController.navigate(Screen.DropReceipt.route) },
                        onViewReport = { navController.navigate(Screen.ArchetypeReport.route) },
                    )
                }
                composable(Screen.DropReceipt.route) {
                    val vm: DropReceiptViewModel = viewModel(
                        factory = DropReceiptViewModelFactory(receiptRepository),
                    )
                    DropReceiptScreen(
                        viewModel = vm,
                        onNavigateBack = { navController.popBackStack() },
                    )
                }
                composable(Screen.ArchetypeReport.route) {
                    val vm: ArchetypeReportViewModel = viewModel(
                        factory = ArchetypeReportViewModelFactory(receiptRepository),
                    )
                    ArchetypeReportScreen(
                        viewModel = vm,
                        onNavigateBack = { navController.popBackStack() },
                    )
                }
            }
        }
    }
}
