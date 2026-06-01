package com.softbite.buildabunny.receipts.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.softbite.buildabunny.receipts.data.model.MoodTag

@Composable
fun MoodTagChip(
    tag: MoodTag,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bg = if (selected) tagColor(tag) else Color.Transparent
    val border = if (selected) tagColor(tag) else Color(0xFFD1D5DB)

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        color = bg,
        border = BorderStroke(1.5.dp, border),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
        ) {
            Text(tag.emoji, style = MaterialTheme.typography.bodyMedium)
            AnimatedVisibility(visible = selected) {
                Text(
                    " ${tag.label}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF1F2937),
                )
            }
        }
    }
}

@Composable
fun MoodTagReadOnly(tag: MoodTag, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = tagColor(tag),
        border = BorderStroke(1.dp, tagColor(tag).copy(alpha = 0.6f)),
    ) {
        Text(
            tag.emoji,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

fun tagColor(tag: MoodTag): Color = when (tag) {
    MoodTag.SIDE_EYE -> Color(0xFFFEF3C7)
    MoodTag.FUMING -> Color(0xFFFEE2E2)
    MoodTag.CLOWN -> Color(0xFFFFEDD5)
    MoodTag.DEAD -> Color(0xFFF3F4F6)
    MoodTag.ROLLING -> Color(0xFFE0E7FF)
    MoodTag.CRYING -> Color(0xFFDBEAFE)
    MoodTag.CHAOS -> Color(0xFFFEF9C3)
    MoodTag.RED_FLAG -> Color(0xFFFFE4E6)
}
