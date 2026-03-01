package io.github.xingray.compose_nexus.sample.demos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.controls.*
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusType

@Composable
fun InputDemo() {
    DemoPage(title = "Input", description = "Input data using keyboard.") {
        DemoSection("Basic usage") {
            var v1 by remember { mutableStateOf("") }
            NexusInput(value = v1, onValueChange = { v1 = it }, placeholder = "Please input")
        }
        DemoSection("Clearable") {
            var v2 by remember { mutableStateOf("clearable text") }
            NexusInput(value = v2, onValueChange = { v2 = it }, clearable = true, placeholder = "Clearable")
        }
        DemoSection("Password") {
            var v3 by remember { mutableStateOf("") }
            NexusInput(value = v3, onValueChange = { v3 = it }, showPassword = true, placeholder = "Password")
        }
        DemoSection("Sizes") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                var v by remember { mutableStateOf("") }
                NexusInput(value = v, onValueChange = { v = it }, size = ComponentSize.Large, placeholder = "Large")
                NexusInput(value = v, onValueChange = { v = it }, size = ComponentSize.Default, placeholder = "Default")
                NexusInput(value = v, onValueChange = { v = it }, size = ComponentSize.Small, placeholder = "Small")
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
        DemoSection("Step & precision") {
            var v by remember { mutableDoubleStateOf(5.0) }
            NexusInputNumber(value = v, onValueChange = { v = it }, step = 0.5, precision = 1, min = 0.0, max = 20.0)
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
    }
}

@Composable
fun RadioDemo() {
    DemoPage(title = "Radio", description = "Single selection among multiple options.") {
        DemoSection("Basic usage") {
            var sel by remember { mutableStateOf("A") }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("A", "B", "C").forEach { option ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusRadio(selected = sel == option, onClick = { sel = option })
                        NexusText(text = "Option $option")
                    }
                }
            }
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
        DemoSection("Disabled") {
            NexusSwitch(checked = true, onCheckedChange = {}, disabled = true)
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
            NexusSelect(state = state, options = options, onSelect = {}, placeholder = "Select an option")
        }
    }
}

@Composable
fun SliderDemo() {
    DemoPage(title = "Slider", description = "Drag the slider within a fixed range.") {
        DemoSection("Basic usage") {
            var v by remember { mutableFloatStateOf(30f) }
            NexusSlider(value = v, onValueChange = { v = it })
        }
        DemoSection("With step") {
            var v by remember { mutableFloatStateOf(50f) }
            NexusSlider(value = v, onValueChange = { v = it }, step = 10f)
        }
    }
}

@Composable
fun DatePickerDemo() {
    DemoPage(title = "DatePicker", description = "A date picker with calendar panel.") {
        DemoSection("Basic usage") {
            val state = rememberDatePickerState()
            NexusDatePicker(state = state, placeholder = "Pick a date")
        }
    }
}

@Composable
fun TimePickerDemo() {
    DemoPage(title = "TimePicker", description = "A time picker with hour/minute/second columns.") {
        DemoSection("Basic usage") {
            val state = rememberTimePickerState()
            NexusTimePicker(state = state, placeholder = "Pick a time")
        }
        DemoSection("With seconds") {
            val state = rememberTimePickerState()
            NexusTimePicker(state = state, showSeconds = true, placeholder = "HH:MM:SS")
        }
    }
}

@Composable
fun FormDemo() {
    DemoPage(title = "Form", description = "Form consists of Input, Radio, Select, Checkbox and so on.") {
        DemoSection("Basic form") {
            var name by remember { mutableStateOf("") }
            var agree by remember { mutableStateOf(false) }
            NexusForm {
                NexusFormItem(label = "Name", required = true) {
                    NexusInput(value = name, onValueChange = { name = it }, placeholder = "Enter name")
                }
                NexusFormItem(label = "Agreement") {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NexusCheckbox(checked = agree, onCheckedChange = { agree = it })
                        NexusText(text = "I agree to the terms")
                    }
                }
                NexusFormItem(label = "") {
                    NexusButton(onClick = {}, type = NexusType.Primary) { NexusText(text = "Submit") }
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
    }
}

@Composable
fun InputTagDemo() {
    DemoPage(title = "InputTag", description = "Manage a list of tags with an input.") {
        DemoSection("Basic usage") {
            val state = rememberInputTagState(initialTags = listOf("Kotlin", "Compose"))
            NexusInputTag(state = state, placeholder = "Enter tags, press Enter")
        }
    }
}

@Composable
fun CascaderDemo() {
    DemoPage(title = "Cascader", description = "If the options have a clear hierarchical structure, cascader can be used.") {
        DemoSection("Basic usage") {
            val options = listOf(
                CascaderOption("asia", "Asia", listOf(
                    CascaderOption("china", "China", listOf(
                        CascaderOption("bj", "Beijing"),
                        CascaderOption("sh", "Shanghai"),
                    )),
                    CascaderOption("japan", "Japan"),
                )),
                CascaderOption("europe", "Europe", listOf(
                    CascaderOption("france", "France"),
                    CascaderOption("germany", "Germany"),
                )),
            )
            val state = rememberCascaderState(options = options)
            NexusCascader(state = state, placeholder = "Select region")
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
            NexusTransfer(state = state, sourceTitle = "Available", targetTitle = "Selected")
        }
    }
}
