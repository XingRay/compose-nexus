package io.github.xingray.compose.nexus.controls

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
import io.github.xingray.compose.nexus.containers.DialogState
import io.github.xingray.compose.nexus.containers.NexusDialog
import io.github.xingray.compose.nexus.containers.rememberDialogState
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType

enum class MessageBoxAction {
    Confirm,
    Cancel,
    Close,
}

typealias MessageBoxBeforeClose = (action: io.github.xingray.compose.nexus.controls.MessageBoxAction, instance: io.github.xingray.compose.nexus.controls.MessageBoxRequest, done: () -> Unit) -> Unit

@Stable
class MessageBoxRequest(
    val title: String = "",
    val message: String = "",
    val type: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Info,
    val center: Boolean = false,
    val draggable: Boolean = false,
    val overflow: Boolean = false,
    val showClose: Boolean = true,
    val showCancelButton: Boolean = false,
    val showConfirmButton: Boolean = true,
    val cancelButtonText: String = "Cancel",
    val confirmButtonText: String = "OK",
    val cancelButtonType: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Info,
    val confirmButtonType: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary,
    val roundButton: Boolean = false,
    val buttonSize: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
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
    val beforeClose: io.github.xingray.compose.nexus.controls.MessageBoxBeforeClose? = null,
    val icon: (@Composable () -> Unit)? = null,
    val content: (@Composable () -> Unit)? = null,
    val callback: ((action: io.github.xingray.compose.nexus.controls.MessageBoxAction, value: String?) -> Unit)? = null,
) {
    var visible by mutableStateOf(true)
    var currentInput by mutableStateOf(inputValue)
    var currentInputError by mutableStateOf<String?>(null)
    var pendingAction by mutableStateOf<io.github.xingray.compose.nexus.controls.MessageBoxAction?>(null)
}

@Stable
class MessageBoxState {
    var request: io.github.xingray.compose.nexus.controls.MessageBoxRequest? by mutableStateOf(null)
        private set

    fun show(req: io.github.xingray.compose.nexus.controls.MessageBoxRequest) {
        request = req
    }

    fun alert(
        message: String,
        title: String = "Alert",
        type: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Info,
        callback: ((action: io.github.xingray.compose.nexus.controls.MessageBoxAction, value: String?) -> Unit)? = null,
    ) {
        show(
            _root_ide_package_.io.github.xingray.compose.nexus.controls.MessageBoxRequest(
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
        type: io.github.xingray.compose.nexus.theme.NexusType = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Warning,
        callback: ((action: io.github.xingray.compose.nexus.controls.MessageBoxAction, value: String?) -> Unit)? = null,
    ) {
        show(
            _root_ide_package_.io.github.xingray.compose.nexus.controls.MessageBoxRequest(
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
        callback: ((action: io.github.xingray.compose.nexus.controls.MessageBoxAction, value: String?) -> Unit)? = null,
    ) {
        show(
            _root_ide_package_.io.github.xingray.compose.nexus.controls.MessageBoxRequest(
                title = title,
                message = message,
                type = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Info,
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
fun rememberMessageBoxState(): io.github.xingray.compose.nexus.controls.MessageBoxState = remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.MessageBoxState() }

@Composable
fun NexusMessageBoxHost(
    state: io.github.xingray.compose.nexus.controls.MessageBoxState,
    modifier: Modifier = Modifier,
) {
    val request = state.request ?: return
    val dialogState = _root_ide_package_.io.github.xingray.compose.nexus.containers.rememberDialogState(initialVisible = request.visible)

    dialogState.visible = request.visible

    fun doneClose(action: io.github.xingray.compose.nexus.controls.MessageBoxAction) {
        request.pendingAction = action
        request.visible = false
        dialogState.visible = false
    }

    fun doAction(action: io.github.xingray.compose.nexus.controls.MessageBoxAction) {
        if (action == _root_ide_package_.io.github.xingray.compose.nexus.controls.MessageBoxAction.Confirm && request.showInput) {
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

    _root_ide_package_.io.github.xingray.compose.nexus.containers.NexusDialog(
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
            doAction(_root_ide_package_.io.github.xingray.compose.nexus.controls.MessageBoxAction.Close)
            if (!request.visible) done()
        },
        onClosed = {
            val action = request.pendingAction ?: _root_ide_package_.io.github.xingray.compose.nexus.controls.MessageBoxAction.Close
            val callbackAction = if (request.distinguishCancelAndClose) {
                action
            } else if (action == _root_ide_package_.io.github.xingray.compose.nexus.controls.MessageBoxAction.Close) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.MessageBoxAction.Cancel
            } else {
                action
            }
            request.callback?.invoke(callbackAction, request.currentInput)
            state.clear()
        },
        footer = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (request.showCancelButton) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                        text = request.cancelButtonText,
                        onClick = { doAction(_root_ide_package_.io.github.xingray.compose.nexus.controls.MessageBoxAction.Cancel) },
                        type = request.cancelButtonType,
                        size = request.buttonSize,
                        round = request.roundButton,
                        textButton = true,
                    )
                }
                if (request.showConfirmButton) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                        text = request.confirmButtonText,
                        onClick = { doAction(_root_ide_package_.io.github.xingray.compose.nexus.controls.MessageBoxAction.Confirm) },
                        type = request.confirmButtonType,
                        size = request.buttonSize,
                        round = request.roundButton,
                    )
                }
            }
        },
    ) {
        val iconColor = when (request.type) {
            _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary -> _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.primary.base
            _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Success -> _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.success.base
            _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Warning -> _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.warning.base
            _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Danger -> _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.danger.base
            _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Info, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default -> _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.info.base
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            if (request.icon != null) {
                request.icon.invoke()
            } else {
                val iconText = when (request.type) {
                    _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Success -> "✓"
                    _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Warning -> "!"
                    _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Danger -> "✕"
                    _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Info, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default -> "i"
                }
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = iconText,
                    style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.large,
                    color = iconColor,
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (request.content != null) {
                    request.content.invoke()
                } else {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = request.message,
                        color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.text.regular,
                        style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.base,
                    )
                }
                if (request.showInput) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusInput(
                        value = request.currentInput,
                        onValueChange = {
                            request.currentInput = it
                            request.currentInputError = null
                        },
                        placeholder = request.inputPlaceholder,
                    )
                    if (!request.currentInputError.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                            text = request.currentInputError.orEmpty(),
                            color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.danger.base,
                            style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.extraSmall,
                        )
                    }
                }
            }
        }
    }
}
