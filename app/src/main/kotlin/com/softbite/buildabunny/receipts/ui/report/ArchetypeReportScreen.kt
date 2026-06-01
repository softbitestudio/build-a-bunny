package com.softbite.buildabunny.receipts.ui.report

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.softbite.buildabunny.receipts.data.model.Archetype
import com.softbite.buildabunny.receipts.data.model.MoodTag
import com.softbite.buildabunny.receipts.ui.components.MistyCanvas

private val MistyPurple = Color(0xFF8B5CF6)
private val MistyPurpleLight = Color(0xFFEDE9FE)
private val MistyPurpleDark = Color(0xFF6D28D9)
private val ReceiptsAccent = Color(0xFFEC4899)
private val DarkInk = Color(0xFF1A0A2E)

private val LevelColors = mapOf(
    1 to Color(0xFF10B981),
    2 to Color(0xFFF59E0B),
    3 to Color(0xFFEF4444),
    4 to Color(0xFF7C3AED),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchetypeReportScreen(
    viewModel: ArchetypeReportViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsState(initial = null)
    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Pattern Report", color = Color.White, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                actions = {
                    state?.let { s ->
                        if (s.archetypes.isNotEmpty()) {
                            IconButton(onClick = { shareReport(context, s) }) {
                                Icon(Icons.Default.Share, "Share", tint = Color.White)
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MistyPurpleDark),
            )
        },
        containerColor = MistyPurpleLight,
    ) { innerPadding ->
        val currentState = state
        if (currentState == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text("Loading...", color = DarkInk)
            }
        } else {
            ReportContent(
                state = currentState,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun ReportContent(state: ArchetypeReportUiState, modifier: Modifier = Modifier) {
    val count = state.receipts.size
    val needed = 10

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Header
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                MistyCanvas(modifier = Modifier.size(72.dp))
                Spacer(Modifier.height(12.dp))
                Text(
                    "Misty's Pattern Report",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = DarkInk,
                )
                Text(
                    "$count receipt${if (count == 1) "" else "s"} filed and counting",
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkInk.copy(alpha = 0.55f),
                )
            }
        }

        // Progress toward analysis
        if (count < needed) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Pattern Analysis: In Progress",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkInk,
                        )
                        Spacer(Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = count / needed.toFloat(),
                            modifier = Modifier.fillMaxWidth(),
                            color = MistyPurple,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "$count / $needed receipts — ${needed - count} more until Misty can identify the pattern.",
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkInk.copy(alpha = 0.65f),
                        )
                    }
                }
            }
        }

        // Dominant mood tags
        if (state.dominantTags.isNotEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Your Most Common Feelings",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkInk,
                        )
                        Spacer(Modifier.height(12.dp))
                        state.dominantTags.forEach { (tag, tagCount) ->
                            DominantTagRow(tag, tagCount, count.coerceAtLeast(1))
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }

        // Archetypes
        if (state.archetypes.isNotEmpty()) {
            item {
                Text(
                    "Detected Archetypes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = DarkInk,
                )
            }
            items(state.archetypes) { archetype ->
                ArchetypeCard(archetype)
            }
        } else if (count >= needed) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("✨", style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "No dominant patterns detected yet.",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = DarkInk,
                        )
                        Text(
                            "Keep logging. Patterns tend to reveal themselves over time.",
                            style = MaterialTheme.typography.bodySmall,
                            color = DarkInk.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 6.dp),
                        )
                    }
                }
            }
        }

        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
private fun DominantTagRow(tag: MoodTag, count: Int, total: Int) {
    val fraction = count / total.toFloat()
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(tag.emoji, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(end = 8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(tag.label, style = MaterialTheme.typography.labelMedium, color = DarkInk)
            LinearProgressIndicator(
                progress = fraction,
                modifier = Modifier.fillMaxWidth().padding(top = 3.dp),
                color = MistyPurple,
            )
        }
        Text(
            "$count",
            style = MaterialTheme.typography.labelMedium,
            color = MistyPurpleDark,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
private fun ArchetypeCard(archetype: Archetype, modifier: Modifier = Modifier) {
    val levelColor = LevelColors[archetype.level] ?: MistyPurple

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp),
    ) {
        Column {
            // Header with level badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(archetype.emoji, style = MaterialTheme.typography.headlineMedium)
                Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                    Text(
                        archetype.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkInk,
                    )
                    Text(
                        archetype.tagline,
                        style = MaterialTheme.typography.bodySmall,
                        color = DarkInk.copy(alpha = 0.65f),
                        fontStyle = FontStyle.Italic,
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = levelColor.copy(alpha = 0.15f),
                ) {
                    Text(
                        "Lvl ${archetype.level}",
                        style = MaterialTheme.typography.labelSmall,
                        color = levelColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
            }

            // Description
            Text(
                archetype.description,
                style = MaterialTheme.typography.bodySmall,
                color = DarkInk.copy(alpha = 0.8f),
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(12.dp))

            // Antidote section
            Surface(
                color = Color(0xFFF0FDF4),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Counter: ${archetype.antidoteName}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF065F46),
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        archetype.antidote,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF064E3B),
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
    }
}

private fun shareReport(context: Context, state: ArchetypeReportUiState) {
    val tags = state.dominantTags.take(3).joinToString(" ") { (tag, _) -> tag.emoji }
    val archetypeNames = state.archetypes.take(3).joinToString(", ") { it.name }
    val text = buildString {
        appendLine("🧾 My Receipts Pattern Report")
        appendLine("${state.receipts.size} receipts filed.")
        appendLine()
        if (tags.isNotEmpty()) {
            appendLine("Main vibes: $tags")
        }
        if (archetypeNames.isNotEmpty()) {
            appendLine("Patterns detected: $archetypeNames")
            appendLine()
            appendLine("Misty has thoughts. Many thoughts.")
        }
        appendLine()
        append("via Receipts — Because your memory isn't the problem")
    }
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("Pattern Report", text))
}
