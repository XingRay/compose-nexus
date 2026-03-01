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
import androidx.compose.runtime.LaunchedEffect
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
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import kotlinx.coroutines.delay

/**
 * Autocomplete state holder.
 */
@Stable
class AutocompleteState(
    initialValue: String = "",
) {
    var value by mutableStateOf(initialValue)
    var isOpen by mutableStateOf(false)
        internal set
    var suggestions by mutableStateOf(emptyList<String>())
        internal set
}

@Composable
fun rememberAutocompleteState(
    initialValue: String = "",
): AutocompleteState = remember { AutocompleteState(initialValue) }

/**
 * Element Plus Autocomplete — an input with suggestion dropdown based on user input.
 *
 * @param state Autocomplete state.
 * @param modifier Modifier.
 * @param placeholder Input placeholder.
 * @param size Component size.
 * @param debounceMs Debounce delay before fetching suggestions.
 * @param fetchSuggestions Suspending function that returns suggestions for the current query.
 * @param onSelect Callback when a suggestion is selected.
 */
@Composable
fun NexusAutocomplete(
    state: AutocompleteState = rememberAutocompleteState(),
    modifier: Modifier = Modifier,
    placeholder: String = "",
    size: ComponentSize = ComponentSize.Default,
    debounceMs: Long = 300L,
    fetchSuggestions: suspend (query: String) -> List<String> = { emptyList() },
    onSelect: ((String) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows

    // Debounced suggestion fetch
    LaunchedEffect(state.value) {
        if (state.value.isNotEmpty()) {
            delay(debounceMs)
            val results = fetchSuggestions(state.value)
            state.suggestions = results
            state.isOpen = results.isNotEmpty()
        } else {
            state.suggestions = emptyList()
            state.isOpen = false
        }
    }

    Column(modifier = modifier) {
        NexusInput(
            value = state.value,
            onValueChange = { state.value = it },
            placeholder = placeholder,
            size = size,
            clearable = true,
        )

        if (state.isOpen && state.suggestions.isNotEmpty()) {
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
                    state.suggestions.forEach { suggestion ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    state.value = suggestion
                                    state.isOpen = false
                                    onSelect?.invoke(suggestion)
                                }
                                .pointerHoverIcon(PointerIcon.Hand)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        ) {
                            NexusText(
                                text = suggestion,
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
