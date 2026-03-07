package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType
import kotlinx.coroutines.delay

enum class DropdownPlacement {
    Top,
    TopStart,
    TopEnd,
    Bottom,
    BottomStart,
    BottomEnd,
}

enum class DropdownTrigger {
    Click,
    Hover,
    ContextMenu,
}

enum class DropdownEffect {
    Light,
    Dark,
}

@Stable
class DropdownState {
    var expanded: Boolean by mutableStateOf(false)
        private set

    fun open() {
        expanded = true
    }

    fun close() {
        expanded = false
    }

    fun toggle() {
        expanded = !expanded
    }

    fun handleOpen() = open()
    fun handleClose() = close()
}

@Composable
fun rememberDropdownState(): DropdownState = remember { DropdownState() }

private data class DropdownMenuContext(
    val hideOnClick: Boolean,
    val onCommand: ((Any?) -> Unit)?,
    val closeMenu: () -> Unit,
)

private val LocalDropdownMenuContext = staticCompositionLocalOf<DropdownMenuContext?> { null }

@Composable
fun NexusDropdown(
    state: DropdownState = rememberDropdownState(),
    modifier: Modifier = Modifier,
    splitButton: Boolean = false,
    buttonText: String = "Dropdown",
    disabled: Boolean = false,
    type: NexusType = NexusType.Default,
    size: ComponentSize = ComponentSize.Default,
    placement: DropdownPlacement = DropdownPlacement.BottomStart,
    triggerMode: DropdownTrigger = DropdownTrigger.Hover,
    effect: DropdownEffect = DropdownEffect.Light,
    hideOnClick: Boolean = true,
    maxHeight: Dp? = null,
    showTimeout: Long = 150L,
    hideTimeout: Long = 150L,
    onClick: (() -> Unit)? = null,
    onCommand: ((Any?) -> Unit)? = null,
    onVisibleChange: ((Boolean) -> Unit)? = null,
    trigger: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows

    val triggerInteraction = remember { MutableInteractionSource() }
    val isHovered by triggerInteraction.collectIsHoveredAsState()

    LaunchedEffect(isHovered, triggerMode, disabled) {
        if (disabled || triggerMode != DropdownTrigger.Hover) return@LaunchedEffect
        if (isHovered) {
            delay(showTimeout.coerceAtLeast(0L))
            state.open()
        } else {
            delay(hideTimeout.coerceAtLeast(0L))
            state.close()
        }
    }

    LaunchedEffect(state.expanded) {
        onVisibleChange?.invoke(state.expanded)
    }

    val (menuAlignment, menuPaddingModifier) = when (placement) {
        DropdownPlacement.Bottom -> Alignment.BottomCenter to Modifier.padding(top = 4.dp)
        DropdownPlacement.BottomStart -> Alignment.BottomStart to Modifier.padding(top = 4.dp)
        DropdownPlacement.BottomEnd -> Alignment.BottomEnd to Modifier.padding(top = 4.dp)
        DropdownPlacement.Top -> Alignment.TopCenter to Modifier.padding(bottom = 4.dp)
        DropdownPlacement.TopStart -> Alignment.TopStart to Modifier.padding(bottom = 4.dp)
        DropdownPlacement.TopEnd -> Alignment.TopEnd to Modifier.padding(bottom = 4.dp)
    }

    val menuBackground = if (effect == DropdownEffect.Dark) colorScheme.text.primary else colorScheme.fill.blank
    val menuBorder = if (effect == DropdownEffect.Dark) Color.Transparent else colorScheme.border.lighter
    val menuTextColor = if (effect == DropdownEffect.Dark) colorScheme.white else colorScheme.text.regular

    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .hoverable(triggerInteraction)
                .then(
                    if (!disabled && (triggerMode == DropdownTrigger.Click || triggerMode == DropdownTrigger.ContextMenu)) {
                        Modifier
                            .clickable(
                                interactionSource = triggerInteraction,
                                indication = null,
                            ) {
                                state.toggle()
                            }
                            .pointerHoverIcon(PointerIcon.Hand)
                    } else {
                        Modifier
                    }
                ),
        ) {
            if (splitButton) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    NexusButton(
                        text = buttonText,
                        onClick = { onClick?.invoke() },
                        type = type,
                        size = size,
                        disabled = disabled,
                    )
                    NexusButton(
                        text = "▾",
                        onClick = { state.toggle() },
                        type = type,
                        size = size,
                        disabled = disabled,
                    )
                }
            } else {
                trigger()
            }
        }

        if (state.expanded) {
            Box(
                modifier = Modifier
                    .align(menuAlignment)
                    .then(menuPaddingModifier)
                    .widthIn(min = 120.dp)
                    .shadow(shadows.light.elevation, shapes.base)
                    .clip(shapes.base)
                    .background(menuBackground)
                    .border(1.dp, menuBorder, shapes.base)
                    .padding(vertical = 6.dp),
            ) {
                val menuContext = DropdownMenuContext(
                    hideOnClick = hideOnClick,
                    onCommand = onCommand,
                    closeMenu = { state.close() },
                )
                CompositionLocalProvider(LocalDropdownMenuContext provides menuContext) {
                    ProvideContentColorTextStyle(
                        contentColor = menuTextColor,
                        textStyle = NexusTheme.typography.base,
                    ) {
                        val menuScrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .then(
                                    if (maxHeight != null) {
                                        Modifier.heightIn(max = maxHeight).verticalScroll(menuScrollState)
                                    } else {
                                        Modifier
                                    }
                                ),
                        ) {
                            content()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NexusDropdownItem(
    text: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    command: Any? = null,
    disabled: Boolean = false,
    divided: Boolean = false,
    icon: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val context = LocalDropdownMenuContext.current

    if (divided) {
        NexusDivider(
            color = colorScheme.border.lighter,
            modifier = Modifier.padding(vertical = 6.dp),
        )
    }

    val bgColor = when {
        isHovered && !disabled -> colorScheme.primary.light9
        else -> Color.Transparent
    }
    val textColor = when {
        disabled -> colorScheme.disabled.text
        isHovered -> colorScheme.primary.base
        else -> colorScheme.text.regular
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor)
            .then(
                if (!disabled) {
                    Modifier
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                        ) {
                            onClick?.invoke()
                            context?.onCommand?.invoke(command)
                            if (context?.hideOnClick == true) {
                                context.closeMenu()
                            }
                        }
                        .pointerHoverIcon(PointerIcon.Hand)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 14.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (icon != null) {
            icon()
        }
        if (content != null) {
            content()
        } else {
            NexusText(
                text = text,
                color = textColor,
                style = typography.base,
            )
        }
    }
}
