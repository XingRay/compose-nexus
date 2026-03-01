package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Mention candidate item.
 */
data class MentionOption(
    val value: String,
    val label: String = value,
)

/**
 * Mention state holder.
 */
@Stable
class MentionState(
    initialValue: String = "",
) {
    var value by mutableStateOf(initialValue)
    var isOpen by mutableStateOf(false)
        internal set
    internal var mentionQuery by mutableStateOf("")
    internal var cursorPosition by mutableStateOf(0)
}

@Composable
fun rememberMentionState(
    initialValue: String = "",
): MentionState = remember { MentionState(initialValue) }

/**
 * Element Plus Mention — a textarea/input that triggers a mention dropdown when '@' is typed.
 *
 * @param state Mention state.
 * @param options Available mention candidates.
 * @param modifier Modifier.
 * @param placeholder Placeholder text.
 * @param trigger Trigger character (default '@').
 * @param onSelect Callback when a mention is selected.
 */
@Composable
fun NexusMention(
    state: MentionState = rememberMentionState(),
    options: List<MentionOption> = emptyList(),
    modifier: Modifier = Modifier,
    placeholder: String = "",
    trigger: Char = '@',
    onSelect: ((MentionOption) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows

    // Detect mention trigger
    val triggerIndex = state.value.lastIndexOf(trigger)
    val mentionActive = triggerIndex >= 0 &&
            !state.value.substring(triggerIndex + 1).contains(' ')

    val query = if (mentionActive) {
        state.value.substring(triggerIndex + 1)
    } else ""

    val filteredOptions = if (mentionActive && query.isNotEmpty()) {
        options.filter { it.label.contains(query, ignoreCase = true) }
    } else if (mentionActive) {
        options
    } else {
        emptyList()
    }

    state.isOpen = mentionActive && filteredOptions.isNotEmpty()

    Column(modifier = modifier) {
        NexusTextarea(
            value = state.value,
            onValueChange = { state.value = it },
            placeholder = placeholder,
            modifier = Modifier.fillMaxWidth(),
        )

        if (state.isOpen) {
            Popup(
                alignment = Alignment.TopStart,
                properties = PopupProperties(focusable = false),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(shadows.light.elevation, shapes.base)
                        .clip(shapes.base)
                        .background(colorScheme.fill.blank)
                        .heightIn(max = 200.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    filteredOptions.forEach { option ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Replace the @query with @value
                                    val before = state.value.substring(0, triggerIndex)
                                    val after = if (state.value.length > triggerIndex + query.length + 1) {
                                        state.value.substring(triggerIndex + query.length + 1)
                                    } else ""
                                    state.value = "$before$trigger${option.value} $after"
                                    state.isOpen = false
                                    onSelect?.invoke(option)
                                }
                                .pointerHoverIcon(PointerIcon.Hand)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        ) {
                            NexusText(
                                text = option.label,
                                color = colorScheme.text.regular,
                                style = typography.base,
                            )
                        }
                    }
                }
            }
        }
    }
}
