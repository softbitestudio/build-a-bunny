package com.softbite.buildabunny.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.softbite.buildabunny.data.model.CharacterConfig
import com.softbite.buildabunny.data.repository.CharacterRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class GalleryUiState(
    val characters: List<CharacterConfig> = emptyList(),
)

class GalleryViewModel(private val repository: CharacterRepository) : ViewModel() {

    val uiState: StateFlow<GalleryUiState> = repository.characters
        .map { GalleryUiState(characters = it.sortedByDescending { c -> c.createdAt }) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GalleryUiState())

    fun deleteCharacter(id: String) {
        repository.delete(id)
    }
}

class GalleryViewModelFactory(
    private val repository: CharacterRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        GalleryViewModel(repository) as T
}
