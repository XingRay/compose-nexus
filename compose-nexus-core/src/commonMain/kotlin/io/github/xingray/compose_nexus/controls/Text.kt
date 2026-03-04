package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import io.github.xingray.compose_nexus.foundation.LocalContentColor
import io.github.xingray.compose_nexus.foundation.LocalTextStyle
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import io.github.xingray.compose_nexus.theme.typeColor

/**
 * Element Plus Text — styled text component with semantic type colors.
 *
 * @param text The text to display.
 * @param modifier Modifier.
 * @param type Semantic color type (Primary, Success, Warning, Danger, Info).
 * @param style Text style. Defaults to the current [LocalTextStyle].
 * @param color Explicit color override. If [Color.Unspecified], resolved from [type].
 * @param maxLines Maximum lines, 0 for unlimited.
 * @param overflow Text overflow behavior.
 */
@Composable
fun NexusText(
    text: String,
    modifier: Modifier = Modifier,
    type: NexusType = NexusType.Default,
    size: ComponentSize = ComponentSize.Default,
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
        type != NexusType.Default -> NexusTheme.colorScheme.typeColor(type)?.base
            ?: LocalContentColor.current
        else -> LocalContentColor.current
    }
    val sizeStyle = when (size) {
        ComponentSize.Large -> NexusTheme.typography.large
        ComponentSize.Default -> NexusTheme.typography.base
        ComponentSize.Small -> NexusTheme.typography.small
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
    type: NexusType = NexusType.Default,
    size: ComponentSize = ComponentSize.Default,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    tag: String = "span",
    content: @Composable () -> Unit,
) {
    @Suppress("UNUSED_VARIABLE")
    val currentTag = tag

    val resolvedColor = when {
        color.isSpecified -> color
        type != NexusType.Default -> NexusTheme.colorScheme.typeColor(type)?.base
            ?: LocalContentColor.current
        else -> LocalContentColor.current
    }
    val sizeStyle = when (size) {
        ComponentSize.Large -> NexusTheme.typography.large
        ComponentSize.Default -> NexusTheme.typography.base
        ComponentSize.Small -> NexusTheme.typography.small
    }
    val mergedStyle = sizeStyle.merge(style)

    androidx.compose.foundation.layout.Box(modifier = modifier) {
        ProvideContentColorTextStyle(
            contentColor = resolvedColor,
            textStyle = mergedStyle,
        ) {
            content()
        }
    }
}
