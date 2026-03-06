package io.github.xingray.compose.nexus.containers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.controls.LocalNexusConfig
import io.github.xingray.compose.nexus.controls.NexusCardShadowMode
import io.github.xingray.compose.nexus.controls.NexusDivider
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose.nexus.theme.NexusTheme

/**
 * Element Plus Card — a container with optional header, border, and shadow.
 *
 * @param modifier Modifier.
 * @param shadow Shadow display mode: Always / Hover / Never.
 * @param header Optional header content. A divider is drawn below the header.
 * @param content Card body content.
 */
@Composable
fun NexusCard(
    modifier: Modifier = Modifier,
    shadow: io.github.xingray.compose.nexus.containers.CardShadow = _root_ide_package_.io.github.xingray.compose.nexus.containers.CardShadow.Always,
    headerText: String? = null,
    footerText: String? = null,
    bodyModifier: Modifier = Modifier,
    bodyPadding: Dp = 20.dp,
    headerClass: String? = null,
    bodyClass: String? = null,
    footerClass: String? = null,
    header: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes
    val shadows = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shadows
    val configShadow = _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalNexusConfig.current.card.shadow
    val resolvedShadow = when (configShadow) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusCardShadowMode.Always -> _root_ide_package_.io.github.xingray.compose.nexus.containers.CardShadow.Always
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusCardShadowMode.Hover -> _root_ide_package_.io.github.xingray.compose.nexus.containers.CardShadow.Hover
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusCardShadowMode.Never -> _root_ide_package_.io.github.xingray.compose.nexus.containers.CardShadow.Never
        null -> shadow
    }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val headerContent = header ?: headerText?.let {
        @Composable { _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = it) }
    }
    val footerContent = footer ?: footerText?.let {
        @Composable { _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = it) }
    }

    @Suppress("UNUSED_VARIABLE")
    val _unusedClasses = Triple(headerClass, bodyClass, footerClass)

    val elevation = when (resolvedShadow) {
        _root_ide_package_.io.github.xingray.compose.nexus.containers.CardShadow.Always -> shadows.light.elevation
        _root_ide_package_.io.github.xingray.compose.nexus.containers.CardShadow.Hover -> if (isHovered) shadows.light.elevation else 0.dp
        _root_ide_package_.io.github.xingray.compose.nexus.containers.CardShadow.Never -> 0.dp
    }

    Column(
        modifier = modifier
            .hoverable(interactionSource)
            .shadow(elevation, shapes.base)
            .clip(shapes.base)
            .background(colorScheme.fill.blank)
            .border(1.dp, colorScheme.border.lighter, shapes.base),
    ) {
        // Header
        if (headerContent != null) {
            _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle(
                contentColor = colorScheme.text.primary,
                textStyle = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.base,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                ) {
                    headerContent()
                }
            }
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDivider(color = colorScheme.border.lighter)
        }

        // Body
        _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle(
            contentColor = colorScheme.text.regular,
            textStyle = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.base,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bodyPadding)
                    .then(bodyModifier),
            ) {
                content()
            }
        }

        // Footer
        if (footerContent != null) {
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDivider(color = colorScheme.border.lighter)
            _root_ide_package_.io.github.xingray.compose.nexus.foundation.ProvideContentColorTextStyle(
                contentColor = colorScheme.text.secondary,
                textStyle = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.small,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                ) {
                    footerContent()
                }
            }
        }
    }
}

enum class CardShadow {
    Always,
    Hover,
    Never,
}
