package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import io.github.xingray.compose.nexus.foundation.LocalContentColor
import io.github.xingray.compose.nexus.foundation.LocalTextStyle
import io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType
import io.github.xingray.compose.nexus.theme.typeColor

/**
 * Element Plus Text — styled text component with semantic type colors.
 *
 * @param text The text to display.
 * @param modifier Modifier.
 * @param type Semantic color type (Primary, Success, Warning, Danger, Info).
 * @param style Text style. Defaults to the current [io.github.xingray.compose.nexus.foundation.LocalTextStyle].
 * @param color Explicit color override. If [Color.Unspecified], resolved from [type].
 * @param maxLines Maximum lines, 0 for unlimited.
 * @param overflow Text overflow behavior.
 */
@Composable
fun NexusText(
    text: String,
    modifier: Modifier = Modifier,
    type: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default,
    size: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    truncated: Boolean = false,
    lineClamp: Int? = null,
    tag: String = "span",
    maxLines: Int = 0,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    @Suppress("UNUSED_VARIABLE")
    val currentTag = tag

    val resolvedColor = when {
        color.isSpecified -> color
        type != _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default -> _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.typeColor(type)?.base
            ?: _root_ide_package_.io.github.xingray.compose.nexus.foundation.LocalContentColor.current
        else -> _root_ide_package_.io.github.xingray.compose.nexus.foundation.LocalContentColor.current
    }
    val sizeStyle = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.large
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.base
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.small
    }
    val mergedStyle = sizeStyle.merge(style).merge(TextStyle(color = resolvedColor))
    val effectiveMaxLines = when {
        lineClamp != null && lineClamp > 0 -> lineClamp
        truncated -> 1
        maxLines > 0 -> maxLines
        else -> 0
    }
    val effectiveOverflow = when {
        (truncated || (lineClamp != null && lineClamp > 0)) && overflow == TextOverflow.Clip -> TextOverflow.Ellipsis
        else -> overflow
    }

    if (effectiveMaxLines > 0) {
        BasicText(
            text = text,
            modifier = modifier,
            style = mergedStyle,
            maxLines = effectiveMaxLines,
            overflow = effectiveOverflow,
        )
    } else {
        BasicText(
            text = text,
            modifier = modifier,
            style = mergedStyle,
        )
    }
}

@Composable
fun NexusText(
    modifier: Modifier = Modifier,
    type: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default,
    size: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    tag: String = "span",
    content: @Composable () -> Unit,
) {
    @Suppress("UNUSED_VARIABLE")
    val currentTag = tag

    val resolvedColor = when {
        color.isSpecified -> color
        type != _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default -> _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.typeColor(type)?.base
            ?: _root_ide_package_.io.github.xingray.compose.nexus.foundation.LocalContentColor.current
        else -> _root_ide_package_.io.github.xingray.compose.nexus.foundation.LocalContentColor.current
    }
    val sizeStyle = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.large
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.base
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.small
    }
    val mergedStyle = sizeStyle.merge(style)

    androidx.compose.foundation.layout.Box(modifier = modifier) {
        _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle(
            contentColor = resolvedColor,
            textStyle = mergedStyle,
        ) {
            content()
        }
    }
}
