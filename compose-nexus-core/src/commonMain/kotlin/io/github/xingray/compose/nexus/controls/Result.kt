package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.NexusTheme

enum class NexusResultIcon {
    Primary,
    Success,
    Warning,
    Info,
    Error,
}

@Composable
fun NexusResult(
    modifier: Modifier = Modifier,
    title: String = "",
    subTitle: String = "",
    icon: NexusResultIcon = NexusResultIcon.Info,
    iconSlot: (@Composable () -> Unit)? = null,
    titleSlot: (@Composable () -> Unit)? = null,
    subTitleSlot: (@Composable () -> Unit)? = null,
    extra: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val iconColor = when (icon) {
        NexusResultIcon.Primary -> colorScheme.primary.base
        NexusResultIcon.Success -> colorScheme.success.base
        NexusResultIcon.Warning -> colorScheme.warning.base
        NexusResultIcon.Info -> colorScheme.info.base
        NexusResultIcon.Error -> colorScheme.danger.base
    }
    val iconText = when (icon) {
        NexusResultIcon.Primary -> "P"
        NexusResultIcon.Success -> "✓"
        NexusResultIcon.Warning -> "!"
        NexusResultIcon.Info -> "i"
        NexusResultIcon.Error -> "×"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        if (iconSlot != null) {
            iconSlot()
        } else {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(iconColor.copy(alpha = 0.14f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                NexusText(
                    text = iconText,
                    color = iconColor,
                    style = typography.extraLarge,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (titleSlot != null) {
            titleSlot()
        } else {
            NexusText(
                text = title,
                color = colorScheme.text.primary,
                style = typography.large,
            )
        }

        if (subTitleSlot != null || subTitle.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            if (subTitleSlot != null) {
                subTitleSlot()
            } else {
                NexusText(
                    text = subTitle,
                    color = colorScheme.text.secondary,
                    style = typography.small,
                )
            }
        }

        if (extra != null) {
            Spacer(modifier = Modifier.height(16.dp))
            extra()
        }
    }
}
