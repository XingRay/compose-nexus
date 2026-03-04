package io.github.xingray.compose_nexus.controls

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
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType

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
fun rememberPopconfirmState(initialVisible: Boolean = false): PopconfirmState =
    remember { PopconfirmState(initialVisible) }

@Composable
fun NexusPopconfirm(
    title: String,
    state: PopconfirmState = rememberPopconfirmState(),
    modifier: Modifier = Modifier,
    placement: PopoverPlacement = PopoverPlacement.Top,
    trigger: PopoverTrigger = PopoverTrigger.Click,
    effect: TooltipTheme = TooltipTheme.Light,
    confirmButtonText: String = "OK",
    cancelButtonText: String = "Cancel",
    confirmButtonType: NexusType = NexusType.Primary,
    cancelButtonType: NexusType = NexusType.Info,
    width: Dp = 180.dp,
    hideIcon: Boolean = false,
    iconColor: Color = NexusTheme.colorScheme.warning.base,
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

    NexusPopover(
        state = rememberPopoverState(initialVisible = state.visible),
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
                            NexusText(
                                text = "!",
                                color = iconColor,
                                style = NexusTheme.typography.base,
                            )
                        }
                    }
                    NexusText(
                        text = title,
                        style = NexusTheme.typography.small,
                        color = NexusTheme.colorScheme.text.regular,
                    )
                }
                if (actions != null) {
                    actions(confirmAction, cancelAction)
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        NexusButton(
                            text = cancelButtonText,
                            onClick = cancelAction,
                            type = cancelButtonType,
                            size = ComponentSize.Small,
                            textButton = true,
                        )
                        NexusButton(
                            text = confirmButtonText,
                            onClick = confirmAction,
                            type = confirmButtonType,
                            size = ComponentSize.Small,
                        )
                    }
                }
            }
        },
    ) {
        reference()
    }
}
