package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.foundation.LocalContentColor

/**
 * Element Plus Icon — displays an icon with customizable size and color.
 *
 * @param painter The icon painter.
 * @param contentDescription Accessibility description.
 * @param modifier Modifier.
 * @param size Icon size. Defaults to 16dp.
 * @param color Icon tint color. Defaults to [LocalContentColor].
 */
@Composable
fun NexusIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    color: Color = LocalContentColor.current,
) {
    val colorFilter = if (color.isSpecified) ColorFilter.tint(color) else null
    val semanticsModifier = if (contentDescription != null) {
        Modifier.semantics {
            this.contentDescription = contentDescription
            this.role = Role.Image
        }
    } else {
        Modifier
    }
    Box(
        modifier = modifier
            .then(semanticsModifier)
            .size(size)
            .paint(
                painter = painter,
                colorFilter = colorFilter,
                contentScale = ContentScale.Fit,
            ),
    )
}

/**
 * Overload accepting an [ImageVector].
 */
@Composable
fun NexusIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    color: Color = LocalContentColor.current,
) {
    NexusIcon(
        painter = rememberVectorPainter(imageVector),
        contentDescription = contentDescription,
        modifier = modifier,
        size = size,
        color = color,
    )
}
