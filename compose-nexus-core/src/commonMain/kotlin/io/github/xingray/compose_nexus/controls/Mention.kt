package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme

data class MentionOption(
    val value: String? = null,
    val label: String? = null,
    val disabled: Boolean = false,
    val payload: Map<String, Any?> = emptyMap(),
)

data class MentionOptionProps(
    val value: String = "value",
    val label: String = "label",
    val disabled: String = "disabled",
)

enum class NexusMentionPlacement {
    Bottom,
    Top,
}

private data class MentionHit(
    val start: Int,
    val prefix: String,
    val pattern: String,
)

@Stable
class MentionState(
    initialValue: String = "",
) {
    var value by mutableStateOf(initialValue)
    var isOpen by mutableStateOf(false)
        internal set
    internal var mentionQuery by mutableStateOf("")
    internal var activePrefix by mutableStateOf("@")
}

@Composable
fun rememberMentionState(
    initialValue: String = "",
): MentionState = remember { MentionState(initialValue) }

@Composable
fun NexusMention(
    state: MentionState = rememberMentionState(),
    options: List<MentionOption> = emptyList(),
    modifier: Modifier = Modifier,
    placeholder: String = "",
    type: NexusInputType = NexusInputType.Text,
    size: ComponentSize = ComponentSize.Default,
    props: MentionOptionProps = MentionOptionProps(),
    prefix: List<String>? = null,
    trigger: Char = '@',
    split: Char = ' ',
    filterOption: ((pattern: String, option: MentionOption) -> Boolean)? = null,
    placement: NexusMentionPlacement = NexusMentionPlacement.Bottom,
    whole: Boolean = false,
    checkIsWhole: ((pattern: String, prefix: String) -> Boolean)? = null,
    loading: Boolean = false,
    disabled: Boolean = false,
    readonly: Boolean = false,
    onSearch: ((pattern: String, prefix: String) -> Unit)? = null,
    onSelect: ((option: MentionOption, prefix: String) -> Unit)? = null,
    onWholeRemove: ((pattern: String, prefix: String) -> Unit)? = null,
    onInput: ((String) -> Unit)? = null,
    onChange: ((String) -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    labelContent: (@Composable (item: MentionOption, index: Int) -> Unit)? = null,
    loadingContent: (@Composable () -> Unit)? = null,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows
    val prefixes = (prefix ?: listOf(trigger.toString())).filter { it.length == 1 }.ifEmpty { listOf("@") }

    fun optionValue(option: MentionOption): String {
        val payloadValue = option.payload[props.value]?.toString()
        return payloadValue ?: option.value.orEmpty()
    }

    fun optionLabel(option: MentionOption): String {
        val payloadLabel = option.payload[props.label]?.toString()
        return payloadLabel ?: option.label ?: optionValue(option)
    }

    fun optionDisabled(option: MentionOption): Boolean {
        val payloadDisabled = option.payload[props.disabled]
        return when (payloadDisabled) {
            is Boolean -> payloadDisabled
            is String -> payloadDisabled.equals("true", ignoreCase = true)
            else -> option.disabled
        }
    }

    fun detectMentionHit(text: String): MentionHit? {
        var bestStart = -1
        var bestPrefix = ""
        prefixes.forEach { p ->
            val idx = text.lastIndexOf(p)
            if (idx > bestStart) {
                bestStart = idx
                bestPrefix = p
            }
        }
        if (bestStart < 0) return null
        val tail = text.substring(bestStart + 1)
        if (tail.contains(split)) return null
        return MentionHit(
            start = bestStart,
            prefix = bestPrefix,
            pattern = tail,
        )
    }

    val mentionHit = detectMentionHit(state.value)
    val filteredOptions = when {
        mentionHit == null -> emptyList()
        mentionHit.pattern.isEmpty() -> options
        filterOption != null -> options.filter { filterOption(mentionHit.pattern, it) }
        else -> options.filter {
            optionLabel(it).contains(mentionHit.pattern, ignoreCase = true) ||
                optionValue(it).contains(mentionHit.pattern, ignoreCase = true)
        }
    }

    state.isOpen = mentionHit != null && (filteredOptions.isNotEmpty() || loading)
    state.mentionQuery = mentionHit?.pattern.orEmpty()
    state.activePrefix = mentionHit?.prefix ?: prefixes.first()

    var lastSearchKey by remember { mutableStateOf("") }
    if (mentionHit != null) {
        val currentKey = "${mentionHit.prefix}|${mentionHit.pattern}"
        if (currentKey != lastSearchKey) {
            lastSearchKey = currentKey
            onSearch?.invoke(mentionHit.pattern, mentionHit.prefix)
        }
    } else {
        lastSearchKey = ""
    }

    fun replaceCurrentMention(option: MentionOption) {
        val hit = mentionHit ?: return
        val before = state.value.substring(0, hit.start)
        val inserted = "${hit.prefix}${optionValue(option)}$split"
        state.value = before + inserted
        state.isOpen = false
        onSelect?.invoke(option, hit.prefix)
        onInput?.invoke(state.value)
        onChange?.invoke(state.value)
    }

    Column(modifier = modifier) {
        NexusInput(
            value = state.value,
            onValueChange = { newValue ->
                if (whole && newValue.length + 1 == state.value.length && state.value.startsWith(newValue)) {
                    val oldHit = detectMentionHit(state.value)
                    if (oldHit != null && oldHit.start + oldHit.prefix.length + oldHit.pattern.length == state.value.length) {
                        val shouldRemoveWhole = checkIsWhole?.invoke(oldHit.pattern, oldHit.prefix) ?: true
                        if (shouldRemoveWhole) {
                            val before = state.value.substring(0, oldHit.start)
                            state.value = before
                            onWholeRemove?.invoke(oldHit.pattern, oldHit.prefix)
                            onInput?.invoke(state.value)
                            onChange?.invoke(state.value)
                            return@NexusInput
                        }
                    }
                }
                state.value = newValue
                onInput?.invoke(newValue)
            },
            type = type,
            size = size,
            placeholder = placeholder,
            disabled = disabled,
            readonly = readonly,
            onFocus = onFocus,
            onBlur = onBlur,
            onChange = onChange,
            modifier = Modifier.fillMaxWidth(),
        )

        if (state.isOpen) {
            Popup(
                alignment = if (placement == NexusMentionPlacement.Bottom) Alignment.TopStart else Alignment.BottomStart,
                properties = PopupProperties(focusable = false),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(shadows.light.elevation, shapes.base)
                        .clip(shapes.base)
                        .background(colorScheme.fill.blank)
                        .heightIn(max = 220.dp),
                ) {
                    if (header != null) {
                        Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                            header()
                        }
                    }

                    if (loading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                        ) {
                            if (loadingContent != null) {
                                loadingContent()
                            } else {
                                NexusText(
                                    text = "Loading...",
                                    color = colorScheme.text.secondary,
                                    style = typography.small,
                                )
                            }
                        }
                    } else {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            filteredOptions.forEachIndexed { index, option ->
                                val disabledOption = optionDisabled(option)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .then(
                                            if (!disabledOption) {
                                                Modifier
                                                    .clickable { replaceCurrentMention(option) }
                                                    .pointerHoverIcon(PointerIcon.Hand)
                                            } else {
                                                Modifier
                                            }
                                        )
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                ) {
                                    if (labelContent != null) {
                                        labelContent(option, index)
                                    } else {
                                        NexusText(
                                            text = optionLabel(option),
                                            color = if (disabledOption) colorScheme.text.disabled else colorScheme.text.regular,
                                            style = typography.base,
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (footer != null) {
                        Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                            footer()
                        }
                    }
                }
            }
        }
    }
}
