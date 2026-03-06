package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.NexusTheme

/**
 * Element Plus Breadcrumb — a navigation path indicator.
 *
 * @param modifier Modifier.
 * @param separator Separator character between items.
 * @param content Breadcrumb items, typically [io.github.xingray.compose.nexus.controls.NexusBreadcrumbItem].
 */
@Composable
fun NexusBreadcrumb(
    modifier: Modifier = Modifier,
    separator: String = "/",
    separatorIcon: (@Composable () -> Unit)? = null,
    content: io.github.xingray.compose.nexus.controls.NexusBreadcrumbScope.() -> Unit,
) {
    val scope = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusBreadcrumbScopeImpl().apply(content)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        scope.items.forEachIndexed { index, item ->
            item.content()
            if (index < scope.items.lastIndex) {
                if (separatorIcon != null) {
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        separatorIcon()
                    }
                } else {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                        text = " $separator ",
                        color = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme.text.placeholder,
                        style = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography.small,
                    )
                }
            }
        }
    }
}

interface NexusBreadcrumbScope {
    fun item(content: @Composable () -> Unit)
}

private class NexusBreadcrumbScopeImpl : io.github.xingray.compose.nexus.controls.NexusBreadcrumbScope {
    val items = mutableListOf<io.github.xingray.compose.nexus.controls.BreadcrumbEntry>()

    override fun item(content: @Composable () -> Unit) {
        items.add(_root_ide_package_.io.github.xingray.compose.nexus.controls.BreadcrumbEntry(content))
    }
}

private class BreadcrumbEntry(val content: @Composable () -> Unit)

/**
 * A convenience composable for a clickable breadcrumb item.
 *
 * @param text Item label.
 * @param modifier Modifier.
 * @param isCurrent Whether this is the current (last) item.
 * @param onClick Click handler.
 */
@Composable
fun NexusBreadcrumbItem(
    text: String,
    modifier: Modifier = Modifier,
    to: Any? = null,
    replace: Boolean = false,
    isCurrent: Boolean = false,
    onClick: (() -> Unit)? = null,
    onNavigate: ((to: Any, replace: Boolean) -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography

    if (isCurrent) {
        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
            text = text,
            color = colorScheme.text.primary,
            style = typography.small,
            modifier = modifier,
        )
    } else {
        Row(
            modifier = modifier
                .clickable {
                    onClick?.invoke()
                    if (to != null) {
                        onNavigate?.invoke(to, replace)
                    }
                }
                .pointerHoverIcon(PointerIcon.Hand),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            if (content != null) {
                content()
            } else {
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                    text = text,
                    color = colorScheme.primary.base,
                    style = typography.small,
                )
            }
        }
    }
}
