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
    val trigger: FormValidateTrigger = FormValidateTrigger.Submit,
    val validator: ((Any?) -> String?)? = null,
)

@Stable
class NexusFormState(
    initialModel: Map<String, Any?> = emptyMap(),
) {
    private val model = mutableStateMapOf<String, Any?>().apply { putAll(initialModel) }
    private val initialValues = mutableStateMapOf<String, Any?>().apply { putAll(initialModel) }
    private val fieldErrors = mutableStateMapOf<String, String?>()
    private val fieldStatuses = mutableStateMapOf<String, FormItemValidateStatus>()

    fun setFieldValue(prop: String, value: Any?) {
        model[prop] = value
    }

    fun getFieldValue(prop: String): Any? = model[prop]

    fun setFieldError(prop: String, error: String?) {
        fieldErrors[prop] = error
        fieldStatuses[prop] = if (error.isNullOrBlank()) FormItemValidateStatus.Success else FormItemValidateStatus.Error
    }

    fun getError(prop: String): String? = fieldErrors[prop]

    fun getStatus(prop: String): FormItemValidateStatus =
        fieldStatuses[prop] ?: FormItemValidateStatus.None

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
        rules: Map<String, List<NexusFormRule>>,
        props: List<String>? = null,
        trigger: FormValidateTrigger = FormValidateTrigger.Submit,
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
        rules: List<NexusFormRule>,
        trigger: FormValidateTrigger = FormValidateTrigger.Submit,
        onValidate: ((prop: String, isValid: Boolean, message: String?) -> Unit)? = null,
    ): Boolean {
        val value = model[prop]
        fieldStatuses[prop] = FormItemValidateStatus.Validating
        val message = runValidation(value, rules, trigger)
        val isValid = message == null
        fieldErrors[prop] = message
        fieldStatuses[prop] = if (isValid) FormItemValidateStatus.Success else FormItemValidateStatus.Error
        onValidate?.invoke(prop, isValid, message)
        return isValid
    }

    private fun runValidation(
        value: Any?,
        rules: List<NexusFormRule>,
        trigger: FormValidateTrigger,
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

    private fun shouldRunRule(ruleTrigger: FormValidateTrigger, currentTrigger: FormValidateTrigger): Boolean {
        if (currentTrigger == FormValidateTrigger.Submit) return true
        return ruleTrigger == currentTrigger || ruleTrigger == FormValidateTrigger.Submit
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
): NexusFormState = androidx.compose.runtime.remember {
    NexusFormState(initialModel = initialModel)
}

internal data class FormConfig(
    val state: NexusFormState,
    val rules: Map<String, List<NexusFormRule>>,
    val inline: Boolean,
    val labelPosition: LabelPosition,
    val labelWidth: Dp,
    val labelSuffix: String,
    val hideRequiredAsterisk: Boolean,
    val requireAsteriskPosition: FormAsteriskPosition,
    val showMessage: Boolean,
    val inlineMessage: Boolean,
    val statusIcon: Boolean,
    val size: ComponentSize?,
    val disabled: Boolean,
    val onValidate: ((prop: String, isValid: Boolean, message: String?) -> Unit)?,
)

internal val LocalFormConfig = compositionLocalOf {
    FormConfig(
        state = NexusFormState(),
        rules = emptyMap(),
        inline = false,
        labelPosition = LabelPosition.Right,
        labelWidth = 80.dp,
        labelSuffix = "",
        hideRequiredAsterisk = false,
        requireAsteriskPosition = FormAsteriskPosition.Left,
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
    state: NexusFormState = rememberNexusFormState(),
    rules: Map<String, List<NexusFormRule>> = emptyMap(),
    inline: Boolean = false,
    labelPosition: LabelPosition = LabelPosition.Right,
    labelWidth: Dp = 80.dp,
    labelSuffix: String = "",
    hideRequiredAsterisk: Boolean = false,
    requireAsteriskPosition: FormAsteriskPosition = FormAsteriskPosition.Left,
    showMessage: Boolean = true,
    inlineMessage: Boolean = false,
    statusIcon: Boolean = false,
    size: ComponentSize? = null,
    disabled: Boolean = false,
    onValidate: ((prop: String, isValid: Boolean, message: String?) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val config = FormConfig(
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

    CompositionLocalProvider(LocalFormConfig provides config) {
        NexusConfigProvider(size = size) {
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
    labelPosition: LabelPosition? = null,
    labelWidth: Dp? = null,
    required: Boolean = false,
    rules: List<NexusFormRule> = emptyList(),
    error: String? = null,
    showMessage: Boolean? = null,
    inlineMessage: Boolean? = null,
    size: ComponentSize? = null,
    validateStatus: FormItemValidateStatus = FormItemValidateStatus.None,
    labelContent: (@Composable () -> Unit)? = null,
    errorContent: (@Composable (String) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val config = LocalFormConfig.current

    val effectiveLabelPosition = labelPosition ?: config.labelPosition
    val effectiveLabelWidth = labelWidth ?: config.labelWidth
    val effectiveShowMessage = showMessage ?: config.showMessage
    val effectiveInlineMessage = inlineMessage ?: config.inlineMessage
    val itemRules = if (rules.isNotEmpty()) rules else prop?.let { config.rules[it] }.orEmpty()
    val shouldShowAsterisk = !config.hideRequiredAsterisk && (required || itemRules.any { it.required })
    val message = error ?: prop?.let { config.state.getError(it) }
    val status = when {
        validateStatus != FormItemValidateStatus.None -> validateStatus
        prop != null -> config.state.getStatus(prop)
        !message.isNullOrBlank() -> FormItemValidateStatus.Error
        else -> FormItemValidateStatus.None
    }

    val defaultLabel: @Composable () -> Unit = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (shouldShowAsterisk && config.requireAsteriskPosition == FormAsteriskPosition.Left) {
                NexusText(
                    text = "* ",
                    color = colorScheme.danger.base,
                    style = typography.base,
                )
            }
            NexusText(
                text = label + config.labelSuffix,
                color = colorScheme.text.regular,
                style = typography.base,
            )
            if (shouldShowAsterisk && config.requireAsteriskPosition == FormAsteriskPosition.Right) {
                NexusText(
                    text = " *",
                    color = colorScheme.danger.base,
                    style = typography.base,
                )
            }
        }
    }

    val itemContent: @Composable () -> Unit = {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            NexusConfigProvider(size = size) {
                content()
            }
            if (config.statusIcon) {
                ValidationStatusIcon(status = status)
            }
            if (effectiveShowMessage && effectiveInlineMessage && !message.isNullOrBlank()) {
                if (errorContent != null) {
                    errorContent(message)
                } else {
                    NexusText(
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
        LabelPosition.Top -> {
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
                            NexusText(
                                text = message,
                                color = colorScheme.danger.base,
                                style = typography.extraSmall,
                            )
                        }
                    }
                }
            }
        }
        LabelPosition.Left, LabelPosition.Right -> {
            Row(
                modifier = baseModifier,
                verticalAlignment = Alignment.Top,
            ) {
                Box(
                    modifier = Modifier
                        .width(effectiveLabelWidth)
                        .padding(top = 8.dp, end = 12.dp),
                    contentAlignment = when (effectiveLabelPosition) {
                        LabelPosition.Right -> Alignment.CenterEnd
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
                                NexusText(
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
    status: FormItemValidateStatus,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val (text, color) = when (status) {
        FormItemValidateStatus.Error -> "✕" to colorScheme.danger.base
        FormItemValidateStatus.Validating -> "…" to colorScheme.primary.base
        FormItemValidateStatus.Success -> "✓" to colorScheme.success.base
        FormItemValidateStatus.None -> return
    }

    NexusText(
        text = text,
        color = color,
        style = typography.small,
    )
}
