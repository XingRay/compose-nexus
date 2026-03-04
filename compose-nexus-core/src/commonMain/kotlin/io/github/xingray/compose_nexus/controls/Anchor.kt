package io.github.xingray.compose_nexus.controls

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
import io.github.xingray.compose_nexus.theme.NexusTheme

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
    val subLinks: List<AnchorLink> = emptyList(),
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
): AnchorState = remember(initialHref) { AnchorState(initialHref) }

@Composable
fun NexusAnchor(
    links: List<AnchorLink>,
    modifier: Modifier = Modifier,
    state: AnchorState = rememberAnchorState(),
    offset: Int = 0,
    bound: Int = 15,
    duration: Int = 300,
    marker: Boolean = true,
    type: AnchorType = AnchorType.Default,
    direction: AnchorDirection = AnchorDirection.Vertical,
    selectScrollTop: Boolean = false,
    onChange: ((href: String) -> Unit)? = null,
    onClick: ((href: String) -> Unit)? = null,
    onRequestScroll: ((href: String) -> Unit)? = null,
    linkContent: (@Composable (link: AnchorLink, selected: Boolean) -> Unit)? = null,
) {
    // Keep API parity with Element attrs in current simplified implementation.
    @Suppress("UNUSED_VARIABLE")
    val _unusedConfig = Triple(offset, bound, duration)
    @Suppress("UNUSED_VARIABLE")
    val _unusedSelectScrollTop = selectScrollTop

    state.bindScrollAction(onRequestScroll)

    if (direction == AnchorDirection.Horizontal) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            links.forEach { link ->
                val selected = state.currentHref == link.href
                AnchorLinkItem(
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
                AnchorLinkTree(
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
    link: AnchorLink,
    state: AnchorState,
    level: Int,
    marker: Boolean,
    type: AnchorType,
    onChange: ((String) -> Unit)?,
    onClick: ((String) -> Unit)?,
    linkContent: (@Composable (link: AnchorLink, selected: Boolean) -> Unit)?,
) {
    val selected = state.currentHref == link.href
    AnchorLinkItem(
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
        AnchorLinkTree(
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
    link: AnchorLink,
    selected: Boolean,
    level: Int,
    marker: Boolean,
    type: AnchorType,
    onClick: () -> Unit,
    linkContent: (@Composable (link: AnchorLink, selected: Boolean) -> Unit)?,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val textColor = if (selected) colorScheme.primary.base else colorScheme.text.regular
    val rowModifier = Modifier
        .fillMaxWidth()
        .clip(NexusTheme.shapes.base)
        .clickable { onClick() }
        .pointerHoverIcon(PointerIcon.Hand)
        .padding(start = (level * 12 + 4).dp, top = 6.dp, bottom = 6.dp, end = 8.dp)

    Box(modifier = rowModifier) {
        if (type == AnchorType.Default && marker && selected) {
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
                .padding(start = if (type == AnchorType.Default && marker) 8.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (linkContent != null) {
                linkContent(link, selected)
            } else {
                NexusText(
                    text = link.title,
                    color = textColor,
                    style = typography.base,
                )
            }
        }

        if (type == AnchorType.Underline && selected) {
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
