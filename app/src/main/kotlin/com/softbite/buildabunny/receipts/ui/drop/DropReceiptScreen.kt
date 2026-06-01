package com.softbite.buildabunny.receipts.ui.drop

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softbite.buildabunny.receipts.data.model.MoodTag
import com.softbite.buildabunny.receipts.ui.components.MistyCanvas
import com.softbite.buildabunny.receipts.ui.components.MoodTagChip

private val MistyPurple = Color(0xFF8B5CF6)
private val MistyPurpleLight = Color(0xFFEDE9FE)
private val MistyPurpleDark = Color(0xFF6D28D9)
private val ReceiptsAccent = Color(0xFFEC4899)
private val DarkInk = Color(0xFF1A0A2E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropReceiptScreen(
    viewModel: DropReceiptViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.phase) {
        if (state.phase == DropPhase.SAVED) onNavigateBack()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Drop a Receipt", color = Color.White, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MistyPurple),
            )
        },
        containerColor = MistyPurpleLight,
    ) { innerPadding ->
        AnimatedContent(
            targetState = state.phase,
            transitionSpec = {
                (fadeIn() + slideInVertically { it / 4 }).togetherWith(
                    fadeOut() + slideOutVertically { -it / 4 }
                )
            },
            label = "phase_transition",
            modifier = Modifier.padding(innerPadding),
        ) { phase ->
            when (phase) {
                DropPhase.COMPOSING -> ComposingContent(
                    text = state.text,
                    selectedTags = state.selectedTags,
                    onTextChange = viewModel::updateText,
                    onTagToggle = viewModel::toggleTag,
                    onGenerate = viewModel::generateRoast,
                )
                DropPhase.ROAST_SHOWN -> RoastContent(
                    text = state.text,
                    tags = state.selectedTags,
                    diagnosis = state.diagnosis,
                    roast = state.roast,
                    onSave = viewModel::saveReceipt,
                    onDiscard = onNavigateBack,
                    onRevise = viewModel::resetToCompose,
                )
                DropPhase.SAVED -> {}
            }
        }
    }
}

@Composable
private fun ComposingContent(
    text: String,
    selectedTags: Set<MoodTag>,
    onTextChange: (String) -> Unit,
    onTagToggle: (MoodTag) -> Unit,
    onGenerate: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
    ) {
        Text(
            "What happened?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = DarkInk,
        )
        Text(
            "Be specific. Misty needs the full context.",
            style = MaterialTheme.typography.bodySmall,
            color = DarkInk.copy(alpha = 0.55f),
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp),
        )

        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                Text(
                    "e.g. He said he forgot about our plans even though I sent three reminders.",
                    color = DarkInk.copy(alpha = 0.35f),
                )
            },
            minLines = 5,
            maxLines = 10,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MistyPurple,
                unfocusedBorderColor = MistyPurple.copy(alpha = 0.4f),
                cursorColor = MistyPurple,
            ),
        )

        Spacer(Modifier.height(24.dp))

        Text(
            "How does this make you feel?",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = DarkInk,
        )
        Text(
            "Select at least one.",
            style = MaterialTheme.typography.bodySmall,
            color = DarkInk.copy(alpha = 0.55f),
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp),
        )

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MoodTag.entries.forEach { tag ->
                MoodTagChip(
                    tag = tag,
                    selected = tag in selectedTags,
                    onClick = { onTagToggle(tag) },
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        val canGenerate = text.isNotBlank() && selectedTags.isNotEmpty()
        Button(
            onClick = onGenerate,
            enabled = canGenerate,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ReceiptsAccent,
                disabledContainerColor = ReceiptsAccent.copy(alpha = 0.35f),
            ),
        ) {
            Text(
                "Generate Roast 🔥",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }

        if (!canGenerate) {
            Text(
                if (text.isBlank()) "Add some context first." else "Pick at least one mood tag.",
                style = MaterialTheme.typography.labelSmall,
                color = DarkInk.copy(alpha = 0.45f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun RoastContent(
    text: String,
    tags: Set<MoodTag>,
    diagnosis: String,
    roast: String,
    onSave: () -> Unit,
    onDiscard: () -> Unit,
    onRevise: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Receipt preview (condensed)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Your receipt:",
                    style = MaterialTheme.typography.labelMedium,
                    color = DarkInk.copy(alpha = 0.5f),
                )
                Text(
                    "\"$text\"",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = DarkInk,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    tags.forEach { tag ->
                        Text(tag.emoji, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Misty's verdict
        MistyCanvas(modifier = Modifier.size(80.dp))

        Spacer(Modifier.height(12.dp))

        Text(
            "Misty has thoughts.",
            style = MaterialTheme.typography.labelLarge,
            color = MistyPurpleDark,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(16.dp))

        // Diagnosis badge
        Surface(
            color = MistyPurple,
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                diagnosis,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }

        Spacer(Modifier.height(16.dp))

        // Roast card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MistyPurpleLight),
            elevation = CardDefaults.cardElevation(0.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                roast,
                style = MaterialTheme.typography.bodyLarge,
                color = DarkInk,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(20.dp),
            )
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MistyPurple),
        ) {
            Text(
                "Save to Timeline",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = onRevise,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text("Revise Receipt")
        }

        Spacer(Modifier.height(6.dp))

        OutlinedButton(
            onClick = onDiscard,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text("Actually nevermind", color = DarkInk.copy(alpha = 0.55f))
        }
    }
}
