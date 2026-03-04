package io.github.xingray.compose_nexus.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose_nexus.controls.NexusDivider
import io.github.xingray.compose_nexus.controls.NexusText
import io.github.xingray.compose_nexus.sample.demos.*
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.darkColorScheme
import io.github.xingray.compose_nexus.theme.lightColorScheme

enum class DemoRoute(val label: String, val group: String) {
    Overview("Overview", ""),
    // Basic
    Button("Button", "Basic"),
    Border("Border", "Basic"),
    Color("Color", "Basic"),
    LayoutContainer("Layout Container", "Basic"),
    Icon("Icon", "Basic"),
    Layout("Layout", "Basic"),
    Text("Text", "Basic"),
    Typography("Typography", "Basic"),
    Link("Link", "Basic"),
    Scrollbar("Scrollbar", "Basic"),
    Tag("Tag", "Basic"),
    Divider("Divider", "Others"),
    Space("Space", "Basic"),
    Splitter("Splitter", "Basic"),
    ConfigProvider("Config Provider", "Configuration"),
    // Form
    Input("Input", "Form"),
    InputNumber("InputNumber", "Form"),
    Textarea("Textarea", "Form"),
    Checkbox("Checkbox", "Form"),
    Radio("Radio", "Form"),
    Rate("Rate", "Form"),
    Switch("Switch", "Form"),
    Select("Select", "Form"),
    VirtualizedSelect("Virtualized Select", "Form"),
    Slider("Slider", "Form"),
    DatePickerPanel("DatePickerPanel", "Form"),
    DatePicker("DatePicker", "Form"),
    DateTimePicker("DateTimePicker", "Form"),
    TimePicker("TimePicker", "Form"),
    TimeSelect("TimeSelect", "Form"),
    Form("Form", "Form"),
    Autocomplete("Autocomplete", "Form"),
    ColorPickerPanel("ColorPickerPanel", "Form"),
    ColorPicker("ColorPicker", "Form"),
    InputTag("InputTag", "Form"),
    Mention("Mention", "Form"),
    Cascader("Cascader", "Form"),
    Transfer("Transfer", "Form"),
    TreeSelect("TreeSelect", "Form"),
    Upload("Upload", "Form"),
    // Data
    Table("Table", "Data Display"),
    VirtualizedTable("Virtualized Table", "Data Display"),
    Tree("Tree", "Data Display"),
    VirtualizedTree("Virtualized Tree", "Data Display"),
    Statistic("Statistic", "Data Display"),
    Segmented("Segmented", "Data Display"),
    Pagination("Pagination", "Data Display"),
    Progress("Progress", "Data Display"),
    Result("Result", "Data Display"),
    Avatar("Avatar", "Data Display"),
    Badge("Badge", "Data Display"),
    Skeleton("Skeleton", "Data Display"),
    Empty("Empty", "Data Display"),
    Image("Image", "Data Display"),
    InfiniteScroll("Infinite Scroll", "Data Display"),
    Timeline("Timeline", "Data Display"),
    Calendar("Calendar", "Data Display"),
    Descriptions("Descriptions", "Data Display"),
    // Navigation
    Affix("Affix", "Navigation"),
    Anchor("Anchor", "Navigation"),
    Backtop("Backtop", "Navigation"),
    Menu("Menu", "Navigation"),
    PageHeader("Page Header", "Navigation"),
    Steps("Steps", "Navigation"),
    Breadcrumb("Breadcrumb", "Navigation"),
    Tabs("Tabs", "Navigation"),
    Dropdown("Dropdown", "Navigation"),
    // Feedback
    Alert("Alert", "Feedback"),
    Message("Message", "Feedback"),
    MessageBox("Message Box", "Feedback"),
    Notification("Notification", "Feedback"),
    Loading("Loading", "Feedback"),
    Popconfirm("Popconfirm", "Feedback"),
    Popover("Popover", "Feedback"),
    Tooltip("Tooltip", "Feedback"),
    Tour("Tour", "Feedback"),
    // Container
    Card("Card", "Container"),
    Collapse("Collapse", "Container"),
    Dialog("Dialog", "Container"),
    Drawer("Drawer", "Container"),
    Carousel("Carousel", "Container"),
    Watermark("Watermark", "Others"),
}

@Composable
fun App(modifier: Modifier = Modifier) {
    var isDarkMode by remember { mutableStateOf(false) }
    val colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme()

    NexusTheme(colorScheme = colorScheme) {
        val colors = NexusTheme.colorScheme
        var currentRoute by remember { mutableStateOf(DemoRoute.Overview) }

        Column(
            modifier = modifier
                .fillMaxSize()
                .safeContentPadding()
                .background(colors.background.page),
        ) {
            // Top header
            AppHeader(
                isDarkMode = isDarkMode,
                onToggleDarkMode = { isDarkMode = !isDarkMode },
            )

            // Body: sidebar + content
            Row(modifier = Modifier.weight(1f)) {
                // Sidebar
                AppSidebar(
                    currentRoute = currentRoute,
                    onRouteChange = { currentRoute = it },
                )

                // Content area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(NexusTheme.shapes.base)
                        .background(colors.fill.blank)
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    DemoContent(currentRoute, onNavigate = { currentRoute = it })
                }
            }
        }
    }
}

@Composable
private fun AppHeader(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(colorScheme.fill.blank)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        NexusText(
            text = "Compose Nexus",
            color = colorScheme.primary.base,
            style = typography.large,
        )
        Spacer(modifier = Modifier.width(12.dp))
        NexusText(
            text = "Component Library",
            color = colorScheme.text.secondary,
            style = typography.small,
        )
        Spacer(modifier = Modifier.weight(1f))
        // Dark mode toggle
        Box(
            modifier = Modifier
                .clip(NexusTheme.shapes.base)
                .background(colorScheme.fill.light)
                .clickable { onToggleDarkMode() }
                .pointerHoverIcon(PointerIcon.Hand)
                .padding(horizontal = 12.dp, vertical = 6.dp),
        ) {
            NexusText(
                text = if (isDarkMode) "Light" else "Dark",
                color = colorScheme.text.regular,
                style = typography.small,
            )
        }
    }
    NexusDivider()
}

@Composable
private fun AppSidebar(
    currentRoute: DemoRoute,
    onRouteChange: (DemoRoute) -> Unit,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    Column(
        modifier = Modifier
            .width(220.dp)
            .fillMaxHeight()
            .background(colorScheme.fill.blank)
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
    ) {
        var lastGroup = ""
        DemoRoute.entries.forEach { route ->
            // Group header
            if (route.group != lastGroup && route.group.isNotEmpty()) {
                lastGroup = route.group
                Spacer(modifier = Modifier.height(12.dp))
                NexusText(
                    text = route.group,
                    color = colorScheme.text.placeholder,
                    style = typography.extraSmall,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                )
            }

            // Menu item
            val isActive = currentRoute == route
            val interactionSource = remember { MutableInteractionSource() }
            val isHovered by interactionSource.collectIsHoveredAsState()

            val bgColor = when {
                isActive -> colorScheme.primary.light9
                isHovered -> colorScheme.fill.light
                else -> Color.Transparent
            }
            val textColor = when {
                isActive -> colorScheme.primary.base
                isHovered -> colorScheme.primary.base
                else -> colorScheme.text.regular
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 36.dp)
                    .hoverable(interactionSource)
                    .background(bgColor)
                    .clickable { onRouteChange(route) }
                    .pointerHoverIcon(PointerIcon.Hand)
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                NexusText(
                    text = route.label,
                    color = textColor,
                    style = typography.base,
                )
            }
        }
    }
}

@Composable
private fun DemoContent(route: DemoRoute, onNavigate: (DemoRoute) -> Unit) {
    when (route) {
        DemoRoute.Overview -> OverviewDemo(onNavigate = onNavigate)
        DemoRoute.Button -> ButtonDemo()
        DemoRoute.Border -> BorderDemo()
        DemoRoute.Color -> ColorDemo()
        DemoRoute.LayoutContainer -> LayoutContainerDemo()
        DemoRoute.Icon -> IconDemo()
        DemoRoute.Layout -> LayoutDemo()
        DemoRoute.Text -> TextDemo()
        DemoRoute.Typography -> TypographyDemo()
        DemoRoute.Link -> LinkDemo()
        DemoRoute.Scrollbar -> ScrollbarDemo()
        DemoRoute.Tag -> TagDemo()
        DemoRoute.Divider -> DividerDemo()
        DemoRoute.Space -> SpaceDemo()
        DemoRoute.Splitter -> SplitterDemo()
        DemoRoute.ConfigProvider -> ConfigProviderDemo()
        DemoRoute.Input -> InputDemo()
        DemoRoute.InputNumber -> InputNumberDemo()
        DemoRoute.Textarea -> TextareaDemo()
        DemoRoute.Checkbox -> CheckboxDemo()
        DemoRoute.Radio -> RadioDemo()
        DemoRoute.Rate -> RateDemo()
        DemoRoute.Switch -> SwitchDemo()
        DemoRoute.Select -> SelectDemo()
        DemoRoute.VirtualizedSelect -> VirtualizedSelectDemo()
        DemoRoute.Slider -> SliderDemo()
        DemoRoute.DatePickerPanel -> DatePickerPanelDemo()
        DemoRoute.DatePicker -> DatePickerDemo()
        DemoRoute.DateTimePicker -> DateTimePickerDemo()
        DemoRoute.TimePicker -> TimePickerDemo()
        DemoRoute.TimeSelect -> TimeSelectDemo()
        DemoRoute.Form -> FormDemo()
        DemoRoute.Autocomplete -> AutocompleteDemo()
        DemoRoute.ColorPickerPanel -> ColorPickerPanelDemo()
        DemoRoute.ColorPicker -> ColorPickerDemo()
        DemoRoute.InputTag -> InputTagDemo()
        DemoRoute.Mention -> MentionDemo()
        DemoRoute.Cascader -> CascaderDemo()
        DemoRoute.Transfer -> TransferDemo()
        DemoRoute.TreeSelect -> TreeSelectDemo()
        DemoRoute.Upload -> UploadDemo()
        DemoRoute.Table -> TableDemo()
        DemoRoute.VirtualizedTable -> VirtualizedTableDemo()
        DemoRoute.Tree -> TreeDemo()
        DemoRoute.VirtualizedTree -> VirtualizedTreeDemo()
        DemoRoute.Statistic -> StatisticDemo()
        DemoRoute.Segmented -> SegmentedDemo()
        DemoRoute.Pagination -> PaginationDemo()
        DemoRoute.Progress -> ProgressDemo()
        DemoRoute.Result -> ResultDemo()
        DemoRoute.Avatar -> AvatarDemo()
        DemoRoute.Badge -> BadgeDemo()
        DemoRoute.Skeleton -> SkeletonDemo()
        DemoRoute.Empty -> EmptyDemo()
        DemoRoute.Image -> ImageDemo()
        DemoRoute.InfiniteScroll -> InfiniteScrollDemo()
        DemoRoute.Timeline -> TimelineDemo()
        DemoRoute.Calendar -> CalendarDemo()
        DemoRoute.Descriptions -> DescriptionsDemo()
        DemoRoute.Affix -> AffixDemo()
        DemoRoute.Anchor -> AnchorDemo()
        DemoRoute.Backtop -> BacktopDemo()
        DemoRoute.Menu -> MenuDemo()
        DemoRoute.PageHeader -> PageHeaderDemo()
        DemoRoute.Steps -> StepsDemo()
        DemoRoute.Breadcrumb -> BreadcrumbDemo()
        DemoRoute.Tabs -> TabsDemo()
        DemoRoute.Dropdown -> DropdownDemo()
        DemoRoute.Alert -> AlertDemo()
        DemoRoute.Message -> MessageDemo()
        DemoRoute.MessageBox -> MessageBoxDemo()
        DemoRoute.Notification -> NotificationDemo()
        DemoRoute.Loading -> LoadingDemo()
        DemoRoute.Popconfirm -> PopconfirmDemo()
        DemoRoute.Popover -> PopoverDemo()
        DemoRoute.Tooltip -> TooltipDemo()
        DemoRoute.Tour -> TourDemo()
        DemoRoute.Card -> CardDemo()
        DemoRoute.Collapse -> CollapseDemo()
        DemoRoute.Dialog -> DialogDemo()
        DemoRoute.Drawer -> DrawerDemo()
        DemoRoute.Carousel -> CarouselDemo()
        DemoRoute.Watermark -> WatermarkDemo()
    }
}
