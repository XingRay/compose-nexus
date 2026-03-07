package io.github.xingray.compose.nexus.sample

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
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.sample.demos.AffixDemo
import io.github.xingray.compose.nexus.sample.demos.AlertDemo
import io.github.xingray.compose.nexus.sample.demos.AnchorDemo
import io.github.xingray.compose.nexus.sample.demos.AutocompleteDemo
import io.github.xingray.compose.nexus.sample.demos.AvatarDemo
import io.github.xingray.compose.nexus.sample.demos.BacktopDemo
import io.github.xingray.compose.nexus.sample.demos.BadgeDemo
import io.github.xingray.compose.nexus.sample.demos.BorderDemo
import io.github.xingray.compose.nexus.sample.demos.BreadcrumbDemo
import io.github.xingray.compose.nexus.sample.demos.ButtonDemo
import io.github.xingray.compose.nexus.sample.demos.CalendarDemo
import io.github.xingray.compose.nexus.sample.demos.CardDemo
import io.github.xingray.compose.nexus.sample.demos.CarouselDemo
import io.github.xingray.compose.nexus.sample.demos.CascaderDemo
import io.github.xingray.compose.nexus.sample.demos.CheckboxDemo
import io.github.xingray.compose.nexus.sample.demos.CollapseDemo
import io.github.xingray.compose.nexus.sample.demos.ColorDemo
import io.github.xingray.compose.nexus.sample.demos.ColorPickerDemo
import io.github.xingray.compose.nexus.sample.demos.ColorPickerPanelDemo
import io.github.xingray.compose.nexus.sample.demos.ConfigProviderDemo
import io.github.xingray.compose.nexus.sample.demos.DatePickerDemo
import io.github.xingray.compose.nexus.sample.demos.DatePickerPanelDemo
import io.github.xingray.compose.nexus.sample.demos.DateTimePickerDemo
import io.github.xingray.compose.nexus.sample.demos.DescriptionsDemo
import io.github.xingray.compose.nexus.sample.demos.DialogDemo
import io.github.xingray.compose.nexus.sample.demos.DividerDemo
import io.github.xingray.compose.nexus.sample.demos.DrawerDemo
import io.github.xingray.compose.nexus.sample.demos.DropdownDemo
import io.github.xingray.compose.nexus.sample.demos.EmptyDemo
import io.github.xingray.compose.nexus.sample.demos.FormDemo
import io.github.xingray.compose.nexus.sample.demos.IconDemo
import io.github.xingray.compose.nexus.sample.demos.ImageDemo
import io.github.xingray.compose.nexus.sample.demos.InfiniteScrollDemo
import io.github.xingray.compose.nexus.sample.demos.InputDemo
import io.github.xingray.compose.nexus.sample.demos.InputNumberDemo
import io.github.xingray.compose.nexus.sample.demos.InputTagDemo
import io.github.xingray.compose.nexus.sample.demos.LayoutContainerDemo
import io.github.xingray.compose.nexus.sample.demos.LayoutDemo
import io.github.xingray.compose.nexus.sample.demos.LinkDemo
import io.github.xingray.compose.nexus.sample.demos.LoadingDemo
import io.github.xingray.compose.nexus.sample.demos.MentionDemo
import io.github.xingray.compose.nexus.sample.demos.MenuDemo
import io.github.xingray.compose.nexus.sample.demos.MessageBoxDemo
import io.github.xingray.compose.nexus.sample.demos.MessageDemo
import io.github.xingray.compose.nexus.sample.demos.NotificationDemo
import io.github.xingray.compose.nexus.sample.demos.OverviewDemo
import io.github.xingray.compose.nexus.sample.demos.PageHeaderDemo
import io.github.xingray.compose.nexus.sample.demos.PaginationDemo
import io.github.xingray.compose.nexus.sample.demos.PopconfirmDemo
import io.github.xingray.compose.nexus.sample.demos.PopoverDemo
import io.github.xingray.compose.nexus.sample.demos.ProgressDemo
import io.github.xingray.compose.nexus.sample.demos.RadioDemo
import io.github.xingray.compose.nexus.sample.demos.RateDemo
import io.github.xingray.compose.nexus.sample.demos.ResultDemo
import io.github.xingray.compose.nexus.sample.demos.ScrollbarDemo
import io.github.xingray.compose.nexus.sample.demos.SegmentedDemo
import io.github.xingray.compose.nexus.sample.demos.SelectDemo
import io.github.xingray.compose.nexus.sample.demos.SkeletonDemo
import io.github.xingray.compose.nexus.sample.demos.SliderDemo
import io.github.xingray.compose.nexus.sample.demos.SpaceDemo
import io.github.xingray.compose.nexus.sample.demos.SplitterDemo
import io.github.xingray.compose.nexus.sample.demos.StatisticDemo
import io.github.xingray.compose.nexus.sample.demos.StepsDemo
import io.github.xingray.compose.nexus.sample.demos.SwitchDemo
import io.github.xingray.compose.nexus.sample.demos.TableDemo
import io.github.xingray.compose.nexus.sample.demos.TabsDemo
import io.github.xingray.compose.nexus.sample.demos.TagDemo
import io.github.xingray.compose.nexus.sample.demos.TextDemo
import io.github.xingray.compose.nexus.sample.demos.TextareaDemo
import io.github.xingray.compose.nexus.sample.demos.TimePickerDemo
import io.github.xingray.compose.nexus.sample.demos.TimeSelectDemo
import io.github.xingray.compose.nexus.sample.demos.TimelineDemo
import io.github.xingray.compose.nexus.sample.demos.TooltipDemo
import io.github.xingray.compose.nexus.sample.demos.TourDemo
import io.github.xingray.compose.nexus.sample.demos.TransferDemo
import io.github.xingray.compose.nexus.sample.demos.TreeDemo
import io.github.xingray.compose.nexus.sample.demos.TreeSelectDemo
import io.github.xingray.compose.nexus.sample.demos.TypographyDemo
import io.github.xingray.compose.nexus.sample.demos.UploadDemo
import io.github.xingray.compose.nexus.sample.demos.VirtualizedSelectDemo
import io.github.xingray.compose.nexus.sample.demos.VirtualizedTableDemo
import io.github.xingray.compose.nexus.sample.demos.VirtualizedTreeDemo
import io.github.xingray.compose.nexus.sample.demos.WatermarkDemo
import io.github.xingray.compose.nexus.theme.NexusTheme

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
    val colorScheme = if (isDarkMode) _root_ide_package_.io.github.xingray.compose.nexus.theme.darkColorScheme() else _root_ide_package_.io.github.xingray.compose.nexus.theme.lightColorScheme()

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
    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusDivider()
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
