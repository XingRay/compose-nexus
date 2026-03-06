package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.controls.toTooltipPlacement
import io.github.xingray.compose.nexus.controls.toTooltipTrigger
import io.github.xingray.compose.nexus.theme.NexusTheme

enum class PopoverTrigger {
    Click,
    Focus,
    Hover,
    ContextMenu,
}

enum class PopoverPlacement {
    Top,
    TopStart,
    TopEnd,
    Bottom,
    BottomStart,
    BottomEnd,
    Left,
    LeftStart,
    LeftEnd,
    Right,
    RightStart,
    RightEnd,
}

@Stable
class PopoverState(initialVisible: Boolean = false) {
    var visible by mutableStateOf(initialVisible)

    fun show() {
        visible = true
    }

    fun hide() {
        visible = false
    }

    fun toggle() {
        visible = !visible
    }
}

@Composable
fun rememberPopoverState(initialVisible: Boolean = false): io.github.xingray.compose.nexus.controls.PopoverState =
    remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverState(initialVisible) }

@Composable
fun NexusPopover(
    state: io.github.xingray.compose.nexus.controls.PopoverState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberPopoverState(),
    modifier: Modifier = Modifier,
    title: String? = null,
    content: String = "",
    width: Dp = 150.dp,
    placement: io.github.xingray.compose.nexus.controls.PopoverPlacement = _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverPlacement.Bottom,
    trigger: io.github.xingray.compose.nexus.controls.PopoverTrigger = _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverTrigger.Hover,
    effect: io.github.xingray.compose.nexus.controls.TooltipTheme = _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTheme.Light,
    disabled: Boolean = false,
    visible: Boolean? = null,
    showArrow: Boolean = true,
    showAfter: Long = 0L,
    hideAfter: Long = 200L,
    autoClose: Long = 0L,
    onShow: (() -> Unit)? = null,
    onHide: (() -> Unit)? = null,
    popoverContent: (@Composable () -> Unit)? = null,
    reference: @Composable () -> Unit,
) {
    val tooltipState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberTooltipState(state.visible)
    val controlledVisible = visible ?: state.visible

    LaunchedEffect(state.visible, visible) {
        if (visible == null) {
            tooltipState.visible = state.visible
        }
    }

    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTooltip(
        text = "",
        modifier = modifier,
        placement = placement.toTooltipPlacement(),
        theme = effect,
        trigger = trigger.toTooltipTrigger(),
        disabled = disabled,
        visible = controlledVisible,
        showArrow = showArrow,
        showAfter = showAfter,
        hideAfter = hideAfter,
        autoClose = autoClose,
        state = tooltipState,
        onShow = {
            state.visible = true
            onShow?.invoke()
        },
        onHide = {
            state.visible = false
            onHide?.invoke()
        },
        popupContent = {
            Box(modifier = Modifier.width(width)) {
                if (popoverContent != null) {
                    popoverContent()
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (!title.isNullOrBlank()) {
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                text = title,
                                style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.base,
                                color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.text.primary,
                            )
                        }
                        if (content.isNotBlank()) {
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                text = content,
                                style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.small,
                                color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.text.regular,
                            )
                        }
                    }
                }
            }
        },
    ) {
        reference()
    }
}

internal fun io.github.xingray.compose.nexus.controls.PopoverPlacement.toTooltipPlacement(): io.github.xingray.compose.nexus.controls.TooltipPlacement =
    when (this) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverPlacement.Top -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.Top
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverPlacement.TopStart -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.TopStart
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverPlacement.TopEnd -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.TopEnd
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverPlacement.Bottom -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.Bottom
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverPlacement.BottomStart -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.BottomStart
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverPlacement.BottomEnd -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.BottomEnd
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverPlacement.Left -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.Left
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverPlacement.LeftStart -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.LeftStart
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverPlacement.LeftEnd -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.LeftEnd
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverPlacement.Right -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.Right
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverPlacement.RightStart -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.RightStart
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverPlacement.RightEnd -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipPlacement.RightEnd
    }

internal fun io.github.xingray.compose.nexus.controls.PopoverTrigger.toTooltipTrigger(): io.github.xingray.compose.nexus.controls.TooltipTrigger =
    when (this) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverTrigger.Click -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTrigger.Click
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverTrigger.Focus -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTrigger.Focus
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverTrigger.Hover -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTrigger.Hover
        _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverTrigger.ContextMenu -> _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTrigger.ContextMenu
    }
