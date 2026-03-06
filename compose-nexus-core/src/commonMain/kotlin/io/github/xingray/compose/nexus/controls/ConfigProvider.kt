package io.github.xingray.compose.nexus.controls

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.xingray.compose.nexus.controls.merge
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusType

enum class NexusCardShadowMode {
    Always,
    Hover,
    Never,
}

data class NexusButtonConfig(
    val autoInsertSpace: Boolean? = null,
    val type: io.github.xingray.compose.nexus.theme.NexusType? = null,
    val plain: Boolean? = null,
    val text: Boolean? = null,
    val round: Boolean? = null,
    val dashed: Boolean? = null,
)

data class NexusLinkConfig(
    val type: io.github.xingray.compose.nexus.theme.NexusType? = null,
    val underline: io.github.xingray.compose.nexus.controls.NexusLinkUnderline? = null,
)

data class NexusCardConfig(
    val shadow: io.github.xingray.compose.nexus.controls.NexusCardShadowMode? = null,
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
    val size: io.github.xingray.compose.nexus.theme.ComponentSize? = null,
    val zIndex: Int? = null,
    val namespace: String? = null,
    val button: io.github.xingray.compose.nexus.controls.NexusButtonConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButtonConfig(),
    val link: io.github.xingray.compose.nexus.controls.NexusLinkConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusLinkConfig(),
    val card: io.github.xingray.compose.nexus.controls.NexusCardConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusCardConfig(),
    val dialog: io.github.xingray.compose.nexus.controls.NexusDialogConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDialogConfig(),
    val message: io.github.xingray.compose.nexus.controls.NexusMessageConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusMessageConfig(),
    val emptyValues: List<Any?>? = null,
    val valueOnClear: Any? = null,
    val table: io.github.xingray.compose.nexus.controls.NexusTableConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTableConfig(),
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

private fun io.github.xingray.compose.nexus.controls.NexusButtonConfig.merge(parent: io.github.xingray.compose.nexus.controls.NexusButtonConfig): io.github.xingray.compose.nexus.controls.NexusButtonConfig {
    return copy(
        autoInsertSpace = autoInsertSpace ?: parent.autoInsertSpace,
        type = type ?: parent.type,
        plain = plain ?: parent.plain,
        text = text ?: parent.text,
        round = round ?: parent.round,
        dashed = dashed ?: parent.dashed,
    )
}

private fun io.github.xingray.compose.nexus.controls.NexusLinkConfig.merge(parent: io.github.xingray.compose.nexus.controls.NexusLinkConfig): io.github.xingray.compose.nexus.controls.NexusLinkConfig {
    return copy(
        type = type ?: parent.type,
        underline = underline ?: parent.underline,
    )
}

private fun io.github.xingray.compose.nexus.controls.NexusCardConfig.merge(parent: io.github.xingray.compose.nexus.controls.NexusCardConfig): io.github.xingray.compose.nexus.controls.NexusCardConfig {
    return copy(shadow = shadow ?: parent.shadow)
}

private fun io.github.xingray.compose.nexus.controls.NexusDialogConfig.merge(parent: io.github.xingray.compose.nexus.controls.NexusDialogConfig): io.github.xingray.compose.nexus.controls.NexusDialogConfig {
    return copy(
        alignCenter = alignCenter ?: parent.alignCenter,
        draggable = draggable ?: parent.draggable,
        overflow = overflow ?: parent.overflow,
    )
}

private fun io.github.xingray.compose.nexus.controls.NexusMessageConfig.merge(parent: io.github.xingray.compose.nexus.controls.NexusMessageConfig): io.github.xingray.compose.nexus.controls.NexusMessageConfig {
    return copy(max = max ?: parent.max)
}

private fun io.github.xingray.compose.nexus.controls.NexusTableConfig.merge(parent: io.github.xingray.compose.nexus.controls.NexusTableConfig): io.github.xingray.compose.nexus.controls.NexusTableConfig {
    return copy(showOverflowTooltip = showOverflowTooltip ?: parent.showOverflowTooltip)
}

val LocalNexusConfig = staticCompositionLocalOf { _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusConfig() }

@Composable
fun NexusConfigProvider(
    config: io.github.xingray.compose.nexus.controls.NexusConfig,
    content: @Composable () -> Unit,
) {
    val parent = _root_ide_package_.io.github.xingray.compose.nexus.controls.LocalNexusConfig.current
    val merged = remember(parent, config) { config.merge(parent) }
    CompositionLocalProvider(_root_ide_package_.io.github.xingray.compose.nexus.controls.LocalNexusConfig provides merged) {
        content()
    }
}

@Composable
fun NexusConfigProvider(
    locale: String? = null,
    size: io.github.xingray.compose.nexus.theme.ComponentSize? = null,
    zIndex: Int? = null,
    namespace: String? = null,
    button: io.github.xingray.compose.nexus.controls.NexusButtonConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButtonConfig(),
    link: io.github.xingray.compose.nexus.controls.NexusLinkConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusLinkConfig(),
    card: io.github.xingray.compose.nexus.controls.NexusCardConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusCardConfig(),
    dialog: io.github.xingray.compose.nexus.controls.NexusDialogConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDialogConfig(),
    message: io.github.xingray.compose.nexus.controls.NexusMessageConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusMessageConfig(),
    emptyValues: List<Any?>? = null,
    valueOnClear: Any? = null,
    table: io.github.xingray.compose.nexus.controls.NexusTableConfig = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusTableConfig(),
    content: @Composable () -> Unit,
) {
    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusConfigProvider(
        config = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusConfig(
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
