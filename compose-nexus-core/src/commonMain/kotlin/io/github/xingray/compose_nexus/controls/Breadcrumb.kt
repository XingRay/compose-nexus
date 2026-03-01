package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.theme.NexusTheme

/**
 * Element Plus Breadcrumb — a navigation path indicator.
 *
 * @param modifier Modifier.
 * @param separator Separator character between items.
 * @param content Breadcrumb items, typically [NexusBreadcrumbItem].
 */
@Composable
fun NexusBreadcrumb(
    modifier: Modifier = Modifier,
    separator: String = "/",
    content: NexusBreadcrumbScope.() -> Unit,
) {
    val scope = NexusBreadcrumbScopeImpl(separator).apply(content)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        scope.items.forEachIndexed { index, item ->
            item.content()
            if (index < scope.items.lastIndex) {
                NexusText(
                    text = " $separator ",
                    color = NexusTheme.colorScheme.text.placeholder,
                    style = NexusTheme.typography.small,
                )
            }
        }
    }
}

interface NexusBreadcrumbScope {
    fun item(content: @Composable () -> Unit)
}

private class NexusBreadcrumbScopeImpl(val separator: String) : NexusBreadcrumbScope {
    val items = mutableListOf<BreadcrumbEntry>()

    override fun item(content: @Composable () -> Unit) {
        items.add(BreadcrumbEntry(content))
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
    isCurrent: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    if (isCurrent) {
        NexusText(
            text = text,
            color = colorScheme.text.primary,
            style = typography.small,
            modifier = modifier,
        )
    } else {
        NexusLink(
            text = text,
            onClick = onClick ?: {},
            modifier = modifier,
        )
    }
}
