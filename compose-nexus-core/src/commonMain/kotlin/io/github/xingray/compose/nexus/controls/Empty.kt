package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import io.github.xingray.compose.nexus.theme.NexusTheme

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
    imageUrl: String = "",
    imageSize: Int? = null,
    description: String = "No Data",
    image: (@Composable () -> Unit)? = null,
    descriptionSlot: (@Composable () -> Unit)? = null,
    actions: (@Composable () -> Unit)? = null,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val resolvedImageSize = (imageSize ?: 96).dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Image slot has highest priority, then imageUrl, then fallback illustration.
        if (image != null) {
            Box(modifier = Modifier.size(resolvedImageSize)) {
                image()
            }
        } else if (imageUrl.isNotBlank()) {
            Box(
                modifier = Modifier
                    .size(resolvedImageSize)
                    .background(colorScheme.fill.light, _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base)
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = imageUrl,
                    color = colorScheme.text.placeholder,
                    style = typography.extraSmall,
                )
            }
        } else {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = "□",
                color = colorScheme.text.placeholder,
                style = typography.extraLarge,
                modifier = Modifier.size(resolvedImageSize),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (descriptionSlot != null) {
            descriptionSlot()
        } else {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = description,
                color = colorScheme.text.secondary,
                style = typography.base,
            )
        }

        // Actions
        if (actions != null) {
            Spacer(modifier = Modifier.height(16.dp))
            actions()
        }
    }
}
