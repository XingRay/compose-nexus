package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme

@Composable
fun NexusPageHeader(
    modifier: Modifier = Modifier,
    icon: String? = "←",
    title: String = "",
    content: String = "",
    onBack: (() -> Unit)? = null,
    breadcrumb: (@Composable () -> Unit)? = null,
    iconSlot: (@Composable () -> Unit)? = null,
    titleSlot: (@Composable () -> Unit)? = null,
    contentSlot: (@Composable () -> Unit)? = null,
    extra: (@Composable () -> Unit)? = null,
    main: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (breadcrumb != null) {
            breadcrumb()
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (iconSlot != null || !icon.isNullOrEmpty()) {
                Row(
                    modifier = Modifier
                        .then(
                            if (onBack != null) {
                                Modifier
                                    .clickable { onBack() }
                                    .pointerHoverIcon(PointerIcon.Hand)
                            } else {
                                Modifier
                            }
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (iconSlot != null) {
                        iconSlot()
                    } else {
                        NexusText(
                            text = icon ?: "",
                            color = colorScheme.primary.base,
                            style = typography.base,
                        )
                    }
                }
            }

            if (titleSlot != null) {
                titleSlot()
            } else if (title.isNotBlank()) {
                NexusText(
                    text = title,
                    color = colorScheme.text.primary,
                    style = typography.base,
                )
            }

            if (contentSlot != null) {
                contentSlot()
            } else if (content.isNotBlank()) {
                NexusText(
                    text = content,
                    color = colorScheme.text.secondary,
                    style = typography.base,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (extra != null) {
                extra()
            }
        }

        if (main != null) {
            main()
        }
    }
}

