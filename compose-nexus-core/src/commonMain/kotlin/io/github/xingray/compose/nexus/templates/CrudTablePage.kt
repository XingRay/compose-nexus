package io.github.xingray.compose.nexus.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.containers.DialogState
import io.github.xingray.compose.nexus.containers.NexusDialog
import io.github.xingray.compose.nexus.containers.rememberDialogState
import io.github.xingray.compose.nexus.controls.NexusButton
import io.github.xingray.compose.nexus.controls.NexusInput
import io.github.xingray.compose.nexus.controls.NexusPagination
import io.github.xingray.compose.nexus.controls.NexusTable
import io.github.xingray.compose.nexus.controls.NexusTableColumn
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.controls.PaginationState
import io.github.xingray.compose.nexus.controls.rememberPaginationState
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType

/**
 * CrudTablePage state holder.
 */
@Stable
class CrudTablePageState<T>(
    val pageSize: Int = 10,
) {
    var searchQuery by mutableStateOf("")
    var isCreateDialogOpen by mutableStateOf(false)
    var isEditDialogOpen by mutableStateOf(false)
    var isDeleteDialogOpen by mutableStateOf(false)
    var editingItem by mutableStateOf<T?>(null)
    var deletingItem by mutableStateOf<T?>(null)
}

@Composable
fun <T> rememberCrudTablePageState(
    pageSize: Int = 10,
): io.github.xingray.compose.nexus.templates.CrudTablePageState<T> = remember { _root_ide_package_.io.github.xingray.compose.nexus.templates.CrudTablePageState(pageSize) }

/**
 * CrudTablePage — a full CRUD table page template.
 *
 * Layout: Filter bar → Table → Pagination → Create/Edit/Delete dialogs.
 *
 * @param T Row data type.
 * @param data Current page data.
 * @param columns Table column definitions.
 * @param totalPages Total number of pages.
 * @param paginationState Pagination state.
 * @param state Page state.
 * @param modifier Modifier.
 * @param title Page title.
 * @param onSearch Callback for search/filter.
 * @param onPageChange Callback for page navigation.
 * @param onCreate Callback for creating a new item.
 * @param createDialogContent Content of the create dialog.
 * @param editDialogContent Content of the edit dialog.
 * @param deleteDialogContent Content of the delete confirmation.
 */
@Composable
fun <T> NexusCrudTablePage(
    data: List<T>,
    columns: List<io.github.xingray.compose.nexus.controls.NexusTableColumn<T>>,
    totalPages: Int,
    paginationState: io.github.xingray.compose.nexus.controls.PaginationState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberPaginationState(pageCount = totalPages),
    state: io.github.xingray.compose.nexus.templates.CrudTablePageState<T> = _root_ide_package_.io.github.xingray.compose.nexus.templates.rememberCrudTablePageState(),
    modifier: Modifier = Modifier,
    title: String = "Data Management",
    onSearch: ((String) -> Unit)? = null,
    onPageChange: ((Int) -> Unit)? = null,
    onCreate: (() -> Unit)? = null,
    createDialogContent: (@Composable () -> Unit)? = null,
    editDialogContent: (@Composable (T) -> Unit)? = null,
    deleteDialogContent: (@Composable (T) -> Unit)? = null,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography

    val createDialogState = _root_ide_package_.io.github.xingray.compose.nexus.containers.rememberDialogState(state.isCreateDialogOpen)
    val editDialogState = _root_ide_package_.io.github.xingray.compose.nexus.containers.rememberDialogState(state.isEditDialogOpen)
    val deleteDialogState = _root_ide_package_.io.github.xingray.compose.nexus.containers.rememberDialogState(state.isDeleteDialogOpen)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background.page)
            .padding(20.dp),
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = title,
                color = colorScheme.text.primary,
                style = typography.extraLarge,
            )
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                onClick = {
                    state.isCreateDialogOpen = true
                    createDialogState.open()
                    onCreate?.invoke()
                },
                type = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary,
            ) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "+ New")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filter bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusInput(
                value = state.searchQuery,
                onValueChange = { state.searchQuery = it },
                placeholder = "Search...",
                clearable = true,
                modifier = Modifier.weight(1f),
            )
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                onClick = { onSearch?.invoke(state.searchQuery) },
                type = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary,
            ) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "Search")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Table
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTable(
            data = data,
            columns = columns,
            modifier = Modifier.weight(1f),
            stripe = true,
            border = true,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pagination
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusPagination(
                state = paginationState,
                onPageChange = onPageChange,
            )
        }
    }

    // Create dialog
    if (createDialogContent != null) {
        _root_ide_package_.io.github.xingray.compose.nexus.containers.NexusDialog(
            state = createDialogState,
            title = "Create",
        ) {
            createDialogContent()
        }
    }

    // Edit dialog
    val editItem = state.editingItem
    if (editDialogContent != null && editItem != null) {
        _root_ide_package_.io.github.xingray.compose.nexus.containers.NexusDialog(
            state = editDialogState,
            title = "Edit",
        ) {
            editDialogContent(editItem)
        }
    }

    // Delete dialog
    val deleteItem = state.deletingItem
    if (deleteDialogContent != null && deleteItem != null) {
        _root_ide_package_.io.github.xingray.compose.nexus.containers.NexusDialog(
            state = deleteDialogState,
            title = "Confirm Delete",
            width = 400.dp,
        ) {
            deleteDialogContent(deleteItem)
        }
    }
}
