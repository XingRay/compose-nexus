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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.controls.NexusButton
import io.github.xingray.compose_nexus.controls.NexusDivider
import io.github.xingray.compose_nexus.controls.NexusText
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * A key-value description entry for [NexusDetailPage].
 */
data class DetailField(
    val label: String,
    val value: String,
)

/**
 * DetailPage — a detail view template with header, description fields, and tabbed sections.
 *
 * Layout: PageHeader (title + back button) → Description fields → Tab sections.
 *
 * @param modifier Modifier.
 * @param title Page title.
 * @param fields List of label-value description fields.
 * @param onBack Callback for the back button.
 * @param headerExtra Optional extra content in the header area (e.g., action buttons).
 * @param sections Optional tabbed or sectioned content below the fields.
 */
@Composable
fun NexusDetailPage(
    modifier: Modifier = Modifier,
    title: String = "Detail",
    fields: List<DetailField> = emptyList(),
    onBack: (() -> Unit)? = null,
    headerExtra: (@Composable () -> Unit)? = null,
    sections: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background.page)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // Page header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (onBack != null) {
                NexusButton(onClick = onBack) {
                    NexusText(text = "← Back")
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
            NexusText(
                text = title,
                color = colorScheme.text.primary,
                style = typography.extraLarge,
                modifier = Modifier.weight(1f),
            )
            if (headerExtra != null) {
                headerExtra()
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Description fields card
        if (fields.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shapes.base)
                    .background(colorScheme.fill.blank)
                    .padding(20.dp),
            ) {
                fields.forEachIndexed { index, field ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                    ) {
                        NexusText(
                            text = field.label,
                            color = colorScheme.text.secondary,
                            style = typography.base,
                            modifier = Modifier.width(120.dp),
                        )
                        NexusText(
                            text = field.value,
                            color = colorScheme.text.primary,
                            style = typography.base,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    if (index < fields.lastIndex) {
                        NexusDivider()
                    }
                }
            }
        }

        // Tabbed sections
        if (sections != null) {
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shapes.base)
                    .background(colorScheme.fill.blank)
                    .padding(20.dp),
            ) {
                sections()
            }
        }
    }
}
