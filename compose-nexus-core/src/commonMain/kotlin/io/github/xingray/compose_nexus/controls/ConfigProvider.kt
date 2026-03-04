package io.github.xingray.compose_nexus.controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.xingray.compose_nexus.theme.ComponentSize
import io.github.xingray.compose_nexus.theme.NexusType

enum class NexusCardShadowMode {
    Always,
    Hover,
    Never,
}

data class NexusButtonConfig(
    val autoInsertSpace: Boolean? = null,
    val type: NexusType? = null,
    val plain: Boolean? = null,
    val text: Boolean? = null,
    val round: Boolean? = null,
    val dashed: Boolean? = null,
)

data class NexusLinkConfig(
    val type: NexusType? = null,
    val underline: NexusLinkUnderline? = null,
)

data class NexusCardConfig(
    val shadow: NexusCardShadowMode? = null,
)

data class NexusDialogConfig(
    val alignCenter: Boolean? = null,
    val draggable: Boolean? = null,
    val overflow: Boolean? = null,
)

data class NexusMessageConfig(
    val max: Int? = null,
)

data class NexusTableConfig(
    val showOverflowTooltip: Boolean? = null,
)

data class NexusConfig(
    val locale: String? = null,
    val size: ComponentSize? = null,
    val zIndex: Int? = null,
    val namespace: String? = null,
    val button: NexusButtonConfig = NexusButtonConfig(),
    val link: NexusLinkConfig = NexusLinkConfig(),
    val card: NexusCardConfig = NexusCardConfig(),
    val dialog: NexusDialogConfig = NexusDialogConfig(),
    val message: NexusMessageConfig = NexusMessageConfig(),
    val emptyValues: List<Any?>? = null,
    val valueOnClear: Any? = null,
    val table: NexusTableConfig = NexusTableConfig(),
) {
    fun merge(parent: NexusConfig): NexusConfig {
        return copy(
            locale = locale ?: parent.locale,
            size = size ?: parent.size,
            zIndex = zIndex ?: parent.zIndex,
            namespace = namespace ?: parent.namespace,
            button = button.merge(parent.button),
            link = link.merge(parent.link),
            card = card.merge(parent.card),
            dialog = dialog.merge(parent.dialog),
            message = message.merge(parent.message),
            emptyValues = emptyValues ?: parent.emptyValues,
            valueOnClear = valueOnClear ?: parent.valueOnClear,
            table = table.merge(parent.table),
        )
    }
}

private fun NexusButtonConfig.merge(parent: NexusButtonConfig): NexusButtonConfig {
    return copy(
        autoInsertSpace = autoInsertSpace ?: parent.autoInsertSpace,
        type = type ?: parent.type,
        plain = plain ?: parent.plain,
        text = text ?: parent.text,
        round = round ?: parent.round,
        dashed = dashed ?: parent.dashed,
    )
}

private fun NexusLinkConfig.merge(parent: NexusLinkConfig): NexusLinkConfig {
    return copy(
        type = type ?: parent.type,
        underline = underline ?: parent.underline,
    )
}

private fun NexusCardConfig.merge(parent: NexusCardConfig): NexusCardConfig {
    return copy(shadow = shadow ?: parent.shadow)
}

private fun NexusDialogConfig.merge(parent: NexusDialogConfig): NexusDialogConfig {
    return copy(
        alignCenter = alignCenter ?: parent.alignCenter,
        draggable = draggable ?: parent.draggable,
        overflow = overflow ?: parent.overflow,
    )
}

private fun NexusMessageConfig.merge(parent: NexusMessageConfig): NexusMessageConfig {
    return copy(max = max ?: parent.max)
}

private fun NexusTableConfig.merge(parent: NexusTableConfig): NexusTableConfig {
    return copy(showOverflowTooltip = showOverflowTooltip ?: parent.showOverflowTooltip)
}

val LocalNexusConfig = staticCompositionLocalOf { NexusConfig() }

@Composable
fun NexusConfigProvider(
    config: NexusConfig,
    content: @Composable () -> Unit,
) {
    val parent = LocalNexusConfig.current
    val merged = remember(parent, config) { config.merge(parent) }
    CompositionLocalProvider(LocalNexusConfig provides merged) {
        content()
    }
}

@Composable
fun NexusConfigProvider(
    locale: String? = null,
    size: ComponentSize? = null,
    zIndex: Int? = null,
    namespace: String? = null,
    button: NexusButtonConfig = NexusButtonConfig(),
    link: NexusLinkConfig = NexusLinkConfig(),
    card: NexusCardConfig = NexusCardConfig(),
    dialog: NexusDialogConfig = NexusDialogConfig(),
    message: NexusMessageConfig = NexusMessageConfig(),
    emptyValues: List<Any?>? = null,
    valueOnClear: Any? = null,
    table: NexusTableConfig = NexusTableConfig(),
    content: @Composable () -> Unit,
) {
    NexusConfigProvider(
        config = NexusConfig(
            locale = locale,
            size = size,
            zIndex = zIndex,
            namespace = namespace,
            button = button,
            link = link,
            card = card,
            dialog = dialog,
            message = message,
            emptyValues = emptyValues,
            valueOnClear = valueOnClear,
            table = table,
        ),
        content = content,
    )
}
