package com.softbite.buildabunny.ui.creator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softbite.buildabunny.data.model.CustomizationOptions
import com.softbite.buildabunny.ui.components.BunnyCanvas
import com.softbite.buildabunny.ui.components.CategoryTabRow
import com.softbite.buildabunny.ui.components.OptionGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorScreen(
    viewModel: CreatorViewModel,
    onNavigateToGallery: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.snackbarMessage) {
        state.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.snackbarShown()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Your Bunny",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateToGallery) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to gallery")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::saveCharacter) {
                        Icon(Icons.Default.Save, contentDescription = "Save bunny")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            BunnyCanvas(
                config = state.config,
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .weight(1f)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
            )

            OutlinedTextField(
                value = state.config.name,
                onValueChange = viewModel::updateName,
                label = { Text("Bunny name") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
            )

            CategoryTabRow(
                activeCategory = state.activeCategory,
                onCategorySelected = viewModel::selectCategory,
            )

            Surface(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth(),
            ) {
                OptionGrid(
                    options = CustomizationOptions.forCategory(state.activeCategory),
                    selectedId = state.config.selectedOptionFor(state.activeCategory),
                    onOptionSelected = { optionId ->
                        viewModel.selectOption(state.activeCategory, optionId)
                    },
                )
            }
        }
    }
}
