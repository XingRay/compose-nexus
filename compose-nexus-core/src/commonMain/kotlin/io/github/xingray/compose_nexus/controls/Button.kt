package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType
import io.github.xingray.compose_nexus.theme.TypeColor
import io.github.xingray.compose_nexus.theme.generateTypeColor
import io.github.xingray.compose_nexus.theme.typeColor

// ============================================================================
// Button colors resolution
// ============================================================================

private data class ButtonColors(
    val background: Color,
    val content: Color,
    val border: Color,
)

@Composable
private fun resolveButtonColors(
    type: NexusType,
    plain: Boolean,
    dashed: Boolean,
    text: Boolean,
    bg: Boolean,
    link: Boolean,
    customColor: Color?,
    isHovered: Boolean,
    isPressed: Boolean,
    disabled: Boolean,
): ButtonColors {
    val cs = NexusTheme.colorScheme

    // Custom color button
    if (customColor != null) {
        val tc = generateTypeColor(customColor)
        if (disabled) {
            return if (plain) {
                ButtonColors(
                    background = cs.fill.blank,
                    content = tc.light5,
                    border = tc.light5,
                )
            } else {
                ButtonColors(
                    background = tc.light5,
                    content = cs.white,
                    border = tc.light5,
                )
            }
        }
        if (plain) {
            return when {
                isPressed -> ButtonColors(
                    background = tc.dark2,
                    content = cs.white,
                    border = tc.dark2,
                )
                isHovered -> ButtonColors(
                    background = tc.base,
                    content = cs.white,
                    border = tc.base,
                )
                else -> ButtonColors(
                    background = Color.Transparent,
                    content = tc.base,
                    border = tc.base,
                )
            }
        }
        return when {
            isPressed -> ButtonColors(
                background = tc.dark2,
                content = cs.white,
                border = tc.dark2,
            )
            isHovered -> ButtonColors(
                background = tc.light3,
                content = cs.white,
                border = tc.light3,
            )
            else -> ButtonColors(
                background = tc.base,
                content = cs.white,
                border = tc.base,
            )
        }
    }

    val tc: TypeColor? = cs.typeColor(type)

    // Link button
    if (link) {
        val baseColor = tc?.base ?: cs.primary.base
        if (disabled) {
            return ButtonColors(
                background = Color.Transparent,
                content = cs.text.placeholder,
                border = Color.Transparent,
            )
        }
        return ButtonColors(
            background = Color.Transparent,
            content = when {
                isPressed -> tc?.dark2 ?: cs.primary.dark2
                isHovered -> baseColor
                else -> baseColor
            },
            border = Color.Transparent,
        )
    }

    // Text button
    if (text) {
        val baseColor = tc?.base ?: cs.text.regular
        if (disabled) {
            return ButtonColors(
                background = if (bg) cs.fill.light else Color.Transparent,
                content = cs.text.placeholder,
                border = Color.Transparent,
            )
        }
        return ButtonColors(
            background = when {
                bg && isPressed -> cs.fill.darker
                bg && isHovered -> cs.fill.light
                bg -> cs.fill.light
                isPressed -> cs.fill.light
                isHovered -> cs.fill.lighter
                else -> Color.Transparent
            },
            content = when {
                isPressed -> tc?.dark2 ?: cs.text.primary
                isHovered -> tc?.base ?: cs.text.primary
                else -> baseColor
            },
            border = Color.Transparent,
        )
    }

    // Dashed button — light bg stays constant, text/border darken on hover/press
    if (dashed) {
        if (disabled) {
            return if (tc != null) {
                ButtonColors(
                    background = tc.light9,
                    content = tc.light5,
                    border = tc.light8,
                )
            } else {
                ButtonColors(
                    background = cs.fill.blank,
                    content = cs.text.placeholder,
                    border = cs.border.lighter,
                )
            }
        }
        if (tc == null) {
            // Default type dashed
            return when {
                isPressed -> ButtonColors(
                    background = cs.fill.blank,
                    content = cs.primary.dark2,
                    border = cs.primary.dark2,
                )
                isHovered -> ButtonColors(
                    background = cs.fill.blank,
                    content = cs.primary.base,
                    border = cs.primary.base,
                )
                else -> ButtonColors(
                    background = cs.fill.blank,
                    content = cs.text.regular,
                    border = cs.border.base,
                )
            }
        }
        // Typed dashed
        return when {
            isPressed -> ButtonColors(
                background = tc.light9,
                content = tc.dark2,
                border = tc.dark2,
            )
            isHovered -> ButtonColors(
                background = tc.light9,
                content = tc.base,
                border = tc.base,
            )
            else -> ButtonColors(
                background = tc.light9,
                content = tc.base,
                border = tc.light5,
            )
        }
    }

    // Standard buttons (existing logic)
    if (disabled) {
        return if (tc != null) {
            if (plain) {
                ButtonColors(
                    background = tc.light9,
                    content = tc.light5,
                    border = tc.light8,
                )
            } else {
                ButtonColors(
                    background = tc.light5,
                    content = cs.white,
                    border = tc.light5,
                )
            }
        } else {
            ButtonColors(
                background = cs.fill.blank,
                content = cs.text.placeholder,
                border = cs.border.lighter,
            )
        }
    }

    // Default type (no type color)
    if (tc == null) {
        if (plain) {
            return when {
                isPressed -> ButtonColors(
                    background = cs.fill.blank,
                    content = cs.primary.dark2,
                    border = cs.primary.dark2,
                )
                isHovered -> ButtonColors(
                    background = cs.fill.blank,
                    content = cs.primary.base,
                    border = cs.primary.base,
                )
                else -> ButtonColors(
                    background = cs.fill.blank,
                    content = cs.text.regular,
                    border = cs.border.base,
                )
            }
        }
        return when {
            isPressed -> ButtonColors(
                background = cs.fill.blank,
                content = cs.primary.dark2,
                border = cs.primary.dark2,
            )
            isHovered -> ButtonColors(
                background = cs.primary.light9,
                content = cs.primary.base,
                border = cs.primary.light7,
            )
            else -> ButtonColors(
                background = cs.fill.blank,
                content = cs.text.regular,
                border = cs.border.base,
            )
        }
    }

    // Typed button — plain variant
    if (plain) {
        return when {
            isPressed -> ButtonColors(
                background = tc.dark2,
                content = cs.white,
                border = tc.dark2,
            )
            isHovered -> ButtonColors(
                background = tc.base,
                content = cs.white,
                border = tc.base,
            )
            else -> ButtonColors(
                background = tc.light9,
                content = tc.base,
                border = tc.light5,
            )
        }
    }

    // Typed button — solid variant
    return when {
        isPressed -> ButtonColors(
            background = tc.dark2,
            content = cs.white,
            border = tc.dark2,
        )
        isHovered -> ButtonColors(
            background = tc.light3,
            content = cs.white,
            border = tc.light3,
        )
        else -> ButtonColors(
            background = tc.base,
            content = cs.white,
            border = tc.base,
        )
    }
}

// ============================================================================
// Loading spinner
// ============================================================================

@Composable
private fun LoadingSpinner(color: Color, size: Dp = 14.dp) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading-spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "loading-rotation",
    )
    Box(
        modifier = Modifier
            .size(size)
            .rotate(rotation)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val radius = (this.size.minDimension - strokeWidth) / 2f
                val center = Offset(this.size.width / 2f, this.size.height / 2f)
                drawArc(
                    color = color,
                    startAngle = 0f,
                    sweepAngle = 270f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )
            },
    )
}

// ============================================================================
// NexusButton
// ============================================================================

/**
 * Element Plus Button — fully styled button with type, size, plain/round/circle/text/link/dashed variants.
 *
 * @param onClick Click handler.
 * @param modifier Modifier.
 * @param type Semantic type (Primary, Success, Warning, Danger, Info, Default).
 * @param size Component size (Large, Default, Small).
 * @param plain Plain variant with lighter background.
 * @param text Text button with no background or border.
 * @param bg Whether to show background color for text button.
 * @param link Link button style.
 * @param round Round corners (pill shape).
 * @param circle Circle shape (for icon-only buttons).
 * @param dashed Dashed border style.
 * @param loading Show loading state with spinner (disables click).
 * @param disabled Disabled state.
 * @param color Custom button color (overrides type).
 * @param content Button content (text, icon, etc.).
 */
@Composable
fun NexusButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: NexusType = NexusType.Default,
    size: ComponentSize = ComponentSize.Default,
    plain: Boolean = false,
    text: Boolean = false,
    bg: Boolean = false,
    link: Boolean = false,
    round: Boolean = false,
    circle: Boolean = false,
    dashed: Boolean = false,
    loading: Boolean = false,
    disabled: Boolean = false,
    color: Color? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val shapes = NexusTheme.shapes
    val sizes = NexusTheme.sizes
    val typography = NexusTheme.typography

    val isDisabled = disabled || loading
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val colors = resolveButtonColors(
        type = type,
        plain = plain,
        dashed = dashed,
        text = text,
        bg = bg,
        link = link,
        customColor = color,
        isHovered = isHovered,
        isPressed = isPressed,
        disabled = isDisabled,
    )

    // Shape
    val shape: Shape = when {
        circle -> shapes.circle
        round -> shapes.round
        else -> shapes.base
    }

    // Sizing
    val buttonHeight: Dp = when (size) {
        ComponentSize.Large -> sizes.componentLarge
        ComponentSize.Default -> sizes.componentDefault
        ComponentSize.Small -> sizes.componentSmall
    }
    val horizontalPad: Dp = when (size) {
        ComponentSize.Large -> 20.dp
        ComponentSize.Default -> 16.dp
        ComponentSize.Small -> 12.dp
    }
    val textStyle = when (size) {
        ComponentSize.Large -> typography.base
        ComponentSize.Default -> typography.base
        ComponentSize.Small -> typography.extraSmall
    }

    // Circle buttons have equal width/height with no padding
    val circleSize = if (circle) buttonHeight else Dp.Unspecified
    val effectivePadding = if (circle) PaddingValues(0.dp) else PaddingValues(horizontal = horizontalPad)

    // Text/link buttons have no border
    val showBorder = !text && !link
    val borderColor = if (showBorder) colors.border else Color.Transparent

    val loadingAlpha by animateFloatAsState(
        targetValue = if (loading) 0.7f else 1f,
        label = "loading-alpha",
    )

    Box(
        modifier = modifier
            .then(
                if (circleSize != Dp.Unspecified) {
                    Modifier.size(circleSize)
                } else {
                    Modifier.height(buttonHeight)
                }
            )
            .clip(shape)
            .background(colors.background)
            .then(
                when {
                    !showBorder -> Modifier
                    dashed -> Modifier.drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        val outline = shape.createOutline(this.size, layoutDirection, this)
                        val borderPath = androidx.compose.ui.graphics.Path()
                        when (outline) {
                            is androidx.compose.ui.graphics.Outline.Rectangle -> {
                                borderPath.addRect(outline.rect)
                            }
                            is androidx.compose.ui.graphics.Outline.Rounded -> {
                                borderPath.addRoundRect(outline.roundRect)
                            }
                            is androidx.compose.ui.graphics.Outline.Generic -> {
                                borderPath.addPath(outline.path)
                            }
                        }
                        drawPath(
                            path = borderPath,
                            color = borderColor,
                            style = Stroke(
                                width = strokeWidth,
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(4.dp.toPx(), 3.dp.toPx())
                                )
                            )
                        )
                    }
                    else -> Modifier.border(1.dp, borderColor, shape)
                }
            )
            .then(
                if (!isDisabled) {
                    Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onClick,
                            role = Role.Button,
                        )
                        .pointerHoverIcon(PointerIcon.Hand)
                } else {
                    Modifier
                }
            )
            .alpha(loadingAlpha),
        contentAlignment = Alignment.Center,
    ) {
        ProvideContentColorTextStyle(
            contentColor = colors.content,
            textStyle = textStyle,
        ) {
            Row(
                modifier = Modifier.padding(effectivePadding),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (loading) {
                    val spinnerSize = when (size) {
                        ComponentSize.Large -> 16.dp
                        ComponentSize.Default -> 14.dp
                        ComponentSize.Small -> 12.dp
                    }
                    LoadingSpinner(color = colors.content, size = spinnerSize)
                }
                content()
            }
        }
    }
}

// ============================================================================
// NexusButtonGroup
// ============================================================================

enum class ButtonGroupDirection {
    Horizontal,
    Vertical,
}

/**
 * Element Plus ButtonGroup — groups buttons together.
 *
 * @param modifier Modifier.
 * @param direction Layout direction (Horizontal or Vertical).
 * @param content Buttons to group.
 */
@Composable
fun NexusButtonGroup(
    modifier: Modifier = Modifier,
    direction: ButtonGroupDirection = ButtonGroupDirection.Horizontal,
    content: @Composable () -> Unit,
) {
    when (direction) {
        ButtonGroupDirection.Horizontal -> {
            Row(modifier = modifier) {
                content()
            }
        }
        ButtonGroupDirection.Vertical -> {
            Column(modifier = modifier) {
                content()
            }
        }
    }
}
