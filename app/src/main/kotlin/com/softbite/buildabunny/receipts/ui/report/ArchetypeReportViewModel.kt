package com.softbite.buildabunny.receipts.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softbite.buildabunny.receipts.data.model.Archetype
import com.softbite.buildabunny.receipts.data.model.MoodTag
import com.softbite.buildabunny.receipts.data.model.Receipt
import com.softbite.buildabunny.receipts.data.repository.ReceiptRepository
import com.softbite.buildabunny.receipts.engine.ArchetypeEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class ArchetypeReportUiState(
    val receipts: List<Receipt>,
    val archetypes: List<Archetype>,
    val dominantTags: List<Pair<MoodTag, Int>>,
)

class ArchetypeReportViewModel(
    repository: ReceiptRepository,
) : ViewModel() {

    val uiState: Flow<ArchetypeReportUiState> = repository.receipts.map { receipts ->
        ArchetypeReportUiState(
            receipts = receipts,
            archetypes = ArchetypeEngine.detectArchetypes(receipts),
            dominantTags = ArchetypeEngine.dominantTags(receipts),
        )
    }
}

class ArchetypeReportViewModelFactory(
    private val repository: ReceiptRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ArchetypeReportViewModel(repository) as T
}
