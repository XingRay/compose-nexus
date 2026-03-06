package io.github.xingray.compose.nexus.controls

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import kotlinx.coroutines.launch

@Composable
fun NexusSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    size: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    disabled: Boolean = false,
    loading: Boolean = false,
    width: Dp? = null,
    inlinePrompt: Boolean = false,
    activeColor: Color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.primary.base,
    inactiveColor: Color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.border.base,
    activeText: String = "",
    inactiveText: String = "",
    activeIcon: (@Composable () -> Unit)? = null,
    inactiveIcon: (@Composable () -> Unit)? = null,
    activeActionIcon: (@Composable () -> Unit)? = null,
    inactiveActionIcon: (@Composable () -> Unit)? = null,
    activeAction: (@Composable () -> Unit)? = null,
    inactiveAction: (@Composable () -> Unit)? = null,
    active: (@Composable () -> Unit)? = null,
    inactive: (@Composable () -> Unit)? = null,
    label: (@Composable () -> Unit)? = null,
    validateEvent: Boolean = true,
    beforeChange: (suspend (Boolean) -> Boolean)? = null,
    onChange: ((Boolean) -> Unit)? = null,
) {
    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSwitch(
        value = checked,
        onValueChange = onCheckedChange,
        modifier = modifier,
        activeValue = true,
        inactiveValue = false,
        size = size,
        disabled = disabled,
        loading = loading,
        width = width,
        inlinePrompt = inlinePrompt,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        activeText = activeText,
        inactiveText = inactiveText,
        activeIcon = activeIcon,
        inactiveIcon = inactiveIcon,
        activeActionIcon = activeActionIcon,
        inactiveActionIcon = inactiveActionIcon,
        activeAction = activeAction,
        inactiveAction = inactiveAction,
        active = active,
        inactive = inactive,
        label = label,
        validateEvent = validateEvent,
        beforeChange = beforeChange,
        onChange = onChange,
    )
}

@Composable
fun <T> NexusSwitch(
    value: T,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    activeValue: T,
    inactiveValue: T,
    size: io.github.xingray.compose.nexus.theme.ComponentSize = _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default,
    disabled: Boolean = false,
    loading: Boolean = false,
    width: Dp? = null,
    inlinePrompt: Boolean = false,
    activeColor: Color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.primary.base,
    inactiveColor: Color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.border.base,
    activeText: String = "",
    inactiveText: String = "",
    activeIcon: (@Composable () -> Unit)? = null,
    inactiveIcon: (@Composable () -> Unit)? = null,
    activeActionIcon: (@Composable () -> Unit)? = null,
    inactiveActionIcon: (@Composable () -> Unit)? = null,
    activeAction: (@Composable () -> Unit)? = null,
    inactiveAction: (@Composable () -> Unit)? = null,
    active: (@Composable () -> Unit)? = null,
    inactive: (@Composable () -> Unit)? = null,
    label: (@Composable () -> Unit)? = null,
    validateEvent: Boolean = true,
    beforeChange: (suspend (T) -> Boolean)? = null,
    onChange: ((T) -> Unit)? = null,
) {
    @Suppress("UNUSED_VARIABLE")
    val _validateEvent = validateEvent

    val checked = value == activeValue
    var switching by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun commit(nextChecked: Boolean) {
        val nextValue = if (nextChecked) activeValue else inactiveValue
        onValueChange(nextValue)
        onChange?.invoke(nextValue)
    }

    fun requestToggle() {
        if (disabled || loading || switching) return
        val nextChecked = !checked
        val nextValue = if (nextChecked) activeValue else inactiveValue
        if (beforeChange == null) {
            commit(nextChecked)
        } else {
            scope.launch {
                switching = true
                val allow = runCatching { beforeChange(nextValue) }.getOrDefault(false)
                if (allow) {
                    commit(nextChecked)
                }
                switching = false
            }
        }
    }

    _root_ide_package_.io.github.xingray.compose.nexus.controls.SwitchTrack(
        checked = checked,
        onToggle = { requestToggle() },
        modifier = modifier,
        size = size,
        disabled = disabled || switching,
        loading = loading || switching,
        width = width,
        inlinePrompt = inlinePrompt,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        activeText = activeText,
        inactiveText = inactiveText,
        activeIcon = activeIcon,
        inactiveIcon = inactiveIcon,
        activeActionIcon = activeActionIcon,
        inactiveActionIcon = inactiveActionIcon,
        activeAction = activeAction,
        inactiveAction = inactiveAction,
        active = active,
        inactive = inactive,
        label = label,
    )
}

@Composable
private fun SwitchTrack(
    checked: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    size: io.github.xingray.compose.nexus.theme.ComponentSize,
    disabled: Boolean,
    loading: Boolean,
    width: Dp?,
    inlinePrompt: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    activeText: String,
    inactiveText: String,
    activeIcon: (@Composable () -> Unit)?,
    inactiveIcon: (@Composable () -> Unit)?,
    activeActionIcon: (@Composable () -> Unit)?,
    inactiveActionIcon: (@Composable () -> Unit)?,
    activeAction: (@Composable () -> Unit)?,
    inactiveAction: (@Composable () -> Unit)?,
    active: (@Composable () -> Unit)?,
    inactive: (@Composable () -> Unit)?,
    label: (@Composable () -> Unit)?,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val isDisabled = disabled || loading

    val defaultTrackWidth = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 56.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 40.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 32.dp
    }
    val trackWidth = width ?: defaultTrackWidth
    val trackHeight = when (size) {
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Large -> 24.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Default -> 20.dp
        _root_ide_package_.io.github.xingray.compose.nexus.theme.ComponentSize.Small -> 16.dp
    }
    val thumbSize = trackHeight - 4.dp
    val thumbTravel = (trackWidth - trackHeight).coerceAtLeast(0.dp)

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) thumbTravel else 0.dp,
        animationSpec = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.motion.tweenFast(),
        label = "switch-thumb",
    )

    val trackColor = when {
        isDisabled && checked -> activeColor.copy(alpha = 0.5f)
        isDisabled -> inactiveColor.copy(alpha = 0.5f)
        checked -> activeColor
        else -> inactiveColor
    }
    val borderColor = if (isDisabled) colorScheme.disabled.border else Color.Transparent

    val sideInactive = inactive ?: inactiveIcon?.let { { it() } } ?: run {
        if (inactiveText.isNotEmpty()) {
            {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = inactiveText,
                    style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.extraSmall
                )
            }
        } else {
            null
        }
    }
    val sideActive = active ?: activeIcon?.let { { it() } } ?: run {
        if (activeText.isNotEmpty()) {
            {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = activeText,
                    style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.extraSmall
                )
            }
        } else {
            null
        }
    }

    val promptContent: (@Composable () -> Unit)? = when {
        checked && activeIcon != null -> activeIcon
        !checked && inactiveIcon != null -> inactiveIcon
        checked && activeText.isNotEmpty() -> {
            {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = activeText.take(1),
                    color = colorScheme.primary.base,
                    style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.extraSmall
                )
            }
        }
        !checked && inactiveText.isNotEmpty() -> {
            {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = inactiveText.take(1),
                    color = colorScheme.text.placeholder,
                    style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.extraSmall
                )
            }
        }
        else -> null
    }

    val actionContent: (@Composable () -> Unit)? = when {
        loading -> {
            { _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusLoading(loading = true, spinnerSize = (thumbSize - 8.dp).coerceAtLeast(8.dp)) }
        }
        checked && activeAction != null -> activeAction
        !checked && inactiveAction != null -> inactiveAction
        checked && activeActionIcon != null -> activeActionIcon
        !checked && inactiveActionIcon != null -> inactiveActionIcon
        else -> null
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (!inlinePrompt && sideInactive != null) {
            Box(
                modifier = Modifier.padding(end = 2.dp),
                contentAlignment = Alignment.Center,
            ) {
                sideInactive()
            }
        }

        Box(
            modifier = Modifier
                .width(trackWidth)
                .height(trackHeight)
                .clip(RoundedCornerShape(trackHeight / 2))
                .background(trackColor)
                .border(1.dp, borderColor, RoundedCornerShape(trackHeight / 2))
                .then(
                    if (!isDisabled) {
                        Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                role = Role.Switch,
                            ) { onToggle() }
                            .pointerHoverIcon(PointerIcon.Hand)
                    } else {
                        Modifier
                    }
                ),
        ) {
            if (!inlinePrompt) {
                val content = if (checked) active else inactive
                if (content != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = thumbSize / 2),
                        contentAlignment = Alignment.Center,
                    ) {
                        content()
                    }
                }
            }

            Box(
                modifier = Modifier
                    .offset(x = thumbOffset + 2.dp, y = 2.dp)
                    .size(thumbSize)
                    .clip(CircleShape)
                    .background(colorScheme.white),
                contentAlignment = Alignment.Center,
            ) {
                when {
                    actionContent != null -> actionContent()
                    inlinePrompt && promptContent != null -> promptContent()
                }
            }
        }

        if (!inlinePrompt && sideActive != null) {
            Box(
                modifier = Modifier.padding(start = 2.dp),
                contentAlignment = Alignment.Center,
            ) {
                sideActive()
            }
        }

        if (label != null) {
            label()
        }
    }
}
