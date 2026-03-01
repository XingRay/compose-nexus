package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * SearchBar state holder.
 */
@Stable
class SearchBarState(
    initialQuery: String = "",
) {
    var query by mutableStateOf(initialQuery)
    var isDropdownOpen by mutableStateOf(false)
        internal set
}

@Composable
fun rememberSearchBarState(
    initialQuery: String = "",
): SearchBarState = remember { SearchBarState(initialQuery) }

/**
 * SearchBar — a search input with button and optional suggestion dropdown.
 *
 * @param state SearchBar state.
 * @param modifier Modifier.
 * @param placeholder Input placeholder text.
 * @param size Component size.
 * @param suggestions List of suggestion strings to show in a dropdown.
 * @param onSearch Callback when search is triggered (button click or Enter).
 * @param onSuggestionSelect Callback when a suggestion is selected.
 */
@Composable
fun NexusSearchBar(
    state: SearchBarState = rememberSearchBarState(),
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    size: ComponentSize = ComponentSize.Default,
    suggestions: List<String> = emptyList(),
    onSearch: ((String) -> Unit)? = null,
    onSuggestionSelect: ((String) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows
    val typography = NexusTheme.typography

    val filteredSuggestions = if (state.query.isNotEmpty()) {
        suggestions.filter { it.contains(state.query, ignoreCase = true) }
    } else {
        emptyList()
    }

    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NexusInput(
                value = state.query,
                onValueChange = {
                    state.query = it
                    state.isDropdownOpen = it.isNotEmpty() && filteredSuggestions.isNotEmpty()
                },
                modifier = Modifier.weight(1f),
                placeholder = placeholder,
                size = size,
                clearable = true,
            )
            NexusButton(
                onClick = {
                    state.isDropdownOpen = false
                    onSearch?.invoke(state.query)
                },
                type = io.github.xingray.compose_nexus.theme.NexusType.Primary,
                size = size,
            ) {
                NexusText(text = "Search")
            }
        }

        // Suggestions dropdown
        if (state.isDropdownOpen && filteredSuggestions.isNotEmpty()) {
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
                    filteredSuggestions.forEach { suggestion ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    state.query = suggestion
                                    state.isDropdownOpen = false
                                    onSuggestionSelect?.invoke(suggestion)
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
