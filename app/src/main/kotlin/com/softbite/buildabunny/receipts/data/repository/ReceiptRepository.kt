package com.softbite.buildabunny.receipts.data.repository

import com.softbite.buildabunny.receipts.data.model.Receipt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ReceiptRepository {
    private val _receipts = MutableStateFlow<List<Receipt>>(emptyList())
    val receipts: Flow<List<Receipt>> = _receipts.asStateFlow()

    fun add(receipt: Receipt) {
        _receipts.update { current -> listOf(receipt) + current }
    }

    fun delete(id: String) {
        _receipts.update { current -> current.filter { it.id != id } }
    }

    fun snapshot(): List<Receipt> = _receipts.value
}
