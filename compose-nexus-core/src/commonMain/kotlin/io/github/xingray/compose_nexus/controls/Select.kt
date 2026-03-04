package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme

data class SelectOption<T>(
    val value: T,
    val label: String,
    val disabled: Boolean = false,
    val options: List<SelectOption<T>> = emptyList(),
    val payload: Map<String, Any?> = emptyMap(),
)

data class SelectOptionProps(
    val value: String = "value",
    val label: String = "label",
    val options: String = "options",
    val disabled: String = "disabled",
)

private data class ResolvedSelectOption<T>(
    val value: T,
    val label: String,
    val disabled: Boolean,
    val group: String? = null,
    val raw: SelectOption<T>,
)

private sealed interface SelectDropdownRow<out T> {
    data class Group(val label: String) : SelectDropdownRow<Nothing>
    data class Option<T>(val option: ResolvedSelectOption<T>) : SelectDropdownRow<T>
    data class Create(val query: String) : SelectDropdownRow<Nothing>
}

@Stable
class SelectState<T>(
    initialSelected: T? = null,
    initialSelectedValues: List<T> = emptyList(),
) {
    var selected: T? by mutableStateOf(initialSelected)
    val selectedValues = mutableStateListOf<T>().apply { addAll(initialSelectedValues) }
    var expanded: Boolean by mutableStateOf(false)
    var query: String by mutableStateOf("")
}

@Composable
fun <T> rememberSelectState(
    initialSelected: T? = null,
    initialSelectedValues: List<T> = emptyList(),
): SelectState<T> = remember { SelectState(initialSelected, initialSelectedValues) }

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> NexusSelect(
    state: SelectState<T>,
    options: List<SelectOption<T>>,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    size: ComponentSize = ComponentSize.Default,
    placeholder: String = "Select",
    disabled: Boolean = false,
    clearable: Boolean = false,
    clearIcon: (@Composable () -> Unit)? = null,
    multiple: Boolean = false,
    multipleLimit: Int = 0,
    collapseTags: Boolean = false,
    collapseTagsTooltip: Boolean = false,
    maxCollapseTags: Int = 1,
    filterable: Boolean = false,
    filterMethod: ((String, SelectOption<T>) -> Boolean)? = null,
    remote: Boolean = false,
    remoteMethod: ((String) -> List<SelectOption<T>>)? = null,
    allowCreate: Boolean = false,
    defaultFirstOption: Boolean = false,
    reserveKeyword: Boolean = true,
    loading: Boolean = false,
    loadingText: String = "Loading",
    noMatchText: String = "No matching data",
    noDataText: String = "No data",
    props: SelectOptionProps = SelectOptionProps(),
    valueKey: ((T) -> Any?)? = null,
    height: Int = 274,
    itemHeight: Int = 34,
    fitInputWidth: Boolean = false,
    dropdownWidth: Dp? = null,
    virtualized: Boolean = false,
    emptyValues: Set<Any?> = setOf(null),
    valueOnClear: T? = null,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    prefix: (@Composable () -> Unit)? = null,
    empty: (@Composable () -> Unit)? = null,
    loadingContent: (@Composable () -> Unit)? = null,
    optionContent: (@Composable (SelectOption<T>, Boolean) -> Unit)? = null,
    tagContent: (@Composable (SelectOption<T>, Boolean, () -> Unit) -> Unit)? = null,
    labelContent: (@Composable (index: Int, label: String, value: T) -> Unit)? = null,
    createOption: ((String) -> SelectOption<T>)? = null,
    onChange: ((Any?) -> Unit)? = null,
    onVisibleChange: ((Boolean) -> Unit)? = null,
    onRemoveTag: ((T) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    val sizes = NexusTheme.sizes
    val typography = NexusTheme.typography
    val shadows = NexusTheme.shadows

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    var focused by remember { mutableStateOf(false) }
    var inputWidth by remember { mutableStateOf(280.dp) }
    val createdOptions = remember { mutableStateListOf<SelectOption<T>>() }

    val controlHeight: Dp = when (size) {
        ComponentSize.Large -> sizes.componentLarge
        ComponentSize.Default -> sizes.componentDefault
        ComponentSize.Small -> sizes.componentSmall
    }
    val horizontalPadding: Dp = when (size) {
        ComponentSize.Large -> sizes.inputPaddingLarge
        ComponentSize.Default -> sizes.inputPaddingDefault
        ComponentSize.Small -> sizes.inputPaddingSmall
    }
    val textStyle = when (size) {
        ComponentSize.Large -> typography.base
        ComponentSize.Default -> typography.base
        ComponentSize.Small -> typography.extraSmall
    }

    fun valueEquals(left: T, right: T): Boolean {
        return if (valueKey != null) valueKey(left) == valueKey(right) else left == right
    }

    fun resolveOption(option: SelectOption<T>, group: String? = null): List<ResolvedSelectOption<T>> {
        val payload = option.payload
        @Suppress("UNCHECKED_CAST")
        val mappedValue = payload[props.value] as? T ?: option.value
        val mappedLabel = payload[props.label]?.toString() ?: option.label
        val mappedDisabled = when (val disabledValue = payload[props.disabled]) {
            is Boolean -> disabledValue
            is String -> disabledValue.equals("true", ignoreCase = true)
            else -> option.disabled
        }
        @Suppress("UNCHECKED_CAST")
        val mappedChildren = (payload[props.options] as? List<SelectOption<T>>) ?: option.options

        return if (mappedChildren.isNotEmpty()) {
            mappedChildren.flatMap { child ->
                resolveOption(child, group = mappedLabel)
            }
        } else {
            listOf(
                ResolvedSelectOption(
                    value = mappedValue,
                    label = mappedLabel,
                    disabled = mappedDisabled,
                    group = group,
                    raw = option,
                ),
            )
        }
    }

    val sourceOptions = if (remote && filterable && remoteMethod != null) {
        remoteMethod(state.query)
    } else {
        options + createdOptions
    }
    val resolvedOptions = sourceOptions.flatMap { resolveOption(it) }

    val filteredOptions = when {
        remote -> resolvedOptions
        filterable && state.query.isNotEmpty() -> {
            resolvedOptions.filter { option ->
                filterMethod?.invoke(state.query, option.raw)
                    ?: option.label.contains(state.query, ignoreCase = true)
            }
        }
        else -> resolvedOptions
    }

    val selectedOption = state.selected?.let { current ->
        resolvedOptions.firstOrNull { valueEquals(it.value, current) }
    }
    val selectedLabel = selectedOption?.label
    val hasSingleSelection = state.selected != null && !emptyValues.contains(state.selected)
    val hasSelection = if (multiple) state.selectedValues.isNotEmpty() else hasSingleSelection
    val selectedOptions = if (multiple) {
        state.selectedValues.mapNotNull { value ->
            resolvedOptions.firstOrNull { valueEquals(it.value, value) }
        }
    } else {
        emptyList()
    }
    val visibleTags = if (collapseTags && selectedOptions.size > maxCollapseTags.coerceAtLeast(1)) {
        selectedOptions.take(maxCollapseTags.coerceAtLeast(1))
    } else {
        selectedOptions
    }
    val hiddenTagCount = (selectedOptions.size - visibleTags.size).coerceAtLeast(0)

    fun emitVisible(visible: Boolean) {
        if (state.expanded != visible) {
            state.expanded = visible
            onVisibleChange?.invoke(visible)
        } else {
            onVisibleChange?.invoke(visible)
        }
    }

    fun removeSelectedTag(value: T) {
        val idx = state.selectedValues.indexOfFirst { valueEquals(it, value) }
        if (idx >= 0) {
            state.selectedValues.removeAt(idx)
            onRemoveTag?.invoke(value)
            onChange?.invoke(state.selectedValues.toList())
        }
    }

    fun clearSelection() {
        if (multiple) {
            state.selectedValues.clear()
            onChange?.invoke(emptyList<T>())
        } else {
            state.selected = valueOnClear
            onChange?.invoke(valueOnClear)
        }
        if (!reserveKeyword) {
            state.query = ""
        }
        onClear?.invoke()
    }

    fun selectOption(option: ResolvedSelectOption<T>) {
        if (option.disabled) return
        if (multiple) {
            val exists = state.selectedValues.any { valueEquals(it, option.value) }
            if (exists) {
                removeSelectedTag(option.value)
            } else {
                if (multipleLimit == 0 || state.selectedValues.size < multipleLimit) {
                    state.selectedValues.add(option.value)
                    onSelect(option.value)
                    onChange?.invoke(state.selectedValues.toList())
                }
            }
            if (!reserveKeyword) state.query = ""
        } else {
            state.selected = option.value
            onSelect(option.value)
            onChange?.invoke(option.value)
            if (!reserveKeyword) state.query = ""
            emitVisible(false)
        }
    }

    fun createAndSelect(query: String) {
        if (!allowCreate || query.isBlank()) return
        val created = createOption ?: return
        val newOption = created(query.trim())
        if (createdOptions.none { valueEquals(it.value, newOption.value) }) {
            createdOptions.add(newOption)
        }
        selectOption(
            ResolvedSelectOption(
                value = newOption.value,
                label = newOption.label,
                disabled = newOption.disabled,
                raw = newOption,
            ),
        )
    }

    val showCreateOption = allowCreate && filterable && state.query.isNotBlank() &&
        filteredOptions.none { it.label.equals(state.query.trim(), ignoreCase = true) }
    val dropdownRows = buildList<SelectDropdownRow<T>> {
        var currentGroup: String? = null
        filteredOptions.forEach { option ->
            if (option.group != null && option.group != currentGroup) {
                currentGroup = option.group
                add(SelectDropdownRow.Group(currentGroup))
            }
            add(SelectDropdownRow.Option(option))
        }
        if (showCreateOption) {
            add(SelectDropdownRow.Create(state.query.trim()))
        }
    }

    val borderColor = when {
        disabled -> colorScheme.disabled.border
        state.expanded || focused -> colorScheme.primary.base
        isHovered -> colorScheme.border.dark
        else -> colorScheme.border.base
    }
    val bgColor = if (disabled) colorScheme.disabled.background else colorScheme.fill.blank
    val panelWidth = dropdownWidth ?: if (fitInputWidth) inputWidth else 280.dp

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    inputWidth = it.size.width.dp
                }
                .defaultMinSize(minHeight = controlHeight)
                .clip(shapes.base)
                .background(bgColor)
                .border(1.dp, borderColor, shapes.base)
                .then(
                    if (!disabled && !filterable) {
                        Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                            ) {
                                emitVisible(!state.expanded)
                                if (state.expanded) onFocus?.invoke() else onBlur?.invoke()
                            }
                            .pointerHoverIcon(PointerIcon.Hand)
                    } else {
                        Modifier
                    },
                )
                .padding(horizontal = horizontalPadding, vertical = if (multiple) 4.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (prefix != null) {
                prefix()
            }

            Box(modifier = Modifier.weight(1f)) {
                if (multiple) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        visibleTags.forEachIndexed { index, option ->
                            val removeAction = { removeSelectedTag(option.value) }
                            if (tagContent != null) {
                                tagContent(option.raw, disabled, removeAction)
                            } else {
                                NexusTag(
                                    text = option.label,
                                    size = ComponentSize.Small,
                                    type = io.github.xingray.compose_nexus.theme.NexusType.Info,
                                    closable = !disabled,
                                    onClose = { removeAction() },
                                )
                            }
                            if (labelContent != null && index < state.selectedValues.size) {
                                labelContent(index, option.label, option.value)
                            }
                        }
                        if (hiddenTagCount > 0) {
                            val text = if (collapseTagsTooltip) {
                                "+$hiddenTagCount (${selectedOptions.drop(visibleTags.size).joinToString(", ") { it.label }})"
                            } else {
                                "+$hiddenTagCount"
                            }
                            NexusTag(
                                text = text,
                                size = ComponentSize.Small,
                                type = io.github.xingray.compose_nexus.theme.NexusType.Info,
                                effect = TagEffect.Plain,
                            )
                        }
                        if (filterable && !disabled) {
                            BasicTextField(
                                value = state.query,
                                onValueChange = {
                                    state.query = it
                                    if (!state.expanded) emitVisible(true)
                                },
                                modifier = Modifier
                                    .defaultMinSize(minWidth = 60.dp)
                                    .onFocusChanged {
                                        focused = it.isFocused
                                        if (it.isFocused) {
                                            emitVisible(true)
                                            onFocus?.invoke()
                                        } else {
                                            onBlur?.invoke()
                                        }
                                    }
                                    .onPreviewKeyEvent { keyEvent ->
                                        if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                                            when {
                                                defaultFirstOption && filteredOptions.isNotEmpty() -> {
                                                    selectOption(filteredOptions.first())
                                                    true
                                                }
                                                showCreateOption -> {
                                                    createAndSelect(state.query)
                                                    true
                                                }
                                                else -> false
                                            }
                                        } else {
                                            false
                                        }
                                    },
                                textStyle = textStyle.merge(androidx.compose.ui.text.TextStyle(color = colorScheme.text.regular)),
                                singleLine = true,
                                enabled = !disabled,
                                cursorBrush = SolidColor(colorScheme.primary.base),
                                decorationBox = { inner ->
                                    if (state.selectedValues.isEmpty() && state.query.isEmpty()) {
                                        NexusText(
                                            text = placeholder,
                                            color = colorScheme.text.placeholder,
                                            style = textStyle,
                                        )
                                    }
                                    inner()
                                },
                            )
                        } else if (state.selectedValues.isEmpty()) {
                            NexusText(
                                text = placeholder,
                                color = colorScheme.text.placeholder,
                                style = textStyle,
                            )
                        }
                    }
                } else {
                    if (filterable && !disabled) {
                        BasicTextField(
                            value = if (state.expanded || state.query.isNotEmpty() || selectedLabel == null) state.query else selectedLabel,
                            onValueChange = {
                                state.query = it
                                if (!state.expanded) emitVisible(true)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged {
                                    focused = it.isFocused
                                    if (it.isFocused) {
                                        emitVisible(true)
                                        onFocus?.invoke()
                                    } else {
                                        onBlur?.invoke()
                                    }
                                }
                                .onPreviewKeyEvent { keyEvent ->
                                    if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                                        when {
                                            defaultFirstOption && filteredOptions.isNotEmpty() -> {
                                                selectOption(filteredOptions.first())
                                                true
                                            }
                                            showCreateOption -> {
                                                createAndSelect(state.query)
                                                true
                                            }
                                            else -> false
                                        }
                                    } else {
                                        false
                                    }
                                },
                            textStyle = textStyle.merge(androidx.compose.ui.text.TextStyle(color = colorScheme.text.regular)),
                            singleLine = true,
                            enabled = !disabled,
                            cursorBrush = SolidColor(colorScheme.primary.base),
                            decorationBox = { inner ->
                                if ((selectedLabel == null && state.query.isEmpty()) || (!state.expanded && selectedLabel == null)) {
                                    NexusText(
                                        text = placeholder,
                                        color = colorScheme.text.placeholder,
                                        style = textStyle,
                                    )
                                }
                                inner()
                            },
                        )
                    } else if (selectedOption != null && labelContent != null) {
                        labelContent(0, selectedOption.label, selectedOption.value)
                    } else {
                        NexusText(
                            text = selectedLabel ?: placeholder,
                            color = if (selectedLabel != null) colorScheme.text.regular else colorScheme.text.placeholder,
                            style = textStyle,
                        )
                    }
                }
            }

            if (clearable && hasSelection && !disabled) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) { clearSelection() },
                    contentAlignment = Alignment.Center,
                ) {
                    if (clearIcon != null) {
                        clearIcon()
                    } else {
                        NexusText(
                            text = "✕",
                            color = colorScheme.text.placeholder,
                            style = typography.extraSmall,
                        )
                    }
                }
            } else {
                NexusText(
                    text = if (state.expanded) "▲" else "▼",
                    color = colorScheme.text.placeholder,
                    style = typography.extraSmall,
                )
            }
        }

        if (state.expanded) {
            Popup(
                alignment = Alignment.TopStart,
                onDismissRequest = {
                    emitVisible(false)
                    onBlur?.invoke()
                },
                properties = PopupProperties(focusable = true),
            ) {
                Column(
                    modifier = Modifier
                        .width(panelWidth)
                        .padding(top = 4.dp)
                        .shadow(shadows.light.elevation, shapes.base)
                        .clip(shapes.base)
                        .background(colorScheme.fill.blank)
                        .border(1.dp, colorScheme.border.lighter, shapes.base),
                ) {
                    if (header != null) {
                        Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                            header()
                        }
                    }

                    @Composable
                    fun renderRow(row: SelectDropdownRow<T>) {
                        when (row) {
                            is SelectDropdownRow.Group -> {
                                NexusText(
                                    text = row.label,
                                    color = colorScheme.text.placeholder,
                                    style = typography.extraSmall,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                )
                            }
                            is SelectDropdownRow.Option -> {
                                val option = row.option
                                val optionInteraction = remember { MutableInteractionSource() }
                                val optionHovered by optionInteraction.collectIsHoveredAsState()
                                val isSelected = if (multiple) {
                                    state.selectedValues.any { valueEquals(it, option.value) }
                                } else {
                                    state.selected?.let { valueEquals(it, option.value) } == true
                                }
                                val optionBg = when {
                                    isSelected -> colorScheme.primary.light9
                                    optionHovered && !option.disabled -> colorScheme.fill.light
                                    else -> Color.Transparent
                                }
                                val optionTextColor = when {
                                    option.disabled -> colorScheme.disabled.text
                                    isSelected -> colorScheme.primary.base
                                    else -> colorScheme.text.regular
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .then(if (virtualized) Modifier.height(itemHeight.dp) else Modifier)
                                        .background(optionBg)
                                        .then(
                                            if (!option.disabled) {
                                                Modifier
                                                    .clickable(
                                                        interactionSource = optionInteraction,
                                                        indication = null,
                                                    ) { selectOption(option) }
                                                    .pointerHoverIcon(PointerIcon.Hand)
                                            } else {
                                                Modifier
                                            }
                                        )
                                        .padding(horizontal = horizontalPadding, vertical = 8.dp),
                                    contentAlignment = Alignment.CenterStart,
                                ) {
                                    if (optionContent != null) {
                                        optionContent(option.raw, isSelected)
                                    } else {
                                        NexusText(
                                            text = option.label,
                                            color = optionTextColor,
                                            style = textStyle,
                                        )
                                    }
                                }
                            }
                            is SelectDropdownRow.Create -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .then(if (virtualized) Modifier.height(itemHeight.dp) else Modifier)
                                        .clickable { createAndSelect(row.query) }
                                        .padding(horizontal = horizontalPadding, vertical = 8.dp),
                                    contentAlignment = Alignment.CenterStart,
                                ) {
                                    NexusText(
                                        text = "Create \"${row.query}\"",
                                        color = colorScheme.primary.base,
                                        style = textStyle,
                                    )
                                }
                            }
                        }
                    }

                    when {
                        loading -> {
                            Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                                if (loadingContent != null) {
                                    loadingContent()
                                } else {
                                    NexusText(
                                        text = loadingText,
                                        color = colorScheme.text.secondary,
                                        style = textStyle,
                                    )
                                }
                            }
                        }
                        dropdownRows.isEmpty() -> {
                            Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                                if (empty != null) {
                                    empty()
                                } else {
                                    NexusText(
                                        text = if (state.query.isNotEmpty()) noMatchText else noDataText,
                                        color = colorScheme.text.placeholder,
                                        style = textStyle,
                                    )
                                }
                            }
                        }
                        else -> {
                            if (virtualized) {
                                LazyColumn(
                                    modifier = Modifier.height(height.dp),
                                ) {
                                    items(dropdownRows) { row ->
                                        renderRow(row)
                                    }
                                }
                            } else {
                                Column(
                                    modifier = Modifier
                                        .heightIn(max = height.dp)
                                        .verticalScroll(rememberScrollState()),
                                ) {
                                    dropdownRows.forEach { row ->
                                        renderRow(row)
                                    }
                                }
                            }
                        }
                    }

                    if (footer != null) {
                        Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                            footer()
                        }
                    }
                }
            }
        }
    }
}
