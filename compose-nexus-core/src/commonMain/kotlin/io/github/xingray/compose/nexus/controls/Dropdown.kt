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
fun rememberDropdownState(): io.github.xingray.compose.nexus.controls.DropdownState = remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownState() }

private data class DropdownMenuContext(
    val hideOnClick: Boolean,
    val onCommand: ((Any?) -> Unit)?,
    val closeMenu: () -> Unit,
)

private val LocalDropdownMenuContext = staticCompositionLocalOf<io.github.xingray.compose.nexus.controls.DropdownMenuContext?> { null }

@Composable
fun NexusDropdown(
    state: io.github.xingray.compose.nexus.controls.DropdownState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberDropdownState(),
    modifier: Modifier = Modifier,
    splitButton: Boolean = false,
    buttonText: String = "Dropdown",
    disabled: Boolean = false,
    type: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default,
    size: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    placement: io.github.xingray.compose.nexus.controls.DropdownPlacement = _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownPlacement.BottomStart,
    triggerMode: io.github.xingray.compose.nexus.controls.DropdownTrigger = _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownTrigger.Hover,
    effect: io.github.xingray.compose.nexus.controls.DropdownEffect = _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownEffect.Light,
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
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes
    val shadows = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shadows

    val triggerInteraction = remember { MutableInteractionSource() }
    val isHovered by triggerInteraction.collectIsHoveredAsState()

    LaunchedEffect(isHovered, triggerMode, disabled) {
        if (disabled || triggerMode != _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownTrigger.Hover) return@LaunchedEffect
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
        _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownPlacement.Bottom -> Alignment.BottomCenter to Modifier.padding(top = 4.dp)
        _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownPlacement.BottomStart -> Alignment.BottomStart to Modifier.padding(top = 4.dp)
        _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownPlacement.BottomEnd -> Alignment.BottomEnd to Modifier.padding(top = 4.dp)
        _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownPlacement.Top -> Alignment.TopCenter to Modifier.padding(bottom = 4.dp)
        _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownPlacement.TopStart -> Alignment.TopStart to Modifier.padding(bottom = 4.dp)
        _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownPlacement.TopEnd -> Alignment.TopEnd to Modifier.padding(bottom = 4.dp)
    }

    val menuBackground = if (effect == _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownEffect.Dark) colorScheme.text.primary else colorScheme.fill.blank
    val menuBorder = if (effect == _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownEffect.Dark) Color.Transparent else colorScheme.border.lighter
    val menuTextColor = if (effect == _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownEffect.Dark) colorScheme.white else colorScheme.text.regular

    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .hoverable(triggerInteraction)
                .then(
                    if (!disabled && (triggerMode == _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownTrigger.Click || triggerMode == _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownTrigger.ContextMenu)) {
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
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                        text = buttonText,
                        onClick = { onClick?.invoke() },
                        type = type,
                        size = size,
                        disabled = disabled,
                    )
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
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
                val menuContext = _root_ide_package_.io.github.xingray.compose.nexus.controls.DropdownMenuContext(
                    hideOnClick = hideOnClick,
                    onCommand = onCommand,
                    closeMenu = { state.close() },
                )
                CompositionLocalProvider(_root_ide_package_.io.github.xingray.compose.nexus.controls.LocalDropdownMenuContext provides menuContext) {
                    _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle(
                        contentColor = menuTextColor,
                        textStyle = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.base,
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
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val context = _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalDropdownMenuContext.current

    if (divided) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDivider(
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
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = text,
                color = textColor,
                style = typography.base,
            )
        }
    }
}
