package io.github.xingray.compose.nexus.controls

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
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
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
import io.github.xingray.compose.nexus.controls.drawShapeOverlayStroke
import io.github.xingray.compose.nexus.controls.drawShapeStroke
import io.github.xingray.compose.nexus.controls.withAutoInsertedSpace
import io.github.xingray.compose.nexus.foundation.NotAllowedPointerIcon
import io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType
import io.github.xingray.compose.nexus.theme.TypeColor
import io.github.xingray.compose.nexus.theme.generateTypeColor
import io.github.xingray.compose.nexus.theme.typeColor

// ============================================================================
// Button colors resolution
// ============================================================================

private data class ButtonColors(
    val background: Color,
    val content: Color,
    val border: Color,
)

private data class ButtonGroupConfig(
    val size: io.github.xingray.compose.nexus.theme.ComponentSize?,
    val type: io.github.xingray.compose.nexus.theme.NexusType?,
)

private val LocalButtonGroupConfig = compositionLocalOf {
    _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonGroupConfig(
        size = null,
        type = null,
    )
}

private fun String.withAutoInsertedSpace(enabled: Boolean): String {
    if (!enabled || this.length != 2) return this
    if (this.all { it in '\u4E00'..'\u9FFF' }) {
        return "${this[0]} ${this[1]}"
    }
    return this
}

private fun Modifier.drawShapeStroke(
    shape: Shape,
    color: Color,
    width: Dp,
    dashPattern: List<Dp>? = null,
): Modifier = drawBehind {
    val outline = shape.createOutline(size, layoutDirection, this)
    val path = androidx.compose.ui.graphics.Path()
    when (outline) {
        is androidx.compose.ui.graphics.Outline.Rectangle -> path.addRect(outline.rect)
        is androidx.compose.ui.graphics.Outline.Rounded -> path.addRoundRect(outline.roundRect)
        is androidx.compose.ui.graphics.Outline.Generic -> path.addPath(outline.path)
    }
    val pathEffect = dashPattern?.let { pattern ->
        PathEffect.dashPathEffect(pattern.map { it.toPx() }.toFloatArray())
    }
    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = width.toPx(),
            pathEffect = pathEffect,
        ),
    )
}

private fun Modifier.drawShapeOverlayStroke(
    shape: Shape,
    color: Color,
    width: Dp,
): Modifier = drawWithContent {
    drawContent()
    val outline = shape.createOutline(size, layoutDirection, this)
    val path = androidx.compose.ui.graphics.Path()
    when (outline) {
        is androidx.compose.ui.graphics.Outline.Rectangle -> path.addRect(outline.rect)
        is androidx.compose.ui.graphics.Outline.Rounded -> path.addRoundRect(outline.roundRect)
        is androidx.compose.ui.graphics.Outline.Generic -> path.addPath(outline.path)
    }
    drawPath(
        path = path,
        color = color,
        style = Stroke(width = width.toPx()),
    )
}

@Composable
private fun resolveButtonColors(
    type: io.github.xingray.compose.nexus.theme.NexusType,
    plain: Boolean,
    dashed: Boolean,
    text: Boolean,
    bg: Boolean,
    link: Boolean,
    customColor: Color?,
    isHovered: Boolean,
    isPressed: Boolean,
    disabled: Boolean,
): io.github.xingray.compose.nexus.controls.ButtonColors {
    val cs = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme

    // Custom color button
    if (customColor != null) {
        val tc = _root_ide_package_.io.github.xingray.compose.nexus.theme.generateTypeColor(customColor)
        if (disabled) {
            return if (plain) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = cs.fill.blank,
                    content = tc.light5,
                    border = tc.light5,
                )
            } else {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = tc.light5,
                    content = cs.white,
                    border = tc.light5,
                )
            }
        }
        if (plain) {
            return when {
                isPressed -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = tc.dark2,
                    content = cs.white,
                    border = tc.dark2,
                )
                isHovered -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = tc.base,
                    content = cs.white,
                    border = tc.base,
                )
                else -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = Color.Transparent,
                    content = tc.base,
                    border = tc.base,
                )
            }
        }
        return when {
            isPressed -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                background = tc.dark2,
                content = cs.white,
                border = tc.dark2,
            )
            isHovered -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                background = tc.light3,
                content = cs.white,
                border = tc.light3,
            )
            else -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                background = tc.base,
                content = cs.white,
                border = tc.base,
            )
        }
    }

    val tc: io.github.xingray.compose.nexus.theme.TypeColor? = cs.typeColor(type)

    // Link button
    if (link) {
        val baseColor = tc?.base ?: cs.primary.base
        if (disabled) {
            return _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                background = Color.Transparent,
                content = cs.text.placeholder,
                border = Color.Transparent,
            )
        }
        return _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
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
            return _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                background = if (bg) cs.fill.light else Color.Transparent,
                content = cs.text.placeholder,
                border = Color.Transparent,
            )
        }
        return _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
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
                _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = tc.light9,
                    content = tc.light5,
                    border = tc.light8,
                )
            } else {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = cs.fill.blank,
                    content = cs.text.placeholder,
                    border = cs.border.lighter,
                )
            }
        }
        if (tc == null) {
            // Default type dashed
            return when {
                isPressed -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = cs.fill.blank,
                    content = cs.primary.base,
                    border = cs.primary.light5,
                )
                isHovered -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = cs.fill.blank,
                    content = cs.primary.base,
                    border = cs.primary.base,
                )
                else -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = cs.fill.blank,
                    content = cs.text.regular,
                    border = cs.border.base,
                )
            }
        }
        // Typed dashed
        return when {
            isPressed -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                background = tc.light9,
                content = tc.dark2,
                border = tc.dark2,
            )
            isHovered -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                background = tc.light9,
                content = tc.base,
                border = tc.base,
            )
            else -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
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
                _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = tc.light9,
                    content = tc.light5,
                    border = tc.light8,
                )
            } else {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = tc.light5,
                    content = cs.white,
                    border = tc.light5,
                )
            }
        } else {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
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
                isPressed -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = cs.fill.blank,
                    content = cs.primary.base,
                    border = cs.primary.light5,
                )
                isHovered -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = cs.fill.blank,
                    content = cs.primary.base,
                    border = cs.primary.base,
                )
                else -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                    background = cs.fill.blank,
                    content = cs.text.regular,
                    border = cs.border.base,
                )
            }
        }
        return when {
            isPressed -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                background = cs.primary.light9,
                content = cs.primary.base,
                border = cs.primary.light5,
            )
            isHovered -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                background = cs.primary.light9,
                content = cs.primary.base,
                border = cs.primary.light7,
            )
            else -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                background = cs.fill.blank,
                content = cs.text.regular,
                border = cs.border.base,
            )
        }
    }

    // Typed button — plain variant
    if (plain) {
        return when {
            isPressed -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                background = tc.dark2,
                content = cs.white,
                border = tc.dark2,
            )
            isHovered -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                background = tc.base,
                content = cs.white,
                border = tc.base,
            )
            else -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
                background = tc.light9,
                content = tc.base,
                border = tc.light5,
            )
        }
    }

    // Typed button — solid variant
    return when {
        isPressed -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
            background = tc.dark2,
            content = cs.white,
            border = tc.dark2,
        )
        isHovered -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
            background = tc.light3,
            content = cs.white,
            border = tc.light3,
        )
        else -> _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonColors(
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
    type: io.github.xingray.compose.nexus.theme.NexusType? = null,
    size: io.github.xingray.compose.nexus.theme.ComponentSize? = null,
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
    tag: String = "button",
    autoInsertSpace: Boolean = false,
    icon: (@Composable () -> Unit)? = null,
    loadingContent: (@Composable () -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes
    val sizes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.sizes
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val cs = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val config = _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalNexusConfig.current
    val buttonConfig = config.button
    val groupConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalButtonGroupConfig.current
    val resolvedType = type ?: groupConfig.type ?: buttonConfig.type ?: _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default
    val resolvedSize = size ?: groupConfig.size ?: config.size ?: _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default
    val resolvedPlain = plain || (buttonConfig.plain == true)
    val resolvedText = text || (buttonConfig.text == true)
    val resolvedRound = round || (buttonConfig.round == true)
    val resolvedDashed = dashed || (buttonConfig.dashed == true)

    // keep API parity with Element Plus tag prop even though Compose does not render HTML tags directly.
    @Suppress("UNUSED_VARIABLE")
    val currentTag = tag
    val shouldAutoInsertSpace = autoInsertSpace || (buttonConfig.autoInsertSpace == true)

    val isDisabled = disabled || loading
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    val colors = _root_ide_package_.io.github.xingray.compose.nexus.controls.resolveButtonColors(
        type = resolvedType,
        plain = resolvedPlain,
        dashed = resolvedDashed,
        text = resolvedText,
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
        resolvedRound -> shapes.round
        else -> shapes.base
    }

    // Sizing
    val buttonHeight: Dp = when (resolvedSize) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> sizes.componentLarge
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> sizes.componentDefault
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> sizes.componentSmall
    }
    val horizontalPad: Dp = when (resolvedSize) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 20.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 16.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 12.dp
    }
    val textStyle = when (resolvedSize) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> typography.base
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> typography.base
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> typography.extraSmall
    }

    // Circle buttons have equal width/height with no padding
    val circleSize = if (circle) buttonHeight else Dp.Unspecified
    val effectivePadding = if (circle) PaddingValues(0.dp) else PaddingValues(horizontal = horizontalPad)

    // Text/link buttons have no border
    val showBorder = !resolvedText && !link
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
                    resolvedDashed -> Modifier.drawShapeStroke(
                        shape = shape,
                        color = borderColor,
                        width = 1.dp,
                        dashPattern = listOf(4.dp, 3.dp),
                    )
                    else -> Modifier.border(1.dp, borderColor, shape)
                }
            )
            .then(
                if (isFocused && !isDisabled) {
                    Modifier.drawShapeOverlayStroke(
                        shape = shape,
                        color = cs.primary.light7,
                        width = 2.dp,
                    )
                } else {
                    Modifier
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
                        .focusable(
                            interactionSource = interactionSource,
                        )
                        .pointerHoverIcon(PointerIcon.Hand)
                } else {
                    Modifier
                        .pointerHoverIcon(_root_ide_package_.io.github.xingray.compose.nexus.foundation.NotAllowedPointerIcon)
                }
            )
            .alpha(loadingAlpha),
        contentAlignment = Alignment.Center,
    ) {
        _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle(
            contentColor = colors.content,
            textStyle = textStyle,
        ) {
            Row(
                modifier = Modifier.padding(effectivePadding),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (loading) {
                    val spinnerSize = when (resolvedSize) {
                        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 16.dp
                        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 14.dp
                        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 12.dp
                    }
                    if (loadingContent != null) {
                        loadingContent()
                    } else {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.LoadingSpinner(color = colors.content, size = spinnerSize)
                    }
                } else if (icon != null) {
                    icon()
                }
                content()
            }
        }
    }
}

@Composable
fun NexusButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: io.github.xingray.compose.nexus.theme.NexusType? = null,
    size: io.github.xingray.compose.nexus.theme.ComponentSize? = null,
    plain: Boolean = false,
    textButton: Boolean = false,
    bg: Boolean = false,
    link: Boolean = false,
    round: Boolean = false,
    circle: Boolean = false,
    dashed: Boolean = false,
    loading: Boolean = false,
    disabled: Boolean = false,
    color: Color? = null,
    tag: String = "button",
    autoInsertSpace: Boolean = false,
    icon: (@Composable () -> Unit)? = null,
    loadingContent: (@Composable () -> Unit)? = null,
) {
    val configAutoInsertSpace = _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalNexusConfig.current.button.autoInsertSpace == true
    val shouldAutoInsertSpace = autoInsertSpace || configAutoInsertSpace
    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
        onClick = onClick,
        modifier = modifier,
        type = type,
        size = size,
        plain = plain,
        text = textButton,
        bg = bg,
        link = link,
        round = round,
        circle = circle,
        dashed = dashed,
        loading = loading,
        disabled = disabled,
        color = color,
        tag = tag,
        autoInsertSpace = shouldAutoInsertSpace,
        icon = icon,
        loadingContent = loadingContent,
    ) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = text.withAutoInsertedSpace(shouldAutoInsertSpace))
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
    direction: io.github.xingray.compose.nexus.controls.ButtonGroupDirection = _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonGroupDirection.Horizontal,
    size: io.github.xingray.compose.nexus.theme.ComponentSize? = null,
    type: io.github.xingray.compose.nexus.theme.NexusType? = null,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalButtonGroupConfig provides _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonGroupConfig(
            size = size,
            type = type,
        ),
    ) {
        when (direction) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonGroupDirection.Horizontal -> {
                Row(modifier = modifier) {
                    content()
                }
            }
            _root_ide_package_.io.github.xingray.compose.nexus.controls.ButtonGroupDirection.Vertical -> {
                Column(modifier = modifier) {
                    content()
                }
            }
        }
    }
}
