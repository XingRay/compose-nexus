package io.github.xingray.compose.nexus.controls

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.rotate
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
import io.github.xingray.compose.nexus.foundation.LocalContentColor
import io.github.xingray.compose.nexus.foundation.ProvideContentColor

/**
 * Element Plus Icon — displays an icon with customizable size and color.
 *
 * @param painter The icon painter.
 * @param contentDescription Accessibility description.
 * @param modifier Modifier.
 * @param size Icon size. Defaults to 16dp.
 * @param color Icon tint color. Defaults to [io.github.xingray.compose.nexus.foundation.LocalContentColor].
 * @param spinning Whether icon should rotate continuously.
 */
@Composable
fun NexusIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    color: Color = LocalContentColor.current,
    spinning: Boolean = false,
) {
    val colorFilter = if (color.isSpecified) ColorFilter.tint(color) else null
    val spinModifier = spinningModifier(spinning)
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
            .then(spinModifier)
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
    spinning: Boolean = false,
) {
    NexusIcon(
        painter = rememberVectorPainter(imageVector),
        contentDescription = contentDescription,
        modifier = modifier,
        size = size,
        color = color,
        spinning = spinning,
    )
}

/**
 * Slot-based icon wrapper similar to Element Plus `el-icon`.
 */
@Composable
fun NexusIcon(
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 16.dp,
    color: Color = LocalContentColor.current,
    spinning: Boolean = false,
    content: @Composable () -> Unit,
) {
    val spinModifier = spinningModifier(spinning)
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
            .then(spinModifier),
    ) {
        ProvideContentColor(color) {
            content()
        }
    }
}

@Composable
private fun spinningModifier(spinning: Boolean): Modifier {
    if (!spinning) return Modifier
    val infiniteTransition = rememberInfiniteTransition(label = "icon-spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "icon-rotation",
    )
    return Modifier.rotate(rotation)
}
