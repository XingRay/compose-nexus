package io.github.xingray.compose_nexus.controls

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
import io.github.xingray.compose_nexus.theme.NexusTheme

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
fun rememberPopoverState(initialVisible: Boolean = false): PopoverState =
    remember { PopoverState(initialVisible) }

@Composable
fun NexusPopover(
    state: PopoverState = rememberPopoverState(),
    modifier: Modifier = Modifier,
    title: String? = null,
    content: String = "",
    width: Dp = 150.dp,
    placement: PopoverPlacement = PopoverPlacement.Bottom,
    trigger: PopoverTrigger = PopoverTrigger.Hover,
    effect: TooltipTheme = TooltipTheme.Light,
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
    val tooltipState = rememberTooltipState(state.visible)
    val controlledVisible = visible ?: state.visible

    LaunchedEffect(state.visible, visible) {
        if (visible == null) {
            tooltipState.visible = state.visible
        }
    }

    NexusTooltip(
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
                            NexusText(
                                text = title,
                                style = NexusTheme.typography.base,
                                color = NexusTheme.colorScheme.text.primary,
                            )
                        }
                        if (content.isNotBlank()) {
                            NexusText(
                                text = content,
                                style = NexusTheme.typography.small,
                                color = NexusTheme.colorScheme.text.regular,
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

internal fun PopoverPlacement.toTooltipPlacement(): TooltipPlacement =
    when (this) {
        PopoverPlacement.Top -> TooltipPlacement.Top
        PopoverPlacement.TopStart -> TooltipPlacement.TopStart
        PopoverPlacement.TopEnd -> TooltipPlacement.TopEnd
        PopoverPlacement.Bottom -> TooltipPlacement.Bottom
        PopoverPlacement.BottomStart -> TooltipPlacement.BottomStart
        PopoverPlacement.BottomEnd -> TooltipPlacement.BottomEnd
        PopoverPlacement.Left -> TooltipPlacement.Left
        PopoverPlacement.LeftStart -> TooltipPlacement.LeftStart
        PopoverPlacement.LeftEnd -> TooltipPlacement.LeftEnd
        PopoverPlacement.Right -> TooltipPlacement.Right
        PopoverPlacement.RightStart -> TooltipPlacement.RightStart
        PopoverPlacement.RightEnd -> TooltipPlacement.RightEnd
    }

internal fun PopoverTrigger.toTooltipTrigger(): TooltipTrigger =
    when (this) {
        PopoverTrigger.Click -> TooltipTrigger.Click
        PopoverTrigger.Focus -> TooltipTrigger.Focus
        PopoverTrigger.Hover -> TooltipTrigger.Hover
        PopoverTrigger.ContextMenu -> TooltipTrigger.ContextMenu
    }
