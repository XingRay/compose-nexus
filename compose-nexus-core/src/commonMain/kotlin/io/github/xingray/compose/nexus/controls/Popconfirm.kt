package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType

@Stable
class PopconfirmState(initialVisible: Boolean = false) {
    var visible by mutableStateOf(initialVisible)

    fun show() {
        visible = true
    }

    fun hide() {
        visible = false
    }
}

@Composable
fun rememberPopconfirmState(initialVisible: Boolean = false): io.github.xingray.compose.nexus.controls.PopconfirmState =
    remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.PopconfirmState(initialVisible) }

@Composable
fun NexusPopconfirm(
    title: String,
    state: io.github.xingray.compose.nexus.controls.PopconfirmState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberPopconfirmState(),
    modifier: Modifier = Modifier,
    placement: io.github.xingray.compose.nexus.controls.PopoverPlacement = _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverPlacement.Top,
    trigger: io.github.xingray.compose.nexus.controls.PopoverTrigger = _root_ide_package_.io.github.xingray.compose.nexus.controls.PopoverTrigger.Click,
    effect: io.github.xingray.compose.nexus.controls.TooltipTheme = _root_ide_package_.io.github.xingray.compose.nexus.controls.TooltipTheme.Light,
    confirmButtonText: String = "OK",
    cancelButtonText: String = "Cancel",
    confirmButtonType: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary,
    cancelButtonType: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Info,
    width: Dp = 180.dp,
    hideIcon: Boolean = false,
    iconColor: Color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.warning.base,
    icon: (@Composable () -> Unit)? = null,
    actions: (@Composable (confirm: () -> Unit, cancel: () -> Unit) -> Unit)? = null,
    onConfirm: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    reference: @Composable () -> Unit,
) {
    val confirmAction = {
        state.hide()
        onConfirm?.invoke()
        Unit
    }
    val cancelAction = {
        state.hide()
        onCancel?.invoke()
        Unit
    }

    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusPopover(
        state = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberPopoverState(initialVisible = state.visible),
        modifier = modifier,
        width = width,
        placement = placement,
        trigger = trigger,
        effect = effect,
        visible = state.visible,
        popoverContent = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (!hideIcon) {
                        if (icon != null) {
                            icon()
                        } else {
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                text = "!",
                                color = iconColor,
                                style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.base,
                            )
                        }
                    }
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = title,
                        style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.small,
                        color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.text.regular,
                    )
                }
                if (actions != null) {
                    actions(confirmAction, cancelAction)
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                            text = cancelButtonText,
                            onClick = cancelAction,
                            type = cancelButtonType,
                            size = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small,
                            textButton = true,
                        )
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                            text = confirmButtonText,
                            onClick = confirmAction,
                            type = confirmButtonType,
                            size = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small,
                        )
                    }
                }
            }
        },
    ) {
        reference()
    }
}
