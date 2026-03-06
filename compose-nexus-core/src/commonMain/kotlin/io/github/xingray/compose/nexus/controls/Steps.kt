package io.github.xingray.compose.nexus.controls

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.NexusTheme

enum class StepStatus {
    Wait,
    Process,
    Finish,
    Error,
    Success,
}

enum class StepsDirection {
    Horizontal,
    Vertical,
}

data class StepItem(
    val title: String,
    val description: String = "",
    val icon: (@Composable () -> Unit)? = null,
    val status: io.github.xingray.compose.nexus.controls.StepStatus? = null,
)

@Stable
class StepsState(
    initialActive: Int = 0,
    val steps: List<io.github.xingray.compose.nexus.controls.StepItem>,
) {
    var active by mutableIntStateOf(initialActive.coerceIn(0, steps.lastIndex.coerceAtLeast(0)))
        private set

    fun goTo(index: Int) {
        active = index.coerceIn(0, steps.lastIndex.coerceAtLeast(0))
    }

    fun next() = goTo(active + 1)
    fun prev() = goTo(active - 1)

    fun resolveStatus(
        index: Int,
        processStatus: io.github.xingray.compose.nexus.controls.StepStatus,
        finishStatus: io.github.xingray.compose.nexus.controls.StepStatus,
    ): io.github.xingray.compose.nexus.controls.StepStatus {
        val step = steps.getOrNull(index) ?: return _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Wait
        if (step.status != null) return step.status
        return when {
            index < active -> finishStatus
            index == active -> processStatus
            else -> _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Wait
        }
    }
}

@Composable
fun rememberStepsState(
    initialActive: Int = 0,
    steps: List<io.github.xingray.compose.nexus.controls.StepItem>,
): io.github.xingray.compose.nexus.controls.StepsState = remember(steps) {
    _root_ide_package_.io.github.xingray.compose.nexus.controls.StepsState(initialActive, steps)
}

@Composable
fun NexusSteps(
    state: io.github.xingray.compose.nexus.controls.StepsState,
    modifier: Modifier = Modifier,
    direction: io.github.xingray.compose.nexus.controls.StepsDirection = _root_ide_package_.io.github.xingray.compose.nexus.controls.StepsDirection.Horizontal,
    space: Dp? = null,
    processStatus: io.github.xingray.compose.nexus.controls.StepStatus = _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Process,
    finishStatus: io.github.xingray.compose.nexus.controls.StepStatus = _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Finish,
    alignCenter: Boolean = false,
    simple: Boolean = false,
) {
    if (simple) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.SimpleSteps(
            state = state,
            modifier = modifier,
            processStatus = processStatus,
            finishStatus = finishStatus,
        )
        return
    }

    when (direction) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.StepsDirection.Horizontal -> _root_ide_package_.io.github.xingray.compose.nexus.controls.HorizontalSteps(
            state = state,
            modifier = modifier,
            space = space,
            processStatus = processStatus,
            finishStatus = finishStatus,
            alignCenter = alignCenter,
        )
        _root_ide_package_.io.github.xingray.compose.nexus.controls.StepsDirection.Vertical -> _root_ide_package_.io.github.xingray.compose.nexus.controls.VerticalSteps(
            state = state,
            modifier = modifier,
            processStatus = processStatus,
            finishStatus = finishStatus,
            alignCenter = alignCenter,
        )
    }
}

@Composable
private fun SimpleSteps(
    state: io.github.xingray.compose.nexus.controls.StepsState,
    modifier: Modifier,
    processStatus: io.github.xingray.compose.nexus.controls.StepStatus,
    finishStatus: io.github.xingray.compose.nexus.controls.StepStatus,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.fill.light, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        state.steps.forEachIndexed { index, step ->
            val status = state.resolveStatus(index, processStatus, finishStatus)
            val color = when (status) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Process -> colorScheme.primary.base
                _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Error -> colorScheme.danger.base
                _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Finish, _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Success -> colorScheme.text.primary
                _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Wait -> colorScheme.text.placeholder
            }

            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = step.title,
                color = color,
                style = typography.small,
            )
            if (index < state.steps.lastIndex) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = "  >  ",
                    color = colorScheme.text.placeholder,
                    style = typography.small,
                )
            }
        }
    }
}

@Composable
private fun HorizontalSteps(
    state: io.github.xingray.compose.nexus.controls.StepsState,
    modifier: Modifier,
    space: Dp?,
    processStatus: io.github.xingray.compose.nexus.controls.StepStatus,
    finishStatus: io.github.xingray.compose.nexus.controls.StepStatus,
    alignCenter: Boolean,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        state.steps.forEachIndexed { index, step ->
            _root_ide_package_.io.github.xingray.compose.nexus.controls.StepHead(
                index = index,
                step = step,
                status = state.resolveStatus(index, processStatus, finishStatus),
                alignCenter = alignCenter,
                modifier = if (space != null) Modifier.width(space) else Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun VerticalSteps(
    state: io.github.xingray.compose.nexus.controls.StepsState,
    modifier: Modifier,
    processStatus: io.github.xingray.compose.nexus.controls.StepStatus,
    finishStatus: io.github.xingray.compose.nexus.controls.StepStatus,
    alignCenter: Boolean,
) {
    Column(
        modifier = modifier,
    ) {
        state.steps.forEachIndexed { index, step ->
            val status = state.resolveStatus(index, processStatus, finishStatus)
            Row(
                modifier = Modifier.padding(bottom = if (index < state.steps.lastIndex) 24.dp else 0.dp),
            ) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.StepCircle(index = index, status = status, step = step)
                Spacer(modifier = Modifier.width(12.dp))
                Column(horizontalAlignment = if (alignCenter) Alignment.CenterHorizontally else Alignment.Start) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.StepTextContent(step = step, status = status, alignCenter = alignCenter)
                }
            }
        }
    }
}

@Composable
private fun StepHead(
    index: Int,
    step: io.github.xingray.compose.nexus.controls.StepItem,
    status: io.github.xingray.compose.nexus.controls.StepStatus,
    alignCenter: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = if (alignCenter) Alignment.CenterHorizontally else Alignment.Start,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (index > 0) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(2.dp)
                        .background(_root_ide_package_.io.github.xingray.compose.nexus.controls.stepLineColor(status)),
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            _root_ide_package_.io.github.xingray.compose.nexus.controls.StepCircle(index = index, status = status, step = step)
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(8.dp))
        _root_ide_package_.io.github.xingray.compose.nexus.controls.StepTextContent(step = step, status = status, alignCenter = alignCenter)
    }
}

@Composable
private fun StepCircle(
    index: Int,
    status: io.github.xingray.compose.nexus.controls.StepStatus,
    step: io.github.xingray.compose.nexus.controls.StepItem,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography

    val bgColor: Color
    val contentColor: Color
    val borderColor: Color
    val displayText: String

    when (status) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Finish, _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Success -> {
            bgColor = colorScheme.primary.base
            contentColor = colorScheme.white
            borderColor = colorScheme.primary.base
            displayText = "✓"
        }
        _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Process -> {
            bgColor = colorScheme.primary.base
            contentColor = colorScheme.white
            borderColor = colorScheme.primary.base
            displayText = (index + 1).toString()
        }
        _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Error -> {
            bgColor = Color.Transparent
            contentColor = colorScheme.danger.base
            borderColor = colorScheme.danger.base
            displayText = "✕"
        }
        _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Wait -> {
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
        if (step.icon != null) {
            step.icon()
        } else {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = displayText,
                color = contentColor,
                style = typography.extraSmall,
            )
        }
    }
}

@Composable
private fun StepTextContent(
    step: io.github.xingray.compose.nexus.controls.StepItem,
    status: io.github.xingray.compose.nexus.controls.StepStatus,
    alignCenter: Boolean,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography

    val titleColor = when (status) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Process -> colorScheme.primary.base
        _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Error -> colorScheme.danger.base
        _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Finish, _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Success -> colorScheme.text.primary
        _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Wait -> colorScheme.text.placeholder
    }

    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
        text = step.title,
        color = titleColor,
        style = typography.small,
    )
    if (step.description.isNotEmpty()) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
            text = step.description,
            color = colorScheme.text.secondary,
            style = typography.extraSmall,
            modifier = if (alignCenter) Modifier else Modifier,
        )
    }
}

@Composable
private fun stepLineColor(currentStatus: io.github.xingray.compose.nexus.controls.StepStatus): Color {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    return when (currentStatus) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.StepStatus.Wait -> colorScheme.border.lighter
        else -> colorScheme.primary.base
    }
}

