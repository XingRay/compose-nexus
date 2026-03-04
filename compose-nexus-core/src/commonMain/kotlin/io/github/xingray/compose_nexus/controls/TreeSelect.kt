package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.mutableStateListOf
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

@Stable
class TreeSelectState<T>(
    val nodes: List<TreeNode<T>>,
) {
    var selectedKey by mutableStateOf<T?>(null)
        internal set
    val selectedKeys = mutableStateListOf<T>()
    var isOpen by mutableStateOf(false)
        internal set
    var query by mutableStateOf("")
    val treeState = TreeState<T>()

    fun open() {
        isOpen = true
    }

    fun close() {
        isOpen = false
    }

    fun clear() {
        selectedKey = null
        selectedKeys.clear()
    }

    fun selectSingle(key: T) {
        selectedKey = key
        treeState.select(key)
    }

    fun toggleMulti(key: T) {
        if (key in selectedKeys) {
            selectedKeys.remove(key)
        } else {
            selectedKeys.add(key)
        }
    }

    fun displayText(
        multiple: Boolean,
        cacheData: List<TreeNode<T>> = emptyList(),
    ): String {
        return if (multiple) {
            selectedKeys.joinToString(", ") { key ->
                findLabel(nodes, key) ?: findLabel(cacheData, key) ?: key.toString()
            }
        } else {
            val key = selectedKey ?: return ""
            findLabel(nodes, key) ?: findLabel(cacheData, key) ?: key.toString()
        }
    }

    private fun findLabel(nodes: List<TreeNode<T>>, key: T): String? {
        for (node in nodes) {
            if (node.key == key) return node.label
            val found = findLabel(node.children, key)
            if (found != null) return found
        }
        return null
    }
}

@Composable
fun <T> rememberTreeSelectState(
    nodes: List<TreeNode<T>>,
): TreeSelectState<T> = remember(nodes) { TreeSelectState(nodes) }

@Composable
fun <T> NexusTreeSelect(
    state: TreeSelectState<T>,
    modifier: Modifier = Modifier,
    placeholder: String = "Select",
    disabled: Boolean = false,
    clearable: Boolean = true,
    multiple: Boolean = false,
    showCheckbox: Boolean = false,
    checkStrictly: Boolean = false,
    filterable: Boolean = false,
    filterMethod: ((String, TreeNode<T>) -> Boolean)? = null,
    defaultExpandAll: Boolean = false,
    cacheData: List<TreeNode<T>> = emptyList(),
    nodeContent: (@Composable (TreeNode<T>, Boolean) -> Unit)? = null,
    onSelect: ((T) -> Unit)? = null,
    onChange: ((Any?) -> Unit)? = null,
    onVisibleChange: ((Boolean) -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows

    fun setVisible(visible: Boolean) {
        if (state.isOpen != visible) {
            state.isOpen = visible
            onVisibleChange?.invoke(visible)
        }
    }

    val displayText = state.displayText(
        multiple = multiple || showCheckbox,
        cacheData = cacheData,
    )
    val hasSelection = if (multiple || showCheckbox) state.selectedKeys.isNotEmpty() else state.selectedKey != null
    val filteredNodes = if (filterable && state.query.isNotEmpty()) {
        filterTree(
            nodes = state.nodes,
            query = state.query,
            filterMethod = filterMethod,
        )
    } else {
        state.nodes
    }

    fun canSelect(node: TreeNode<T>): Boolean {
        if (node.disabled) return false
        return checkStrictly || node.children.isEmpty()
    }

    fun selectNode(node: TreeNode<T>) {
        if (!canSelect(node)) return
        if (multiple || showCheckbox) {
            state.toggleMulti(node.key)
            onSelect?.invoke(node.key)
            onChange?.invoke(state.selectedKeys.toList())
        } else {
            state.selectSingle(node.key)
            onSelect?.invoke(node.key)
            onChange?.invoke(node.key)
            setVisible(false)
        }
    }

    Column(modifier = modifier) {
        NexusInput(
            value = displayText,
            onValueChange = {},
            placeholder = placeholder,
            disabled = disabled,
            clearable = false,
            readonly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !disabled) {
                    setVisible(!state.isOpen)
                },
            suffix = {
                if (clearable && hasSelection && !disabled) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                state.clear()
                                onChange?.invoke(if (multiple || showCheckbox) emptyList<T>() else null)
                                onClear?.invoke()
                            }
                            .pointerHoverIcon(PointerIcon.Hand),
                    ) {
                        NexusText(
                            text = "✕",
                            color = colorScheme.text.placeholder,
                            style = typography.extraSmall,
                        )
                    }
                } else {
                    NexusText(
                        text = if (state.isOpen) "▴" else "▾",
                        color = colorScheme.text.placeholder,
                        style = typography.extraSmall,
                    )
                }
            },
            onFocus = onFocus,
            onBlur = onBlur,
        )

        if (state.isOpen) {
            Popup(
                alignment = Alignment.TopStart,
                properties = PopupProperties(focusable = true),
                onDismissRequest = {
                    setVisible(false)
                    onBlur?.invoke()
                },
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(shadows.light.elevation, shapes.base)
                        .clip(shapes.base)
                        .background(colorScheme.fill.blank)
                        .border(1.dp, colorScheme.border.lighter, shapes.base)
                        .padding(8.dp),
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (filterable) {
                            NexusInput(
                                value = state.query,
                                onValueChange = { state.query = it },
                                placeholder = "Filter node",
                                clearable = true,
                            )
                        }

                        Box(
                            modifier = Modifier
                                .heightIn(max = 300.dp)
                                .verticalScroll(rememberScrollState()),
                        ) {
                            NexusTree(
                                nodes = filteredNodes,
                                state = state.treeState,
                                defaultExpandAll = defaultExpandAll,
                                onNodeClick = { node ->
                                    selectNode(node)
                                },
                                nodeContent = { node ->
                                    val checked = if (multiple || showCheckbox) {
                                        state.selectedKeys.contains(node.key)
                                    } else {
                                        state.selectedKey == node.key
                                    }
                                    if (showCheckbox) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        ) {
                                            NexusCheckbox(
                                                checked = checked,
                                                onCheckedChange = { selectNode(node) },
                                                disabled = node.disabled,
                                            )
                                            if (nodeContent != null) {
                                                nodeContent(node, checked)
                                            } else {
                                                NexusText(
                                                    text = node.label,
                                                    color = if (node.disabled) colorScheme.text.disabled else colorScheme.text.regular,
                                                )
                                            }
                                        }
                                    } else {
                                        if (nodeContent != null) {
                                            nodeContent(node, checked)
                                        } else {
                                            NexusText(
                                                text = node.label,
                                                color = if (node.disabled) colorScheme.text.disabled else colorScheme.text.regular,
                                            )
                                        }
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun <T> filterTree(
    nodes: List<TreeNode<T>>,
    query: String,
    filterMethod: ((String, TreeNode<T>) -> Boolean)?,
): List<TreeNode<T>> {
    fun matches(node: TreeNode<T>): Boolean {
        return filterMethod?.invoke(query, node) ?: node.label.contains(query, ignoreCase = true)
    }

    return nodes.mapNotNull { node ->
        val children = filterTree(node.children, query, filterMethod)
        if (matches(node) || children.isNotEmpty()) {
            node.copy(children = children)
        } else {
            null
        }
    }
}
