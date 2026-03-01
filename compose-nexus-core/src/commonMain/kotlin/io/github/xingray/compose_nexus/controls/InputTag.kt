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
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * InputTag state holder.
 */
@Stable
class InputTagState(
    initialTags: List<String> = emptyList(),
) {
    val tags = mutableStateListOf<String>().apply { addAll(initialTags) }
    var inputText by mutableStateOf("")

    fun addTag(tag: String) {
        val trimmed = tag.trim()
        if (trimmed.isNotEmpty() && trimmed !in tags) {
            tags.add(trimmed)
        }
        inputText = ""
    }

    fun removeTag(index: Int) {
        if (index in tags.indices) {
            tags.removeAt(index)
        }
    }

    fun removeLastTag() {
        if (tags.isNotEmpty() && inputText.isEmpty()) {
            tags.removeAt(tags.lastIndex)
        }
    }
}

@Composable
fun rememberInputTagState(
    initialTags: List<String> = emptyList(),
): InputTagState = remember { InputTagState(initialTags) }

/**
 * Element Plus InputTag — an input that manages a list of tags.
 *
 * Type text and press Enter to add a tag. Press Backspace on empty input to remove the last tag.
 *
 * @param state InputTag state.
 * @param modifier Modifier.
 * @param placeholder Placeholder text when no tags and no input.
 * @param size Component size.
 * @param maxTags Maximum number of tags (0 = unlimited).
 * @param onTagsChange Callback when the tag list changes.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NexusInputTag(
    state: InputTagState = rememberInputTagState(),
    modifier: Modifier = Modifier,
    placeholder: String = "Enter tags",
    size: ComponentSize = ComponentSize.Default,
    maxTags: Int = 0,
    onTagsChange: ((List<String>) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    val minHeight = when (size) {
        ComponentSize.Large -> 40.dp
        ComponentSize.Default -> 32.dp
        ComponentSize.Small -> 24.dp
    }

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = minHeight)
            .clip(shapes.base)
            .border(1.dp, colorScheme.border.base, shapes.base)
            .background(colorScheme.fill.blank)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            // Existing tags
            state.tags.forEachIndexed { index, tag ->
                NexusTag(
                    text = tag,
                    closable = true,
                    size = ComponentSize.Small,
                    onClose = {
                        state.removeTag(index)
                        onTagsChange?.invoke(state.tags.toList())
                    },
                )
            }

            // Input field
            val canAddMore = maxTags <= 0 || state.tags.size < maxTags
            if (canAddMore) {
                BasicTextField(
                    value = state.inputText,
                    onValueChange = { state.inputText = it },
                    modifier = Modifier
                        .defaultMinSize(minWidth = 60.dp)
                        .height(minHeight - 8.dp)
                        .onKeyEvent { event ->
                            when (event.key) {
                                Key.Enter -> {
                                    state.addTag(state.inputText)
                                    onTagsChange?.invoke(state.tags.toList())
                                    true
                                }
                                Key.Backspace -> {
                                    if (state.inputText.isEmpty()) {
                                        state.removeLastTag()
                                        onTagsChange?.invoke(state.tags.toList())
                                        true
                                    } else false
                                }
                                else -> false
                            }
                        },
                    textStyle = TextStyle(
                        color = colorScheme.text.regular,
                        fontSize = typography.base.fontSize,
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            if (state.inputText.isEmpty() && state.tags.isEmpty()) {
                                NexusText(
                                    text = placeholder,
                                    color = colorScheme.text.placeholder,
                                    style = typography.base,
                                )
                            }
                            innerTextField()
                        }
                    },
                )
            }
        }
    }
}
