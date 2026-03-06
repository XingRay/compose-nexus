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
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType

/**
 * FormPage — a full-page form template with header, form body, and submit/reset actions.
 *
 * @param modifier Modifier.
 * @param title Page title.
 * @param subtitle Optional subtitle or description.
 * @param onSubmit Callback when submit is clicked.
 * @param onReset Callback when reset is clicked.
 * @param submitText Submit button label.
 * @param resetText Reset button label.
 * @param formContent Form body content (typically NexusForm + NexusFormItems).
 */
@Composable
fun NexusFormPage(
    modifier: Modifier = Modifier,
    title: String = "Form",
    subtitle: String? = null,
    onSubmit: (() -> Unit)? = null,
    onReset: (() -> Unit)? = null,
    submitText: String = "Submit",
    resetText: String = "Reset",
    formContent: @Composable () -> Unit,
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
        // Header
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
            text = title,
            color = colorScheme.text.primary,
            style = typography.extraLarge,
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(4.dp))
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = subtitle,
                color = colorScheme.text.secondary,
                style = typography.base,
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Form card
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(shapes.base)
                .background(colorScheme.fill.blank)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            formContent()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Action buttons
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDivider()
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            if (onReset != null) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(onClick = onReset) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = resetText)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                onClick = { onSubmit?.invoke() },
                type = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary,
            ) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = submitText)
            }
        }
    }
}
