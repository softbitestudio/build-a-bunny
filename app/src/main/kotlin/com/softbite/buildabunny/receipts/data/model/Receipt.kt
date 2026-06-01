package com.softbite.buildabunny.receipts.data.model

import java.util.UUID

data class Receipt(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val moodTags: List<MoodTag>,
    val timestamp: Long = System.currentTimeMillis(),
    val roast: String,
    val diagnosis: String,
    val realityCheck: String,
)
