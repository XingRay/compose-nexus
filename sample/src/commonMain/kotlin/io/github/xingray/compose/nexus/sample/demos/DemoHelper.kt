package io.github.xingray.compose.nexus.sample.demos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.controls.NexusDivider
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.theme.NexusTheme

@Composable
fun DemoSection(
    title: String,
    description: String = "",
    content: @Composable () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(16.dp))
        NexusText(text = title, color = colorScheme.text.primary, style = typography.medium)
        if (description.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            NexusText(text = description, color = colorScheme.text.secondary, style = typography.small)
        }
        Spacer(modifier = Modifier.height(12.dp))
        content()
        Spacer(modifier = Modifier.height(16.dp))
        NexusDivider()
    }
}

@Composable
fun DemoPage(
    title: String,
    description: String = "",
    content: @Composable () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    Column(modifier = Modifier.fillMaxWidth()) {
        NexusText(text = title, color = colorScheme.text.primary, style = typography.extraLarge)
        if (description.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            NexusText(text = description, color = colorScheme.text.secondary, style = typography.base)
        }
        Spacer(modifier = Modifier.height(8.dp))
        NexusDivider()
        content()
    }
}
