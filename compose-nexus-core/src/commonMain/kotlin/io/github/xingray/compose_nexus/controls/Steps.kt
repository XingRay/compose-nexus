package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Step status.
 */
enum class StepStatus {
    Wait,
    Process,
    Finish,
    Error,
    Success,
}

/**
 * Step direction.
 */
enum class StepsDirection {
    Horizontal,
    Vertical,
}

/**
 * A single step definition.
 */
data class StepItem(
    val title: String,
    val description: String = "",
    val status: StepStatus? = null, // null = auto-resolved from active
)

/**
 * Steps state holder.
 */
@Stable
class StepsState(
    initialActive: Int = 0,
    val steps: List<StepItem>,
) {
    var active by mutableIntStateOf(initialActive.coerceIn(0, steps.lastIndex.coerceAtLeast(0)))
        private set

    fun goTo(index: Int) {
        active = index.coerceIn(0, steps.lastIndex.coerceAtLeast(0))
    }

    fun next() = goTo(active + 1)
    fun prev() = goTo(active - 1)

    fun resolveStatus(index: Int): StepStatus {
        val step = steps.getOrNull(index) ?: return StepStatus.Wait
        if (step.status != null) return step.status
        return when {
            index < active -> StepStatus.Finish
            index == active -> StepStatus.Process
            else -> StepStatus.Wait
        }
    }
}

@Composable
fun rememberStepsState(
    initialActive: Int = 0,
    steps: List<StepItem>,
): StepsState = remember(steps) {
    StepsState(initialActive, steps)
}

/**
 * Element Plus Steps — a step progress indicator.
 *
 * @param state Steps state.
 * @param modifier Modifier.
 * @param direction Horizontal or vertical layout.
 */
@Composable
fun NexusSteps(
    state: StepsState,
    modifier: Modifier = Modifier,
    direction: StepsDirection = StepsDirection.Horizontal,
) {
    when (direction) {
        StepsDirection.Horizontal -> HorizontalSteps(state, modifier)
        StepsDirection.Vertical -> VerticalSteps(state, modifier)
    }
}

@Composable
private fun HorizontalSteps(
    state: StepsState,
    modifier: Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        state.steps.forEachIndexed { index, step ->
            StepHead(
                index = index,
                step = step,
                status = state.resolveStatus(index),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun VerticalSteps(
    state: StepsState,
    modifier: Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        state.steps.forEachIndexed { index, step ->
            val status = state.resolveStatus(index)
            Row(
                modifier = Modifier.padding(bottom = if (index < state.steps.lastIndex) 24.dp else 0.dp),
            ) {
                StepCircle(index = index, status = status)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    StepTextContent(step = step, status = status)
                }
            }
        }
    }
}

@Composable
private fun StepHead(
    index: Int,
    step: StepItem,
    status: StepStatus,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left line
            if (index > 0) {
                val lineColor = stepLineColor(status)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(lineColor),
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            StepCircle(index = index, status = status)

            // Right line placeholder (weight balance)
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))
        StepTextContent(step = step, status = status)
    }
}

@Composable
private fun StepCircle(
    index: Int,
    status: StepStatus,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val bgColor: Color
    val contentColor: Color
    val borderColor: Color
    val displayText: String

    when (status) {
        StepStatus.Finish, StepStatus.Success -> {
            bgColor = colorScheme.primary.base
            contentColor = colorScheme.white
            borderColor = colorScheme.primary.base
            displayText = "✓"
        }
        StepStatus.Process -> {
            bgColor = colorScheme.primary.base
            contentColor = colorScheme.white
            borderColor = colorScheme.primary.base
            displayText = (index + 1).toString()
        }
        StepStatus.Error -> {
            bgColor = Color.Transparent
            contentColor = colorScheme.danger.base
            borderColor = colorScheme.danger.base
            displayText = "✕"
        }
        StepStatus.Wait -> {
            bgColor = Color.Transparent
            contentColor = colorScheme.text.placeholder
            borderColor = colorScheme.text.placeholder
            displayText = (index + 1).toString()
        }
    }

    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(bgColor)
            .border(2.dp, borderColor, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        NexusText(
            text = displayText,
            color = contentColor,
            style = typography.extraSmall,
        )
    }
}

@Composable
private fun StepTextContent(
    step: StepItem,
    status: StepStatus,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val titleColor = when (status) {
        StepStatus.Process -> colorScheme.primary.base
        StepStatus.Error -> colorScheme.danger.base
        StepStatus.Finish, StepStatus.Success -> colorScheme.text.primary
        StepStatus.Wait -> colorScheme.text.placeholder
    }

    NexusText(
        text = step.title,
        color = titleColor,
        style = typography.small,
    )
    if (step.description.isNotEmpty()) {
        NexusText(
            text = step.description,
            color = colorScheme.text.secondary,
            style = typography.extraSmall,
        )
    }
}

@Composable
private fun stepLineColor(currentStatus: StepStatus): Color {
    val colorScheme = NexusTheme.colorScheme
    return when (currentStatus) {
        StepStatus.Wait -> colorScheme.border.lighter
        else -> colorScheme.primary.base
    }
}
