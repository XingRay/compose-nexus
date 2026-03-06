package io.github.xingray.compose.nexus.controls

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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType
import io.github.xingray.compose.nexus.theme.typeColor

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
    type: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default,
    size: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    effect: io.github.xingray.compose.nexus.controls.TagEffect = _root_ide_package_.io.github.xingray.compose.nexus.controls.TagEffect.Light,
    closable: Boolean = false,
    disableTransitions: Boolean = false,
    hit: Boolean = false,
    color: Color? = null,
    round: Boolean = false,
    onClick: (() -> Unit)? = null,
    onClose: (() -> Unit)? = null,
) {
    @Suppress("UNUSED_VARIABLE")
    val _disableTransitions = disableTransitions
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes

    val tc = colorScheme.typeColor(type)
    val shape = if (round) shapes.round else shapes.small

    val bgColor: Color
    val textColor: Color
    var borderColor: Color

    if (color != null) {
        when (effect) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.TagEffect.Plain -> {
                bgColor = Color.Transparent
                textColor = color
                borderColor = color
            }
            _root_ide_package_.io.github.xingray.compose.nexus.controls.TagEffect.Dark -> {
                bgColor = color
                textColor = if (color.luminance() > 0.55f) colorScheme.text.primary else colorScheme.white
                borderColor = color
            }
            _root_ide_package_.io.github.xingray.compose.nexus.controls.TagEffect.Light -> {
                bgColor = color
                textColor = if (color.luminance() > 0.55f) colorScheme.text.primary else colorScheme.white
                borderColor = color
            }
        }
    } else {
        when (effect) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.TagEffect.Dark -> {
                bgColor = tc?.base ?: colorScheme.info.base
                textColor = colorScheme.white
                borderColor = tc?.base ?: colorScheme.info.base
            }
            _root_ide_package_.io.github.xingray.compose.nexus.controls.TagEffect.Plain -> {
                bgColor = Color.Transparent
                textColor = tc?.base ?: colorScheme.text.regular
                borderColor = tc?.light5 ?: colorScheme.border.base
            }
            _root_ide_package_.io.github.xingray.compose.nexus.controls.TagEffect.Light -> {
                bgColor = tc?.light9 ?: colorScheme.fill.base
                textColor = tc?.base ?: colorScheme.text.regular
                borderColor = tc?.light8 ?: colorScheme.border.lighter
            }
        }
    }
    if (hit) {
        borderColor = tc?.base ?: textColor
    }

    val tagHeight = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 32.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 24.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 20.dp
    }
    val textStyle = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> typography.small
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> typography.extraSmall
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> typography.extraSmall
    }

    Row(
        modifier = modifier
            .height(tagHeight)
            .clip(shape)
            .background(bgColor)
            .border(1.dp, borderColor, shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
            text = text,
            color = textColor,
            style = textStyle,
        )
        if (closable) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = "✕",
                color = textColor.copy(alpha = 0.7f),
                style = textStyle,
                modifier = Modifier.clickable { onClose?.invoke() },
            )
        }
    }
}

@Composable
fun NexusCheckTag(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    disabled: Boolean = false,
    type: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val tc = colorScheme.typeColor(type)
    val bg = if (checked) (tc?.base ?: colorScheme.primary.base) else colorScheme.fill.light

    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTag(
        text = text,
        modifier = modifier.then(
            if (!disabled) {
                Modifier.clickable { onCheckedChange(!checked) }
            } else {
                Modifier
            }
        ),
        type = type,
        effect = if (checked) _root_ide_package_.io.github.xingray.compose.nexus.controls.TagEffect.Dark else _root_ide_package_.io.github.xingray.compose.nexus.controls.TagEffect.Light,
        color = bg,
        hit = true,
        onClick = null,
    )
}
