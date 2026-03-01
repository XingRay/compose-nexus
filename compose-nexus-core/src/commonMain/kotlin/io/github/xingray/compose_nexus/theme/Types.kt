package io.github.xingray.compose_nexus.theme

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
 * Resolve [NexusType] to the corresponding [TypeColor] from the [NexusColorScheme].
 * Returns `null` for [NexusType.Default] since default has no dedicated type color.
 */
fun NexusColorScheme.typeColor(type: NexusType): TypeColor? = when (type) {
    NexusType.Default -> null
    NexusType.Primary -> primary
    NexusType.Success -> success
    NexusType.Warning -> warning
    NexusType.Danger -> danger
    NexusType.Info -> info
}
