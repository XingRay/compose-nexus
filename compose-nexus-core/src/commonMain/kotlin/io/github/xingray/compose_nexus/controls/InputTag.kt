package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType

enum class NexusInputTagTrigger {
    Enter,
    Space,
}

@Stable
class InputTagState(
    initialTags: List<String> = emptyList(),
) {
    val tags = mutableStateListOf<String>().apply { addAll(initialTags) }
    var inputText by mutableStateOf("")

    fun addTag(tag: String): Boolean {
        val trimmed = tag.trim()
        if (trimmed.isNotEmpty() && trimmed !in tags) {
            tags.add(trimmed)
            inputText = ""
            return true
        }
        inputText = ""
        return false
    }

    fun addTags(values: List<String>): Boolean {
        var changed = false
        values.forEach { v ->
            changed = addTag(v) || changed
        }
        return changed
    }

    fun removeTag(index: Int): String? {
        if (index in tags.indices) {
            return tags.removeAt(index)
        }
        return null
    }

    fun removeLastTag(): String? {
        if (tags.isNotEmpty() && inputText.isEmpty()) {
            return tags.removeAt(tags.lastIndex)
        }
        return null
    }

    fun moveTag(oldIndex: Int, newIndex: Int): Boolean {
        if (oldIndex !in tags.indices || newIndex !in tags.indices || oldIndex == newIndex) return false
        val value = tags.removeAt(oldIndex)
        tags.add(newIndex, value)
        return true
    }

    fun clear() {
        tags.clear()
        inputText = ""
    }
}

@Composable
fun rememberInputTagState(
    initialTags: List<String> = emptyList(),
): InputTagState = remember { InputTagState(initialTags) }

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NexusInputTag(
    state: InputTagState = rememberInputTagState(),
    modifier: Modifier = Modifier,
    placeholder: String = "Enter tags",
    size: ComponentSize = ComponentSize.Default,
    maxTags: Int = 0,
    tagType: NexusType = NexusType.Info,
    tagEffect: TagEffect = TagEffect.Light,
    trigger: NexusInputTagTrigger = NexusInputTagTrigger.Enter,
    delimiter: String? = null,
    collapseTags: Boolean = false,
    collapseTagsTooltip: Boolean = false,
    maxCollapseTags: Int = 1,
    saveOnBlur: Boolean = true,
    clearable: Boolean = false,
    clearIcon: (@Composable () -> Unit)? = null,
    draggable: Boolean = false,
    disabled: Boolean = false,
    readonly: Boolean = false,
    minLength: Int = 0,
    maxLength: Int = 0,
    validateEvent: Boolean = true,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    tagContent: (@Composable (value: String, index: Int) -> Unit)? = null,
    onTagsChange: ((List<String>) -> Unit)? = null,
    onInput: ((String) -> Unit)? = null,
    onAddTag: ((String) -> Unit)? = null,
    onRemoveTag: ((String, Int) -> Unit)? = null,
    onDragTag: ((oldIndex: Int, newIndex: Int, value: String) -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    val minHeight = when (size) {
        ComponentSize.Large -> 40.dp
        ComponentSize.Default -> 32.dp
        ComponentSize.Small -> 24.dp
    }
    val inputTextStyle = when (size) {
        ComponentSize.Large -> typography.base
        ComponentSize.Default -> typography.base
        ComponentSize.Small -> typography.small
    }

    val canAddMore = maxTags <= 0 || state.tags.size < maxTags
    @Suppress("UNUSED_VARIABLE")
    val shouldTriggerValidate = validateEvent

    fun emitChange() {
        onTagsChange?.invoke(state.tags.toList())
    }

    fun tryAddTagFromText(raw: String) {
        val text = raw.trim()
        if (text.isEmpty() || !canAddMore || disabled || readonly) {
            state.inputText = ""
            return
        }
        if ((minLength > 0 && text.length < minLength) || (maxLength > 0 && text.length > maxLength)) {
            return
        }
        if (state.addTag(text)) {
            onAddTag?.invoke(text)
            emitChange()
        }
    }

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = minHeight)
            .clip(shapes.base)
            .border(1.dp, if (disabled) colorScheme.disabled.border else colorScheme.border.base, shapes.base)
            .background(if (disabled) colorScheme.disabled.background else colorScheme.fill.blank)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            if (prefix != null) {
                prefix()
            }

            val collapseLimit = maxCollapseTags.coerceAtLeast(1)
            val shouldCollapse = collapseTags && state.tags.size > collapseLimit
            val visibleTags = if (shouldCollapse) state.tags.take(collapseLimit) else state.tags.toList()

            visibleTags.forEachIndexed { index, tag ->
                val actualIndex = index
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = if (draggable && !disabled && !readonly) {
                        Modifier.clickable {
                            val oldIndex = actualIndex
                            val newIndex = if (oldIndex == state.tags.lastIndex) 0 else oldIndex + 1
                            if (state.moveTag(oldIndex, newIndex)) {
                                onDragTag?.invoke(oldIndex, newIndex, tag)
                                emitChange()
                            }
                        }
                    } else {
                        Modifier
                    },
                ) {
                    if (tagContent != null) {
                        tagContent(tag, actualIndex)
                    } else {
                        NexusTag(
                            text = tag,
                            type = tagType,
                            effect = tagEffect,
                            closable = !readonly && !disabled,
                            size = ComponentSize.Small,
                            onClose = {
                                val removed = state.removeTag(actualIndex)
                                if (removed != null) {
                                    onRemoveTag?.invoke(removed, actualIndex)
                                    emitChange()
                                }
                            },
                        )
                    }
                }
            }

            if (shouldCollapse) {
                val hiddenValues = state.tags.drop(collapseLimit)
                val hiddenCount = hiddenValues.size
                val collapseText = if (collapseTagsTooltip) {
                    "+$hiddenCount (${hiddenValues.joinToString(", ")})"
                } else {
                    "+$hiddenCount"
                }
                NexusTag(
                    text = collapseText,
                    type = NexusType.Info,
                    effect = TagEffect.Plain,
                    size = ComponentSize.Small,
                )
            }

            if (canAddMore && !disabled) {
                BasicTextField(
                    value = state.inputText,
                    onValueChange = { newText ->
                        val processed = if (maxLength > 0) newText.take(maxLength) else newText
                        state.inputText = processed
                        onInput?.invoke(processed)

                        if (!delimiter.isNullOrEmpty() && processed.contains(delimiter)) {
                            val parts = processed.split(delimiter)
                            val complete = parts.dropLast(1).map { it.trim() }.filter { it.isNotEmpty() }
                            val tail = parts.lastOrNull().orEmpty()
                            state.inputText = tail
                            complete.forEach { tryAddTagFromText(it) }
                        }
                    },
                    modifier = Modifier
                        .defaultMinSize(minWidth = 60.dp)
                        .height(minHeight - 8.dp)
                        .onFocusChanged {
                            if (it.isFocused) onFocus?.invoke() else {
                                if (saveOnBlur) {
                                    tryAddTagFromText(state.inputText)
                                }
                                onBlur?.invoke()
                            }
                        }
                        .onKeyEvent { event ->
                            when (event.key) {
                                Key.Enter -> {
                                    if (trigger == NexusInputTagTrigger.Enter) {
                                        tryAddTagFromText(state.inputText)
                                        true
                                    } else {
                                        false
                                    }
                                }
                                Key.Spacebar -> {
                                    if (trigger == NexusInputTagTrigger.Space) {
                                        tryAddTagFromText(state.inputText)
                                        true
                                    } else {
                                        false
                                    }
                                }
                                Key.Backspace -> {
                                    if (state.inputText.isEmpty()) {
                                        val removed = state.removeLastTag()
                                        if (removed != null) {
                                            onRemoveTag?.invoke(removed, state.tags.size)
                                            emitChange()
                                        }
                                        true
                                    } else false
                                }
                                else -> false
                            }
                        },
                    enabled = !readonly,
                    textStyle = TextStyle(
                        color = if (readonly) colorScheme.text.disabled else colorScheme.text.regular,
                        fontSize = inputTextStyle.fontSize,
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (state.inputText.isEmpty() && state.tags.isEmpty()) {
                                NexusText(
                                    text = placeholder,
                                    color = colorScheme.text.placeholder,
                                    style = inputTextStyle,
                                )
                            }
                            innerTextField()
                        }
                    },
                )
            }

            if (suffix != null) {
                suffix()
            }
        }

        if (clearable && state.tags.isNotEmpty() && !disabled && !readonly) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 2.dp)
                    .clickable {
                        state.clear()
                        onClear?.invoke()
                        emitChange()
                    },
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
        }
    }
}
