package io.github.xingray.compose.nexus.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.controls.NexusButton
import io.github.xingray.compose.nexus.controls.NexusDivider
import io.github.xingray.compose.nexus.controls.NexusInput
import io.github.xingray.compose.nexus.controls.NexusTag
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType

/**
 * A selectable item for [io.github.xingray.compose.nexus.templates.NexusSearchSelectPage].
 */
data class SearchSelectItem<T>(
    val key: T,
    val label: String,
)

/**
 * State holder for SearchSelectPage.
 */
@Stable
class SearchSelectPageState<T>(
    allItems: List<io.github.xingray.compose.nexus.templates.SearchSelectItem<T>>,
) {
    val allItems = allItems.toList()
    var query by mutableStateOf("")
    val selectedKeys = mutableStateListOf<T>()

    val filteredItems: List<io.github.xingray.compose.nexus.templates.SearchSelectItem<T>>
        get() = if (query.isBlank()) allItems
        else allItems.filter { it.label.contains(query, ignoreCase = true) }

    fun toggleSelect(key: T) {
        if (key in selectedKeys) selectedKeys.remove(key)
        else selectedKeys.add(key)
    }

    fun isSelected(key: T): Boolean = key in selectedKeys

    fun removeSelected(key: T) {
        selectedKeys.remove(key)
    }

    fun clearAll() {
        selectedKeys.clear()
    }

    fun selectedItems(): List<io.github.xingray.compose.nexus.templates.SearchSelectItem<T>> =
        allItems.filter { it.key in selectedKeys }
}

@Composable
fun <T> rememberSearchSelectPageState(
    allItems: List<io.github.xingray.compose.nexus.templates.SearchSelectItem<T>>,
): io.github.xingray.compose.nexus.templates.SearchSelectPageState<T> = remember(allItems) { _root_ide_package_.io.github.xingray.compose.nexus.templates.SearchSelectPageState(allItems) }

/**
 * SearchSelectPage — a full-page template for searching, selecting, and confirming items.
 *
 * Layout: SearchBar → Result list (selectable) → Selected tags → Confirm/Cancel buttons.
 *
 * @param T Item key type.
 * @param state Page state.
 * @param modifier Modifier.
 * @param title Page title.
 * @param onConfirm Callback with selected keys when confirmed.
 * @param onCancel Callback when cancelled.
 */
@Composable
fun <T> NexusSearchSelectPage(
    state: io.github.xingray.compose.nexus.templates.SearchSelectPageState<T>,
    modifier: Modifier = Modifier,
    title: String = "Search & Select",
    onConfirm: ((List<T>) -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background.page)
            .padding(20.dp),
    ) {
        // Title
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
            text = title,
            color = colorScheme.text.primary,
            style = typography.extraLarge,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search input
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusInput(
            value = state.query,
            onValueChange = { state.query = it },
            placeholder = "Search...",
            clearable = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Results list
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(shapes.base)
                .border(1.dp, colorScheme.border.lighter, shapes.base)
                .background(colorScheme.fill.blank)
                .verticalScroll(rememberScrollState()),
        ) {
            val filtered = state.filteredItems
            if (filtered.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = "No results found",
                        color = colorScheme.text.placeholder,
                        style = typography.base,
                    )
                }
            } else {
                filtered.forEach { item ->
                    val selected = state.isSelected(item.key)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (selected) colorScheme.primary.light9
                                else colorScheme.fill.blank
                            )
                            .clickable { state.toggleSelect(item.key) }
                            .pointerHoverIcon(PointerIcon.Hand)
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                            text = item.label,
                            color = if (selected) colorScheme.primary.base
                            else colorScheme.text.regular,
                            style = typography.base,
                            modifier = Modifier.weight(1f),
                        )
                        if (selected) {
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                text = "✓",
                                color = colorScheme.primary.base,
                                style = typography.base,
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Selected tags
        if (state.selectedKeys.isNotEmpty()) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = "Selected (${state.selectedKeys.size})",
                color = colorScheme.text.secondary,
                style = typography.small,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                state.selectedItems().take(10).forEach { item ->
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTag(
                        text = item.label,
                        closable = true,
                        onClose = { state.removeSelected(item.key) },
                    )
                }
                if (state.selectedKeys.size > 10) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = "+${state.selectedKeys.size - 10}",
                        color = colorScheme.text.secondary,
                        style = typography.extraSmall,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Action buttons
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDivider()
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(onClick = { onCancel?.invoke() }) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                onClick = { onConfirm?.invoke(state.selectedKeys.toList()) },
                type = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary,
            ) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "Confirm")
            }
        }
    }
}
