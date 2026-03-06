package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.NexusTheme

enum class AnchorType {
    Default,
    Underline,
}

enum class AnchorDirection {
    Vertical,
    Horizontal,
}

data class AnchorLink(
    val title: String,
    val href: String,
    val subLinks: List<io.github.xingray.compose.nexus.controls.AnchorLink> = emptyList(),
)

@Stable
class AnchorState internal constructor(
    initialHref: String? = null,
) {
    var currentHref by mutableStateOf(initialHref)
        internal set

    private var scrollToImpl: ((String) -> Unit)? = null

    fun scrollTo(href: String) {
        currentHref = href
        scrollToImpl?.invoke(href)
    }

    internal fun bindScrollAction(action: ((String) -> Unit)?) {
        scrollToImpl = action
    }
}

@Composable
fun rememberAnchorState(
    initialHref: String? = null,
): io.github.xingray.compose.nexus.controls.AnchorState = remember(initialHref) { _root_ide_package_.io.github.xingray.compose.nexus.controls.AnchorState(initialHref) }

@Composable
fun NexusAnchor(
    links: List<io.github.xingray.compose.nexus.controls.AnchorLink>,
    modifier: Modifier = Modifier,
    state: io.github.xingray.compose.nexus.controls.AnchorState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberAnchorState(),
    offset: Int = 0,
    bound: Int = 15,
    duration: Int = 300,
    marker: Boolean = true,
    type: io.github.xingray.compose.nexus.controls.AnchorType = _root_ide_package_.io.github.xingray.compose.nexus.controls.AnchorType.Default,
    direction: io.github.xingray.compose.nexus.controls.AnchorDirection = _root_ide_package_.io.github.xingray.compose.nexus.controls.AnchorDirection.Vertical,
    selectScrollTop: Boolean = false,
    onChange: ((href: String) -> Unit)? = null,
    onClick: ((href: String) -> Unit)? = null,
    onRequestScroll: ((href: String) -> Unit)? = null,
    linkContent: (@Composable (link: io.github.xingray.compose.nexus.controls.AnchorLink, selected: Boolean) -> Unit)? = null,
) {
    // Keep API parity with Element attrs in current simplified implementation.
    @Suppress("UNUSED_VARIABLE")
    val _unusedConfig = Triple(offset, bound, duration)
    @Suppress("UNUSED_VARIABLE")
    val _unusedSelectScrollTop = selectScrollTop

    state.bindScrollAction(onRequestScroll)

    if (direction == _root_ide_package_.io.github.xingray.compose.nexus.controls.AnchorDirection.Horizontal) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            links.forEach { link ->
                val selected = state.currentHref == link.href
                _root_ide_package_.io.github.xingray.compose.nexus.controls.AnchorLinkItem(
                    link = link,
                    selected = selected,
                    level = 0,
                    marker = marker,
                    type = type,
                    onClick = {
                        state.currentHref = link.href
                        onClick?.invoke(link.href)
                        onChange?.invoke(link.href)
                        state.scrollTo(link.href)
                    },
                    linkContent = linkContent,
                )
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            links.forEach { link ->
                _root_ide_package_.io.github.xingray.compose.nexus.controls.AnchorLinkTree(
                    link = link,
                    state = state,
                    level = 0,
                    marker = marker,
                    type = type,
                    onChange = onChange,
                    onClick = onClick,
                    linkContent = linkContent,
                )
            }
        }
    }
}

@Composable
private fun AnchorLinkTree(
    link: io.github.xingray.compose.nexus.controls.AnchorLink,
    state: io.github.xingray.compose.nexus.controls.AnchorState,
    level: Int,
    marker: Boolean,
    type: io.github.xingray.compose.nexus.controls.AnchorType,
    onChange: ((String) -> Unit)?,
    onClick: ((String) -> Unit)?,
    linkContent: (@Composable (link: io.github.xingray.compose.nexus.controls.AnchorLink, selected: Boolean) -> Unit)?,
) {
    val selected = state.currentHref == link.href
    _root_ide_package_.io.github.xingray.compose.nexus.controls.AnchorLinkItem(
        link = link,
        selected = selected,
        level = level,
        marker = marker,
        type = type,
        onClick = {
            state.currentHref = link.href
            onClick?.invoke(link.href)
            onChange?.invoke(link.href)
            state.scrollTo(link.href)
        },
        linkContent = linkContent,
    )
    link.subLinks.forEach { child ->
        _root_ide_package_.io.github.xingray.compose.nexus.controls.AnchorLinkTree(
            link = child,
            state = state,
            level = level + 1,
            marker = marker,
            type = type,
            onChange = onChange,
            onClick = onClick,
            linkContent = linkContent,
        )
    }
}

@Composable
private fun AnchorLinkItem(
    link: io.github.xingray.compose.nexus.controls.AnchorLink,
    selected: Boolean,
    level: Int,
    marker: Boolean,
    type: io.github.xingray.compose.nexus.controls.AnchorType,
    onClick: () -> Unit,
    linkContent: (@Composable (link: io.github.xingray.compose.nexus.controls.AnchorLink, selected: Boolean) -> Unit)?,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography

    val textColor = if (selected) colorScheme.primary.base else colorScheme.text.regular
    val rowModifier = Modifier
        .fillMaxWidth()
        .clip(_root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes.base)
        .clickable { onClick() }
        .pointerHoverIcon(PointerIcon.Hand)
        .padding(start = (level * 12 + 4).dp, top = 6.dp, bottom = 6.dp, end = 8.dp)

    Box(modifier = rowModifier) {
        if (type == _root_ide_package_.io.github.xingray.compose.nexus.controls.AnchorType.Default && marker && selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 0.dp)
                    .background(colorScheme.primary.base, RoundedCornerShape(1.dp))
                    .fillMaxWidth(0.006f),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = if (type == _root_ide_package_.io.github.xingray.compose.nexus.controls.AnchorType.Default && marker) 8.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (linkContent != null) {
                linkContent(link, selected)
            } else {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = link.title,
                    color = textColor,
                    style = typography.base,
                )
            }
        }

        if (type == _root_ide_package_.io.github.xingray.compose.nexus.controls.AnchorType.Underline && selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = (level * 12 + 4).dp)
                    .background(colorScheme.primary.base, RoundedCornerShape(2.dp))
                    .fillMaxWidth(0.35f)
                    .padding(vertical = 1.dp),
            )
        }
    }
}
