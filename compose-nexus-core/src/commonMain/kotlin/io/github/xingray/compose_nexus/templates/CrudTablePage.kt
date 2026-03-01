package io.github.xingray.compose_nexus.templates

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
import io.github.xingray.compose_nexus.containers.DialogState
import io.github.xingray.compose_nexus.containers.NexusDialog
import io.github.xingray.compose_nexus.containers.rememberDialogState
import io.github.xingray.compose_nexus.controls.NexusButton
import io.github.xingray.compose_nexus.controls.NexusInput
import io.github.xingray.compose_nexus.controls.NexusPagination
import io.github.xingray.compose_nexus.controls.NexusTable
import io.github.xingray.compose_nexus.controls.NexusTableColumn
import io.github.xingray.compose_nexus.controls.NexusText
import io.github.xingray.compose_nexus.controls.PaginationState
import io.github.xingray.compose_nexus.controls.rememberPaginationState
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType

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
): CrudTablePageState<T> = remember { CrudTablePageState(pageSize) }

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
    columns: List<NexusTableColumn<T>>,
    totalPages: Int,
    paginationState: PaginationState = rememberPaginationState(pageCount = totalPages),
    state: CrudTablePageState<T> = rememberCrudTablePageState(),
    modifier: Modifier = Modifier,
    title: String = "Data Management",
    onSearch: ((String) -> Unit)? = null,
    onPageChange: ((Int) -> Unit)? = null,
    onCreate: (() -> Unit)? = null,
    createDialogContent: (@Composable () -> Unit)? = null,
    editDialogContent: (@Composable (T) -> Unit)? = null,
    deleteDialogContent: (@Composable (T) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val createDialogState = rememberDialogState(state.isCreateDialogOpen)
    val editDialogState = rememberDialogState(state.isEditDialogOpen)
    val deleteDialogState = rememberDialogState(state.isDeleteDialogOpen)

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
            NexusText(
                text = title,
                color = colorScheme.text.primary,
                style = typography.extraLarge,
            )
            NexusButton(
                onClick = {
                    state.isCreateDialogOpen = true
                    createDialogState.open()
                    onCreate?.invoke()
                },
                type = NexusType.Primary,
            ) {
                NexusText(text = "+ New")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filter bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NexusInput(
                value = state.searchQuery,
                onValueChange = { state.searchQuery = it },
                placeholder = "Search...",
                clearable = true,
                modifier = Modifier.weight(1f),
            )
            NexusButton(
                onClick = { onSearch?.invoke(state.searchQuery) },
                type = NexusType.Primary,
            ) {
                NexusText(text = "Search")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Table
        NexusTable(
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
            NexusPagination(
                state = paginationState,
                onPageChange = onPageChange,
            )
        }
    }

    // Create dialog
    if (createDialogContent != null) {
        NexusDialog(
            state = createDialogState,
            title = "Create",
        ) {
            createDialogContent()
        }
    }

    // Edit dialog
    val editItem = state.editingItem
    if (editDialogContent != null && editItem != null) {
        NexusDialog(
            state = editDialogState,
            title = "Edit",
        ) {
            editDialogContent(editItem)
        }
    }

    // Delete dialog
    val deleteItem = state.deletingItem
    if (deleteDialogContent != null && deleteItem != null) {
        NexusDialog(
            state = deleteDialogState,
            title = "Confirm Delete",
            width = 400.dp,
        ) {
            deleteDialogContent(deleteItem)
        }
    }
}
