package io.github.xingray.compose_nexus.templates

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
import io.github.xingray.compose_nexus.controls.NexusButton
import io.github.xingray.compose_nexus.controls.NexusDivider
import io.github.xingray.compose_nexus.controls.NexusSteps
import io.github.xingray.compose_nexus.controls.NexusText
import io.github.xingray.compose_nexus.controls.StepItem
import io.github.xingray.compose_nexus.controls.StepsState
import io.github.xingray.compose_nexus.controls.rememberStepsState
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType

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
    steps: List<StepItem>,
    modifier: Modifier = Modifier,
    title: String = "Wizard",
    stepsState: StepsState = rememberStepsState(steps = steps),
    onFinish: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    stepContent: @Composable (stepIndex: Int) -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background.page)
            .padding(20.dp),
    ) {
        // Title
        NexusText(
            text = title,
            color = colorScheme.text.primary,
            style = typography.extraLarge,
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Steps indicator
        NexusSteps(
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
        NexusDivider()
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Left: Cancel
            NexusButton(onClick = { onCancel?.invoke() }) {
                NexusText(text = "Cancel")
            }

            // Right: Prev / Next / Submit
            Row {
                if (stepsState.active > 0) {
                    NexusButton(onClick = { stepsState.prev() }) {
                        NexusText(text = "Previous")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                if (stepsState.active < steps.lastIndex) {
                    NexusButton(
                        onClick = { stepsState.next() },
                        type = NexusType.Primary,
                    ) {
                        NexusText(text = "Next")
                    }
                } else {
                    NexusButton(
                        onClick = { onFinish?.invoke() },
                        type = NexusType.Success,
                    ) {
                        NexusText(text = "Submit")
                    }
                }
            }
        }
    }
}
