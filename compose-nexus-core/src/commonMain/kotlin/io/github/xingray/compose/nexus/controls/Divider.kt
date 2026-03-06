package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose.nexus.theme.NexusTheme

enum class DividerDirection {
    Horizontal,
    Vertical,
}

enum class DividerContentPosition {
    Left,
    Center,
    Right,
}

enum class DividerBorderStyle {
    None,
    Solid,
    Hidden,
    Dashed,
    Dotted,
    Double,
}

@Composable
fun NexusDivider(
    modifier: Modifier = Modifier,
    direction: io.github.xingray.compose.nexus.controls.DividerDirection = _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerDirection.Horizontal,
    contentPosition: io.github.xingray.compose.nexus.controls.DividerContentPosition = _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerContentPosition.Center,
    borderStyle: io.github.xingray.compose.nexus.controls.DividerBorderStyle = _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerBorderStyle.Solid,
    color: Color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.border.light,
    thickness: Dp = 1.dp,
    content: (@Composable () -> Unit)? = null,
) {
    if (borderStyle == _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerBorderStyle.None || borderStyle == _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerBorderStyle.Hidden) return

    if (direction == _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerDirection.Vertical) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerLine(
            modifier = modifier
                .fillMaxHeight()
                .width(thickness),
            direction = _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerDirection.Vertical,
            color = color,
            thickness = thickness,
            borderStyle = borderStyle,
        )
        return
    }

    if (content == null) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerLine(
            modifier = modifier
                .fillMaxWidth()
                .height(thickness),
            direction = _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerDirection.Horizontal,
            color = color,
            thickness = thickness,
            borderStyle = borderStyle,
        )
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val leftWeight = when (contentPosition) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerContentPosition.Left -> 0.12f
                _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerContentPosition.Center -> 1f
                _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerContentPosition.Right -> 1f
            }
            val rightWeight = when (contentPosition) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerContentPosition.Left -> 1f
                _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerContentPosition.Center -> 1f
                _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerContentPosition.Right -> 0.12f
            }

            _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerLine(
                modifier = Modifier
                    .weight(leftWeight)
                    .height(thickness),
                direction = _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerDirection.Horizontal,
                color = color,
                thickness = thickness,
                borderStyle = borderStyle,
            )
            _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle(
                contentColor = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.text.regular,
                textStyle = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.small,
            ) {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    content()
                }
            }
            _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerLine(
                modifier = Modifier
                    .weight(rightWeight)
                    .height(thickness),
                direction = _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerDirection.Horizontal,
                color = color,
                thickness = thickness,
                borderStyle = borderStyle,
            )
        }
    }
}

@Composable
private fun DividerLine(
    modifier: Modifier,
    direction: io.github.xingray.compose.nexus.controls.DividerDirection,
    color: Color,
    thickness: Dp,
    borderStyle: io.github.xingray.compose.nexus.controls.DividerBorderStyle,
) {
    if (borderStyle == _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerBorderStyle.Solid) {
        Box(modifier = modifier.background(color))
        return
    }

    Canvas(modifier = modifier) {
        val strokeWidth = thickness.toPx().coerceAtLeast(1f)
        val (startX, startY, endX, endY) = if (direction == _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerDirection.Horizontal) {
            listOf(0f, size.height / 2f, size.width, size.height / 2f)
        } else {
            listOf(size.width / 2f, 0f, size.width / 2f, size.height)
        }

        when (borderStyle) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerBorderStyle.Dashed -> {
                drawLine(
                    color = color,
                    start = androidx.compose.ui.geometry.Offset(startX, startY),
                    end = androidx.compose.ui.geometry.Offset(endX, endY),
                    strokeWidth = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f),
                )
            }

            _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerBorderStyle.Dotted -> {
                drawLine(
                    color = color,
                    start = androidx.compose.ui.geometry.Offset(startX, startY),
                    end = androidx.compose.ui.geometry.Offset(endX, endY),
                    strokeWidth = strokeWidth,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 4f), 0f),
                )
            }

            _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerBorderStyle.Double -> {
                if (direction == _root_ide_package_.io.github.xingray.compose.nexus.controls.DividerDirection.Horizontal) {
                    val y1 = size.height * 0.35f
                    val y2 = size.height * 0.65f
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(0f, y1),
                        end = androidx.compose.ui.geometry.Offset(size.width, y1),
                        strokeWidth = strokeWidth,
                    )
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(0f, y2),
                        end = androidx.compose.ui.geometry.Offset(size.width, y2),
                        strokeWidth = strokeWidth,
                    )
                } else {
                    val x1 = size.width * 0.35f
                    val x2 = size.width * 0.65f
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(x1, 0f),
                        end = androidx.compose.ui.geometry.Offset(x1, size.height),
                        strokeWidth = strokeWidth,
                    )
                    drawLine(
                        color = color,
                        start = androidx.compose.ui.geometry.Offset(x2, 0f),
                        end = androidx.compose.ui.geometry.Offset(x2, size.height),
                        strokeWidth = strokeWidth,
                    )
                }
            }

            else -> Unit
        }
    }
}
