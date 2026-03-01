package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import io.github.xingray.compose_nexus.theme.typeColor

/**
 * Tag visual effect.
 */
enum class TagEffect {
    Light,
    Dark,
    Plain,
}

/**
 * Element Plus Tag — a small label for marking and categorization.
 *
 * @param text Tag label text.
 * @param modifier Modifier.
 * @param type Semantic type color.
 * @param size Component size.
 * @param effect Visual effect (Light, Dark, Plain).
 * @param closable Whether a close button is shown.
 * @param round Use rounded pill shape.
 * @param onClose Called when close button is clicked.
 */
@Composable
fun NexusTag(
    text: String,
    modifier: Modifier = Modifier,
    type: NexusType = NexusType.Default,
    size: ComponentSize = ComponentSize.Default,
    effect: TagEffect = TagEffect.Light,
    closable: Boolean = false,
    round: Boolean = false,
    onClose: (() -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    val tc = colorScheme.typeColor(type)
    val shape = if (round) shapes.round else shapes.small

    val bgColor: Color
    val textColor: Color
    val borderColor: Color

    when (effect) {
        TagEffect.Dark -> {
            bgColor = tc?.base ?: colorScheme.info.base
            textColor = colorScheme.white
            borderColor = tc?.base ?: colorScheme.info.base
        }
        TagEffect.Plain -> {
            bgColor = Color.Transparent
            textColor = tc?.base ?: colorScheme.text.regular
            borderColor = tc?.light5 ?: colorScheme.border.base
        }
        TagEffect.Light -> {
            bgColor = tc?.light9 ?: colorScheme.fill.base
            textColor = tc?.base ?: colorScheme.text.regular
            borderColor = tc?.light8 ?: colorScheme.border.lighter
        }
    }

    val tagHeight = when (size) {
        ComponentSize.Large -> 32.dp
        ComponentSize.Default -> 24.dp
        ComponentSize.Small -> 20.dp
    }
    val textStyle = when (size) {
        ComponentSize.Large -> typography.small
        ComponentSize.Default -> typography.extraSmall
        ComponentSize.Small -> typography.extraSmall
    }

    Row(
        modifier = modifier
            .height(tagHeight)
            .clip(shape)
            .background(bgColor)
            .border(1.dp, borderColor, shape)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        NexusText(
            text = text,
            color = textColor,
            style = textStyle,
        )
        if (closable) {
            NexusText(
                text = "✕",
                color = textColor.copy(alpha = 0.7f),
                style = textStyle,
                modifier = Modifier.clickable { onClose?.invoke() },
            )
        }
    }
}
