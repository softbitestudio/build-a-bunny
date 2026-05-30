package com.softbite.buildabunny.ui.creator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softbite.buildabunny.data.model.CustomizationCategory
import com.softbite.buildabunny.data.model.CharacterConfig
import com.softbite.buildabunny.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreatorViewModel(
    private val repository: CharacterRepository,
    characterId: String?,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        characterId?.let { repository.find(it) }.let { found ->
            CreatorUiState(
                config = found ?: CharacterConfig(),
                isSaved = found != null,
            )
        }
    )
    val uiState: StateFlow<CreatorUiState> = _uiState.asStateFlow()

    fun selectCategory(category: CustomizationCategory) {
        _uiState.update { it.copy(activeCategory = category) }
    }

    fun selectOption(category: CustomizationCategory, optionId: String) {
        _uiState.update { state ->
            state.copy(
                config = state.config.withOption(category, optionId),
                isSaved = false,
            )
        }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(config = it.config.copy(name = name), isSaved = false) }
    }

    fun saveCharacter() {
        val config = _uiState.value.config
        repository.save(config)
        _uiState.update { it.copy(isSaved = true, snackbarMessage = "\"${config.name}\" saved!") }
    }

    fun snackbarShown() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}

class CreatorViewModelFactory(
    private val repository: CharacterRepository,
    private val characterId: String?,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CreatorViewModel(repository, characterId) as T
}
