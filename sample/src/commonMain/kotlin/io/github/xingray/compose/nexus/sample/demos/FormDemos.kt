package io.github.xingray.compose.nexus.sample.demos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.controls.CascaderOption
import io.github.xingray.compose.nexus.controls.CheckboxGroupType
import io.github.xingray.compose.nexus.controls.CheckboxOption
import io.github.xingray.compose.nexus.controls.FormValidateTrigger
import io.github.xingray.compose.nexus.controls.LabelPosition
import io.github.xingray.compose.nexus.controls.MentionOption
import io.github.xingray.compose.nexus.controls.MentionOptionProps
import io.github.xingray.compose.nexus.controls.NexusAutocomplete
import io.github.xingray.compose.nexus.controls.NexusButton
import io.github.xingray.compose.nexus.controls.NexusCascader
import io.github.xingray.compose.nexus.controls.NexusCheckbox
import io.github.xingray.compose.nexus.controls.NexusCheckboxButton
import io.github.xingray.compose.nexus.controls.NexusCheckboxGroup
import io.github.xingray.compose.nexus.controls.NexusColorFormat
import io.github.xingray.compose.nexus.controls.NexusColorPicker
import io.github.xingray.compose.nexus.controls.NexusColorPickerPanel
import io.github.xingray.compose.nexus.controls.NexusDate
import io.github.xingray.compose.nexus.controls.NexusDatePicker
import io.github.xingray.compose.nexus.controls.NexusDatePickerPanel
import io.github.xingray.compose.nexus.controls.NexusDatePickerPanelType
import io.github.xingray.compose.nexus.controls.NexusDateTime
import io.github.xingray.compose.nexus.controls.NexusDateTimePicker
import io.github.xingray.compose.nexus.controls.NexusForm
import io.github.xingray.compose.nexus.controls.NexusFormItem
import io.github.xingray.compose.nexus.controls.NexusFormRule
import io.github.xingray.compose.nexus.controls.NexusInput
import io.github.xingray.compose.nexus.controls.NexusInputNumber
import io.github.xingray.compose.nexus.controls.NexusInputNumberAlign
import io.github.xingray.compose.nexus.controls.NexusInputNumberControlsPosition
import io.github.xingray.compose.nexus.controls.NexusInputTag
import io.github.xingray.compose.nexus.controls.NexusInputTagTrigger
import io.github.xingray.compose.nexus.controls.NexusInputType
import io.github.xingray.compose.nexus.controls.NexusMention
import io.github.xingray.compose.nexus.controls.NexusRadio
import io.github.xingray.compose.nexus.controls.NexusRadioGroup
import io.github.xingray.compose.nexus.controls.NexusRadioGroupType
import io.github.xingray.compose.nexus.controls.NexusRadioOption
import io.github.xingray.compose.nexus.controls.NexusRadioOptionProps
import io.github.xingray.compose.nexus.controls.NexusRangeSlider
import io.github.xingray.compose.nexus.controls.NexusRate
import io.github.xingray.compose.nexus.controls.NexusSelect
import io.github.xingray.compose.nexus.controls.NexusSelectV2
import io.github.xingray.compose.nexus.controls.NexusSlider
import io.github.xingray.compose.nexus.controls.NexusSliderPlacement
import io.github.xingray.compose.nexus.controls.NexusSwitch
import io.github.xingray.compose.nexus.controls.NexusTag
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.controls.NexusTextarea
import io.github.xingray.compose.nexus.controls.NexusTextareaAutosize
import io.github.xingray.compose.nexus.controls.NexusTime
import io.github.xingray.compose.nexus.controls.NexusTimePicker
import io.github.xingray.compose.nexus.controls.NexusTimeSelect
import io.github.xingray.compose.nexus.controls.NexusTransfer
import io.github.xingray.compose.nexus.controls.NexusTreeSelect
import io.github.xingray.compose.nexus.controls.NexusUpload
import io.github.xingray.compose.nexus.controls.NexusUploadListType
import io.github.xingray.compose.nexus.controls.NexusUploadRawFile
import io.github.xingray.compose.nexus.controls.NexusUploadStatus
import io.github.xingray.compose.nexus.controls.NexusWordLimitPosition
import io.github.xingray.compose.nexus.controls.SelectOption
import io.github.xingray.compose.nexus.controls.SelectOptionProps
import io.github.xingray.compose.nexus.controls.TagEffect
import io.github.xingray.compose.nexus.controls.TransferFormat
import io.github.xingray.compose.nexus.controls.TransferItem
import io.github.xingray.compose.nexus.controls.TransferPropsAlias
import io.github.xingray.compose.nexus.controls.TransferTargetOrder
import io.github.xingray.compose.nexus.controls.TreeNode
import io.github.xingray.compose.nexus.controls.rememberAutocompleteState
import io.github.xingray.compose.nexus.controls.rememberCascaderState
import io.github.xingray.compose.nexus.controls.rememberCheckboxGroupState
import io.github.xingray.compose.nexus.controls.rememberDatePickerState
import io.github.xingray.compose.nexus.controls.rememberDateTimePickerState
import io.github.xingray.compose.nexus.controls.rememberInputTagState
import io.github.xingray.compose.nexus.controls.rememberMentionState
import io.github.xingray.compose.nexus.controls.rememberNexusFormState
import io.github.xingray.compose.nexus.controls.rememberRadioGroupState
import io.github.xingray.compose.nexus.controls.rememberSelectState
import io.github.xingray.compose.nexus.controls.rememberSelectV2State
import io.github.xingray.compose.nexus.controls.rememberTimePickerState
import io.github.xingray.compose.nexus.controls.rememberTimeSelectState
import io.github.xingray.compose.nexus.controls.rememberTransferState
import io.github.xingray.compose.nexus.controls.rememberTreeSelectState
import io.github.xingray.compose.nexus.controls.rememberUploadState
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun InputDemo() {
    DemoPage(title = "Input", description = "Input data using keyboard.") {
        DemoSection("Basic usage") {
            var v1 by remember { mutableStateOf("") }
            NexusInput(value = v1, onValueChange = { v1 = it }, placeholder = "Please input")
        }
        DemoSection("Disabled") {
            var v by remember { mutableStateOf("disabled input") }
            NexusInput(value = v, onValueChange = { v = it }, disabled = true)
        }
        DemoSection("Clearable") {
            var v by remember { mutableStateOf("clearable text") }
            NexusInput(value = v, onValueChange = { v = it }, clearable = true, placeholder = "Clearable")
        }
        DemoSection("Custom clear icon") {
            var v by remember { mutableStateOf("custom icon") }
            NexusInput(
                value = v,
                onValueChange = { v = it },
                clearable = true,
                clearIcon = {
                    NexusText(
                        text = "⊗",
                        color = NexusTheme.colorScheme.danger.base
                    )
                },
                placeholder = "Clear icon",
            )
        }
        DemoSection("Formatter") {
            var money by remember { mutableStateOf("12345.67") }
            NexusInput(
                value = money,
                onValueChange = { money = it },
                formatter = { raw ->
                    val clean = raw.filter { it.isDigit() || it == '.' }
                    if (clean.isEmpty()) {
                        ""
                    } else {
                        val parts = clean.split(".")
                        val intPart = parts[0].reversed().chunked(3).joinToString(",").reversed()
                        val decimal = parts.getOrNull(1)?.take(2)
                        if (decimal.isNullOrEmpty()) "$$intPart" else "$$intPart.$decimal"
                    }
                },
                parser = { text ->
                    text.replace("$", "").replace(",", "")
                },
                placeholder = "Currency",
            )
        }
        DemoSection("Password box") {
            var v by remember { mutableStateOf("") }
            NexusInput(
                value = v,
                onValueChange = { v = it },
                type = NexusInputType.Password,
                showPassword = true,
                placeholder = "Please input password",
            )
        }
        DemoSection("Input with icon") {
            var v1 by remember { mutableStateOf("") }
            var v2 by remember { mutableStateOf("") }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusInput(
                    value = v1,
                    onValueChange = { v1 = it },
                    prefix = { NexusText(text = "🔍") },
                    placeholder = "Prefix icon",
                )
                NexusInput(
                    value = v2,
                    onValueChange = { v2 = it },
                    suffix = {
                        NexusText(
                            text = "@example.com",
                            style = NexusTheme.typography.extraSmall
                        )
                    },
                    placeholder = "Suffix slot",
                )
            }
        }
        DemoSection("Textarea") {
            var txt by remember { mutableStateOf("") }
            NexusInput(
                value = txt,
                onValueChange = { txt = it },
                type = NexusInputType.Textarea,
                rows = 4,
                placeholder = "Please input",
            )
        }
        DemoSection("Autosize Textarea") {
            var txt by remember { mutableStateOf("") }
            NexusInput(
                value = txt,
                onValueChange = { txt = it },
                type = NexusInputType.Textarea,
                autosize = true,
                autosizeOptions = NexusTextareaAutosize(minRows = 2, maxRows = 6),
                placeholder = "Type multiple lines...",
            )
        }
        DemoSection("Mixed input") {
            var url by remember { mutableStateOf("") }
            NexusInput(
                value = url,
                onValueChange = { url = it },
                prepend = {
                    NexusText(
                        text = "https://",
                        style = NexusTheme.typography.small,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                },
                append = {
                    NexusButton(
                        text = "Go",
                        size = ComponentSize.Small,
                        onClick = {},
                        type = NexusType.Primary,
                    )
                },
                placeholder = "your-site",
            )
        }
        DemoSection("Sizes") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                var v by remember { mutableStateOf("") }
                NexusInput(
                    value = v,
                    onValueChange = { v = it },
                    size = ComponentSize.Large,
                    placeholder = "Large"
                )
                NexusInput(
                    value = v,
                    onValueChange = { v = it },
                    size = ComponentSize.Default,
                    placeholder = "Default"
                )
                NexusInput(
                    value = v,
                    onValueChange = { v = it },
                    size = ComponentSize.Small,
                    placeholder = "Small"
                )
            }
        }
        DemoSection("Limit length") {
            var text by remember { mutableStateOf("") }
            var textarea by remember { mutableStateOf("") }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NexusInput(
                    value = text,
                    onValueChange = { text = it },
                    maxLength = 20,
                    minLength = 5,
                    showWordLimit = true,
                    wordLimitPosition = NexusWordLimitPosition.Outside,
                    placeholder = "5-20 chars",
                )
                NexusInput(
                    value = textarea,
                    onValueChange = { textarea = it },
                    type = NexusInputType.Textarea,
                    maxLength = 120,
                    showWordLimit = true,
                    rows = 3,
                    placeholder = "Textarea with word limit",
                )
            }
        }
        DemoSection("Events") {
            var value by remember { mutableStateOf("") }
            var logs by remember { mutableStateOf(listOf("Logs:")) }
            fun push(msg: String) {
                logs = (listOf(msg) + logs).take(6)
            }
            NexusInput(
                value = value,
                onValueChange = { value = it },
                clearable = true,
                placeholder = "Observe input/change/focus/blur",
                onInput = { push("input: $it") },
                onChange = { push("change: $it") },
                onFocus = { push("focus") },
                onBlur = { push("blur") },
                onClear = { push("clear") },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                logs.forEach { line ->
                    NexusText(
                        text = line,
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                }
            }
        }
    }
}

@Composable
fun InputNumberDemo() {
    DemoPage(title = "InputNumber", description = "Input numerical values with a customizable range.") {
        DemoSection("Basic usage") {
            var v by remember { mutableDoubleStateOf(1.0) }
            NexusInputNumber(value = v, onValueChange = { v = it }, min = 1.0, max = 10.0)
        }
        DemoSection("Disabled") {
            var v by remember { mutableDoubleStateOf(2.0) }
            NexusInputNumber(value = v, onValueChange = { v = it }, disabled = true)
        }
        DemoSection("Steps") {
            var v by remember { mutableDoubleStateOf(0.0) }
            NexusInputNumber(value = v, onValueChange = { v = it }, step = 2.0, min = -10.0, max = 10.0)
        }
        DemoSection("Step strictly") {
            var v by remember { mutableDoubleStateOf(3.0) }
            NexusInputNumber(
                value = v,
                onValueChange = { v = it },
                step = 2.0,
                stepStrictly = true,
                min = 0.0,
                max = 20.0,
            )
        }
        DemoSection("Precision") {
            var v by remember { mutableDoubleStateOf(5.2) }
            NexusInputNumber(
                value = v,
                onValueChange = { v = it },
                step = 0.1,
                precision = 2,
                min = 0.0,
                max = 20.0,
            )
        }
        DemoSection("Size") {
            var v1 by remember { mutableDoubleStateOf(1.0) }
            var v2 by remember { mutableDoubleStateOf(1.0) }
            var v3 by remember { mutableDoubleStateOf(1.0) }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusInputNumber(
                    value = v1,
                    onValueChange = { v1 = it },
                    size = ComponentSize.Large
                )
                NexusInputNumber(
                    value = v2,
                    onValueChange = { v2 = it },
                    size = ComponentSize.Default
                )
                NexusInputNumber(
                    value = v3,
                    onValueChange = { v3 = it },
                    size = ComponentSize.Small
                )
            }
        }
        DemoSection("Controls position") {
            var v1 by remember { mutableDoubleStateOf(10.0) }
            var v2 by remember { mutableDoubleStateOf(10.0) }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusInputNumber(value = v1, onValueChange = { v1 = it })
                NexusInputNumber(
                    value = v2,
                    onValueChange = { v2 = it },
                    controlsPosition = NexusInputNumberControlsPosition.Right,
                )
            }
        }
        DemoSection("Custom icon") {
            var v by remember { mutableDoubleStateOf(4.0) }
            NexusInputNumber(
                value = v,
                onValueChange = { v = it },
                increaseIcon = {
                    NexusText(
                        text = "▲",
                        color = NexusTheme.colorScheme.primary.base
                    )
                },
                decreaseIcon = {
                    NexusText(
                        text = "▼",
                        color = NexusTheme.colorScheme.primary.base
                    )
                },
            )
        }
        DemoSection("Prefix and suffix") {
            var v by remember { mutableDoubleStateOf(88.0) }
            NexusInputNumber(
                value = v,
                onValueChange = { v = it },
                prefix = {
                    NexusText(
                        text = "$",
                        style = NexusTheme.typography.small
                    )
                },
                suffix = {
                    NexusText(
                        text = "USD",
                        style = NexusTheme.typography.extraSmall
                    )
                },
                align = NexusInputNumberAlign.Right,
            )
        }
        DemoSection("Events") {
            var v by remember { mutableDoubleStateOf(6.0) }
            var logs by remember { mutableStateOf(listOf("Logs:")) }
            fun push(msg: String) {
                logs = (listOf(msg) + logs).take(6)
            }
            NexusInputNumber(
                value = v,
                onValueChange = { v = it },
                onChange = { current, old -> push("change: $old -> $current") },
                onFocus = { push("focus") },
                onBlur = { push("blur") },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                logs.forEach { line ->
                    NexusText(
                        text = line,
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                }
            }
        }
    }
}

@Composable
fun TextareaDemo() {
    DemoPage(title = "Textarea", description = "Multi-line text input.") {
        DemoSection("Basic usage") {
            var v by remember { mutableStateOf("") }
            NexusTextarea(value = v, onValueChange = { v = it }, placeholder = "Type something...")
        }
        DemoSection("With word limit") {
            var v by remember { mutableStateOf("") }
            NexusTextarea(value = v, onValueChange = { v = it }, maxLength = 100, showWordLimit = true, placeholder = "Max 100 characters")
        }
    }
}

@Composable
fun CheckboxDemo() {
    DemoPage(title = "Checkbox", description = "A group of options for multiple choices.") {
        DemoSection("Basic usage") {
            var c1 by remember { mutableStateOf(false) }
            var c2 by remember { mutableStateOf(true) }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusCheckbox(checked = c1, onCheckedChange = { c1 = it })
                    NexusText(text = "Unchecked")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusCheckbox(checked = c2, onCheckedChange = { c2 = it })
                    NexusText(text = "Checked")
                }
            }
        }
        DemoSection("Disabled & Indeterminate") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NexusCheckbox(checked = false, onCheckedChange = {}, disabled = true)
                NexusCheckbox(checked = true, onCheckedChange = {}, disabled = true)
                NexusCheckbox(checked = false, onCheckedChange = {}, indeterminate = true)
            }
        }
        DemoSection("Checkbox group") {
            val state = rememberCheckboxGroupState(initialSelected = setOf("Option A"))
            NexusCheckboxGroup(
                state = state,
                onChange = {},
            ) {
                NexusCheckbox(value = "Option A") { NexusText(text = "Option A") }
                NexusCheckbox(value = "Option B") { NexusText(text = "Option B") }
                NexusCheckbox(value = "Option C") { NexusText(text = "Option C") }
            }
        }
        DemoSection("Options attribute") {
            val state = rememberCheckboxGroupState(initialSelected = setOf("read"))
            NexusCheckboxGroup(
                state = state,
                options = listOf(
                    CheckboxOption("read", "Read"),
                    CheckboxOption("write", "Write"),
                    CheckboxOption("admin", "Admin", disabled = true),
                ),
            )
        }
        DemoSection("Minimum / Maximum checked") {
            val state = rememberCheckboxGroupState(initialSelected = setOf("A"))
            NexusCheckboxGroup(
                state = state,
                min = 1,
                max = 2,
            ) {
                NexusCheckbox(value = "A") { NexusText(text = "A") }
                NexusCheckbox(value = "B") { NexusText(text = "B") }
                NexusCheckbox(value = "C") { NexusText(text = "C") }
            }
        }
        DemoSection("Button style") {
            val state = rememberCheckboxGroupState(initialSelected = setOf("Shanghai"))
            NexusCheckboxGroup(
                state = state,
                type = CheckboxGroupType.Button,
                size = ComponentSize.Small,
            ) {
                NexusCheckboxButton(value = "Shanghai") { NexusText(text = "Shanghai") }
                NexusCheckboxButton(value = "Beijing") { NexusText(text = "Beijing") }
                NexusCheckboxButton(value = "Shenzhen") { NexusText(text = "Shenzhen") }
            }
        }
        DemoSection("With borders") {
            val state = rememberCheckboxGroupState(initialSelected = setOf("Kotlin"))
            NexusCheckboxGroup(state = state) {
                NexusCheckbox(
                    value = "Kotlin",
                    border = true
                ) { NexusText(text = "Kotlin") }
                NexusCheckbox(
                    value = "Compose",
                    border = true
                ) { NexusText(text = "Compose") }
                NexusCheckbox(
                    value = "Element",
                    border = true,
                    disabled = true
                ) { NexusText(text = "Element") }
            }
        }
    }
}

@Composable
fun RadioDemo() {
    DemoPage(title = "Radio", description = "Single selection among multiple options.") {
        DemoSection("Basic usage") {
            val state = rememberRadioGroupState("Option 1")
            NexusRadioGroup(state = state) {
                NexusRadio(value = "Option 1") { NexusText(text = "Option 1") }
                NexusRadio(value = "Option 2") { NexusText(text = "Option 2") }
            }
        }
        DemoSection("Disabled") {
            val state = rememberRadioGroupState("A")
            NexusRadioGroup(state = state, disabled = true) {
                NexusRadio(value = "A") { NexusText(text = "A") }
                NexusRadio(value = "B") { NexusText(text = "B") }
            }
        }
        DemoSection("Radio Group") {
            val state = rememberRadioGroupState("Shanghai")
            var current by remember { mutableStateOf("Shanghai") }
            NexusRadioGroup(
                state = state,
                onChange = { current = it },
            ) {
                NexusRadio(value = "Shanghai") { NexusText(text = "Shanghai") }
                NexusRadio(value = "Beijing") { NexusText(text = "Beijing") }
                NexusRadio(value = "Shenzhen") { NexusText(text = "Shenzhen") }
            }
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = "Current: $current",
                style = NexusTheme.typography.small,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }
        DemoSection("With borders") {
            val state = rememberRadioGroupState("A")
            NexusRadioGroup(state = state, size = ComponentSize.Small) {
                NexusRadio(
                    value = "A",
                    border = true
                ) { NexusText(text = "Option A") }
                NexusRadio(
                    value = "B",
                    border = true
                ) { NexusText(text = "Option B") }
                NexusRadio(
                    value = "C",
                    border = true,
                    disabled = true
                ) { NexusText(text = "Option C") }
            }
        }
        DemoSection("Options attribute") {
            val state = rememberRadioGroupState("1")
            NexusRadioGroup(
                state = state,
                options = listOf(
                    NexusRadioOption(payload = mapOf("id" to "1", "name" to "Option 1")),
                    NexusRadioOption(payload = mapOf("id" to "2", "name" to "Option 2")),
                    NexusRadioOption(payload = mapOf("id" to "3", "name" to "Option 3", "inactive" to true)),
                ),
                props = NexusRadioOptionProps(value = "id", label = "name", disabled = "inactive"),
            )
        }
        DemoSection("Radio Button") {
            val state = rememberRadioGroupState("New York")
            NexusRadioGroup(
                state = state,
                type = NexusRadioGroupType.Button,
                fill = NexusTheme.colorScheme.primary.base,
                textColor = NexusTheme.colorScheme.white,
            ) {
                NexusRadio(value = "New York") { NexusText(text = "New York") }
                NexusRadio(value = "Washington") { NexusText(text = "Washington") }
                NexusRadio(value = "Los Angeles") { NexusText(text = "Los Angeles") }
                NexusRadio(
                    value = "Chicago",
                    disabled = true
                ) { NexusText(text = "Chicago") }
            }
        }
    }
}

@Composable
fun RateDemo() {
    DemoPage(title = "Rate", description = "Used for rating.") {
        DemoSection("Basic usage") {
            var value by remember { mutableFloatStateOf(3f) }
            NexusRate(
                value = value,
                onValueChange = { value = it },
                lowThreshold = 2,
                highThreshold = 4,
                colors = listOf(
                    NexusTheme.colorScheme.danger.base,
                    NexusTheme.colorScheme.warning.base,
                    NexusTheme.colorScheme.success.base,
                ),
            )
        }
        DemoSection("Sizes") {
            var v1 by remember { mutableFloatStateOf(3f) }
            var v2 by remember { mutableFloatStateOf(3f) }
            var v3 by remember { mutableFloatStateOf(3f) }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusRate(
                    value = v1,
                    onValueChange = { v1 = it },
                    size = ComponentSize.Large
                )
                NexusRate(
                    value = v2,
                    onValueChange = { v2 = it },
                    size = ComponentSize.Default
                )
                NexusRate(
                    value = v3,
                    onValueChange = { v3 = it },
                    size = ComponentSize.Small
                )
            }
        }
        DemoSection("With allow-half") {
            var value by remember { mutableFloatStateOf(2.5f) }
            NexusRate(
                value = value,
                onValueChange = { value = it },
                allowHalf = true,
            )
        }
        DemoSection("With text") {
            var value by remember { mutableFloatStateOf(4f) }
            NexusRate(
                value = value,
                onValueChange = { value = it },
                showText = true,
                texts = listOf("Bad", "Poor", "Okay", "Good", "Excellent"),
            )
        }
        DemoSection("Clearable") {
            var value by remember { mutableFloatStateOf(3f) }
            NexusRate(
                value = value,
                onValueChange = { value = it },
                clearable = true,
                showScore = true,
                scoreTemplate = "{value} points",
            )
        }
        DemoSection("More icons") {
            var value by remember { mutableFloatStateOf(4f) }
            NexusRate(
                value = value,
                onValueChange = { value = it },
                icons = listOf("☹", "◔", "☺"),
                voidIcon = "○",
                colors = listOf(
                    NexusTheme.colorScheme.danger.base,
                    NexusTheme.colorScheme.warning.base,
                    NexusTheme.colorScheme.success.base,
                ),
            )
        }
        DemoSection("Read-only") {
            var value by remember { mutableFloatStateOf(3.5f) }
            NexusRate(
                value = value,
                onValueChange = { value = it },
                allowHalf = true,
                disabled = true,
                showScore = true,
                scoreTemplate = "{value} / 5",
            )
        }
    }
}

@Composable
fun SwitchDemo() {
    DemoPage(title = "Switch", description = "Switch is used for toggling between two opposing states.") {
        DemoSection("Basic usage") {
            var s1 by remember { mutableStateOf(false) }
            var s2 by remember { mutableStateOf(true) }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NexusSwitch(checked = s1, onCheckedChange = { s1 = it })
                NexusSwitch(checked = s2, onCheckedChange = { s2 = it })
            }
        }

        DemoSection("Sizes") {
            var large by remember { mutableStateOf(true) }
            var def by remember { mutableStateOf(false) }
            var small by remember { mutableStateOf(true) }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NexusSwitch(
                    checked = large,
                    onCheckedChange = { large = it },
                    size = ComponentSize.Large,
                )
                NexusSwitch(
                    checked = def,
                    onCheckedChange = { def = it },
                    size = ComponentSize.Default,
                )
                NexusSwitch(
                    checked = small,
                    onCheckedChange = { small = it },
                    size = ComponentSize.Small,
                )
            }
        }

        DemoSection("Text description") {
            var v1 by remember { mutableStateOf(true) }
            var v2 by remember { mutableStateOf(false) }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NexusSwitch(
                    checked = v1,
                    onCheckedChange = { v1 = it },
                    activeText = "Pay by month",
                    inactiveText = "Pay by year",
                    width = 54.dp,
                )
                NexusSwitch(
                    checked = v2,
                    onCheckedChange = { v2 = it },
                    activeText = "Y",
                    inactiveText = "N",
                    inlinePrompt = true,
                    width = 48.dp,
                )
            }
        }

        DemoSection("Display custom icons") {
            var v1 by remember { mutableStateOf(true) }
            var v2 by remember { mutableStateOf(false) }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NexusSwitch(
                    checked = v1,
                    onCheckedChange = { v1 = it },
                    activeIcon = {
                        NexusText(
                            text = "✓",
                            style = NexusTheme.typography.extraSmall
                        )
                    },
                    inactiveIcon = {
                        NexusText(
                            text = "✕",
                            style = NexusTheme.typography.extraSmall
                        )
                    },
                    inlinePrompt = true,
                    width = 50.dp,
                )
                NexusSwitch(
                    checked = v2,
                    onCheckedChange = { v2 = it },
                    activeIcon = { NexusText(text = "☀") },
                    inactiveIcon = { NexusText(text = "☾") },
                    activeText = "Light",
                    inactiveText = "Dark",
                )
            }
        }

        DemoSection("Extended value types") {
            var status by remember { mutableStateOf("OPEN") }
            var level by remember { mutableStateOf(100) }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NexusSwitch(
                    value = status,
                    onValueChange = { status = it },
                    activeValue = "OPEN",
                    inactiveValue = "CLOSE",
                    activeText = "Open",
                    inactiveText = "Close",
                )
                NexusSwitch(
                    value = level,
                    onValueChange = { level = it },
                    activeValue = 100,
                    inactiveValue = 0,
                    activeText = "100",
                    inactiveText = "0",
                    inlinePrompt = true,
                )
                NexusText(
                    text = "status=$status, level=$level",
                    style = NexusTheme.typography.extraSmall,
                    color = NexusTheme.colorScheme.text.secondary,
                )
            }
        }

        DemoSection("Disabled") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusSwitch(checked = true, onCheckedChange = {}, disabled = true)
                NexusSwitch(
                    checked = false,
                    onCheckedChange = {},
                    disabled = true,
                    activeText = "On",
                    inactiveText = "Off",
                )
            }
        }

        DemoSection("Loading") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusSwitch(checked = true, onCheckedChange = {}, loading = true)
                NexusSwitch(checked = false, onCheckedChange = {}, loading = true)
            }
        }

        DemoSection("Prevent switching") {
            var checked by remember { mutableStateOf(false) }
            var logs by remember { mutableStateOf(listOf("Logs:")) }
            fun push(msg: String) {
                logs = (listOf(msg) + logs).take(6)
            }
            NexusSwitch(
                checked = checked,
                onCheckedChange = { checked = it },
                beforeChange = { next ->
                    push("before-change -> $next")
                    delay(250)
                    val allow = next
                    push(if (allow) "allow" else "reject")
                    allow
                },
                activeText = "ON",
                inactiveText = "OFF",
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                logs.forEach { line ->
                    NexusText(
                        text = line,
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                }
            }
        }

        DemoSection("Custom action icon") {
            var checked by remember { mutableStateOf(true) }
            NexusSwitch(
                checked = checked,
                onCheckedChange = { checked = it },
                activeActionIcon = {
                    NexusText(
                        text = "★",
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.warning.base
                    )
                },
                inactiveActionIcon = {
                    NexusText(
                        text = "☆",
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.placeholder
                    )
                },
                width = 52.dp,
            )
        }

        DemoSection("Custom action slot") {
            var checked by remember { mutableStateOf(false) }
            NexusSwitch(
                checked = checked,
                onCheckedChange = { checked = it },
                activeAction = {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(NexusTheme.colorScheme.success.base, CircleShape),
                    )
                },
                inactiveAction = {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(NexusTheme.colorScheme.danger.base, CircleShape),
                    )
                },
                active = {
                    NexusText(
                        text = "Ready",
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.white
                    )
                },
                inactive = {
                    NexusText(
                        text = "Stop",
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.white
                    )
                },
                width = 62.dp,
            )
        }
    }
}

@Composable
fun SelectDemo() {
    DemoPage(title = "Select", description = "When there are plenty of options, use a drop-down menu to display and select desired ones.") {
        DemoSection("Basic usage") {
            val options = listOf(
                SelectOption("opt1", "Option 1"),
                SelectOption("opt2", "Option 2"),
                SelectOption("opt3", "Option 3"),
            )
            val state = rememberSelectState<String>()
            NexusSelect(
                state = state,
                options = options,
                onSelect = {},
                placeholder = "Select an option",
            )
        }
        DemoSection("Options attribute") {
            val options = listOf(
                SelectOption(
                    value = "cn",
                    label = "",
                    payload = mapOf("id" to "cn", "name" to "China"),
                ),
                SelectOption(
                    value = "jp",
                    label = "",
                    payload = mapOf("id" to "jp", "name" to "Japan"),
                ),
            )
            val state = rememberSelectState<String>()
            NexusSelect(
                state = state,
                options = options,
                onSelect = {},
                props = SelectOptionProps(value = "id", label = "name"),
                placeholder = "Select by mapped props",
            )
        }
        DemoSection("Disabled option / disabled select") {
            val state1 = rememberSelectState("opt1")
            val state2 = rememberSelectState<String>()
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusSelect(
                    state = state1,
                    options = listOf(
                        SelectOption("opt1", "Option 1"),
                        SelectOption("opt2", "Option 2", disabled = true),
                        SelectOption("opt3", "Option 3"),
                    ),
                    onSelect = {},
                )
                NexusSelect(
                    state = state2,
                    options = listOf(
                        SelectOption("a", "A"),
                        SelectOption("b", "B"),
                    ),
                    onSelect = {},
                    disabled = true,
                )
            }
        }
        DemoSection("Clearable / Sizes") {
            val large = rememberSelectState<String>()
            val def = rememberSelectState("2")
            val small = rememberSelectState<String>()
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusSelect(
                    state = large,
                    options = listOf(
                        SelectOption("1", "Large 1"),
                        SelectOption("2", "Large 2")
                    ),
                    onSelect = {},
                    clearable = true,
                    size = ComponentSize.Large,
                )
                NexusSelect(
                    state = def,
                    options = listOf(
                        SelectOption("1", "Default 1"),
                        SelectOption("2", "Default 2")
                    ),
                    onSelect = {},
                    clearable = true,
                    size = ComponentSize.Default,
                )
                NexusSelect(
                    state = small,
                    options = listOf(
                        SelectOption("1", "Small 1"),
                        SelectOption("2", "Small 2")
                    ),
                    onSelect = {},
                    size = ComponentSize.Small,
                )
            }
        }
        DemoSection("Basic multiple select") {
            val state = rememberSelectState(initialSelectedValues = listOf("Vue", "React"))
            NexusSelect(
                state = state,
                options = listOf(
                    SelectOption("Vue", "Vue"),
                    SelectOption("React", "React"),
                    SelectOption("Svelte", "Svelte"),
                    SelectOption("Solid", "Solid"),
                ),
                onSelect = {},
                multiple = true,
                clearable = true,
                collapseTags = true,
                collapseTagsTooltip = true,
                maxCollapseTags = 1,
                multipleLimit = 3,
                placeholder = "Pick frameworks",
            )
        }
        DemoSection("Custom template / header / footer") {
            val state = rememberSelectState<String>()
            NexusSelect(
                state = state,
                options = listOf(
                    SelectOption("kotlin", "Kotlin"),
                    SelectOption("java", "Java"),
                    SelectOption("swift", "Swift"),
                ),
                onSelect = {},
                header = {
                    NexusText(
                        text = "Popular languages",
                        style = NexusTheme.typography.small,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                },
                footer = {
                    NexusText(
                        text = "Press Enter to pick first match",
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.placeholder,
                    )
                },
                optionContent = { option, selected ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        NexusText(text = option.label)
                        if (selected) {
                            NexusText(
                                text = "Selected",
                                style = NexusTheme.typography.extraSmall,
                                color = NexusTheme.colorScheme.primary.base,
                            )
                        }
                    }
                },
            )
        }
        DemoSection("Grouping") {
            val state = rememberSelectState<String>()
            NexusSelect(
                state = state,
                options = listOf(
                    SelectOption(
                        value = "frontend",
                        label = "Frontend",
                        options = listOf(
                            SelectOption("vue", "Vue"),
                            SelectOption("react", "React"),
                        ),
                    ),
                    SelectOption(
                        value = "backend",
                        label = "Backend",
                        options = listOf(
                            SelectOption("spring", "Spring"),
                            SelectOption("ktor", "Ktor"),
                        ),
                    ),
                ),
                onSelect = {},
                placeholder = "Select in groups",
            )
        }
        DemoSection("Option filtering") {
            val state = rememberSelectState<String>()
            NexusSelect(
                state = state,
                options = listOf(
                    SelectOption("Shanghai", "Shanghai"),
                    SelectOption("Shenzhen", "Shenzhen"),
                    SelectOption("Beijing", "Beijing"),
                    SelectOption("Hangzhou", "Hangzhou"),
                ),
                onSelect = {},
                filterable = true,
                filterMethod = { query, option ->
                    option.label.startsWith(query, ignoreCase = true)
                },
                placeholder = "Type to filter",
            )
        }
        DemoSection("Remote search / custom loading") {
            val state = rememberSelectState<String>()
            val all = listOf("Alice", "Bob", "Charlie", "David", "Eve")
            NexusSelect(
                state = state,
                options = emptyList(),
                onSelect = {},
                filterable = true,
                remote = true,
                loading = state.query.length == 1,
                loadingText = "Searching...",
                remoteMethod = { query ->
                    all.filter { it.contains(query, ignoreCase = true) }
                        .map { SelectOption(it, it) }
                },
                loadingContent = {
                    NexusText(
                        text = "Loading remote data...",
                        style = NexusTheme.typography.small,
                        color = NexusTheme.colorScheme.primary.base,
                    )
                },
                placeholder = "Remote search",
            )
        }
        DemoSection("Create new items / events") {
            val state = rememberSelectState<String>()
            var logs by remember { mutableStateOf(listOf("Logs:")) }
            fun push(msg: String) {
                logs = (listOf(msg) + logs).take(6)
            }
            NexusSelect(
                state = state,
                options = listOf(
                    SelectOption("Kotlin", "Kotlin"),
                    SelectOption("Compose", "Compose"),
                ),
                onSelect = { push("select: $it") },
                filterable = true,
                allowCreate = true,
                defaultFirstOption = true,
                createOption = { SelectOption(it, it) },
                clearable = true,
                onVisibleChange = { push("visible: $it") },
                onChange = { push("change: $it") },
                onClear = { push("clear") },
                onFocus = { push("focus") },
                onBlur = { push("blur") },
                placeholder = "Type & create",
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                logs.forEach { line ->
                    NexusText(
                        text = line,
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                }
            }
        }
    }
}

@Composable
fun VirtualizedSelectDemo() {
    DemoPage(
        title = "Virtualized Select",
        description = "A select with virtualization for large option datasets.",
    ) {
        val largeOptions = remember {
            (1..1000).map { index ->
                SelectOption("option-$index", "Option $index")
            }
        }

        DemoSection("Basic usage") {
            val state = rememberSelectV2State<String>()
            NexusSelectV2(
                state = state,
                options = largeOptions,
                onSelect = {},
                placeholder = "Select from 1000 options",
                modifier = Modifier.width(320.dp),
            )
        }

        DemoSection("Multiple / hide extra tags") {
            val state = rememberSelectV2State(initialSelectedValues = listOf("option-1", "option-2", "option-3", "option-4"))
            NexusSelectV2(
                state = state,
                options = largeOptions.take(50),
                onSelect = {},
                multiple = true,
                collapseTags = true,
                collapseTagsTooltip = true,
                maxCollapseTags = 1,
                placeholder = "Multiple select",
                modifier = Modifier.width(380.dp),
            )
        }

        DemoSection("Size") {
            val largeState = rememberSelectV2State<String>()
            val defaultState = rememberSelectV2State<String>()
            val smallState = rememberSelectV2State<String>()
            val options = listOf(
                SelectOption("large", "Large"),
                SelectOption("default", "Default"),
                SelectOption("small", "Small"),
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusSelectV2(
                    state = largeState,
                    options = options,
                    onSelect = {},
                    size = ComponentSize.Large,
                    modifier = Modifier.width(260.dp),
                )
                NexusSelectV2(
                    state = defaultState,
                    options = options,
                    onSelect = {},
                    size = ComponentSize.Default,
                    modifier = Modifier.width(260.dp),
                )
                NexusSelectV2(
                    state = smallState,
                    options = options,
                    onSelect = {},
                    size = ComponentSize.Small,
                    modifier = Modifier.width(260.dp),
                )
            }
        }

        DemoSection("Filterable") {
            val state = rememberSelectV2State<String>()
            NexusSelectV2(
                state = state,
                options = largeOptions.take(200),
                onSelect = {},
                filterable = true,
                placeholder = "Type to filter options",
                modifier = Modifier.width(320.dp),
            )
        }

        DemoSection("Disabled") {
            val state = rememberSelectV2State(initialSelected = "option-2")
            NexusSelectV2(
                state = state,
                options = largeOptions.take(20),
                onSelect = {},
                disabled = true,
                modifier = Modifier.width(320.dp),
            )
        }

        DemoSection("Grouping") {
            val state = rememberSelectV2State<String>()
            NexusSelectV2(
                state = state,
                options = listOf(
                    SelectOption(
                        value = "frontend",
                        label = "Frontend",
                        options = listOf(
                            SelectOption("vue", "Vue"),
                            SelectOption("react", "React"),
                            SelectOption("svelte", "Svelte"),
                        ),
                    ),
                    SelectOption(
                        value = "backend",
                        label = "Backend",
                        options = listOf(
                            SelectOption("spring", "Spring"),
                            SelectOption("ktor", "Ktor"),
                            SelectOption("quarkus", "Quarkus"),
                        ),
                    ),
                ),
                onSelect = {},
                placeholder = "Select in groups",
                modifier = Modifier.width(320.dp),
            )
        }

        DemoSection("Clearable") {
            val state = rememberSelectV2State(initialSelected = "option-5")
            NexusSelectV2(
                state = state,
                options = largeOptions.take(30),
                onSelect = {},
                clearable = true,
                modifier = Modifier.width(320.dp),
            )
        }

        DemoSection("Customized option") {
            val state = rememberSelectV2State<String>()
            NexusSelectV2(
                state = state,
                options = largeOptions.take(60),
                onSelect = {},
                optionContent = { option, selected ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        NexusText(text = option.label)
                        NexusText(
                            text = if (selected) "Selected" else option.value,
                            style = NexusTheme.typography.extraSmall,
                            color = if (selected) NexusTheme.colorScheme.primary.base else NexusTheme.colorScheme.text.placeholder,
                        )
                    }
                },
                modifier = Modifier.width(360.dp),
            )
        }

        DemoSection("Custom header / footer") {
            val state = rememberSelectV2State<String>()
            NexusSelectV2(
                state = state,
                options = largeOptions.take(80),
                onSelect = {},
                header = {
                    NexusText(
                        text = "Top 80 options",
                        style = NexusTheme.typography.small,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                },
                footer = {
                    NexusText(
                        text = "Virtualized list in dropdown",
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.placeholder,
                    )
                },
                modifier = Modifier.width(320.dp),
            )
        }

        DemoSection("Allow create") {
            val state = rememberSelectV2State<String>()
            NexusSelectV2(
                state = state,
                options = listOf(
                    SelectOption("kotlin", "Kotlin"),
                    SelectOption("compose", "Compose"),
                ),
                onSelect = {},
                filterable = true,
                allowCreate = true,
                createOption = { SelectOption(it, it) },
                defaultFirstOption = true,
                clearable = true,
                modifier = Modifier.width(320.dp),
            )
        }

        DemoSection("Remote search / custom loading") {
            val state = rememberSelectV2State<String>()
            val allUsers = remember {
                (1..500).map { index -> "User $index" }
            }
            NexusSelectV2(
                state = state,
                options = emptyList(),
                onSelect = {},
                filterable = true,
                remote = true,
                loading = state.query.length == 1,
                remoteMethod = { query ->
                    allUsers.filter { it.contains(query, ignoreCase = true) }
                        .take(100)
                        .map { SelectOption(it, it) }
                },
                loadingContent = {
                    NexusText(
                        text = "Loading remote users...",
                        style = NexusTheme.typography.small,
                        color = NexusTheme.colorScheme.primary.base,
                    )
                },
                placeholder = "Search user",
                modifier = Modifier.width(320.dp),
            )
        }

        DemoSection("Use valueKey / custom label") {
            data class City(val id: Int, val name: String)
            val state = rememberSelectV2State(initialSelected = City(2, "Beijing"))
            val options = listOf(
                SelectOption(City(1, "Shanghai"), "Shanghai"),
                SelectOption(City(2, "Beijing"), "Beijing"),
                SelectOption(City(3, "Shenzhen"), "Shenzhen"),
            )
            NexusSelectV2(
                state = state,
                options = options,
                onSelect = {},
                valueKey = { it.id },
                labelContent = { _, label, value ->
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        NexusTag(text = "#${value.id}", size = ComponentSize.Small)
                        NexusText(text = label)
                    }
                },
                modifier = Modifier.width(320.dp),
            )
        }

        DemoSection("Props") {
            val state = rememberSelectV2State<String>()
            val options = listOf(
                SelectOption(value = "", label = "", payload = mapOf("id" to "cn", "name" to "China", "blocked" to false)),
                SelectOption(value = "", label = "", payload = mapOf("id" to "us", "name" to "United States", "blocked" to false)),
                SelectOption(value = "", label = "", payload = mapOf("id" to "xx", "name" to "Disabled", "blocked" to true)),
            )
            NexusSelectV2(
                state = state,
                options = options,
                onSelect = {},
                props = SelectOptionProps(value = "id", label = "name", disabled = "blocked"),
                modifier = Modifier.width(320.dp),
            )
        }

        DemoSection("Custom tag") {
            val state = rememberSelectV2State(initialSelectedValues = listOf("option-1", "option-2"))
            NexusSelectV2(
                state = state,
                options = largeOptions.take(20),
                onSelect = {},
                multiple = true,
                tagContent = { option, _, onDelete ->
                    NexusTag(
                        text = option.label.uppercase(),
                        type = NexusType.Success,
                        effect = TagEffect.Dark,
                        closable = true,
                        size = ComponentSize.Small,
                        onClose = onDelete,
                    )
                },
                modifier = Modifier.width(420.dp),
            )
        }

        DemoSection("Empty values") {
            val state = rememberSelectV2State(initialSelected = "")
            val options = listOf(
                SelectOption("", "Empty String"),
                SelectOption("none", "None"),
                SelectOption("valid", "Valid"),
            )
            NexusSelectV2(
                state = state,
                options = options,
                onSelect = {},
                clearable = true,
                emptyValues = setOf(null, ""),
                valueOnClear = "",
                placeholder = "Empty string is treated as empty value",
                modifier = Modifier.width(320.dp),
            )
        }

        DemoSection("Custom width") {
            val state1 = rememberSelectV2State<String>()
            val state2 = rememberSelectV2State<String>()
            val options = largeOptions.take(120)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NexusSelectV2(
                    state = state1,
                    options = options,
                    onSelect = {},
                    fitInputWidth = true,
                    placeholder = "Dropdown follows input width",
                    modifier = Modifier.width(220.dp),
                )
                NexusSelectV2(
                    state = state2,
                    options = options,
                    onSelect = {},
                    fitInputWidth = false,
                    dropdownWidth = 420.dp,
                    placeholder = "Custom dropdown width = 420dp",
                    modifier = Modifier.width(220.dp),
                )
            }
        }
    }
}

@Composable
fun SliderDemo() {
    DemoPage(title = "Slider", description = "Drag the slider within a fixed range.") {
        DemoSection("Basic usage") {
            var v by remember { mutableFloatStateOf(30f) }
            NexusSlider(value = v, onValueChange = { v = it })
            Spacer(modifier = Modifier.height(6.dp))
            NexusText(
                text = "Value: ${v.roundToInt()}",
                style = NexusTheme.typography.extraSmall,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }

        DemoSection("Discrete values") {
            var v by remember { mutableFloatStateOf(50f) }
            NexusSlider(
                value = v,
                onValueChange = { v = it },
                step = 10f,
                showStops = true,
            )
        }

        DemoSection("Slider with input box") {
            var v by remember { mutableFloatStateOf(36f) }
            NexusSlider(
                value = v,
                onValueChange = { v = it },
                showInput = true,
                showInputControls = true,
                min = 0f,
                max = 100f,
            )
        }

        DemoSection("Sizes") {
            var large by remember { mutableFloatStateOf(20f) }
            var def by remember { mutableFloatStateOf(45f) }
            var small by remember { mutableFloatStateOf(70f) }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusSlider(
                    value = large,
                    onValueChange = { large = it },
                    size = ComponentSize.Large,
                )
                NexusSlider(
                    value = def,
                    onValueChange = { def = it },
                    size = ComponentSize.Default,
                )
                NexusSlider(
                    value = small,
                    onValueChange = { small = it },
                    size = ComponentSize.Small,
                )
            }
        }

        DemoSection("Placement") {
            var topValue by remember { mutableFloatStateOf(30f) }
            var bottomValue by remember { mutableFloatStateOf(65f) }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusSlider(
                    value = topValue,
                    onValueChange = { topValue = it },
                    placement = NexusSliderPlacement.Top,
                )
                NexusSlider(
                    value = bottomValue,
                    onValueChange = { bottomValue = it },
                    placement = NexusSliderPlacement.Bottom,
                )
            }
        }

        DemoSection("Range selection") {
            var range by remember { mutableStateOf(20f..80f) }
            NexusRangeSlider(
                values = range,
                onValueChange = { range = it },
                showStops = true,
                step = 5f,
            )
            Spacer(modifier = Modifier.height(6.dp))
            NexusText(
                text = "Range: ${range.start.roundToInt()} - ${range.endInclusive.roundToInt()}",
                style = NexusTheme.typography.extraSmall,
                color = NexusTheme.colorScheme.text.secondary,
            )
        }

        DemoSection("Vertical mode") {
            var leftValue by remember { mutableFloatStateOf(40f) }
            var rightValue by remember { mutableFloatStateOf(60f) }
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                NexusSlider(
                    value = leftValue,
                    onValueChange = { leftValue = it },
                    vertical = true,
                    height = 220.dp,
                    placement = NexusSliderPlacement.Right,
                )
                NexusSlider(
                    value = rightValue,
                    onValueChange = { rightValue = it },
                    vertical = true,
                    height = 220.dp,
                    placement = NexusSliderPlacement.Left,
                    showTooltip = true,
                )
            }
        }

        DemoSection("Show marks / events") {
            var v by remember { mutableFloatStateOf(18f) }
            var logs by remember { mutableStateOf(listOf("Logs:")) }
            fun push(msg: String) {
                logs = (listOf(msg) + logs).take(6)
            }
            NexusSlider(
                value = v,
                onValueChange = { v = it },
                min = 0f,
                max = 100f,
                step = 1f,
                marks = mapOf(
                    0f to "0",
                    20f to "20",
                    40f to "40",
                    60f to "60",
                    80f to "80",
                    100f to "100",
                ),
                formatTooltip = { "${it.roundToInt()}%" },
                onInput = { push("input: ${it.roundToInt()}") },
                onChange = { push("change: ${it.roundToInt()}") },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                logs.forEach { line ->
                    NexusText(
                        text = line,
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                }
            }
        }
    }
}

@Composable
fun DatePickerPanelDemo() {
    DemoPage(title = "DatePickerPanel", description = "Core panel component of DatePicker.") {
        DemoSection("Enter Date") {
            val state =
                rememberDatePickerState(initialDate = NexusDate(2026, 3, 4))
            NexusDatePickerPanel(
                state = state,
                type = NexusDatePickerPanelType.Date,
                modifier = Modifier.width(320.dp),
            )
        }
        DemoSection("Border") {
            val state1 =
                rememberDatePickerState(initialDate = NexusDate(2026, 3, 4))
            val state2 =
                rememberDatePickerState(initialDate = NexusDate(2026, 3, 4))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusDatePickerPanel(
                    state = state1,
                    border = true,
                    modifier = Modifier.width(320.dp),
                )
                NexusDatePickerPanel(
                    state = state2,
                    border = false,
                    modifier = Modifier.width(320.dp),
                )
            }
        }
        DemoSection("Disabled") {
            val state =
                rememberDatePickerState(initialDate = NexusDate(2026, 3, 4))
            NexusDatePickerPanel(
                state = state,
                disabled = true,
                modifier = Modifier.width(320.dp),
            )
        }
        DemoSection("Types") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                val dateState =
                    rememberDatePickerState(initialDate = NexusDate(2026, 3, 4))
                val monthState =
                    rememberDatePickerState(initialDate = NexusDate(2026, 3, 1))
                val yearState =
                    rememberDatePickerState(initialDate = NexusDate(2026, 1, 1))
                NexusDatePickerPanel(
                    state = dateState,
                    type = NexusDatePickerPanelType.Date,
                    showWeekNumber = true,
                    modifier = Modifier.width(320.dp),
                )
                NexusDatePickerPanel(
                    state = monthState,
                    type = NexusDatePickerPanelType.Month,
                    modifier = Modifier.width(320.dp),
                )
                NexusDatePickerPanel(
                    state = yearState,
                    type = NexusDatePickerPanelType.Year,
                    modifier = Modifier.width(320.dp),
                )
            }
        }
    }
}

@Composable
fun DatePickerDemo() {
    DemoPage(title = "DatePicker", description = "Use DatePicker for date input.") {
        DemoSection("Enter Date") {
            val state = rememberDatePickerState()
            NexusDatePicker(
                state = state,
                placeholder = "Pick a date",
                type = NexusDatePickerPanelType.Date,
            )
        }
        DemoSection("Other measurements") {
            val monthState = rememberDatePickerState()
            val yearState = rememberDatePickerState()
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusDatePicker(
                    state = monthState,
                    placeholder = "Pick month",
                    type = NexusDatePickerPanelType.Month,
                )
                NexusDatePicker(
                    state = yearState,
                    placeholder = "Pick year",
                    type = NexusDatePickerPanelType.Year,
                )
            }
        }
        DemoSection("Multiple dates") {
            val state = rememberDatePickerState()
            NexusDatePicker(
                state = state,
                placeholder = "Pick multiple dates",
                type = NexusDatePickerPanelType.Dates,
                showConfirm = true,
            )
        }
        DemoSection("Clearable / disabled / size") {
            val clearState =
                rememberDatePickerState(initialDate = NexusDate(2026, 3, 4))
            val disabledState =
                rememberDatePickerState(initialDate = NexusDate(2026, 3, 4))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusDatePicker(
                    state = clearState,
                    clearable = true,
                    size = ComponentSize.Large,
                )
                NexusDatePicker(
                    state = disabledState,
                    disabled = true,
                    size = ComponentSize.Small,
                )
            }
        }
    }
}

@Composable
fun DateTimePickerDemo() {
    DemoPage(title = "DateTimePicker", description = "Select date and time in one picker.") {
        DemoSection("Date and time") {
            val state = rememberDateTimePickerState(
                initialDateTime = NexusDateTime(
                    date = NexusDate(2026, 3, 4),
                    time = NexusTime(10, 30, 0),
                ),
            )
            NexusDateTimePicker(
                state = state,
                type = NexusDatePickerPanelType.DateTime,
                placeholder = "Pick date and time",
            )
        }
        DemoSection("DateTime Formats") {
            val state = rememberDateTimePickerState(
                initialDateTime = NexusDateTime(
                    date = NexusDate(2026, 3, 4),
                    time = NexusTime(18, 5, 30),
                ),
            )
            NexusDateTimePicker(
                state = state,
                type = NexusDatePickerPanelType.DateTime,
                format = "YYYY/MM/DD HH:mm",
                placeholder = "YYYY/MM/DD HH:mm",
            )
        }
        DemoSection("Date and time formats in dropdown panel") {
            val state = rememberDateTimePickerState()
            NexusDateTimePicker(
                state = state,
                type = NexusDatePickerPanelType.DateTime,
                dateFormat = "YYYY/MM/DD",
                timeFormat = "HH:mm",
                format = "YYYY-MM-DD HH:mm",
                placeholder = "Panel format: YYYY/MM/DD + HH:mm",
            )
        }
        DemoSection("Date and time range") {
            val state = rememberDateTimePickerState(
                initialRangeStart = NexusDateTime(
                    date = NexusDate(2026, 3, 2),
                    time = NexusTime(9, 0, 0),
                ),
                initialRangeEnd = NexusDateTime(
                    date = NexusDate(2026, 3, 6),
                    time = NexusTime(18, 30, 0),
                ),
            )
            NexusDateTimePicker(
                state = state,
                type = NexusDatePickerPanelType.DateTimeRange,
                startPlaceholder = "Start",
                endPlaceholder = "End",
            )
        }
        DemoSection("Default time value for range") {
            val state = rememberDateTimePickerState()
            NexusDateTimePicker(
                state = state,
                type = NexusDatePickerPanelType.DateTimeRange,
                defaultTime = NexusTime(8, 30, 0) to NexusTime(20, 0, 0),
                showWeekNumber = true,
                startPlaceholder = "Use default start time",
                endPlaceholder = "Use default end time",
            )
        }
    }
}

@Composable
fun TimePickerDemo() {
    DemoPage(title = "TimePicker", description = "Use Time Picker for time input.") {
        DemoSection("Arbitrary time picker") {
            val state1 = rememberTimePickerState()
            val state2 =
                rememberTimePickerState(initialTime = NexusTime(13, 30, 0))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NexusTimePicker(
                    state = state1,
                    placeholder = "Pick a time",
                    showSeconds = true,
                )
                NexusTimePicker(
                    state = state2,
                    placeholder = "Arrow control mode",
                    arrowControl = true,
                )
            }
        }
        DemoSection("Limit the time range") {
            val state =
                rememberTimePickerState(initialTime = NexusTime(10, 20, 30))
            NexusTimePicker(
                state = state,
                showSeconds = true,
                disabledHours = { role ->
                    if (role == "single") (0..8).toList() + (19..23).toList() else emptyList()
                },
                disabledMinutes = { hour, _ ->
                    if (hour == 9) (0..29).toList() else emptyList()
                },
                disabledSeconds = { hour, minute, _ ->
                    if (hour == 10 && minute == 20) (0..20).toList() else emptyList()
                },
                placeholder = "Only 09:30 - 18:59 available",
            )
        }
        DemoSection("Arbitrary time range") {
            val state = rememberTimePickerState(
                initialRange = NexusTime(9, 0, 0) to NexusTime(18, 0, 0),
            )
            var logs by remember { mutableStateOf(listOf("Logs:")) }
            fun push(msg: String) {
                logs = (listOf(msg) + logs).take(6)
            }
            NexusTimePicker(
                state = state,
                isRange = true,
                showSeconds = true,
                arrowControl = true,
                startPlaceholder = "Start time",
                endPlaceholder = "End time",
                clearable = true,
                rangeSeparator = " ~ ",
                valueFormat = "HH:mm:ss",
                onVisibleChange = { push("visible: $it") },
                onClear = { push("clear") },
                onChange = { push("change: $it") },
                onRangeChange = { start, end -> push("range: $start -> $end") },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                logs.forEach { line ->
                    NexusText(
                        text = line,
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                }
            }
        }
    }
}

@Composable
fun TimeSelectDemo() {
    DemoPage(title = "TimeSelect", description = "Use Time Select for time input.") {
        DemoSection("Fixed time picker") {
            val state = rememberTimeSelectState()
            NexusTimeSelect(
                state = state,
                start = "08:30",
                end = "18:30",
                step = "00:30",
                placeholder = "Select a fixed time",
                includeEndTime = true,
            )
        }

        DemoSection("Time formats") {
            val state24 = rememberTimeSelectState("13:30")
            val state12 = rememberTimeSelectState()
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NexusTimeSelect(
                    state = state24,
                    format = "HH:mm",
                    start = "09:00",
                    end = "18:00",
                    step = "00:15",
                )
                NexusTimeSelect(
                    state = state12,
                    format = "hh:mm A",
                    start = "09:00",
                    end = "18:00",
                    step = "00:30",
                    placeholder = "12-hour format",
                )
            }
        }

        DemoSection("Fixed time range") {
            val startState = rememberTimeSelectState("09:00")
            val endState = rememberTimeSelectState("18:00")
            var logs by remember { mutableStateOf(listOf("Logs:")) }
            fun push(msg: String) {
                logs = (listOf(msg) + logs).take(6)
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NexusTimeSelect(
                    state = startState,
                    start = "08:30",
                    end = "20:30",
                    step = "00:30",
                    maxTime = endState.value,
                    placeholder = "Start time",
                    onChange = {
                        push("start change: $it")
                    },
                )
                NexusTimeSelect(
                    state = endState,
                    start = "08:30",
                    end = "20:30",
                    step = "00:30",
                    minTime = startState.value,
                    placeholder = "End time",
                    onChange = {
                        push("end change: $it")
                    },
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                logs.forEach { line ->
                    NexusText(
                        text = line,
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                }
            }
        }
    }
}

@Composable
fun FormDemo() {
    DemoPage(title = "Form", description = "Form consists of Input, Radio, Select, Checkbox and so on.") {
        DemoSection("Basic form") {
            var name by remember { mutableStateOf("") }
            var region by remember { mutableStateOf("") }
            var agree by remember { mutableStateOf(false) }
            val regionState = rememberSelectState<String>()

            NexusForm(labelWidth = 96.dp) {
                NexusFormItem(label = "Activity", required = true) {
                    NexusInput(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "Please input activity name",
                    )
                }
                NexusFormItem(label = "Region", required = true) {
                    NexusSelect(
                        state = regionState,
                        options = listOf(
                            SelectOption("shanghai", "Shanghai"),
                            SelectOption("beijing", "Beijing"),
                            SelectOption("shenzhen", "Shenzhen"),
                        ),
                        onSelect = { region = it },
                        placeholder = "Please select",
                    )
                }
                NexusFormItem(label = "Agreement") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusCheckbox(checked = agree, onCheckedChange = { agree = it })
                        NexusText(text = "I agree to the terms")
                    }
                }
                NexusFormItem(label = "") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusButton(
                            text = "Create",
                            onClick = {},
                            type = NexusType.Primary
                        )
                        NexusButton(text = "Cancel", onClick = {})
                    }
                }
            }
        }

        DemoSection("Inline form") {
            var user by remember { mutableStateOf("") }
            var city by remember { mutableStateOf("") }
            val cityState = rememberSelectState<String>()
            NexusForm(inline = true, labelWidth = 72.dp) {
                NexusFormItem(label = "User") {
                    NexusInput(value = user, onValueChange = { user = it }, placeholder = "User")
                }
                NexusFormItem(label = "City") {
                    NexusSelect(
                        state = cityState,
                        options = listOf(
                            SelectOption("hangzhou", "Hangzhou"),
                            SelectOption("suzhou", "Suzhou"),
                        ),
                        onSelect = { city = it },
                        placeholder = "City",
                    )
                }
                NexusFormItem(label = "") {
                    NexusButton(
                        text = "Query",
                        onClick = {},
                        type = NexusType.Primary
                    )
                }
            }
        }

        DemoSection("Alignment") {
            var v1 by remember { mutableStateOf("") }
            var v2 by remember { mutableStateOf("") }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusForm(
                    labelPosition = LabelPosition.Left,
                    labelWidth = 90.dp
                ) {
                    NexusFormItem(label = "Left") {
                        NexusInput(value = v1, onValueChange = { v1 = it }, placeholder = "Left aligned label")
                    }
                }
                NexusForm(labelPosition = LabelPosition.Top) {
                    NexusFormItem(label = "Top Label") {
                        NexusInput(value = v2, onValueChange = { v2 = it }, placeholder = "Top aligned label")
                    }
                }
            }
        }

        DemoSection("Validation") {
            val formState = rememberNexusFormState(
                initialModel = mapOf(
                    "name" to "",
                    "desc" to "",
                ),
            )
            var name by remember { mutableStateOf("") }
            var desc by remember { mutableStateOf("") }
            var result by remember { mutableStateOf("") }
            val rules = remember {
                mapOf(
                    "name" to listOf(
                        NexusFormRule(required = true, message = "Please input name"),
                        NexusFormRule(min = 3, max = 20, message = "Length should be 3 to 20"),
                    ),
                    "desc" to listOf(
                        NexusFormRule(required = true, message = "Please input description"),
                        NexusFormRule(min = 10, max = 100, message = "Length should be 10 to 100"),
                    ),
                )
            }

            NexusForm(
                state = formState,
                rules = rules,
                labelWidth = 90.dp,
                statusIcon = true,
                onValidate = { _, _, _ -> },
            ) {
                NexusFormItem(label = "Name", prop = "name") {
                    NexusInput(
                        value = name,
                        onValueChange = {
                            name = it
                            formState.setFieldValue("name", it)
                            formState.validateField("name", rules["name"].orEmpty(), FormValidateTrigger.Change)
                        },
                        placeholder = "Please input name",
                    )
                }
                NexusFormItem(label = "Description", prop = "desc") {
                    NexusInput(
                        value = desc,
                        onValueChange = {
                            desc = it
                            formState.setFieldValue("desc", it)
                        },
                        placeholder = "Please input description",
                    )
                }
                NexusFormItem(label = "") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusButton(
                            text = "Submit",
                            onClick = {
                                val ok = formState.validate(rules)
                                result = if (ok) "Submit success" else "Please fix validation errors"
                            },
                            type = NexusType.Primary,
                        )
                        NexusButton(
                            text = "Reset",
                            onClick = {
                                formState.resetFields()
                                name = formState.getFieldValue("name") as? String ?: ""
                                desc = formState.getFieldValue("desc") as? String ?: ""
                                result = ""
                            },
                        )
                    }
                }
            }
            if (result.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                NexusText(
                    text = result,
                    style = NexusTheme.typography.small,
                    color = NexusTheme.colorScheme.text.secondary
                )
            }
        }

        DemoSection("Custom validation rules") {
            val formState = rememberNexusFormState(
                initialModel = mapOf(
                    "pass" to "",
                    "checkPass" to "",
                ),
            )
            var pass by remember { mutableStateOf("") }
            var checkPass by remember { mutableStateOf("") }
            var result by remember { mutableStateOf("") }

            val rules = remember(pass) {
                mapOf(
                    "pass" to listOf(
                        NexusFormRule(required = true, message = "Please input password"),
                        NexusFormRule(min = 6, message = "Length at least 6"),
                    ),
                    "checkPass" to listOf(
                        NexusFormRule(required = true, message = "Please input password again"),
                        NexusFormRule(validator = {
                            if ((it as? String) != pass) "Two passwords do not match" else null
                        }),
                    ),
                )
            }

            NexusForm(state = formState, rules = rules, labelWidth = 120.dp, statusIcon = true) {
                NexusFormItem(label = "Password", prop = "pass") {
                    NexusInput(
                        value = pass,
                        onValueChange = {
                            pass = it
                            formState.setFieldValue("pass", it)
                        },
                        showPassword = true,
                        placeholder = "Password",
                    )
                }
                NexusFormItem(label = "Confirm", prop = "checkPass") {
                    NexusInput(
                        value = checkPass,
                        onValueChange = {
                            checkPass = it
                            formState.setFieldValue("checkPass", it)
                        },
                        showPassword = true,
                        placeholder = "Confirm password",
                    )
                }
                NexusFormItem(label = "") {
                    NexusButton(
                        text = "Validate",
                        onClick = {
                            val ok = formState.validate(rules)
                            result = if (ok) "Validation passed" else "Validation failed"
                        },
                        type = NexusType.Primary,
                    )
                }
            }
            if (result.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                NexusText(
                    text = result,
                    style = NexusTheme.typography.small,
                    color = NexusTheme.colorScheme.text.secondary
                )
            }
        }

        DemoSection("Add/Delete form item") {
            var domains by remember { mutableStateOf(listOf("example.com")) }
            NexusForm(labelWidth = 90.dp) {
                domains.forEachIndexed { index, domain ->
                    NexusFormItem(label = "Domain ${index + 1}") {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            NexusInput(
                                value = domain,
                                onValueChange = { value ->
                                    domains = domains.toMutableList().also { it[index] = value }
                                },
                                placeholder = "domain",
                            )
                            NexusButton(
                                text = "Delete",
                                onClick = {
                                    domains = domains.toMutableList().also { it.removeAt(index) }
                                },
                                type = NexusType.Danger,
                                plain = true,
                            )
                        }
                    }
                }
                NexusFormItem(label = "") {
                    NexusButton(
                        text = "Add domain",
                        onClick = { domains = domains + "" },
                        type = NexusType.Primary,
                    )
                }
            }
        }

        DemoSection("Number validate") {
            val formState = rememberNexusFormState(initialModel = mapOf("age" to 0.0))
            var age by remember { mutableDoubleStateOf(0.0) }
            val rules = remember {
                mapOf(
                    "age" to listOf(
                        NexusFormRule(required = true, message = "Please input age"),
                        NexusFormRule(validator = {
                            val number = (it as? Double) ?: 0.0
                            if (number <= 0.0) "Age must be greater than 0" else null
                        }),
                    )
                )
            }
            NexusForm(state = formState, rules = rules, labelWidth = 90.dp) {
                NexusFormItem(label = "Age", prop = "age") {
                    NexusInputNumber(
                        value = age,
                        onValueChange = {
                            age = it
                            formState.setFieldValue("age", it)
                        },
                        min = 0.0,
                        max = 120.0,
                    )
                }
                NexusFormItem(label = "") {
                    NexusButton(
                        text = "Validate",
                        onClick = { formState.validate(rules) },
                        type = NexusType.Primary,
                    )
                }
            }
        }

        DemoSection("Size control") {
            var v1 by remember { mutableStateOf("") }
            var v2 by remember { mutableStateOf("") }
            var v3 by remember { mutableStateOf("") }
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusForm(size = ComponentSize.Large, labelWidth = 80.dp) {
                    NexusFormItem(label = "Large") {
                        NexusInput(value = v1, onValueChange = { v1 = it }, placeholder = "Large form")
                    }
                }
                NexusForm(size = ComponentSize.Default, labelWidth = 80.dp) {
                    NexusFormItem(label = "Default") {
                        NexusInput(value = v2, onValueChange = { v2 = it }, placeholder = "Default form")
                    }
                }
                NexusForm(size = ComponentSize.Small, labelWidth = 80.dp) {
                    NexusFormItem(label = "Small") {
                        NexusInput(value = v3, onValueChange = { v3 = it }, placeholder = "Small form")
                    }
                }
            }
        }

        DemoSection("Accessibility") {
            var firstName by remember { mutableStateOf("") }
            var lastName by remember { mutableStateOf("") }
            NexusForm(labelPosition = LabelPosition.Top) {
                NexusFormItem(label = "Your Name") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column {
                            NexusText(
                                text = "First Name",
                                style = NexusTheme.typography.extraSmall,
                                color = NexusTheme.colorScheme.text.placeholder
                            )
                            NexusInput(value = firstName, onValueChange = { firstName = it }, placeholder = "First")
                        }
                        Column {
                            NexusText(
                                text = "Last Name",
                                style = NexusTheme.typography.extraSmall,
                                color = NexusTheme.colorScheme.text.placeholder
                            )
                            NexusInput(value = lastName, onValueChange = { lastName = it }, placeholder = "Last")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AutocompleteDemo() {
    DemoPage(title = "Autocomplete", description = "Get suggestions while you type.") {
        DemoSection("Basic usage") {
            val state = rememberAutocompleteState()
            NexusAutocomplete(
                state = state,
                placeholder = "Type something...",
                fetchSuggestions = { query ->
                    listOf("vue", "react", "angular", "svelte", "compose")
                        .filter { it.contains(query, ignoreCase = true) }
                },
            )
        }
        DemoSection("Custom template") {
            val state = rememberAutocompleteState()
            NexusAutocomplete(
                state = state,
                placeholder = "Search framework",
                fetchSuggestions = { query ->
                    listOf("Vue.js", "React", "Angular", "Svelte", "Solid")
                        .filter { it.contains(query, ignoreCase = true) }
                },
                itemContent = { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        NexusText(text = item)
                        NexusText(
                            text = "Framework",
                            style = NexusTheme.typography.small,
                            color = NexusTheme.colorScheme.text.secondary
                        )
                    }
                },
            )
        }
        DemoSection("Remote search") {
            val state = rememberAutocompleteState()
            NexusAutocomplete(
                state = state,
                placeholder = "Search from remote source",
                fetchSuggestions = { query ->
                    delay(350)
                    listOf("Shanghai", "Shenzhen", "Beijing", "Hangzhou", "Suzhou", "Chengdu")
                        .filter { it.contains(query, ignoreCase = true) }
                },
                highlightFirstItem = true,
            )
        }
        DemoSection("Custom loading") {
            val state = rememberAutocompleteState()
            NexusAutocomplete(
                state = state,
                placeholder = "Try typing 'co'",
                fetchSuggestions = { query ->
                    delay(500)
                    listOf("compose", "codex", "coroutines", "container")
                        .filter { it.contains(query, ignoreCase = true) }
                },
                loadingContent = {
                    NexusText(
                        text = "Loading suggestions...",
                        style = NexusTheme.typography.small,
                        color = NexusTheme.colorScheme.primary.base,
                    )
                },
            )
        }
        DemoSection("Custom header & footer") {
            val state = rememberAutocompleteState()
            NexusAutocomplete(
                state = state,
                placeholder = "Type city",
                fetchSuggestions = { query ->
                    listOf("Paris", "London", "Tokyo", "Berlin", "Milan")
                        .filter { it.contains(query, ignoreCase = true) }
                },
                header = {
                    NexusText(
                        text = "Popular cities",
                        style = NexusTheme.typography.small,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                },
                footer = {
                    NexusText(
                        text = "Press Enter to confirm unmatched text",
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.placeholder,
                    )
                },
                selectWhenUnmatched = true,
            )
        }
        DemoSection("Events") {
            val state = rememberAutocompleteState()
            var logs by remember { mutableStateOf(listOf("Logs:")) }
            fun pushLog(msg: String) {
                logs = (listOf(msg) + logs).take(5)
            }
            NexusAutocomplete(
                state = state,
                placeholder = "Observe input/focus/select events",
                fetchSuggestions = { query ->
                    listOf("Kotlin", "Compose", "Multiplatform", "Element")
                        .filter { it.contains(query, ignoreCase = true) }
                },
                onInput = { pushLog("input: $it") },
                onFocus = { pushLog("focus") },
                onBlur = { pushLog("blur") },
                onClear = { pushLog("clear") },
                onSelect = { pushLog("select: $it") },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                logs.forEach { line ->
                    NexusText(
                        text = line,
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.secondary
                    )
                }
            }
        }
    }
}

@Composable
fun ColorPickerPanelDemo() {
    DemoPage(title = "ColorPickerPanel", description = "Core panel component of ColorPicker.") {
        DemoSection("Basic usage") {
            var value by remember { mutableStateOf("#409EFF") }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusColorPickerPanel(
                    value = value,
                    onValueChange = { value = it },
                    modifier = Modifier.width(280.dp),
                )
                NexusText(
                    text = "Current: $value",
                    style = NexusTheme.typography.small
                )
            }
        }

        DemoSection("Alpha") {
            var value by remember { mutableStateOf("rgba(64, 158, 255, 0.50)") }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusColorPickerPanel(
                    value = value,
                    onValueChange = { value = it },
                    showAlpha = true,
                    colorFormat = NexusColorFormat.Rgb,
                    modifier = Modifier.width(280.dp),
                )
                NexusText(
                    text = "Current: $value",
                    style = NexusTheme.typography.small
                )
            }
        }

        DemoSection("Predefined colors") {
            var value by remember { mutableStateOf("#67C23A") }
            NexusColorPickerPanel(
                value = value,
                onValueChange = { value = it },
                predefine = listOf(
                    "#409EFF",
                    "#67C23A",
                    "#E6A23C",
                    "#F56C6C",
                    "#909399",
                    "rgba(255, 0, 0, 0.5)",
                    "hsl(200, 100%, 50%)",
                ),
                showAlpha = true,
                colorFormat = NexusColorFormat.Hex8,
                modifier = Modifier.width(320.dp),
            )
        }

        DemoSection("Border") {
            var bordered by remember { mutableStateOf("#409EFF") }
            var borderless by remember { mutableStateOf("#67C23A") }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusColorPickerPanel(
                    value = bordered,
                    onValueChange = { bordered = it },
                    border = true,
                    modifier = Modifier.width(260.dp),
                )
                NexusColorPickerPanel(
                    value = borderless,
                    onValueChange = { borderless = it },
                    border = false,
                    modifier = Modifier.width(260.dp),
                )
            }
        }

        DemoSection("Disabled") {
            var value by remember { mutableStateOf("#909399") }
            NexusColorPickerPanel(
                value = value,
                onValueChange = { value = it },
                disabled = true,
                showAlpha = true,
                modifier = Modifier.width(280.dp),
            )
        }
    }
}

@Composable
fun ColorPickerDemo() {
    DemoPage(title = "ColorPicker", description = "Color selector with popup panel.") {
        DemoSection("Basic usage") {
            var value by remember { mutableStateOf("#409EFF") }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusColorPicker(
                    value = value,
                    onValueChange = { value = it },
                )
                NexusText(
                    text = "Current: $value",
                    style = NexusTheme.typography.small
                )
            }
        }

        DemoSection("Alpha") {
            var value by remember { mutableStateOf("rgba(64, 158, 255, 0.75)") }
            NexusColorPicker(
                value = value,
                onValueChange = { value = it },
                showAlpha = true,
                colorFormat = NexusColorFormat.Rgb,
            )
        }

        DemoSection("Predefined colors") {
            var value by remember { mutableStateOf("#67C23A") }
            NexusColorPicker(
                value = value,
                onValueChange = { value = it },
                showAlpha = true,
                colorFormat = NexusColorFormat.Hex8,
                predefine = listOf(
                    "#409EFF",
                    "#67C23A",
                    "#E6A23C",
                    "#F56C6C",
                    "#909399",
                    "rgba(255, 0, 0, 0.5)",
                    "#00FFFF",
                ),
            )
        }

        DemoSection("Sizes") {
            var v1 by remember { mutableStateOf("#409EFF") }
            var v2 by remember { mutableStateOf("#67C23A") }
            var v3 by remember { mutableStateOf("#F56C6C") }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusColorPicker(
                    value = v1,
                    onValueChange = { v1 = it },
                    size = ComponentSize.Large
                )
                NexusColorPicker(
                    value = v2,
                    onValueChange = { v2 = it },
                    size = ComponentSize.Default
                )
                NexusColorPicker(
                    value = v3,
                    onValueChange = { v3 = it },
                    size = ComponentSize.Small
                )
            }
        }

        DemoSection("Disabled & clearable") {
            var active by remember { mutableStateOf("#909399") }
            var disabledValue by remember { mutableStateOf("#303133") }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NexusColorPicker(
                    value = active,
                    onValueChange = { active = it },
                    clearable = true,
                )
                NexusColorPicker(
                    value = disabledValue,
                    onValueChange = { disabledValue = it },
                    disabled = true,
                )
            }
        }
    }
}

@Composable
fun InputTagDemo() {
    DemoPage(title = "InputTag", description = "Manage a list of tags with an input.") {
        DemoSection("Basic usage") {
            val state = rememberInputTagState(initialTags = listOf("Kotlin", "Compose"))
            NexusInputTag(state = state, placeholder = "Enter tags, press Enter")
        }
        DemoSection("Custom trigger") {
            val state = rememberInputTagState(initialTags = listOf("Space"))
            NexusInputTag(
                state = state,
                trigger = NexusInputTagTrigger.Space,
                placeholder = "Press Space to add",
            )
        }
        DemoSection("Maximum tags") {
            val state = rememberInputTagState(initialTags = listOf("A", "B"))
            NexusInputTag(
                state = state,
                maxTags = 3,
                placeholder = "Max 3 tags",
            )
        }
        DemoSection("Collapse tags") {
            val state = rememberInputTagState(
                initialTags = listOf("Kotlin", "Compose", "Multiplatform", "Element"),
            )
            NexusInputTag(
                state = state,
                collapseTags = true,
                collapseTagsTooltip = true,
                maxCollapseTags = 1,
            )
        }
        DemoSection("Disabled") {
            val state = rememberInputTagState(initialTags = listOf("Disabled", "Tag"))
            NexusInputTag(
                state = state,
                disabled = true,
            )
        }
        DemoSection("Clearable / custom clear icon") {
            val state1 = rememberInputTagState(initialTags = listOf("A", "B", "C"))
            val state2 = rememberInputTagState(initialTags = listOf("X", "Y"))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusInputTag(state = state1, clearable = true)
                NexusInputTag(
                    state = state2,
                    clearable = true,
                    clearIcon = {
                        NexusText(
                            text = "⊗",
                            color = NexusTheme.colorScheme.danger.base
                        )
                    },
                )
            }
        }
        DemoSection("Draggable") {
            val state = rememberInputTagState(initialTags = listOf("One", "Two", "Three"))
            NexusInputTag(
                state = state,
                draggable = true,
                suffix = {
                    NexusText(
                        text = "Click tag to reorder",
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.placeholder,
                    )
                },
            )
        }
        DemoSection("Delimiter") {
            val state = rememberInputTagState()
            NexusInputTag(
                state = state,
                delimiter = ",",
                placeholder = "Input then type ',' to split",
            )
        }
        DemoSection("Sizes") {
            val large = rememberInputTagState(initialTags = listOf("Large"))
            val def = rememberInputTagState(initialTags = listOf("Default"))
            val small = rememberInputTagState(initialTags = listOf("Small"))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                NexusInputTag(state = large, size = ComponentSize.Large)
                NexusInputTag(state = def, size = ComponentSize.Default)
                NexusInputTag(state = small, size = ComponentSize.Small)
            }
        }
        DemoSection("Custom tag") {
            val state = rememberInputTagState(initialTags = listOf("alpha", "beta"))
            NexusInputTag(
                state = state,
                tagContent = { value, _ ->
                    NexusTag(
                        text = value.uppercase(),
                        type = NexusType.Success,
                        effect = TagEffect.Dark,
                        closable = false,
                        size = ComponentSize.Small,
                    )
                },
            )
        }
        DemoSection("Custom prefix and suffix") {
            val state = rememberInputTagState(initialTags = listOf("core"))
            NexusInputTag(
                state = state,
                prefix = {
                    NexusText(
                        text = "🏷",
                        style = NexusTheme.typography.small
                    )
                },
                suffix = {
                    NexusText(
                        text = "Tag editor",
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.placeholder
                    )
                },
            )
        }
        DemoSection("Events") {
            val state = rememberInputTagState(initialTags = listOf("event"))
            var logs by remember { mutableStateOf(listOf("Logs:")) }
            fun push(msg: String) {
                logs = (listOf(msg) + logs).take(7)
            }
            NexusInputTag(
                state = state,
                clearable = true,
                onTagsChange = { push("change: ${it.joinToString(",")}") },
                onInput = { push("input: $it") },
                onAddTag = { push("add-tag: $it") },
                onRemoveTag = { v, i -> push("remove-tag[$i]: $v") },
                onDragTag = { old, new, v -> push("drag-tag: $v $old->$new") },
                onFocus = { push("focus") },
                onBlur = { push("blur") },
                onClear = { push("clear") },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                logs.forEach { line ->
                    NexusText(
                        text = line,
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                }
            }
        }
    }
}

@Composable
fun MentionDemo() {
    DemoPage(title = "Mention", description = "Used to mention someone or something in an input.") {
        DemoSection("Basic usage") {
            val state = rememberMentionState()
            NexusMention(
                state = state,
                placeholder = "Type @",
                options = listOf(
                    MentionOption(value = "alice", label = "Alice"),
                    MentionOption(value = "bob", label = "Bob"),
                    MentionOption(value = "charlie", label = "Charlie"),
                ),
            )
        }
        DemoSection("Props") {
            val state = rememberMentionState()
            val options = listOf(
                MentionOption(payload = mapOf("id" to "u100", "name" to "User 100", "inactive" to false)),
                MentionOption(payload = mapOf("id" to "u101", "name" to "User 101", "inactive" to true)),
            )
            NexusMention(
                state = state,
                options = options,
                props = MentionOptionProps(value = "id", label = "name", disabled = "inactive"),
                placeholder = "Custom props mapping, type @",
            )
        }
        DemoSection("Textarea") {
            val state = rememberMentionState()
            NexusMention(
                state = state,
                type = NexusInputType.Textarea,
                placeholder = "Textarea mention, type @",
                options = listOf(
                    MentionOption(value = "compose"),
                    MentionOption(value = "kotlin"),
                    MentionOption(value = "nexus"),
                ),
            )
        }
        DemoSection("Customize label") {
            val state = rememberMentionState()
            NexusMention(
                state = state,
                placeholder = "Type @",
                options = listOf(
                    MentionOption(value = "frontend", label = "Frontend"),
                    MentionOption(value = "backend", label = "Backend"),
                ),
                labelContent = { item, _ ->
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        NexusText(text = item.label ?: item.value.orEmpty())
                        NexusText(
                            text = item.value.orEmpty(),
                            style = NexusTheme.typography.extraSmall,
                            color = NexusTheme.colorScheme.text.placeholder,
                        )
                    }
                },
            )
        }
        DemoSection("Load remote options") {
            val state = rememberMentionState()
            var loading by remember { mutableStateOf(false) }
            var options by remember { mutableStateOf(listOf<MentionOption>()) }
            NexusMention(
                state = state,
                options = options,
                loading = loading,
                placeholder = "Type @a / @b ...",
                onSearch = { pattern, _ ->
                    loading = true
                    options = listOf(
                        MentionOption(value = "${pattern}01"),
                        MentionOption(value = "${pattern}02"),
                        MentionOption(value = "${pattern}03"),
                    )
                    loading = false
                },
            )
        }
        DemoSection("Customize trigger token") {
            val state = rememberMentionState()
            NexusMention(
                state = state,
                prefix = listOf("@", "#"),
                placeholder = "Type @ or #",
                options = listOf(
                    MentionOption(value = "topic"),
                    MentionOption(value = "todo"),
                    MentionOption(value = "teammate"),
                ),
            )
        }
        DemoSection("Delete as a whole") {
            val state = rememberMentionState(initialValue = "@alice ")
            var log by remember { mutableStateOf("Press Backspace at end") }
            NexusMention(
                state = state,
                whole = true,
                options = listOf(
                    MentionOption(value = "alice"),
                    MentionOption(value = "bob"),
                ),
                onWholeRemove = { pattern, prefix ->
                    log = "whole-remove: $prefix$pattern"
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            NexusText(
                text = log,
                style = NexusTheme.typography.extraSmall,
                color = NexusTheme.colorScheme.text.secondary
            )
        }
        DemoSection("Work with form") {
            val formState = rememberNexusFormState(initialModel = mapOf("mention" to ""))
            val rules = remember {
                mapOf(
                    "mention" to listOf(
                        NexusFormRule(required = true, message = "Please mention someone"),
                    )
                )
            }
            NexusForm(state = formState, rules = rules, labelWidth = 100.dp) {
                NexusFormItem(label = "Mention", prop = "mention") {
                    val mentionState = rememberMentionState()
                    NexusMention(
                        state = mentionState,
                        placeholder = "Type @",
                        options = listOf(
                            MentionOption(value = "compose"),
                            MentionOption(value = "element"),
                        ),
                        onInput = { formState.setFieldValue("mention", it) },
                    )
                }
                NexusFormItem(label = "") {
                    NexusButton(
                        text = "Validate",
                        onClick = { formState.validate(rules) },
                        type = NexusType.Primary,
                    )
                }
            }
        }
    }
}

@Composable
fun CascaderDemo() {
    DemoPage(title = "Cascader", description = "If the options have a clear hierarchical structure, cascader can be used.") {
        DemoSection("Basic usage") {
            val options = listOf(
                CascaderOption(
                    "asia", "Asia", listOf(
                        CascaderOption(
                            "china", "China", listOf(
                                CascaderOption("bj", "Beijing"),
                                CascaderOption("sh", "Shanghai"),
                            )
                        ),
                        CascaderOption("japan", "Japan"),
                    )
                ),
                CascaderOption(
                    "europe", "Europe", listOf(
                        CascaderOption("france", "France"),
                        CascaderOption("germany", "Germany"),
                    )
                ),
            )
            val state = rememberCascaderState(options = options)
            NexusCascader(state = state, placeholder = "Select region")
        }
        DemoSection("Disabled option") {
            val options = listOf(
                CascaderOption("guide", "Guide", disabled = true),
                CascaderOption(
                    "component", "Component", listOf(
                        CascaderOption("basic", "Basic"),
                        CascaderOption("form", "Form"),
                    )
                ),
            )
            val state = rememberCascaderState(options = options)
            NexusCascader(state = state, placeholder = "Disabled option demo")
        }
        DemoSection("Clearable") {
            val options = listOf(
                CascaderOption(
                    "frontend", "Frontend", listOf(
                        CascaderOption("vue", "Vue"),
                        CascaderOption("react", "React"),
                    )
                ),
                CascaderOption(
                    "backend", "Backend", listOf(
                        CascaderOption("spring", "Spring"),
                        CascaderOption("ktor", "Ktor"),
                    )
                ),
            )
            val state = rememberCascaderState(options = options)
            NexusCascader(
                state = state,
                placeholder = "Select and clear",
                clearable = true,
            )
        }
        DemoSection("Display only last level") {
            val options = listOf(
                CascaderOption(
                    "continent", "Continent", listOf(
                        CascaderOption(
                            "asia", "Asia", listOf(
                                CascaderOption("china", "China"),
                                CascaderOption("japan", "Japan"),
                            )
                        ),
                    )
                ),
            )
            val state = rememberCascaderState(options = options)
            NexusCascader(
                state = state,
                placeholder = "Only last label shown",
                showAllLevels = false,
            )
        }
        DemoSection("Custom header & footer") {
            val options = listOf(
                CascaderOption(
                    "lang", "Language", listOf(
                        CascaderOption("kotlin", "Kotlin"),
                        CascaderOption("typescript", "TypeScript"),
                    )
                ),
                CascaderOption(
                    "framework", "Framework", listOf(
                        CascaderOption("compose", "Compose"),
                        CascaderOption("vue", "Vue"),
                    )
                ),
            )
            val state = rememberCascaderState(options = options)
            NexusCascader(
                state = state,
                placeholder = "With header and footer",
                header = {
                    NexusText(
                        text = "Select one path",
                        style = NexusTheme.typography.small,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                },
                footer = {
                    NexusText(
                        text = "Tip: leaf node closes dropdown",
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.placeholder,
                    )
                },
            )
        }
    }
}

@Composable
fun TransferDemo() {
    DemoPage(title = "Transfer", description = "Move items between two columns.") {
        DemoSection("Basic usage") {
            val state = rememberTransferState(
                sourceItems = (1..6).map { TransferItem(it, "Item $it") },
            )
            NexusTransfer(
                state = state,
                sourceTitle = "Available",
                targetTitle = "Selected",
            )
        }

        DemoSection("Filterable") {
            val state = rememberTransferState(
                sourceItems = listOf(
                    TransferItem(1, "Shanghai"),
                    TransferItem(2, "Beijing"),
                    TransferItem(3, "Shenzhen"),
                    TransferItem(4, "Hangzhou"),
                    TransferItem(5, "Suzhou"),
                    TransferItem(6, "Chengdu", disabled = true),
                ),
            )
            NexusTransfer(
                state = state,
                filterable = true,
                filterPlaceholder = "Type city",
                filterMethod = { query, item ->
                    item.label.startsWith(query, ignoreCase = true)
                },
            )
        }

        DemoSection("Customizable") {
            val state = rememberTransferState(
                sourceItems = (1..8).map { TransferItem(it, "Option $it") },
                leftDefaultChecked = listOf(1, 2),
            )
            var logs by remember { mutableStateOf(listOf("Logs:")) }
            fun push(msg: String) {
                logs = (listOf(msg) + logs).take(8)
            }
            NexusTransfer(
                state = state,
                titles = "Source Data" to "Target Data",
                buttonTexts = "Add >" to "< Remove",
                targetOrder = TransferTargetOrder.Push,
                format = TransferFormat(
                    noChecked = "{checked}/{total}",
                    hasChecked = "{checked}/{total} checked",
                ),
                renderContent = { item ->
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        NexusText(text = item.label)
                        NexusTag(text = "#${item.key}", size = ComponentSize.Small)
                    }
                },
                leftFooter = {
                    NexusText(
                        text = "left footer",
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                },
                rightFooter = {
                    NexusText(
                        text = "right footer",
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                },
                onChange = { value, direction, moved ->
                    push("change: $direction moved=${moved.joinToString(",")} target=${value.joinToString(",")}")
                },
                onLeftCheckChange = { value, moved ->
                    push("left-check: ${value.joinToString(",")} moved=${moved.joinToString(",")}")
                },
                onRightCheckChange = { value, moved ->
                    push("right-check: ${value.joinToString(",")} moved=${moved.joinToString(",")}")
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                logs.forEach { line ->
                    NexusText(
                        text = line,
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                }
            }
        }

        DemoSection("Custom empty content") {
            val state = rememberTransferState(
                sourceItems = emptyList<TransferItem<Int>>(),
                targetItems = emptyList(),
            )
            NexusTransfer(
                state = state,
                filterable = true,
                leftEmpty = {
                    NexusText(
                        text = "No source data",
                        style = NexusTheme.typography.small,
                        color = NexusTheme.colorScheme.warning.base,
                    )
                },
                rightEmpty = {
                    NexusText(
                        text = "No target data",
                        style = NexusTheme.typography.small,
                        color = NexusTheme.colorScheme.primary.base,
                    )
                },
            )
        }

        DemoSection("Prop aliases") {
            val state = rememberTransferState(
                sourceItems = listOf(
                    TransferItem(
                        key = "a1",
                        label = "",
                        payload = mapOf("value" to "a1", "desc" to "Alpha", "inactive" to false),
                    ),
                    TransferItem(
                        key = "b1",
                        label = "",
                        payload = mapOf("value" to "b1", "desc" to "Beta", "inactive" to true),
                    ),
                    TransferItem(
                        key = "c1",
                        label = "",
                        payload = mapOf("value" to "c1", "desc" to "Gamma", "inactive" to false),
                    ),
                ),
            )
            NexusTransfer(
                state = state,
                props = TransferPropsAlias(
                    key = "value",
                    label = "desc",
                    disabled = "inactive",
                ),
                renderContent = { item ->
                    NexusText(text = item.payload["desc"]?.toString() ?: item.label)
                },
            )
        }
    }
}

@Composable
fun TreeSelectDemo() {
    val baseNodes = remember {
        listOf(
            TreeNode(
                key = "guide",
                label = "Guide",
                children = listOf(
                    TreeNode("design", "Design Principles"),
                    TreeNode("nav", "Navigation"),
                ),
            ),
            TreeNode(
                key = "component",
                label = "Component",
                children = listOf(
                    TreeNode("basic", "Basic"),
                    TreeNode("form", "Form"),
                    TreeNode("data", "Data"),
                ),
            ),
            TreeNode("resource", "Resource"),
        )
    }

    DemoPage(title = "TreeSelect", description = "Tree selector combining Select and Tree behavior.") {
        DemoSection("Basic usage") {
            val state = rememberTreeSelectState(nodes = baseNodes)
            NexusTreeSelect(
                state = state,
                placeholder = "Select node",
            )
        }

        DemoSection("Select any level") {
            val state = rememberTreeSelectState(nodes = baseNodes)
            NexusTreeSelect(
                state = state,
                checkStrictly = true,
                placeholder = "Any level can be selected",
            )
        }

        DemoSection("Multiple Selection") {
            val state = rememberTreeSelectState(nodes = baseNodes)
            NexusTreeSelect(
                state = state,
                multiple = true,
                showCheckbox = true,
                checkStrictly = true,
                placeholder = "Select multiple nodes",
            )
        }

        DemoSection("Disabled Selection") {
            val nodes = remember {
                listOf(
                    TreeNode(
                        "a",
                        "A",
                        children = listOf(TreeNode("a-1", "A-1", disabled = true))
                    ),
                    TreeNode("b", "B", disabled = true),
                    TreeNode("c", "C"),
                )
            }
            val state = rememberTreeSelectState(nodes = nodes)
            NexusTreeSelect(
                state = state,
                showCheckbox = true,
                multiple = true,
                checkStrictly = true,
                placeholder = "Disabled nodes cannot be selected",
            )
        }

        DemoSection("Filterable") {
            val state = rememberTreeSelectState(nodes = baseNodes)
            NexusTreeSelect(
                state = state,
                filterable = true,
                filterMethod = { query, node ->
                    node.label.startsWith(query, ignoreCase = true)
                },
                placeholder = "Type to filter tree nodes",
            )
        }

        DemoSection("Custom content") {
            val state = rememberTreeSelectState(nodes = baseNodes)
            NexusTreeSelect(
                state = state,
                showCheckbox = true,
                checkStrictly = true,
                nodeContent = { node, checked ->
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        NexusText(text = node.label)
                        if (checked) {
                            NexusTag(
                                text = "Selected",
                                size = ComponentSize.Small,
                                type = NexusType.Success,
                            )
                        }
                    }
                },
            )
        }

        DemoSection("Use node-key / cache-data") {
            val state = rememberTreeSelectState(nodes = emptyList<TreeNode<String>>())
            val cacheData = remember {
                listOf(
                    TreeNode("remote-1", "Remote Node 1"),
                    TreeNode("remote-2", "Remote Node 2"),
                )
            }
            remember { state.selectSingle("remote-2") }
            NexusTreeSelect(
                state = state,
                cacheData = cacheData,
                placeholder = "Label from cache-data",
            )
        }
    }
}

@Composable
fun UploadDemo() {
    DemoPage(title = "Upload", description = "Upload files by clicking or drag-and-drop.") {
        DemoSection("Basic Usage") {
            val state = rememberUploadState()
            var counter by remember { mutableStateOf(1) }
            NexusUpload(
                state = state,
                multiple = true,
                limit = 3,
                onSelectFiles = {
                    val file = NexusUploadRawFile(
                        name = "doc-${counter++}.txt",
                        size = 8_192L,
                        type = "text/plain",
                    )
                    listOf(file)
                },
                onExceed = { _, _ -> },
                tip = {
                    NexusText(
                        text = "Mock uploader: max 3 files",
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                },
            )
        }

        DemoSection("Cover Previous File") {
            val state = rememberUploadState()
            var counter by remember { mutableStateOf(1) }
            NexusUpload(
                state = state,
                limit = 1,
                onSelectFiles = {
                    listOf(
                        NexusUploadRawFile(
                            name = "avatar-${counter++}.png",
                            size = 64_000L,
                            type = "image/png",
                        ),
                    )
                },
                onExceed = { files, _ ->
                    state.clearFiles()
                    files.firstOrNull()?.let { state.handleStart(it) }
                },
            )
        }

        DemoSection("User Avatar") {
            val state = rememberUploadState()
            var counter by remember { mutableStateOf(1) }
            NexusUpload(
                state = state,
                listType = NexusUploadListType.PictureCard,
                limit = 1,
                onSelectFiles = {
                    listOf(
                        NexusUploadRawFile(
                            name = "avatar-${counter++}.jpg",
                            size = 120_000L,
                            type = "image/jpeg",
                        ),
                    )
                },
                beforeUpload = { raw ->
                    raw.type.startsWith("image/") && raw.size <= 2 * 1024 * 1024
                },
            )
        }

        DemoSection("Photo Wall / Custom Thumbnail") {
            val state = rememberUploadState()
            var counter by remember { mutableStateOf(1) }
            NexusUpload(
                state = state,
                multiple = true,
                listType = NexusUploadListType.PictureCard,
                onSelectFiles = {
                    listOf(
                        NexusUploadRawFile("photo-${counter++}.png", 320_000L, "image/png"),
                    )
                },
                fileContent = { file, _ ->
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        NexusText(
                            text = file.name,
                            style = NexusTheme.typography.extraSmall
                        )
                        NexusTag(
                            text = file.status.name,
                            size = ComponentSize.Small,
                            type = if (file.status == NexusUploadStatus.Success) NexusType.Success else NexusType.Info,
                        )
                    }
                },
            )
        }

        DemoSection("File List with Thumbnail / File List Control") {
            val state = rememberUploadState()
            var counter by remember { mutableStateOf(1) }
            var logs by remember { mutableStateOf(listOf("Logs:")) }
            fun push(msg: String) {
                logs = (listOf(msg) + logs).take(6)
            }
            NexusUpload(
                state = state,
                listType = NexusUploadListType.Picture,
                multiple = true,
                onSelectFiles = {
                    listOf(
                        NexusUploadRawFile("thumb-${counter++}.jpg", 88_000L, "image/jpeg"),
                    )
                },
                onChange = { file, files ->
                    push("change: ${file.name}, total=${files.size}")
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                logs.forEach { line ->
                    NexusText(
                        text = line,
                        style = NexusTheme.typography.extraSmall,
                        color = NexusTheme.colorScheme.text.secondary,
                    )
                }
            }
        }

        DemoSection("Drag to Upload / Upload Directory") {
            val state = rememberUploadState()
            var counter by remember { mutableStateOf(1) }
            NexusUpload(
                state = state,
                drag = true,
                directory = true,
                multiple = true,
                onSelectFiles = {
                    listOf(
                        NexusUploadRawFile("folder-${counter}/a.txt", 2_048L, "text/plain", isDirectory = true),
                        NexusUploadRawFile("folder-${counter}/b.txt", 2_048L, "text/plain", isDirectory = true),
                    ).also { counter++ }
                },
            )
        }

        DemoSection("Manual Upload") {
            val state = rememberUploadState()
            var counter by remember { mutableStateOf(1) }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                NexusUpload(
                    state = state,
                    autoUpload = false,
                    multiple = true,
                    onSelectFiles = {
                        listOf(NexusUploadRawFile("manual-${counter++}.txt", 4_096L, "text/plain"))
                    },
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NexusButton(
                        text = "Submit",
                        onClick = { state.submit() },
                        type = NexusType.Primary,
                        size = ComponentSize.Small,
                    )
                    NexusButton(
                        text = "Clear",
                        onClick = { state.clearFiles() },
                        size = ComponentSize.Small,
                    )
                }
            }
        }
    }
}
