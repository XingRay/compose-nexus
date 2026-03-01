package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Avatar shape.
 */
enum class AvatarShape {
    Circle,
    Square,
}

/**
 * Element Plus Avatar — a user avatar displaying an icon, text, or image.
 *
 * @param modifier Modifier.
 * @param size Avatar diameter/side length.
 * @param shape Circle or Square.
 * @param backgroundColor Background color.
 * @param content Content composable (icon, text, or image).
 */
@Composable
fun NexusAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    shape: AvatarShape = AvatarShape.Circle,
    backgroundColor: Color = NexusTheme.colorScheme.fill.base,
    content: @Composable () -> Unit,
) {
    val clipShape: Shape = when (shape) {
        AvatarShape.Circle -> CircleShape
        AvatarShape.Square -> NexusTheme.shapes.base
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(clipShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        ProvideContentColorTextStyle(
            contentColor = NexusTheme.colorScheme.white,
            textStyle = NexusTheme.typography.base,
        ) {
            content()
        }
    }
}
