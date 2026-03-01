package io.github.xingray.compose_nexus.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType

/**
 * A single tour step definition.
 */
data class TourStep(
    val title: String,
    val description: String = "",
)

/**
 * Tour state holder.
 */
@Stable
class TourState(
    val steps: List<TourStep>,
) {
    var currentStep by mutableIntStateOf(0)
        private set
    var isActive by mutableStateOf(false)
        private set

    fun start() {
        currentStep = 0
        isActive = true
    }

    fun next() {
        if (currentStep < steps.lastIndex) {
            currentStep++
        } else {
            finish()
        }
    }

    fun prev() {
        if (currentStep > 0) {
            currentStep--
        }
    }

    fun finish() {
        isActive = false
    }

    fun goTo(step: Int) {
        currentStep = step.coerceIn(0, steps.lastIndex.coerceAtLeast(0))
    }
}

@Composable
fun rememberTourState(
    steps: List<TourStep>,
): TourState = remember(steps) { TourState(steps) }

/**
 * Element Plus Tour — a guided tour overlay with step-by-step instructions.
 *
 * When the tour is active, it shows a full-screen overlay with the current step content.
 *
 * @param state Tour state.
 * @param modifier Modifier.
 * @param onFinish Callback when the tour is completed.
 */
@Composable
fun NexusTour(
    state: TourState,
    modifier: Modifier = Modifier,
    onFinish: (() -> Unit)? = null,
) {
    if (!state.isActive) return

    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows

    val step = state.steps.getOrNull(state.currentStep) ?: return

    Popup(
        properties = PopupProperties(focusable = true),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.overlay.lighter),
            contentAlignment = Alignment.Center,
        ) {
            // Consume backdrop clicks
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { /* no-op */ },
            )

            // Tour card
            Column(
                modifier = modifier
                    .width(400.dp)
                    .shadow(shadows.default.elevation, shapes.base)
                    .clip(shapes.base)
                    .background(colorScheme.fill.blank)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { /* consume */ }
                    .padding(24.dp),
            ) {
                // Title
                NexusText(
                    text = step.title,
                    color = colorScheme.text.primary,
                    style = typography.large,
                )

                // Description
                if (step.description.isNotEmpty()) {
                    NexusText(
                        text = step.description,
                        color = colorScheme.text.regular,
                        style = typography.base,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                }

                // Step indicator
                NexusText(
                    text = "${state.currentStep + 1} / ${state.steps.size}",
                    color = colorScheme.text.secondary,
                    style = typography.extraSmall,
                    modifier = Modifier.padding(top = 16.dp),
                )

                // Navigation buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    if (state.currentStep > 0) {
                        NexusButton(
                            onClick = { state.prev() },
                        ) {
                            NexusText(text = "Previous")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    if (state.currentStep < state.steps.lastIndex) {
                        NexusButton(
                            onClick = { state.next() },
                            type = NexusType.Primary,
                        ) {
                            NexusText(text = "Next")
                        }
                    } else {
                        NexusButton(
                            onClick = {
                                state.finish()
                                onFinish?.invoke()
                            },
                            type = NexusType.Primary,
                        ) {
                            NexusText(text = "Finish")
                        }
                    }
                }
            }
        }
    }
}
