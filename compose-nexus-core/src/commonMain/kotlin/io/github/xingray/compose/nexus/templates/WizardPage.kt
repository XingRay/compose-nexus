package io.github.xingray.compose.nexus.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.controls.NexusButton
import io.github.xingray.compose.nexus.controls.NexusDivider
import io.github.xingray.compose.nexus.controls.NexusSteps
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.controls.StepItem
import io.github.xingray.compose.nexus.controls.StepsState
import io.github.xingray.compose.nexus.controls.rememberStepsState
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType

/**
 * WizardPage — a multi-step wizard page template.
 *
 * Layout: Steps indicator → Step content → Previous/Next/Submit buttons.
 *
 * @param steps Step definitions.
 * @param modifier Modifier.
 * @param title Page title.
 * @param stepsState Steps state for tracking active step.
 * @param onFinish Callback when the wizard is completed (last step submitted).
 * @param onCancel Callback for cancel.
 * @param stepContent Content composable for each step index.
 */
@Composable
fun NexusWizardPage(
    steps: List<io.github.xingray.compose.nexus.controls.StepItem>,
    modifier: Modifier = Modifier,
    title: String = "Wizard",
    stepsState: io.github.xingray.compose.nexus.controls.StepsState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberStepsState(steps = steps),
    onFinish: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    stepContent: @Composable (stepIndex: Int) -> Unit,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background.page)
            .padding(20.dp),
    ) {
        // Title
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
            text = title,
            color = colorScheme.text.primary,
            style = typography.extraLarge,
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Steps indicator
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusSteps(
            state = stepsState,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shapes.base)
                .background(colorScheme.fill.blank)
                .padding(20.dp),
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Step content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(shapes.base)
                .background(colorScheme.fill.blank)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            stepContent(stepsState.active)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation buttons
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDivider()
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Left: Cancel
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(onClick = { onCancel?.invoke() }) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "Cancel")
            }

            // Right: Prev / Next / Submit
            Row {
                if (stepsState.active > 0) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(onClick = { stepsState.prev() }) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "Previous")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                if (stepsState.active < steps.lastIndex) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                        onClick = { stepsState.next() },
                        type = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary,
                    ) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "Next")
                    }
                } else {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                        onClick = { onFinish?.invoke() },
                        type = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Success,
                    ) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "Submit")
                    }
                }
            }
        }
    }
}
