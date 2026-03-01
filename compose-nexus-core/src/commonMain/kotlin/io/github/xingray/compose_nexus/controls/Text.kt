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
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    maxLines: Int = 0,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    val resolvedColor = when {
        color.isSpecified -> color
        type != NexusType.Default -> NexusTheme.colorScheme.typeColor(type)?.base
            ?: LocalContentColor.current
        else -> LocalContentColor.current
    }
    val mergedStyle = style.merge(TextStyle(color = resolvedColor))

    if (maxLines > 0) {
        BasicText(
            text = text,
            modifier = modifier,
            style = mergedStyle,
            maxLines = maxLines,
            overflow = overflow,
        )
    } else {
        BasicText(
            text = text,
            modifier = modifier,
            style = mergedStyle,
        )
    }
}
