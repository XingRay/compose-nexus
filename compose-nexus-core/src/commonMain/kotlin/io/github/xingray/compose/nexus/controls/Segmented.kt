package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme

enum class SegmentedDirection {
    Horizontal,
    Vertical,
}

data class SegmentedProps(
    val value: String = "value",
    val label: String = "label",
    val disabled: String = "disabled",
)

data class NexusSegmentedOption<T>(
    val value: T,
    val label: String,
    val disabled: Boolean = false,
    val raw: Any? = null,
)

@Composable
fun <T> NexusSegmented(
    modelValue: T?,
    onValueChange: (T) -> Unit,
    options: List<NexusSegmentedOption<T>>,
    modifier: Modifier = Modifier,
    size: ComponentSize = ComponentSize.Default,
    block: Boolean = false,
    disabled: Boolean = false,
    direction: SegmentedDirection = SegmentedDirection.Horizontal,
    onChange: ((T) -> Unit)? = null,
    optionContent: (@Composable (item: NexusSegmentedOption<T>, selected: Boolean) -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val groupShape = RoundedCornerShape(10.dp)
    val itemShape = RoundedCornerShape(8.dp)
    val itemPadding = when (size) {
        ComponentSize.Large -> Pair(14.dp, 8.dp)
        ComponentSize.Small -> Pair(10.dp, 4.dp)
        else -> Pair(12.dp, 6.dp)
    }
    val textStyle = when (size) {
        ComponentSize.Large -> typography.base
        ComponentSize.Small -> typography.small
        else -> typography.base
    }

    val baseModifier = modifier
        .clip(groupShape)
        .background(colorScheme.fill.lighter)
        .padding(3.dp)
        .then(if (block) Modifier.fillMaxWidth() else Modifier)
        .widthIn(min = 120.dp)

    @Composable
    fun Item(item: NexusSegmentedOption<T>) {
            val selected = modelValue == item.value
            val itemDisabled = disabled || item.disabled
            val background = when {
                selected -> colorScheme.fill.blank
                itemDisabled -> colorScheme.fill.lighter
                else -> colorScheme.fill.lighter
            }
            val textColor = when {
                itemDisabled -> colorScheme.text.disabled
                selected -> colorScheme.primary.base
                else -> colorScheme.text.regular
            }

            Box(
                modifier = Modifier
                    .clip(itemShape)
                    .background(background)
                    .then(
                        if (!itemDisabled) {
                            Modifier
                                .clickable {
                                    onValueChange(item.value)
                                    onChange?.invoke(item.value)
                                }
                                .pointerHoverIcon(PointerIcon.Hand)
                        } else {
                            Modifier
                        }
                    )
                    .padding(horizontal = itemPadding.first, vertical = itemPadding.second),
                contentAlignment = Alignment.Center,
            ) {
                if (optionContent != null) {
                    optionContent(item, selected)
                } else {
                    NexusText(
                        text = item.label,
                        color = textColor,
                        style = textStyle,
                    )
                }
            }
        }
    if (direction == SegmentedDirection.Horizontal) {
        Row(
            modifier = baseModifier,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            options.forEach { item -> Item(item) }
        }
    } else {
        Column(
            modifier = baseModifier,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            options.forEach { item -> Item(item) }
        }
    }
}

@Composable
fun NexusSegmented(
    modelValue: Any?,
    onValueChange: (Any) -> Unit,
    options: List<Any>,
    modifier: Modifier = Modifier,
    props: SegmentedProps = SegmentedProps(),
    size: ComponentSize = ComponentSize.Default,
    block: Boolean = false,
    disabled: Boolean = false,
    direction: SegmentedDirection = SegmentedDirection.Horizontal,
    onChange: ((Any) -> Unit)? = null,
    optionContent: (@Composable (item: NexusSegmentedOption<Any>, selected: Boolean) -> Unit)? = null,
) {
    val normalized = options.map { normalizeSegmentedOption(it, props) }
    // Explicitly route to the generic overload to avoid recursive self-call.
    NexusSegmented<Any>(
        modelValue = modelValue,
        onValueChange = onValueChange,
        options = normalized,
        modifier = modifier,
        size = size,
        block = block,
        disabled = disabled,
        direction = direction,
        onChange = onChange,
        optionContent = optionContent,
    )
}

private fun normalizeSegmentedOption(
    item: Any,
    props: SegmentedProps,
): NexusSegmentedOption<Any> {
    return when (item) {
        is NexusSegmentedOption<*> -> {
            NexusSegmentedOption(
                value = item.value as Any,
                label = item.label,
                disabled = item.disabled,
                raw = item.raw ?: item,
            )
        }
        is String, is Number, is Boolean -> {
            NexusSegmentedOption(
                value = item,
                label = item.toString(),
                disabled = false,
                raw = item,
            )
        }
        is Map<*, *> -> {
            val value = item[props.value] ?: item
            val label = (item[props.label] ?: value).toString()
            val disabled = item[props.disabled] as? Boolean ?: false
            NexusSegmentedOption(
                value = value,
                label = label,
                disabled = disabled,
                raw = item,
            )
        }
        else -> {
            NexusSegmentedOption(
                value = item,
                label = item.toString(),
                disabled = false,
                raw = item,
            )
        }
    }
}
