package io.github.xingray.compose.nexus.containers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.NexusTheme

enum class NexusContainerDirection {
    Horizontal,
    Vertical,
}

/**
 * Element Plus Container layout wrapper.
 *
 * Use [direction] to arrange container children horizontally or vertically.
 */
@Composable
fun NexusContainer(
    modifier: Modifier = Modifier,
    direction: NexusContainerDirection = NexusContainerDirection.Horizontal,
    content: @Composable () -> Unit,
) {
    when (direction) {
        NexusContainerDirection.Horizontal -> {
            Row(modifier = Modifier.fillMaxWidth().then(modifier)) {
                content()
            }
        }

        NexusContainerDirection.Vertical -> {
            Column(modifier = Modifier.fillMaxWidth().then(modifier)) {
                content()
            }
        }
    }
}

/**
 * Element Plus Header region.
 */
@Composable
fun NexusHeader(
    modifier: Modifier = Modifier,
    height: Dp = 60.dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(NexusTheme.colorScheme.primary.light7)
            .padding(horizontal = 16.dp)
            .then(modifier),
        contentAlignment = Alignment.CenterStart,
    ) {
        content()
    }
}

/**
 * Element Plus Aside region.
 */
@Composable
fun NexusAside(
    modifier: Modifier = Modifier,
    width: Dp = 300.dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .background(NexusTheme.colorScheme.success.light7)
            .padding(16.dp)
            .then(modifier),
        contentAlignment = Alignment.TopStart,
    ) {
        content()
    }
}

/**
 * Element Plus Main region.
 */
@Composable
fun NexusMain(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NexusTheme.colorScheme.warning.light7)
            .padding(16.dp)
            .then(modifier),
        contentAlignment = Alignment.TopStart,
    ) {
        content()
    }
}

/**
 * Element Plus Footer region.
 */
@Composable
fun NexusFooter(
    modifier: Modifier = Modifier,
    height: Dp = 60.dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(NexusTheme.colorScheme.danger.light7)
            .padding(horizontal = 16.dp)
            .then(modifier),
        contentAlignment = Alignment.CenterStart,
    ) {
        content()
    }
}
