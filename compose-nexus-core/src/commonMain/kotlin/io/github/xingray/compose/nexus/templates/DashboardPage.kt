package io.github.xingray.compose.nexus.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.controls.NexusDivider
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.theme.NexusTheme

/**
 * A statistic card definition for [io.github.xingray.compose.nexus.templates.NexusDashboardPage].
 */
data class StatCard(
    val title: String,
    val value: String,
    val suffix: String = "",
    val description: String = "",
)

/**
 * DashboardPage — a dashboard page template with stat cards, chart area, and data section.
 *
 * Layout: Title → Stat cards row → Charts area → Bottom data section.
 *
 * @param modifier Modifier.
 * @param title Page title.
 * @param stats List of statistic cards to display.
 * @param chartsContent Optional charts/graphs area content.
 * @param bottomContent Optional bottom section (table, list, etc.).
 */
@Composable
fun NexusDashboardPage(
    modifier: Modifier = Modifier,
    title: String = "Dashboard",
    stats: List<io.github.xingray.compose.nexus.templates.StatCard> = emptyList(),
    chartsContent: (@Composable () -> Unit)? = null,
    bottomContent: (@Composable () -> Unit)? = null,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes
    val shadows = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shadows

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background.page)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // Title
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
            text = title,
            color = colorScheme.text.primary,
            style = typography.extraLarge,
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Stat cards
        if (stats.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                stats.forEach { stat ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(shadows.lighter.elevation, shapes.base)
                            .clip(shapes.base)
                            .background(colorScheme.fill.blank)
                            .padding(20.dp),
                    ) {
                        Column {
                            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                text = stat.title,
                                color = colorScheme.text.secondary,
                                style = typography.small,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = androidx.compose.ui.Alignment.Bottom) {
                                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                    text = stat.value,
                                    color = colorScheme.text.primary,
                                    style = typography.extraLarge,
                                )
                                if (stat.suffix.isNotEmpty()) {
                                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                        text = " ${stat.suffix}",
                                        color = colorScheme.text.secondary,
                                        style = typography.small,
                                    )
                                }
                            }
                            if (stat.description.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                    text = stat.description,
                                    color = colorScheme.text.placeholder,
                                    style = typography.extraSmall,
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Charts area
        if (chartsContent != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shapes.base)
                    .background(colorScheme.fill.blank)
                    .padding(20.dp),
            ) {
                chartsContent()
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Bottom content
        if (bottomContent != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shapes.base)
                    .background(colorScheme.fill.blank)
                    .padding(20.dp),
            ) {
                bottomContent()
            }
        }
    }
}
