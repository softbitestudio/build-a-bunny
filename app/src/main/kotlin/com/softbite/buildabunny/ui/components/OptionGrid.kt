package com.softbite.buildabunny.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.softbite.buildabunny.data.model.CustomizationOption
import com.softbite.buildabunny.ui.theme.SelectedBorder

@Composable
fun OptionGrid(
    options: List<CustomizationOption>,
    selectedId: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 80.dp),
        modifier = modifier.fillMaxWidth().height(200.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(options, key = { it.id }) { option ->
            OptionCell(
                option = option,
                isSelected = option.id == selectedId,
                onClick = { onOptionSelected(option.id) },
            )
        }
    }
}

@Composable
private fun OptionCell(
    option: CustomizationOption,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (isSelected) SelectedBorder else Color.Transparent
    val borderWidth = if (isSelected) 2.5.dp else 0.dp
    val semanticLabel = if (isSelected) "${option.label}, selected" else option.label

    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = if (isSelected) 4.dp else 1.dp,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .semantics { contentDescription = semanticLabel },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth().height(72.dp),
        ) {
            if (option.swatch != null) {
                SwatchIndicator(color = option.swatch, isSelected = isSelected)
            } else {
                LabelIndicator(label = option.label.take(2), isSelected = isSelected)
            }
            Text(
                text = option.label,
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) SelectedBorder else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun SwatchIndicator(color: Color, isSelected: Boolean) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(color)
            .border(if (isSelected) 2.dp else 1.dp, if (isSelected) SelectedBorder else Color.Gray.copy(alpha = 0.3f), CircleShape),
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun LabelIndicator(label: String, isSelected: Boolean) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(if (isSelected) SelectedBorder.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant),
    ) {
        if (isSelected) {
            Icon(Icons.Default.Check, contentDescription = null, tint = SelectedBorder, modifier = Modifier.size(18.dp))
        } else {
            Text(label, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
