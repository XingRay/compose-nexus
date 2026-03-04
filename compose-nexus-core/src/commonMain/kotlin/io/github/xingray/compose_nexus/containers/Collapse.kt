package io.github.xingray.compose_nexus.containers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.controls.NexusDivider
import io.github.xingray.compose_nexus.controls.NexusText
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.NexusTheme
import kotlinx.coroutines.launch

enum class CollapseExpandIconPosition {
    Left,
    Right,
}

@Stable
class CollapseState(
    initialExpanded: Set<String> = emptySet(),
    val accordion: Boolean = false,
) {
    var expanded: Set<String> by mutableStateOf(
        if (accordion) initialExpanded.take(1).toSet() else initialExpanded,
    )
        private set

    val activeNames: List<String>
        get() = expanded.toList()

    fun isExpanded(name: String): Boolean = name in expanded

    fun toggle(name: String) {
        expanded = if (name in expanded) {
            expanded - name
        } else {
            if (accordion) setOf(name) else expanded + name
        }
    }

    fun setActiveNames(names: Collection<String>) {
        expanded = if (accordion) names.firstOrNull()?.let { setOf(it) } ?: emptySet() else names.toSet()
    }

    fun setActiveName(name: String?) {
        expanded = if (name.isNullOrEmpty()) emptySet() else setOf(name)
    }
}

@Composable
fun rememberCollapseState(
    initialExpanded: Set<String> = emptySet(),
    accordion: Boolean = false,
): CollapseState = remember(accordion) { CollapseState(initialExpanded, accordion) }

@Stable
class CollapseScope internal constructor(
    internal val state: CollapseState,
    private val expandIconPosition: CollapseExpandIconPosition,
    private val beforeCollapse: (suspend () -> Boolean)?,
    private val onChange: ((Any) -> Unit)?,
) {
    private var toggling by mutableStateOf(false)

    internal fun itemIconPosition(override: CollapseExpandIconPosition?): CollapseExpandIconPosition {
        return override ?: expandIconPosition
    }

    fun setActiveNames(activeNames: Collection<String>) {
        state.setActiveNames(activeNames)
        emitChange()
    }

    fun requestToggle(name: String) {
        if (toggling) return
    }

    internal fun emitChange() {
        val value: Any = if (state.accordion) {
            state.activeNames.firstOrNull().orEmpty()
        } else {
            state.activeNames
        }
        onChange?.invoke(value)
    }

    internal fun tryToggle(
        name: String,
        scope: kotlinx.coroutines.CoroutineScope,
    ) {
        if (toggling) return
        scope.launch {
            toggling = true
            val allowed = beforeCollapse?.invoke() ?: true
            if (allowed) {
                state.toggle(name)
                emitChange()
            }
            toggling = false
        }
    }
}

@Composable
fun NexusCollapse(
    state: CollapseState = rememberCollapseState(),
    modifier: Modifier = Modifier,
    modelValue: Any? = null,
    expandIconPosition: CollapseExpandIconPosition = CollapseExpandIconPosition.Right,
    beforeCollapse: (suspend () -> Boolean)? = null,
    onChange: ((Any) -> Unit)? = null,
    content: @Composable CollapseScope.() -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme

    LaunchedEffect(modelValue) {
        when (modelValue) {
            is String -> state.setActiveName(modelValue)
            is Number -> state.setActiveName(modelValue.toString())
            is Collection<*> -> {
                state.setActiveNames(
                    modelValue.mapNotNull {
                        when (it) {
                            is String -> it
                            is Number -> it.toString()
                            else -> null
                        }
                    },
                )
            }
        }
    }

    val scope = remember(state, expandIconPosition, beforeCollapse, onChange) {
        CollapseScope(
            state = state,
            expandIconPosition = expandIconPosition,
            beforeCollapse = beforeCollapse,
            onChange = onChange,
        )
    }

    Column(
        modifier = modifier
            .clip(NexusTheme.shapes.base)
            .border(1.dp, colorScheme.border.lighter, NexusTheme.shapes.base),
    ) {
        scope.content()
    }
}

@Composable
fun CollapseScope.NexusCollapseItem(
    name: String,
    modifier: Modifier = Modifier,
    titleText: String = "",
    disabled: Boolean = false,
    expandIconPosition: CollapseExpandIconPosition? = null,
    titleSlot: (@Composable (isActive: Boolean) -> Unit)? = null,
    iconSlot: (@Composable (isActive: Boolean) -> Unit)? = null,
    title: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val motion = NexusTheme.motion
    val isExpanded = state.isExpanded(name)
    val position = itemIconPosition(expandIconPosition)
    val coroutineScope = rememberCoroutineScope()

    @Composable
    fun RenderIcon() {
        if (iconSlot != null) {
            iconSlot(isExpanded)
        } else {
            NexusText(
                text = "▶",
                modifier = Modifier.rotate(if (isExpanded) 90f else 0f),
                color = if (disabled) colorScheme.disabled.text else colorScheme.text.secondary,
                style = NexusTheme.typography.extraSmall,
            )
        }
    }

    @Composable
    fun RenderTitle() {
        if (titleSlot != null) {
            titleSlot(isExpanded)
        } else if (titleText.isNotEmpty()) {
            NexusText(text = titleText)
        } else {
            title()
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        NexusDivider(color = colorScheme.border.lighter)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.fill.blank)
                .then(
                    if (!disabled) {
                        Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { tryToggle(name, coroutineScope) }
                            .pointerHoverIcon(PointerIcon.Hand)
                    } else {
                        Modifier
                    }
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (position == CollapseExpandIconPosition.Left) {
                Box(modifier = Modifier.padding(end = 8.dp)) {
                    RenderIcon()
                }
            }

            ProvideContentColorTextStyle(
                contentColor = if (disabled) colorScheme.disabled.text else colorScheme.text.primary,
                textStyle = NexusTheme.typography.base,
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    RenderTitle()
                }
            }

            if (position == CollapseExpandIconPosition.Right) {
                Box(modifier = Modifier.padding(start = 8.dp)) {
                    RenderIcon()
                }
            }
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(animationSpec = motion.tweenFast()),
            exit = shrinkVertically(animationSpec = motion.tweenFast()),
        ) {
            ProvideContentColorTextStyle(
                contentColor = colorScheme.text.regular,
                textStyle = NexusTheme.typography.base,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorScheme.fill.blank)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                ) {
                    content()
                }
            }
        }
    }
}
