package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
    isHovered: Boolean,
    isPressed: Boolean,
    disabled: Boolean,
): ButtonColors {
    val cs = NexusTheme.colorScheme
    val tc: TypeColor? = cs.typeColor(type)

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
                background = cs.white,
                content = cs.text.placeholder,
                border = cs.border.lighter,
            )
        }
    }

    // Default type (no type color)
    if (tc == null) {
        return when {
            isPressed -> ButtonColors(
                background = cs.white,
                content = cs.primary.dark2,
                border = cs.primary.dark2,
            )
            isHovered -> ButtonColors(
                background = cs.primary.light9,
                content = cs.primary.base,
                border = cs.primary.light7,
            )
            else -> ButtonColors(
                background = cs.white,
                content = cs.text.regular,
                border = cs.border.base,
            )
        }
    }

    // Typed button
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
// NexusButton
// ============================================================================

/**
 * Element Plus Button — fully styled button with type, size, plain/round/circle variants.
 *
 * @param onClick Click handler.
 * @param modifier Modifier.
 * @param type Semantic type (Primary, Success, Warning, Danger, Info, Default).
 * @param size Component size (Large, Default, Small).
 * @param plain Plain variant with lighter background.
 * @param round Round corners (pill shape).
 * @param circle Circle shape (for icon-only buttons).
 * @param loading Show loading state (disables click).
 * @param disabled Disabled state.
 * @param content Button content (text, icon, etc.).
 */
@Composable
fun NexusButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: NexusType = NexusType.Default,
    size: ComponentSize = ComponentSize.Default,
    plain: Boolean = false,
    round: Boolean = false,
    circle: Boolean = false,
    loading: Boolean = false,
    disabled: Boolean = false,
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
    val height: Dp = when (size) {
        ComponentSize.Large -> sizes.componentLarge
        ComponentSize.Default -> sizes.componentDefault
        ComponentSize.Small -> sizes.componentSmall
    }
    val padding: PaddingValues = when (size) {
        ComponentSize.Large -> sizes.buttonPaddingLarge
        ComponentSize.Default -> sizes.buttonPaddingDefault
        ComponentSize.Small -> sizes.buttonPaddingSmall
    }
    val textStyle = when (size) {
        ComponentSize.Large -> typography.base
        ComponentSize.Default -> typography.base
        ComponentSize.Small -> typography.extraSmall
    }

    // Circle buttons have equal width/height with no horizontal padding
    val circleSize = if (circle) height else Dp.Unspecified
    val effectivePadding = if (circle) PaddingValues(0.dp) else padding

    val loadingAlpha by animateFloatAsState(
        targetValue = if (loading) 0.6f else 1f,
        label = "loading-alpha",
    )

    Box(
        modifier = modifier
            .then(
                if (circleSize != Dp.Unspecified) {
                    Modifier.size(circleSize)
                } else {
                    Modifier.defaultMinSize(minHeight = height)
                }
            )
            .clip(shape)
            .background(colors.background)
            .border(1.dp, colors.border, shape)
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
                content = content,
            )
        }
    }
}
