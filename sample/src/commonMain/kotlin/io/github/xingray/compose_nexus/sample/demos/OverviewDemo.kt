package io.github.xingray.compose_nexus.sample.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.controls.NexusText
import io.github.xingray.compose_nexus.theme.NexusTheme

private val componentGroups = listOf(
    "Basic" to listOf("Button", "Text", "Link", "Tag", "Divider", "Space"),
    "Form" to listOf("Input", "InputNumber", "Textarea", "Checkbox", "Radio", "Switch", "Select", "Slider", "DatePicker", "TimePicker", "Form", "Autocomplete", "InputTag", "Cascader", "Transfer"),
    "Data Display" to listOf("Table", "Tree", "Pagination", "Progress", "Avatar", "Skeleton", "Empty", "Calendar"),
    "Navigation" to listOf("Menu", "Steps", "Breadcrumb", "Tabs", "Dropdown"),
    "Feedback" to listOf("Alert", "Message", "Notification", "Loading", "Tooltip", "Tour"),
    "Container" to listOf("Card", "Collapse", "Dialog", "Drawer", "Carousel"),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OverviewDemo() {
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
                    Box(
                        modifier = Modifier
                            .size(width = 140.dp, height = 60.dp)
                            .clip(NexusTheme.shapes.base)
                            .background(colorScheme.fill.lighter)
                            .padding(12.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        NexusText(
                            text = name,
                            color = colorScheme.text.regular,
                            style = typography.base,
                        )
                    }
                }
            }
        }
    }
}
