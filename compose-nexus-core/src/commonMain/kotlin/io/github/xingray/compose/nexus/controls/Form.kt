package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme

enum class LabelPosition {
    Left,
    Right,
    Top,
}

enum class FormAsteriskPosition {
    Left,
    Right,
}

enum class FormValidateTrigger {
    Change,
    Blur,
    Submit,
}

enum class FormItemValidateStatus {
    None,
    Error,
    Validating,
    Success,
}

data class NexusFormRule(
    val required: Boolean = false,
    val min: Int? = null,
    val max: Int? = null,
    val message: String = "Invalid field",
    val trigger: io.github.xingray.compose.nexus.controls.FormValidateTrigger = _root_ide_package_.io.github.xingray.compose.nexus.controls.FormValidateTrigger.Submit,
    val validator: ((Any?) -> String?)? = null,
)

@Stable
class NexusFormState(
    initialModel: Map<String, Any?> = emptyMap(),
) {
    private val model = mutableStateMapOf<String, Any?>().apply { putAll(initialModel) }
    private val initialValues = mutableStateMapOf<String, Any?>().apply { putAll(initialModel) }
    private val fieldErrors = mutableStateMapOf<String, String?>()
    private val fieldStatuses = mutableStateMapOf<String, io.github.xingray.compose.nexus.controls.FormItemValidateStatus>()

    fun setFieldValue(prop: String, value: Any?) {
        model[prop] = value
    }

    fun getFieldValue(prop: String): Any? = model[prop]

    fun setFieldError(prop: String, error: String?) {
        fieldErrors[prop] = error
        fieldStatuses[prop] = if (error.isNullOrBlank()) _root_ide_package_.io.github.xingray.compose.nexus.controls.FormItemValidateStatus.Success else _root_ide_package_.io.github.xingray.compose.nexus.controls.FormItemValidateStatus.Error
    }

    fun getError(prop: String): String? = fieldErrors[prop]

    fun getStatus(prop: String): io.github.xingray.compose.nexus.controls.FormItemValidateStatus =
        fieldStatuses[prop] ?: _root_ide_package_.io.github.xingray.compose.nexus.controls.FormItemValidateStatus.None

    fun setInitialValues(values: Map<String, Any?>) {
        initialValues.clear()
        initialValues.putAll(values)
    }

    fun clearValidate(props: List<String>? = null) {
        val keys = props ?: fieldErrors.keys.toList()
        keys.forEach { key ->
            fieldErrors.remove(key)
            fieldStatuses.remove(key)
        }
    }

    fun resetFields(props: List<String>? = null) {
        val keys = props ?: initialValues.keys.toList()
        keys.forEach { key ->
            model[key] = initialValues[key]
            fieldErrors.remove(key)
            fieldStatuses.remove(key)
        }
    }

    fun validate(
        rules: Map<String, List<io.github.xingray.compose.nexus.controls.NexusFormRule>>,
        props: List<String>? = null,
        trigger: io.github.xingray.compose.nexus.controls.FormValidateTrigger = _root_ide_package_.io.github.xingray.compose.nexus.controls.FormValidateTrigger.Submit,
        onValidate: ((prop: String, isValid: Boolean, message: String?) -> Unit)? = null,
    ): Boolean {
        val keys = props ?: rules.keys.toList()
        var allValid = true
        keys.forEach { key ->
            val valid = validateField(
                prop = key,
                rules = rules[key].orEmpty(),
                trigger = trigger,
                onValidate = onValidate,
            )
            if (!valid) allValid = false
        }
        return allValid
    }

    fun validateField(
        prop: String,
        rules: List<io.github.xingray.compose.nexus.controls.NexusFormRule>,
        trigger: io.github.xingray.compose.nexus.controls.FormValidateTrigger = _root_ide_package_.io.github.xingray.compose.nexus.controls.FormValidateTrigger.Submit,
        onValidate: ((prop: String, isValid: Boolean, message: String?) -> Unit)? = null,
    ): Boolean {
        val value = model[prop]
        fieldStatuses[prop] = _root_ide_package_.io.github.xingray.compose.nexus.controls.FormItemValidateStatus.Validating
        val message = runValidation(value, rules, trigger)
        val isValid = message == null
        fieldErrors[prop] = message
        fieldStatuses[prop] = if (isValid) _root_ide_package_.io.github.xingray.compose.nexus.controls.FormItemValidateStatus.Success else _root_ide_package_.io.github.xingray.compose.nexus.controls.FormItemValidateStatus.Error
        onValidate?.invoke(prop, isValid, message)
        return isValid
    }

    private fun runValidation(
        value: Any?,
        rules: List<io.github.xingray.compose.nexus.controls.NexusFormRule>,
        trigger: io.github.xingray.compose.nexus.controls.FormValidateTrigger,
    ): String? {
        val activeRules = rules.filter { shouldRunRule(it.trigger, trigger) }
        activeRules.forEach { rule ->
            val customMessage = rule.validator?.invoke(value)
            if (customMessage != null) return customMessage

            if (rule.required && isEmptyValue(value)) {
                return rule.message
            }

            if (rule.min != null || rule.max != null) {
                val length = when (value) {
                    null -> 0
                    is String -> value.length
                    is Collection<*> -> value.size
                    else -> value.toString().length
                }
                if (rule.min != null && length < rule.min) return rule.message
                if (rule.max != null && length > rule.max) return rule.message
            }
        }
        return null
    }

    private fun shouldRunRule(ruleTrigger: io.github.xingray.compose.nexus.controls.FormValidateTrigger, currentTrigger: io.github.xingray.compose.nexus.controls.FormValidateTrigger): Boolean {
        if (currentTrigger == _root_ide_package_.io.github.xingray.compose.nexus.controls.FormValidateTrigger.Submit) return true
        return ruleTrigger == currentTrigger || ruleTrigger == _root_ide_package_.io.github.xingray.compose.nexus.controls.FormValidateTrigger.Submit
    }

    private fun isEmptyValue(value: Any?): Boolean = when (value) {
        null -> true
        is String -> value.isBlank()
        is Collection<*> -> value.isEmpty()
        else -> false
    }
}

@Composable
fun rememberNexusFormState(
    initialModel: Map<String, Any?> = emptyMap(),
): io.github.xingray.compose.nexus.controls.NexusFormState = androidx.compose.runtime.remember {
    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusFormState(initialModel = initialModel)
}

internal data class FormConfig(
    val state: io.github.xingray.compose.nexus.controls.NexusFormState,
    val rules: Map<String, List<io.github.xingray.compose.nexus.controls.NexusFormRule>>,
    val inline: Boolean,
    val labelPosition: io.github.xingray.compose.nexus.controls.LabelPosition,
    val labelWidth: Dp,
    val labelSuffix: String,
    val hideRequiredAsterisk: Boolean,
    val requireAsteriskPosition: io.github.xingray.compose.nexus.controls.FormAsteriskPosition,
    val showMessage: Boolean,
    val inlineMessage: Boolean,
    val statusIcon: Boolean,
    val size: io.github.xingray.compose.nexus.theme.ComponentSize?,
    val disabled: Boolean,
    val onValidate: ((prop: String, isValid: Boolean, message: String?) -> Unit)?,
)

internal val LocalFormConfig = compositionLocalOf {
    _root_ide_package_.io.github.xingray.compose.nexus.controls.FormConfig(
        state = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusFormState(),
        rules = emptyMap(),
        inline = false,
        labelPosition = _root_ide_package_.io.github.xingray.compose.nexus.controls.LabelPosition.Right,
        labelWidth = 80.dp,
        labelSuffix = "",
        hideRequiredAsterisk = false,
        requireAsteriskPosition = _root_ide_package_.io.github.xingray.compose.nexus.controls.FormAsteriskPosition.Left,
        showMessage = true,
        inlineMessage = false,
        statusIcon = false,
        size = null,
        disabled = false,
        onValidate = null,
    )
}

@Composable
fun NexusForm(
    modifier: Modifier = Modifier,
    state: io.github.xingray.compose.nexus.controls.NexusFormState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberNexusFormState(),
    rules: Map<String, List<io.github.xingray.compose.nexus.controls.NexusFormRule>> = emptyMap(),
    inline: Boolean = false,
    labelPosition: io.github.xingray.compose.nexus.controls.LabelPosition = _root_ide_package_.io.github.xingray.compose.nexus.controls.LabelPosition.Right,
    labelWidth: Dp = 80.dp,
    labelSuffix: String = "",
    hideRequiredAsterisk: Boolean = false,
    requireAsteriskPosition: io.github.xingray.compose.nexus.controls.FormAsteriskPosition = _root_ide_package_.io.github.xingray.compose.nexus.controls.FormAsteriskPosition.Left,
    showMessage: Boolean = true,
    inlineMessage: Boolean = false,
    statusIcon: Boolean = false,
    size: io.github.xingray.compose.nexus.theme.ComponentSize? = null,
    disabled: Boolean = false,
    onValidate: ((prop: String, isValid: Boolean, message: String?) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val config = _root_ide_package_.io.github.xingray.compose.nexus.controls.FormConfig(
        state = state,
        rules = rules,
        inline = inline,
        labelPosition = labelPosition,
        labelWidth = labelWidth,
        labelSuffix = labelSuffix,
        hideRequiredAsterisk = hideRequiredAsterisk,
        requireAsteriskPosition = requireAsteriskPosition,
        showMessage = showMessage,
        inlineMessage = inlineMessage,
        statusIcon = statusIcon,
        size = size,
        disabled = disabled,
        onValidate = onValidate,
    )

    CompositionLocalProvider(_root_ide_package_.io.github.xingray.compose.nexus.controls.LocalFormConfig provides config) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusConfigProvider(size = size) {
            val baseModifier = modifier
                .fillMaxWidth()
                .alpha(if (disabled) 0.72f else 1f)

            if (inline) {
                Row(
                    modifier = baseModifier,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    content()
                }
            } else {
                Column(
                    modifier = baseModifier,
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
fun NexusFormItem(
    label: String = "",
    modifier: Modifier = Modifier,
    prop: String? = null,
    labelPosition: io.github.xingray.compose.nexus.controls.LabelPosition? = null,
    labelWidth: Dp? = null,
    required: Boolean = false,
    rules: List<io.github.xingray.compose.nexus.controls.NexusFormRule> = emptyList(),
    error: String? = null,
    showMessage: Boolean? = null,
    inlineMessage: Boolean? = null,
    size: io.github.xingray.compose.nexus.theme.ComponentSize? = null,
    validateStatus: io.github.xingray.compose.nexus.controls.FormItemValidateStatus = _root_ide_package_.io.github.xingray.compose.nexus.controls.FormItemValidateStatus.None,
    labelContent: (@Composable () -> Unit)? = null,
    errorContent: (@Composable (String) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val config = _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalFormConfig.current

    val effectiveLabelPosition = labelPosition ?: config.labelPosition
    val effectiveLabelWidth = labelWidth ?: config.labelWidth
    val effectiveShowMessage = showMessage ?: config.showMessage
    val effectiveInlineMessage = inlineMessage ?: config.inlineMessage
    val itemRules = if (rules.isNotEmpty()) rules else prop?.let { config.rules[it] }.orEmpty()
    val shouldShowAsterisk = !config.hideRequiredAsterisk && (required || itemRules.any { it.required })
    val message = error ?: prop?.let { config.state.getError(it) }
    val status = when {
        validateStatus != _root_ide_package_.io.github.xingray.compose.nexus.controls.FormItemValidateStatus.None -> validateStatus
        prop != null -> config.state.getStatus(prop)
        !message.isNullOrBlank() -> _root_ide_package_.io.github.xingray.compose.nexus.controls.FormItemValidateStatus.Error
        else -> _root_ide_package_.io.github.xingray.compose.nexus.controls.FormItemValidateStatus.None
    }

    val defaultLabel: @Composable () -> Unit = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (shouldShowAsterisk && config.requireAsteriskPosition == _root_ide_package_.io.github.xingray.compose.nexus.controls.FormAsteriskPosition.Left) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = "* ",
                    color = colorScheme.danger.base,
                    style = typography.base,
                )
            }
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = label + config.labelSuffix,
                color = colorScheme.text.regular,
                style = typography.base,
            )
            if (shouldShowAsterisk && config.requireAsteriskPosition == _root_ide_package_.io.github.xingray.compose.nexus.controls.FormAsteriskPosition.Right) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = " *",
                    color = colorScheme.danger.base,
                    style = typography.base,
                )
            }
        }
    }

    val itemContent: @Composable () -> Unit = {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusConfigProvider(size = size) {
                content()
            }
            if (config.statusIcon) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.ValidationStatusIcon(status = status)
            }
            if (effectiveShowMessage && effectiveInlineMessage && !message.isNullOrBlank()) {
                if (errorContent != null) {
                    errorContent(message)
                } else {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = message,
                        color = colorScheme.danger.base,
                        style = typography.extraSmall,
                    )
                }
            }
        }
    }

    val baseModifier = if (config.inline) modifier else modifier.fillMaxWidth()
    when (effectiveLabelPosition) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.LabelPosition.Top -> {
            Column(modifier = baseModifier) {
                Box(modifier = Modifier.padding(bottom = 6.dp)) {
                    (labelContent ?: defaultLabel).invoke()
                }
                itemContent()
                if (effectiveShowMessage && !effectiveInlineMessage && !message.isNullOrBlank()) {
                    Box(modifier = Modifier.padding(top = 4.dp)) {
                        if (errorContent != null) {
                            errorContent(message)
                        } else {
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                text = message,
                                color = colorScheme.danger.base,
                                style = typography.extraSmall,
                            )
                        }
                    }
                }
            }
        }
        _root_ide_package_.io.github.xingray.compose.nexus.controls.LabelPosition.Left, _root_ide_package_.io.github.xingray.compose.nexus.controls.LabelPosition.Right -> {
            Row(
                modifier = baseModifier,
                verticalAlignment = Alignment.Top,
            ) {
                Box(
                    modifier = Modifier
                        .width(effectiveLabelWidth)
                        .padding(top = 8.dp, end = 12.dp),
                    contentAlignment = when (effectiveLabelPosition) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.LabelPosition.Right -> Alignment.CenterEnd
                        else -> Alignment.CenterStart
                    },
                ) {
                    (labelContent ?: defaultLabel).invoke()
                }

                Column(modifier = if (config.inline) Modifier else Modifier.weight(1f)) {
                    itemContent()
                    if (effectiveShowMessage && !effectiveInlineMessage && !message.isNullOrBlank()) {
                        Box(modifier = Modifier.padding(top = 4.dp)) {
                            if (errorContent != null) {
                                errorContent(message)
                            } else {
                                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                    text = message,
                                    color = colorScheme.danger.base,
                                    style = typography.extraSmall,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (prop != null && itemRules.isNotEmpty() && !config.disabled) {
        // keep field registration hot so status/error can be queried and shown in item without extra setup.
        config.state.getFieldValue(prop)
    }
}

@Composable
private fun ValidationStatusIcon(
    status: io.github.xingray.compose.nexus.controls.FormItemValidateStatus,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val (text, color) = when (status) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.FormItemValidateStatus.Error -> "✕" to colorScheme.danger.base
        _root_ide_package_.io.github.xingray.compose.nexus.controls.FormItemValidateStatus.Validating -> "…" to colorScheme.primary.base
        _root_ide_package_.io.github.xingray.compose.nexus.controls.FormItemValidateStatus.Success -> "✓" to colorScheme.success.base
        _root_ide_package_.io.github.xingray.compose.nexus.controls.FormItemValidateStatus.None -> return
    }

    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
        text = text,
        color = color,
        style = typography.small,
    )
}
