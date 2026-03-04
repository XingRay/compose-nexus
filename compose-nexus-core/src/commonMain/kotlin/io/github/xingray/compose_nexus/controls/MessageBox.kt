package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.containers.DialogState
import io.github.xingray.compose_nexus.containers.NexusDialog
import io.github.xingray.compose_nexus.containers.rememberDialogState
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType

enum class MessageBoxAction {
    Confirm,
    Cancel,
    Close,
}

typealias MessageBoxBeforeClose = (action: MessageBoxAction, instance: MessageBoxRequest, done: () -> Unit) -> Unit

@Stable
class MessageBoxRequest(
    val title: String = "",
    val message: String = "",
    val type: NexusType = NexusType.Info,
    val center: Boolean = false,
    val draggable: Boolean = false,
    val overflow: Boolean = false,
    val showClose: Boolean = true,
    val showCancelButton: Boolean = false,
    val showConfirmButton: Boolean = true,
    val cancelButtonText: String = "Cancel",
    val confirmButtonText: String = "OK",
    val cancelButtonType: NexusType = NexusType.Info,
    val confirmButtonType: NexusType = NexusType.Primary,
    val roundButton: Boolean = false,
    val buttonSize: ComponentSize = ComponentSize.Default,
    val closeOnClickModal: Boolean = true,
    val closeOnPressEscape: Boolean = true,
    val distinguishCancelAndClose: Boolean = false,
    val showInput: Boolean = false,
    val inputPlaceholder: String = "",
    val inputValue: String = "",
    val inputPattern: Regex? = null,
    val inputValidator: ((String) -> String?)? = null,
    val inputErrorMessage: String = "Illegal input",
    val dangerouslyUseHTMLString: Boolean = false,
    val beforeClose: MessageBoxBeforeClose? = null,
    val icon: (@Composable () -> Unit)? = null,
    val content: (@Composable () -> Unit)? = null,
    val callback: ((action: MessageBoxAction, value: String?) -> Unit)? = null,
) {
    var visible by mutableStateOf(true)
    var currentInput by mutableStateOf(inputValue)
    var currentInputError by mutableStateOf<String?>(null)
    var pendingAction by mutableStateOf<MessageBoxAction?>(null)
}

@Stable
class MessageBoxState {
    var request: MessageBoxRequest? by mutableStateOf(null)
        private set

    fun show(req: MessageBoxRequest) {
        request = req
    }

    fun alert(
        message: String,
        title: String = "Alert",
        type: NexusType = NexusType.Info,
        callback: ((action: MessageBoxAction, value: String?) -> Unit)? = null,
    ) {
        show(
            MessageBoxRequest(
                title = title,
                message = message,
                type = type,
                showCancelButton = false,
                closeOnClickModal = false,
                closeOnPressEscape = false,
                callback = callback,
            ),
        )
    }

    fun confirm(
        message: String,
        title: String = "Confirm",
        type: NexusType = NexusType.Warning,
        callback: ((action: MessageBoxAction, value: String?) -> Unit)? = null,
    ) {
        show(
            MessageBoxRequest(
                title = title,
                message = message,
                type = type,
                showCancelButton = true,
                callback = callback,
            ),
        )
    }

    fun prompt(
        message: String,
        title: String = "Prompt",
        inputPlaceholder: String = "",
        inputValue: String = "",
        inputPattern: Regex? = null,
        inputValidator: ((String) -> String?)? = null,
        callback: ((action: MessageBoxAction, value: String?) -> Unit)? = null,
    ) {
        show(
            MessageBoxRequest(
                title = title,
                message = message,
                type = NexusType.Info,
                showCancelButton = true,
                showInput = true,
                inputPlaceholder = inputPlaceholder,
                inputValue = inputValue,
                inputPattern = inputPattern,
                inputValidator = inputValidator,
                callback = callback,
            ),
        )
    }

    fun clear() {
        request = null
    }
}

@Composable
fun rememberMessageBoxState(): MessageBoxState = remember { MessageBoxState() }

@Composable
fun NexusMessageBoxHost(
    state: MessageBoxState,
    modifier: Modifier = Modifier,
) {
    val request = state.request ?: return
    val dialogState = rememberDialogState(initialVisible = request.visible)

    dialogState.visible = request.visible

    fun doneClose(action: MessageBoxAction) {
        request.pendingAction = action
        request.visible = false
        dialogState.visible = false
    }

    fun doAction(action: MessageBoxAction) {
        if (action == MessageBoxAction.Confirm && request.showInput) {
            val value = request.currentInput
            val patternError = if (request.inputPattern != null && !request.inputPattern.matches(value)) {
                request.inputErrorMessage
            } else {
                null
            }
            val validatorError = request.inputValidator?.invoke(value)
            request.currentInputError = patternError ?: validatorError
            if (request.currentInputError != null) return
        }

        if (request.beforeClose != null) {
            request.beforeClose.invoke(action, request) {
                doneClose(action)
            }
        } else {
            doneClose(action)
        }
    }

    NexusDialog(
        state = dialogState,
        modifier = modifier,
        title = request.title,
        showClose = request.showClose,
        closeOnClickModal = request.closeOnClickModal,
        closeOnPressEscape = request.closeOnPressEscape,
        center = request.center,
        draggable = request.draggable,
        overflow = request.overflow,
        beforeClose = { done ->
            doAction(MessageBoxAction.Close)
            if (!request.visible) done()
        },
        onClosed = {
            val action = request.pendingAction ?: MessageBoxAction.Close
            val callbackAction = if (request.distinguishCancelAndClose) {
                action
            } else if (action == MessageBoxAction.Close) {
                MessageBoxAction.Cancel
            } else {
                action
            }
            request.callback?.invoke(callbackAction, request.currentInput)
            state.clear()
        },
        footer = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (request.showCancelButton) {
                    NexusButton(
                        text = request.cancelButtonText,
                        onClick = { doAction(MessageBoxAction.Cancel) },
                        type = request.cancelButtonType,
                        size = request.buttonSize,
                        round = request.roundButton,
                        textButton = true,
                    )
                }
                if (request.showConfirmButton) {
                    NexusButton(
                        text = request.confirmButtonText,
                        onClick = { doAction(MessageBoxAction.Confirm) },
                        type = request.confirmButtonType,
                        size = request.buttonSize,
                        round = request.roundButton,
                    )
                }
            }
        },
    ) {
        val iconColor = when (request.type) {
            NexusType.Primary -> NexusTheme.colorScheme.primary.base
            NexusType.Success -> NexusTheme.colorScheme.success.base
            NexusType.Warning -> NexusTheme.colorScheme.warning.base
            NexusType.Danger -> NexusTheme.colorScheme.danger.base
            NexusType.Info, NexusType.Default -> NexusTheme.colorScheme.info.base
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            if (request.icon != null) {
                request.icon.invoke()
            } else {
                val iconText = when (request.type) {
                    NexusType.Success -> "✓"
                    NexusType.Warning -> "!"
                    NexusType.Danger -> "✕"
                    NexusType.Info, NexusType.Primary, NexusType.Default -> "i"
                }
                NexusText(
                    text = iconText,
                    style = NexusTheme.typography.large,
                    color = iconColor,
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (request.content != null) {
                    request.content.invoke()
                } else {
                    NexusText(
                        text = request.message,
                        color = NexusTheme.colorScheme.text.regular,
                        style = NexusTheme.typography.base,
                    )
                }
                if (request.showInput) {
                    NexusInput(
                        value = request.currentInput,
                        onValueChange = {
                            request.currentInput = it
                            request.currentInputError = null
                        },
                        placeholder = request.inputPlaceholder,
                    )
                    if (!request.currentInputError.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        NexusText(
                            text = request.currentInputError.orEmpty(),
                            color = NexusTheme.colorScheme.danger.base,
                            style = NexusTheme.typography.extraSmall,
                        )
                    }
                }
            }
        }
    }
}
