package com.softbite.buildabunny.receipts.ui.drop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softbite.buildabunny.receipts.data.model.MoodTag
import com.softbite.buildabunny.receipts.data.model.Receipt
import com.softbite.buildabunny.receipts.data.repository.ReceiptRepository
import com.softbite.buildabunny.receipts.engine.RoastEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class DropPhase { COMPOSING, ROAST_SHOWN, SAVED }

data class DropReceiptUiState(
    val text: String = "",
    val selectedTags: Set<MoodTag> = emptySet(),
    val phase: DropPhase = DropPhase.COMPOSING,
    val roast: String = "",
    val diagnosis: String = "",
    val realityCheck: String = "",
)

class DropReceiptViewModel(
    private val repository: ReceiptRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DropReceiptUiState())
    val uiState: StateFlow<DropReceiptUiState> = _uiState.asStateFlow()

    fun updateText(text: String) {
        _uiState.update { it.copy(text = text) }
    }

    fun toggleTag(tag: MoodTag) {
        _uiState.update { state ->
            val tags = if (tag in state.selectedTags) {
                state.selectedTags - tag
            } else {
                state.selectedTags + tag
            }
            state.copy(selectedTags = tags)
        }
    }

    fun generateRoast() {
        val state = _uiState.value
        if (state.text.isBlank() || state.selectedTags.isEmpty()) return
        val result = RoastEngine.generate(state.text, state.selectedTags.toList())
        _uiState.update {
            it.copy(
                phase = DropPhase.ROAST_SHOWN,
                roast = result.roast,
                diagnosis = result.diagnosis,
                realityCheck = result.realityCheck,
            )
        }
    }

    fun saveReceipt() {
        val state = _uiState.value
        repository.add(
            Receipt(
                text = state.text,
                moodTags = state.selectedTags.toList(),
                roast = state.roast,
                diagnosis = state.diagnosis,
                realityCheck = state.realityCheck,
            )
        )
        _uiState.update { it.copy(phase = DropPhase.SAVED) }
    }

    fun resetToCompose() {
        _uiState.update { it.copy(phase = DropPhase.COMPOSING) }
    }
}

class DropReceiptViewModelFactory(
    private val repository: ReceiptRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        DropReceiptViewModel(repository) as T
}
