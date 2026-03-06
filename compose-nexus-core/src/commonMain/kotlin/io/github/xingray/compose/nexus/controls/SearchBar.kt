package io.github.xingray.compose.nexus.controls

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
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme

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
): io.github.xingray.compose.nexus.controls.SearchBarState = remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.SearchBarState(initialQuery) }

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
    state: io.github.xingray.compose.nexus.controls.SearchBarState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberSearchBarState(),
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    size: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    suggestions: List<String> = emptyList(),
    onSearch: ((String) -> Unit)? = null,
    onSuggestionSelect: ((String) -> Unit)? = null,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes
    val shadows = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shadows
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography

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
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusInput(
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
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                onClick = {
                    state.isDropdownOpen = false
                    onSearch?.invoke(state.query)
                },
                type = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary,
                size = size,
            ) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "Search")
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
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
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
