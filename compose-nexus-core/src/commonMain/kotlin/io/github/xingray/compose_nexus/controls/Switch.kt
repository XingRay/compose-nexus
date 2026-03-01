package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Element Plus Switch — a toggle switch component.
 *
 * @param checked Whether the switch is on.
 * @param onCheckedChange Callback when toggled.
 * @param modifier Modifier.
 * @param size Component size.
 * @param disabled Disabled state.
 * @param loading Loading state (shows spinner concept, disables interaction).
 * @param activeColor Track color when on.
 * @param inactiveColor Track color when off.
 * @param activeText Text shown on the track when on.
 * @param inactiveText Text shown on the track when off.
 * @param label Optional label content.
 */
@Composable
fun NexusSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: ComponentSize = ComponentSize.Default,
    disabled: Boolean = false,
    loading: Boolean = false,
    activeColor: Color = NexusTheme.colorScheme.primary.base,
    inactiveColor: Color = NexusTheme.colorScheme.border.base,
    activeText: String? = null,
    inactiveText: String? = null,
    label: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val isDisabled = disabled || loading

    // Dimensions based on size
    val trackWidth: Dp = when (size) {
        ComponentSize.Large -> 56.dp
        ComponentSize.Default -> 40.dp
        ComponentSize.Small -> 32.dp
    }
    val trackHeight: Dp = when (size) {
        ComponentSize.Large -> 24.dp
        ComponentSize.Default -> 20.dp
        ComponentSize.Small -> 16.dp
    }
    val thumbSize: Dp = trackHeight - 4.dp
    val thumbTravel: Dp = trackWidth - trackHeight

    // Animate thumb position
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) thumbTravel else 0.dp,
        animationSpec = NexusTheme.motion.tweenFast(),
        label = "switch-thumb",
    )

    val trackColor = when {
        isDisabled && checked -> activeColor.copy(alpha = 0.5f)
        isDisabled -> inactiveColor.copy(alpha = 0.5f)
        checked -> activeColor
        else -> inactiveColor
    }

    val textColor = if (isDisabled) colorScheme.disabled.text else colorScheme.text.regular

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Switch track
        Box(
            modifier = Modifier
                .width(trackWidth)
                .height(trackHeight)
                .clip(RoundedCornerShape(trackHeight / 2))
                .background(trackColor)
                .then(
                    if (!isDisabled) {
                        Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                role = Role.Switch,
                            ) { onCheckedChange(!checked) }
                            .pointerHoverIcon(PointerIcon.Hand)
                    } else Modifier
                ),
        ) {
            // Inner text (on/off labels on track)
            if (activeText != null || inactiveText != null) {
                val text = if (checked) activeText else inactiveText
                if (text != null) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(
                                start = if (checked) 6.dp else thumbSize + 4.dp,
                                end = if (checked) thumbSize + 4.dp else 6.dp,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        ProvideContentColorTextStyle(
                            contentColor = colorScheme.white,
                            textStyle = NexusTheme.typography.extraSmall,
                        ) {
                            NexusText(text = text, color = colorScheme.white)
                        }
                    }
                }
            }

            // Thumb
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset + 2.dp, y = 2.dp)
                    .size(thumbSize)
                    .clip(CircleShape)
                    .background(colorScheme.white),
            )
        }

        // Label
        if (label != null) {
            io.github.xingray.compose_nexus.foundation.ProvideContentColor(textColor) {
                label()
            }
        }
    }
}
