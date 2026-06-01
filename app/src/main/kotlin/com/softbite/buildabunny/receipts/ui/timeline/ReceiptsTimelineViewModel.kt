package com.softbite.buildabunny.receipts.ui.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.softbite.buildabunny.receipts.data.model.Receipt
import com.softbite.buildabunny.receipts.data.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow

class ReceiptsTimelineViewModel(
    private val repository: ReceiptRepository,
) : ViewModel() {

    val receipts: Flow<List<Receipt>> = repository.receipts

    fun delete(id: String) = repository.delete(id)

    fun receiptCount(): Int = repository.snapshot().size
}

class ReceiptsTimelineViewModelFactory(
    private val repository: ReceiptRepository,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ReceiptsTimelineViewModel(repository) as T
}
