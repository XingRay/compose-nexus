package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * Autocomplete state holder.
 */
@Stable
class AutocompleteState(
    initialValue: String = "",
) {
    var value by mutableStateOf(initialValue)
    var isOpen by mutableStateOf(false)
        internal set
    var suggestions by mutableStateOf(emptyList<String>())
        internal set
    var highlightedIndex by mutableIntStateOf(-1)
        internal set
    var loading by mutableStateOf(false)
        internal set

    fun close() {
        isOpen = false
    }
}

@Composable
fun rememberAutocompleteState(
    initialValue: String = "",
): AutocompleteState = remember { AutocompleteState(initialValue) }

/**
 * Element Plus Autocomplete — input suggestions with custom item/header/footer/loading slots.
 */
@Composable
fun NexusAutocomplete(
    state: AutocompleteState = rememberAutocompleteState(),
    modifier: Modifier = Modifier,
    placeholder: String = "",
    size: ComponentSize = ComponentSize.Default,
    clearable: Boolean = true,
    disabled: Boolean = false,
    debounceMs: Long = 300L,
    triggerOnFocus: Boolean = true,
    selectWhenUnmatched: Boolean = false,
    hideLoading: Boolean = false,
    highlightFirstItem: Boolean = false,
    fitInputWidth: Boolean = true,
    fetchSuggestions: suspend (query: String) -> List<String> = { emptyList() },
    onSelect: ((String) -> Unit)? = null,
    onInput: ((String) -> Unit)? = null,
    onChange: ((String) -> Unit)? = null,
    onFocus: (() -> Unit)? = null,
    onBlur: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    itemContent: (@Composable (item: String) -> Unit)? = null,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    loadingContent: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows
    val density = LocalDensity.current

    var isFocused by remember { mutableStateOf(false) }
    var popupOffset by remember { mutableStateOf(IntOffset.Zero) }
    var inputWidthPx by remember { mutableIntStateOf(0) }

    val showPopup = state.isOpen && !disabled && (
        state.loading && !hideLoading ||
            state.suggestions.isNotEmpty() ||
            header != null ||
            footer != null
        )

    LaunchedEffect(state.value, isFocused, disabled, debounceMs, triggerOnFocus) {
        if (disabled) {
            state.loading = false
            state.suggestions = emptyList()
            state.isOpen = false
            return@LaunchedEffect
        }

        val shouldFetch = state.value.isNotBlank() || (triggerOnFocus && isFocused)
        if (!shouldFetch) {
            state.loading = false
            state.suggestions = emptyList()
            state.isOpen = false
            state.highlightedIndex = -1
            return@LaunchedEffect
        }

        state.loading = true
        if (debounceMs > 0) {
            delay(debounceMs)
        }
        val result = fetchSuggestions(state.value)
        state.loading = false
        state.suggestions = result
        state.highlightedIndex = if (highlightFirstItem && result.isNotEmpty()) 0 else -1
        state.isOpen = isFocused && (
            result.isNotEmpty() ||
                header != null ||
                footer != null ||
                (!hideLoading && state.loading)
            )
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    val position: Offset = coordinates.positionInWindow()
                    popupOffset = IntOffset(
                        x = position.x.roundToInt(),
                        y = (position.y + coordinates.size.height).roundToInt(),
                    )
                    inputWidthPx = coordinates.size.width
                },
        ) {
            NexusInput(
                value = state.value,
                onValueChange = { newValue ->
                    val hadValue = state.value.isNotEmpty()
                    state.value = newValue
                    onInput?.invoke(newValue)
                    onChange?.invoke(newValue)
                    if (newValue.isEmpty() && hadValue) {
                        onClear?.invoke()
                        state.isOpen = false
                    }
                },
                placeholder = placeholder,
                size = size,
                clearable = clearable,
                disabled = disabled,
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (selectWhenUnmatched && state.value.isNotBlank()) {
                            val hasMatch = state.suggestions.any { it.equals(state.value, ignoreCase = true) }
                            if (!hasMatch) {
                                onSelect?.invoke(state.value)
                                state.close()
                            }
                        }
                    },
                ),
                onFocusChanged = { focused ->
                    if (isFocused == focused) {
                        return@NexusInput
                    }
                    isFocused = focused
                    if (focused) {
                        onFocus?.invoke()
                        if (triggerOnFocus || state.value.isNotBlank()) {
                            state.isOpen = true
                        }
                    } else {
                        onBlur?.invoke()
                        state.close()
                    }
                },
            )
        }

        if (showPopup) {
            Popup(
                alignment = Alignment.TopStart,
                offset = popupOffset,
                properties = PopupProperties(focusable = false),
            ) {
                val popupModifier = if (fitInputWidth && inputWidthPx > 0) {
                    Modifier.width(with(density) { inputWidthPx.toDp() })
                } else {
                    Modifier.widthIn(min = 180.dp)
                }
                Column(
                    modifier = popupModifier
                        .shadow(shadows.light.elevation, shapes.base)
                        .clip(shapes.base)
                        .background(colorScheme.fill.blank)
                        .heightIn(max = 240.dp)
                        .verticalScroll(rememberScrollState()),
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

                    if (state.loading && !hideLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
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
                        state.suggestions.forEachIndexed { index, suggestion ->
                            val background = if (state.highlightedIndex == index) {
                                colorScheme.primary.light9
                            } else {
                                colorScheme.fill.blank
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(background)
                                    .clickable {
                                        state.value = suggestion
                                        state.close()
                                        onSelect?.invoke(suggestion)
                                    }
                                    .pointerHoverIcon(PointerIcon.Hand)
                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                            ) {
                                if (itemContent != null) {
                                    itemContent(suggestion)
                                } else {
                                    NexusText(
                                        text = suggestion,
                                        color = colorScheme.text.regular,
                                        style = typography.base,
                                    )
                                }
                            }
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
