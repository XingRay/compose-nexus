package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Element Plus Divider — a separator line, optionally with text content.
 *
 * @param modifier Modifier for the divider.
 * @param direction Horizontal or vertical.
 * @param contentPosition Position of text content (left/center/right), only for horizontal.
 * @param borderStyle Currently only solid is supported.
 * @param color Divider line color. Defaults to [NexusTheme.colorScheme.border.light].
 * @param content Optional text content displayed in the divider line.
 */
@Composable
fun NexusDivider(
    modifier: Modifier = Modifier,
    direction: DividerDirection = DividerDirection.Horizontal,
    contentPosition: DividerContentPosition = DividerContentPosition.Center,
    color: Color = NexusTheme.colorScheme.border.light,
    thickness: Dp = 1.dp,
    content: (@Composable () -> Unit)? = null,
) {
    if (direction == DividerDirection.Vertical) {
        Box(
            modifier = modifier
                .width(thickness)
                .background(color),
        )
        return
    }

    // Horizontal divider
    if (content == null) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(thickness)
                .background(color),
        )
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val leftWeight = when (contentPosition) {
                DividerContentPosition.Left -> 0.1f
                DividerContentPosition.Center -> 1f
                DividerContentPosition.Right -> 1f
            }
            val rightWeight = when (contentPosition) {
                DividerContentPosition.Left -> 1f
                DividerContentPosition.Center -> 1f
                DividerContentPosition.Right -> 0.1f
            }

            Box(
                modifier = Modifier
                    .weight(leftWeight)
                    .height(thickness)
                    .background(color),
            )
            ProvideContentColorTextStyle(
                contentColor = NexusTheme.colorScheme.text.regular,
                textStyle = NexusTheme.typography.small,
            ) {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    content()
                }
            }
            Box(
                modifier = Modifier
                    .weight(rightWeight)
                    .height(thickness)
                    .background(color),
            )
        }
    }
}

enum class DividerDirection {
    Horizontal,
    Vertical,
}

enum class DividerContentPosition {
    Left,
    Center,
    Right,
}
