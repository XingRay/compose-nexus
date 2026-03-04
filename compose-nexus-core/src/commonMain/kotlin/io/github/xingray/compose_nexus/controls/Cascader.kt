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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Cascader option node.
 *
 * @param T The type of the option value.
 * @param value Unique value for this option.
 * @param label Display label.
 * @param children Child options (next level).
 * @param disabled Whether this option is disabled.
 */
data class CascaderOption<T>(
    val value: T,
    val label: String,
    val children: List<CascaderOption<T>> = emptyList(),
    val disabled: Boolean = false,
)

enum class CascaderExpandTrigger {
    Click,
    Hover,
}

/**
 * Cascader state holder.
 */
@Stable
class CascaderState<T>(
    val options: List<CascaderOption<T>>,
) {
    var selectedPath = mutableStateListOf<T>()
        private set
    var isOpen by mutableStateOf(false)
        internal set
    // Track which option is expanded at each level
    internal var expandedPath = mutableStateListOf<T>()

    fun open() { isOpen = true }
    fun close() { isOpen = false }

    fun select(path: List<T>) {
        selectedPath.clear()
        selectedPath.addAll(path)
        isOpen = false
    }

    fun clear() {
        selectedPath.clear()
    }

    fun displayText(
        showAllLevels: Boolean = true,
        separator: String = " / ",
    ): String {
        if (selectedPath.isEmpty()) return ""
        val labels = mutableListOf<String>()
        var currentOptions = options
        for (value in selectedPath) {
            val found = currentOptions.find { it.value == value }
            if (found != null) {
                labels.add(found.label)
                currentOptions = found.children
            }
        }
        return if (showAllLevels) {
            labels.joinToString(separator)
        } else {
            labels.lastOrNull().orEmpty()
        }
    }

    internal fun expandAt(level: Int, value: T) {
        // Trim any levels beyond current
        while (expandedPath.size > level) {
            expandedPath.removeAt(expandedPath.lastIndex)
        }
        expandedPath.add(value)
    }

    internal fun optionsAtLevel(level: Int): List<CascaderOption<T>> {
        var current = options
        for (i in 0 until level) {
            val expanded = expandedPath.getOrNull(i) ?: return emptyList()
            val node = current.find { it.value == expanded } ?: return emptyList()
            current = node.children
        }
        return current
    }

    internal fun levelCount(): Int {
        // Number of visible columns = expandedPath.size + 1 (if last expanded has children)
        val levels = 1 + expandedPath.size
        // Check if the last expanded actually has children
        val lastOptions = optionsAtLevel(expandedPath.size)
        return if (lastOptions.isEmpty() && expandedPath.isNotEmpty()) expandedPath.size else levels
    }
}

@Composable
fun <T> rememberCascaderState(
    options: List<CascaderOption<T>>,
): CascaderState<T> = remember(options) { CascaderState(options) }

/**
 * Element Plus Cascader — a multi-level cascading selector.
 *
 * @param T Option value type.
 * @param state Cascader state.
 * @param modifier Modifier.
 * @param placeholder Input placeholder.
 * @param onSelect Callback when a final path is selected.
 */
@Composable
fun <T> NexusCascader(
    state: CascaderState<T>,
    modifier: Modifier = Modifier,
    placeholder: String = "Select",
    disabled: Boolean = false,
    clearable: Boolean = false,
    showAllLevels: Boolean = true,
    separator: String = " / ",
    expandTrigger: CascaderExpandTrigger = CascaderExpandTrigger.Click,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    onVisibleChange: ((Boolean) -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onExpandChange: ((List<T>) -> Unit)? = null,
    onSelect: ((List<T>) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows

    Column(modifier = modifier) {
        // Trigger input
        NexusInput(
            value = state.displayText(showAllLevels = showAllLevels, separator = separator),
            onValueChange = {
                if (it.isEmpty()) {
                    state.clear()
                    onClear?.invoke()
                }
            },
            placeholder = placeholder,
            disabled = disabled,
            readonly = true,
            clearable = clearable && state.selectedPath.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !disabled) {
                    state.isOpen = !state.isOpen
                    onVisibleChange?.invoke(state.isOpen)
                },
            suffix = {
                NexusText(
                    text = if (state.isOpen) "▴" else "▾",
                    color = colorScheme.text.placeholder,
                    style = typography.extraSmall,
                )
            },
            onFocusChanged = { focused ->
                if (focused) onFocus?.invoke() else onBlur?.invoke()
            },
        )

        // Dropdown panels
        if (state.isOpen && !disabled) {
            Popup(
                alignment = Alignment.TopStart,
                properties = PopupProperties(focusable = true),
                onDismissRequest = {
                    state.close()
                    onVisibleChange?.invoke(false)
                },
            ) {
                Column(
                    modifier = Modifier
                        .shadow(shadows.light.elevation, shapes.base)
                        .clip(shapes.base)
                        .background(colorScheme.fill.blank)
                        .border(1.dp, colorScheme.border.lighter, shapes.base),
                ) {
                    if (header != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        ) {
                            header()
                        }
                    }

                    Row {
                        // Render each level as a column
                        val levelCount = state.levelCount()
                        for (level in 0 until levelCount) {
                            val levelOptions = state.optionsAtLevel(level)
                            if (levelOptions.isEmpty()) break

                            CascaderColumn(
                                options = levelOptions,
                                expandedValue = state.expandedPath.getOrNull(level),
                                selectedPath = state.selectedPath,
                                level = level,
                                expandTrigger = expandTrigger,
                                onExpand = { value ->
                                    state.expandAt(level, value)
                                    onExpandChange?.invoke(state.expandedPath.toList())
                                },
                                onSelect = { value ->
                                    // Build path up to this level + selected value
                                    val path = state.expandedPath.take(level).toMutableList()
                                    path.add(value)
                                    state.select(path)
                                    onVisibleChange?.invoke(false)
                                    onSelect?.invoke(path)
                                },
                            )
                        }
                    }

                    if (footer != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        ) {
                            footer()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T> CascaderColumn(
    options: List<CascaderOption<T>>,
    expandedValue: T?,
    selectedPath: List<T>,
    level: Int,
    expandTrigger: CascaderExpandTrigger,
    onExpand: (T) -> Unit,
    onSelect: (T) -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    Column(
        modifier = Modifier
            .heightIn(max = 260.dp)
            .verticalScroll(rememberScrollState())
            .padding(4.dp),
    ) {
        options.forEach { option ->
            val hasChildren = option.children.isNotEmpty()
            val isExpanded = option.value == expandedValue
            val isInPath = option.value in selectedPath

            val bgColor = when {
                isExpanded -> colorScheme.fill.light
                isInPath -> colorScheme.primary.light9
                else -> Color.Transparent
            }
            val textColor = when {
                option.disabled -> colorScheme.text.disabled
                isInPath -> colorScheme.primary.base
                else -> colorScheme.text.regular
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor)
                    .then(
                        if (!option.disabled) {
                            Modifier
                                .clickable {
                                    if (hasChildren && expandTrigger == CascaderExpandTrigger.Click) {
                                        onExpand(option.value)
                                    } else if (hasChildren && expandTrigger == CascaderExpandTrigger.Hover) {
                                        onExpand(option.value)
                                    } else {
                                        onSelect(option.value)
                                    }
                                }
                                .pointerHoverIcon(PointerIcon.Hand)
                        } else Modifier
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                NexusText(
                    text = option.label,
                    color = textColor,
                    style = typography.base,
                )
                if (hasChildren) {
                    NexusText(
                        text = "›",
                        color = colorScheme.text.placeholder,
                        style = typography.small,
                    )
                }
            }
        }
    }
}
