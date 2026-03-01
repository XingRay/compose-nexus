package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * TreeSelect state holder — combines Select input with Tree view.
 */
@Stable
class TreeSelectState<T>(
    val nodes: List<TreeNode<T>>,
) {
    var selectedKey by mutableStateOf<T?>(null)
        private set
    var isOpen by mutableStateOf(false)
        internal set
    val treeState = TreeState<T>()

    fun open() { isOpen = true }
    fun close() { isOpen = false }

    fun select(key: T) {
        selectedKey = key
        treeState.select(key)
        isOpen = false
    }

    fun displayText(): String {
        val key = selectedKey ?: return ""
        return findLabel(nodes, key) ?: key.toString()
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

/**
 * Element Plus TreeSelect — a dropdown selector with a tree view inside.
 *
 * @param T Node key type.
 * @param state TreeSelect state.
 * @param modifier Modifier.
 * @param placeholder Input placeholder.
 * @param defaultExpandAll Whether to expand all tree nodes by default.
 * @param onSelect Callback when a node is selected.
 */
@Composable
fun <T> NexusTreeSelect(
    state: TreeSelectState<T>,
    modifier: Modifier = Modifier,
    placeholder: String = "Select",
    defaultExpandAll: Boolean = false,
    onSelect: ((T) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows

    Column(modifier = modifier) {
        // Trigger input
        NexusInput(
            value = state.displayText(),
            onValueChange = {},
            placeholder = placeholder,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { state.isOpen = !state.isOpen },
            suffix = {
                NexusText(
                    text = if (state.isOpen) "▴" else "▾",
                    color = colorScheme.text.placeholder,
                    style = typography.extraSmall,
                )
            },
        )

        // Tree dropdown
        if (state.isOpen) {
            Popup(
                alignment = Alignment.TopStart,
                properties = PopupProperties(focusable = true),
                onDismissRequest = { state.close() },
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(shadows.light.elevation, shapes.base)
                        .clip(shapes.base)
                        .background(colorScheme.fill.blank)
                        .border(1.dp, colorScheme.border.lighter, shapes.base)
                        .heightIn(max = 300.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(4.dp),
                ) {
                    NexusTree(
                        nodes = state.nodes,
                        state = state.treeState,
                        defaultExpandAll = defaultExpandAll,
                        onNodeClick = { node ->
                            state.select(node.key)
                            onSelect?.invoke(node.key)
                        },
                    )
                }
            }
        }
    }
}
