package io.github.xingray.compose.nexus.sample.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.sample.DemoRoute
import io.github.xingray.compose.nexus.theme.NexusTheme

private val componentGroups = listOf(
    "Basic" to listOf("Button", "Border", "Color", "Layout Container", "Icon", "Layout", "Text", "Typography", "Link", "Scrollbar", "Tag", "Space", "Splitter"),
    "Configuration" to listOf("Config Provider"),
    "Form" to listOf("Input", "InputNumber", "Textarea", "Checkbox", "Radio", "Rate", "Switch", "Select", "Virtualized Select", "Slider", "DatePickerPanel", "DatePicker", "DateTimePicker", "TimePicker", "TimeSelect", "Form", "Autocomplete", "ColorPickerPanel", "ColorPicker", "InputTag", "Mention", "Cascader", "Transfer", "TreeSelect", "Upload"),
    "Data Display" to listOf("Table", "Virtualized Table", "Tree", "Virtualized Tree", "Statistic", "Segmented", "Pagination", "Progress", "Result", "Avatar", "Badge", "Skeleton", "Empty", "Image", "Infinite Scroll", "Timeline", "Calendar", "Descriptions"),
    "Navigation" to listOf("Affix", "Anchor", "Backtop", "Menu", "Page Header", "Steps", "Breadcrumb", "Tabs", "Dropdown"),
    "Feedback" to listOf("Alert", "Message", "Message Box", "Notification", "Loading", "Popconfirm", "Popover", "Tooltip", "Tour"),
    "Container" to listOf("Card", "Collapse", "Dialog", "Drawer", "Carousel"),
    "Others" to listOf("Divider", "Watermark"),
)

private val nameToRoute: Map<String, DemoRoute> =
    DemoRoute.entries.associateBy { it.label }

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OverviewDemo(onNavigate: (DemoRoute) -> Unit = {}) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    DemoPage(
        title = "Component Overview",
        description = "Compose Nexus provides ${componentGroups.sumOf { it.second.size }} components in ${componentGroups.size} categories.",
    ) {
        componentGroups.forEach { (group, components) ->
            Spacer(modifier = Modifier.height(20.dp))
            NexusText(
                text = group,
                color = colorScheme.text.primary,
                style = typography.large,
            )
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                components.forEach { name ->
                    val route = nameToRoute[name]
                    val interactionSource = remember { MutableInteractionSource() }
                    val isHovered by interactionSource.collectIsHoveredAsState()
                    val bgColor = if (isHovered) colorScheme.primary.light9 else colorScheme.fill.lighter
                    val textColor = if (isHovered) colorScheme.primary.base else colorScheme.text.regular

                    Box(
                        modifier = Modifier
                            .size(width = 140.dp, height = 60.dp)
                            .clip(NexusTheme.shapes.base)
                            .hoverable(interactionSource)
                            .background(bgColor)
                            .then(
                                if (route != null) {
                                    Modifier
                                        .clickable { onNavigate(route) }
                                        .pointerHoverIcon(PointerIcon.Hand)
                                } else {
                                    Modifier
                                }
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        NexusText(
                            text = name,
                            color = textColor,
                            style = typography.base,
                        )
                    }
                }
            }
        }
    }
}
