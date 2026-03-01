package io.github.xingray.compose_nexus.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.controls.MenuItem
import io.github.xingray.compose_nexus.controls.MenuState
import io.github.xingray.compose_nexus.controls.NexusDivider
import io.github.xingray.compose_nexus.controls.NexusText
import io.github.xingray.compose_nexus.controls.NexusTreeMenu
import io.github.xingray.compose_nexus.controls.rememberMenuState
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * SettingsPage — a settings page with left sidebar menu and right content area.
 *
 * @param sections Menu items for settings sections.
 * @param modifier Modifier.
 * @param title Page title.
 * @param menuState Menu state for tracking active section.
 * @param sidebarWidth Width of the sidebar menu.
 * @param content Content composable, receives the active section key.
 */
@Composable
fun NexusSettingsPage(
    sections: List<MenuItem>,
    modifier: Modifier = Modifier,
    title: String = "Settings",
    menuState: MenuState = rememberMenuState(
        initialActiveKey = sections.firstOrNull()?.key ?: ""
    ),
    sidebarWidth: Dp = 220.dp,
    content: @Composable (activeKey: String) -> Unit,
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

        // Body: sidebar + content
        Row(modifier = Modifier.weight(1f)) {
            // Sidebar
            Column(
                modifier = Modifier
                    .width(sidebarWidth)
                    .fillMaxHeight()
                    .clip(shapes.base)
                    .background(colorScheme.fill.blank),
            ) {
                NexusTreeMenu(
                    items = sections,
                    state = menuState,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Content area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(shapes.base)
                    .background(colorScheme.fill.blank)
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                content(menuState.activeKey)
            }
        }
    }
}
