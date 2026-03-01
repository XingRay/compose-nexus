package io.github.xingray.compose_nexus.controls

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.foundation.ProvideContentColorTextStyle
import io.github.xingray.compose_nexus.theme.NexusTheme

// ============================================================================
// Form configuration via CompositionLocal
// ============================================================================

internal data class FormConfig(
    val labelPosition: LabelPosition = LabelPosition.Right,
    val labelWidth: Dp = 80.dp,
)

internal val LocalFormConfig = compositionLocalOf { FormConfig() }

enum class LabelPosition {
    /** Label on the left, content on the right */
    Left,
    /** Label on the right side of its allocated width */
    Right,
    /** Label on top of the content */
    Top,
}

// ============================================================================
// NexusForm
// ============================================================================

/**
 * Element Plus Form — a container that provides layout configuration to [NexusFormItem] children.
 *
 * @param modifier Modifier.
 * @param labelPosition Position of labels relative to form content.
 * @param labelWidth Width allocated for labels (for Left/Right positions).
 * @param content Form items.
 */
@Composable
fun NexusForm(
    modifier: Modifier = Modifier,
    labelPosition: LabelPosition = LabelPosition.Right,
    labelWidth: Dp = 80.dp,
    content: @Composable () -> Unit,
) {
    val config = FormConfig(labelPosition = labelPosition, labelWidth = labelWidth)
    CompositionLocalProvider(LocalFormConfig provides config) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            content()
        }
    }
}

// ============================================================================
// NexusFormItem
// ============================================================================

/**
 * Element Plus FormItem — a labeled form field with optional validation error.
 *
 * @param label Field label text.
 * @param modifier Modifier.
 * @param required Show required asterisk.
 * @param error Error message text. When non-null, displayed below the content.
 * @param content Form control (Input, Select, etc.).
 */
@Composable
fun NexusFormItem(
    label: String = "",
    modifier: Modifier = Modifier,
    required: Boolean = false,
    error: String? = null,
    content: @Composable () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val config = LocalFormConfig.current

    val labelContent: @Composable () -> Unit = {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (required) {
                NexusText(
                    text = "* ",
                    color = colorScheme.danger.base,
                    style = typography.base,
                )
            }
            NexusText(
                text = label,
                color = colorScheme.text.regular,
                style = typography.base,
            )
        }
    }

    when (config.labelPosition) {
        LabelPosition.Top -> {
            Column(modifier = modifier.fillMaxWidth()) {
                Box(modifier = Modifier.padding(bottom = 6.dp)) {
                    labelContent()
                }
                content()
                if (error != null) {
                    NexusText(
                        text = error,
                        modifier = Modifier.padding(top = 4.dp),
                        color = colorScheme.danger.base,
                        style = typography.extraSmall,
                    )
                }
            }
        }
        LabelPosition.Left, LabelPosition.Right -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                Box(
                    modifier = Modifier
                        .width(config.labelWidth)
                        .padding(top = 8.dp, end = 12.dp),
                    contentAlignment = when (config.labelPosition) {
                        LabelPosition.Right -> Alignment.CenterEnd
                        else -> Alignment.CenterStart
                    },
                ) {
                    labelContent()
                }
                Column(modifier = Modifier.weight(1f)) {
                    content()
                    if (error != null) {
                        NexusText(
                            text = error,
                            modifier = Modifier.padding(top = 4.dp),
                            color = colorScheme.danger.base,
                            style = typography.extraSmall,
                        )
                    }
                }
            }
        }
    }
}
