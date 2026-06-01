package com.softbite.buildabunny.receipts.ui.timeline

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.softbite.buildabunny.receipts.data.model.MoodTag
import com.softbite.buildabunny.receipts.data.model.Receipt
import com.softbite.buildabunny.receipts.ui.components.MistyCanvas
import com.softbite.buildabunny.receipts.ui.components.MoodTagReadOnly
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val MistyPurple = Color(0xFF8B5CF6)
private val MistyPurpleLight = Color(0xFFEDE9FE)
private val MistyPurpleDark = Color(0xFF6D28D9)
private val ReceiptsAccent = Color(0xFFEC4899)
private val DarkInk = Color(0xFF1A0A2E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptsTimelineScreen(
    viewModel: ReceiptsTimelineViewModel,
    onDropReceipt: () -> Unit,
    onViewReport: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val receipts by viewModel.receipts.collectAsState(initial = emptyList())

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Receipts",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            "Because your memory isn't the problem",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.75f),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onViewReport) {
                        Icon(Icons.Default.Analytics, "Pattern Report", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MistyPurple),
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onDropReceipt,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Drop a Receipt") },
                containerColor = ReceiptsAccent,
                contentColor = Color.White,
            )
        },
        containerColor = MistyPurpleLight,
    ) { innerPadding ->
        if (receipts.isEmpty()) {
            EmptyTimeline(
                onDropReceipt = onDropReceipt,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding() + 16.dp,
                    bottom = 96.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                itemsIndexed(receipts, key = { _, r -> r.id }) { index, receipt ->
                    ReceiptCard(
                        receipt = receipt,
                        receiptNumber = receipts.size - index,
                        onDelete = { viewModel.delete(receipt.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyTimeline(onDropReceipt: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp),
        ) {
            MistyCanvas(modifier = Modifier.size(96.dp))
            Spacer(Modifier.height(20.dp))
            Text(
                "Nothing filed yet.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DarkInk,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Drop your first receipt.\nMisty is ready to judge.",
                style = MaterialTheme.typography.bodyMedium,
                color = DarkInk.copy(alpha = 0.65f),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onDropReceipt,
                colors = ButtonDefaults.buttonColors(containerColor = MistyPurple),
            ) {
                Text("Drop a Receipt")
            }
        }
    }
}

@Composable
private fun ReceiptCard(
    receipt: Receipt,
    receiptNumber: Int,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var showTrippingDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showTrippingDialog) {
        AmITrippingDialog(
            realityCheck = receipt.realityCheck,
            onDismiss = { showTrippingDialog = false },
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete this receipt?") },
            text = { Text("Gone forever. Even Misty can't bring it back.") },
            confirmButton = {
                TextButton(onClick = { onDelete(); showDeleteDialog = false }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Keep it") }
            },
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column {
            // Header strip
            Surface(
                color = MistyPurple,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "🧾 Receipt #$receiptNumber",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        receipt.timestamp.toDisplayTime(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.80f),
                    )
                }
            }

            // Receipt text
            Text(
                text = "\"${receipt.text}\"",
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic,
                color = DarkInk,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            )

            // Mood tags
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 12.dp),
            ) {
                items(receipt.moodTags) { tag ->
                    MoodTagReadOnly(tag)
                }
            }

            // Misty's reaction box
            Surface(
                color = MistyPurpleLight,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(14.dp),
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        MistyCanvas(modifier = Modifier.size(32.dp))
                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(
                                receipt.diagnosis,
                                style = MaterialTheme.typography.labelMedium,
                                color = MistyPurpleDark,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        receipt.roast,
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkInk.copy(alpha = 0.85f),
                    )
                }
            }

            // Action row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(
                    onClick = { showTrippingDialog = true },
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Am I Tripping?", style = MaterialTheme.typography.labelMedium)
                }
                Row {
                    IconButton(onClick = { copyToClipboard(context, receipt, receiptNumber) }) {
                        Icon(Icons.Default.ContentCopy, "Copy", tint = MistyPurple)
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, "Delete", tint = Color(0xFFEF4444))
                    }
                }
            }
        }
    }
}

@Composable
private fun AmITrippingDialog(realityCheck: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = DarkInk),
            elevation = CardDefaults.cardElevation(8.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(28.dp),
            ) {
                MistyCanvas(modifier = Modifier.size(80.dp))
                Spacer(Modifier.height(16.dp))
                Text(
                    "Am I Tripping?",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "Misty says:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MistyPurple,
                    modifier = Modifier.padding(top = 2.dp, bottom = 12.dp),
                )
                Text(
                    realityCheck,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFE5E7EB),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = MistyPurple),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Got it. Thanks, Misty.")
                }
            }
        }
    }
}

private fun copyToClipboard(context: Context, receipt: Receipt, number: Int) {
    val tags = receipt.moodTags.joinToString(", ") { "${it.emoji} ${it.label}" }
    val text = buildString {
        appendLine("🧾 Receipt #$number")
        appendLine("\"${receipt.text}\"")
        appendLine()
        appendLine("Tags: $tags")
        appendLine("Diagnosis: ${receipt.diagnosis}")
        appendLine()
        appendLine("Misty says: \"${receipt.roast}\"")
        appendLine()
        append("via Receipts — Because your memory isn't the problem")
    }
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("Receipt", text))
}

private fun Long.toDisplayTime(): String {
    val dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    val now = LocalDateTime.now()
    val timeFmt = DateTimeFormatter.ofPattern("h:mm a")
    return when {
        dt.toLocalDate() == now.toLocalDate() -> "Today, ${dt.format(timeFmt)}"
        dt.toLocalDate() == now.toLocalDate().minusDays(1) -> "Yesterday, ${dt.format(timeFmt)}"
        else -> dt.format(DateTimeFormatter.ofPattern("MMM d, h:mm a"))
    }
}
