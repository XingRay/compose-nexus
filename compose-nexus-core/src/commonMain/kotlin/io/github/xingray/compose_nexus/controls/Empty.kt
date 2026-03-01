package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Element Plus Empty — an empty state placeholder with icon, description, and optional actions.
 *
 * @param modifier Modifier.
 * @param description Description text.
 * @param image Custom image/icon composable. Defaults to a simple "No Data" text icon.
 * @param actions Optional bottom slot for action buttons.
 */
@Composable
fun NexusEmpty(
    modifier: Modifier = Modifier,
    description: String = "No Data",
    image: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Image / icon
        if (image != null) {
            image()
        } else {
            NexusText(
                text = "□",
                color = colorScheme.text.placeholder,
                style = typography.extraLarge,
                modifier = Modifier.size(64.dp),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        NexusText(
            text = description,
            color = colorScheme.text.secondary,
            style = typography.base,
        )

        // Actions
        if (actions != null) {
            Spacer(modifier = Modifier.height(16.dp))
            actions()
        }
    }
}
