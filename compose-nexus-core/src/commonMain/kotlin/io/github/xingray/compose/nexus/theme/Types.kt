package io.github.xingray.compose.nexus.theme

/**
 * Semantic color type used by Button, Text, Link, Tag, Alert, etc.
 * Maps to Element Plus's type prop (primary / success / warning / danger / info).
 */
enum class NexusType {
    Default,
    Primary,
    Success,
    Warning,
    Danger,
    Info,
}

/**
 * Resolve [io.github.xingray.compose.nexus.theme.NexusType] to the corresponding [io.github.xingray.compose.nexus.theme.TypeColor] from the [io.github.xingray.compose.nexus.theme.NexusColorScheme].
 * Returns `null` for [io.github.xingray.compose.nexus.theme.NexusType.Default] since default has no dedicated type color.
 */
fun io.github.xingray.compose.nexus.theme.NexusColorScheme.typeColor(type: io.github.xingray.compose.nexus.theme.NexusType): io.github.xingray.compose.nexus.theme.TypeColor? = when (type) {
    _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Default -> null
    _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary -> primary
    _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Success -> success
    _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Warning -> warning
    _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Danger -> danger
    _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Info -> info
}
