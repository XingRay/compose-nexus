# Compose Nexus UI 库开发计划

## 一、架构设计

**核心理念：** 参照 `compose-unstyled` 的分层架构，将 **行为逻辑** 与 **视觉样式** 分离。

```
compose-nexus-core          ← 无样式原语层 (行为 + 无障碍 + 状态管理) + Element Plus 设计令牌层 + 样式化控件层 + 页面模板层
```

**关键模式（学习 compose-unstyled）：**

| 模式 | 说明 |
|---|---|
| **State Object** | 每个有状态的控件提供 `@Stable` 状态类 + `remember*State()` 工厂 |
| **Scope Composition** | 复合组件使用 Scope 类作为子组件的 receiver，编译期约束嵌套关系 |
| **CompositionLocal** | 跨层级传递主题/状态（`LocalNexusTheme`、`LocalContentColor`） |
| **Slot API** | 通过 `@Composable` lambda 参数实现内容插槽，消费者决定展示内容 |
| **buildModifier** | 条件化组合 Modifier 链 |

---

## 二、设计令牌（复刻 Element Plus）

设计令牌（Design Token）= 把颜色、字号、圆角、间距等设计规范值集中管理的常量系统，方便统一风格、切换主题、支持暗色模式。

| 令牌类别 | 关键值 |
|---|---|
| **Primary** | `#409EFF` (light-1~9, dark-2) |
| **Success** | `#67C23A` |
| **Warning** | `#E6A23C` |
| **Danger** | `#F56C6C` |
| **Info** | `#909399` |
| **Text** | primary `#303133`, regular `#606266`, secondary `#909399`, placeholder `#A8ABB2`, disabled `#C0C4CC` |
| **Border** | `#DCDFE6`, light `#E4E7ED`, lighter `#EBEEF5` |
| **Fill** | `#F0F2F5`, light `#F5F7FA`, blank `#FFFFFF` |
| **Font Size** | extra-large 20sp, large 18sp, medium 16sp, base 14sp, small 13sp, extra-small 12sp |
| **Font Family** | Helvetica Neue, PingFang SC, Microsoft YaHei, sans-serif |
| **Border Radius** | base 4dp, small 2dp, round 20dp, circle 50% |
| **Component Size** | large 40dp, default 32dp, small 24dp |
| **Shadow** | light `0,0,12 @12%α`, default `0,12,32 + 0,8,20`, dark `0,16,48 + 0,12,32 + 0,8,16` |
| **Transition** | duration 300ms, fast 200ms, easing `cubic-bezier(0.645, 0.045, 0.355, 1)` |
| **Dark Mode** | 完整的暗色令牌覆盖（bg `#141414`、text `#E5EAF3` 等） |

### 完整色彩令牌

#### Primary (#409EFF) 变体
```
light-1: #53A8FF    light-2: #66B1FF    light-3: #79BBFF
light-4: #8CC5FF    light-5: #A0CEFF    light-6: #B3D8FF
light-7: #C6E2FF    light-8: #D9ECFF    light-9: #ECF5FF
dark-2:  #337ECC
```

#### Success (#67C23A) 变体
```
light-1: #76C84E    light-2: #85CE61    light-3: #95D475
light-4: #A4DA89    light-5: #B3E09C    light-6: #C2E7B0
light-7: #D1EDC4    light-8: #E1F3D8    light-9: #F0F9EB
dark-2:  #529B2E
```

#### Warning (#E6A23C) 变体
```
light-1: #E8AB50    light-2: #EBB563    light-3: #EEBE76
light-4: #F0C78A    light-5: #F2D09E    light-6: #F5DAB1
light-7: #F8E3C4    light-8: #FAECD8    light-9: #FCF6EC
dark-2:  #B88230
```

#### Danger (#F56C6C) 变体
```
light-1: #F67B7B    light-2: #F78989    light-3: #F89898
light-4: #F9A7A7    light-5: #FAB6B6    light-6: #FBC4C4
light-7: #FCD3D3    light-8: #FDE2E2    light-9: #FEF0F0
dark-2:  #C45656
```

#### Info (#909399) 变体
```
light-1: #9B9EA3    light-2: #A6A9AD    light-3: #B1B3B8
light-4: #BCBEC2    light-5: #C8C9CC    light-6: #D3D4D6
light-7: #DEDFE0    light-8: #E9E9EB    light-9: #F4F4F5
dark-2:  #73767A
```

### 暗色模式令牌

```
背景色:
  --el-bg-color:         #141414
  --el-bg-color-page:    #0A0A0A
  --el-bg-color-overlay: #1D1E1F

文字色:
  --el-text-color-primary:     #E5EAF3
  --el-text-color-regular:     #CFD3DC
  --el-text-color-secondary:   #A3A6AD
  --el-text-color-placeholder: #8D9095
  --el-text-color-disabled:    #6C6E72

边框色:
  --el-border-color:             #4C4D4F
  --el-border-color-light:       #414243
  --el-border-color-lighter:     #363637
  --el-border-color-extra-light: #2A2B2C
  --el-border-color-dark:        #58585A
  --el-border-color-darker:      #636466

填充色:
  --el-fill-color:             #303030
  --el-fill-color-light:       #262727
  --el-fill-color-lighter:     #1D1D1D
  --el-fill-color-extra-light: #191919
  --el-fill-color-dark:        #39393A
  --el-fill-color-darker:      #424243
  --el-fill-color-blank:       #141414

阴影:
  --el-box-shadow:         0px 12px 32px 4px rgba(0,0,0,0.36), 0px 8px 20px rgba(0,0,0,0.72)
  --el-box-shadow-light:   0px 0px 12px rgba(0,0,0,0.72)
  --el-box-shadow-lighter: 0px 0px 6px rgba(0,0,0,0.72)
  --el-box-shadow-dark:    0px 16px 48px 16px rgba(0,0,0,0.72), 0px 12px 32px #000, 0px 8px 16px -8px #000

遮罩:
  --el-mask-color:             rgba(0,0,0,0.8)
  --el-mask-color-extra-light: rgba(0,0,0,0.3)
```

---

## 三、完整控件清单

### 第一层：控件（Primitives）— 基础 UI 元素

#### P0 - 核心基础（Phase 1）

| # | 控件名 | EP 对应 | 说明 |
|---|---|---|---|
| 1 | `Button` | Button | 主要/成功/警告/危险/信息/文本 类型，大/中/小 尺寸，plain/round/circle 变体，loading/disabled 状态 |
| 2 | `Text` | Text | 带语义类型（primary/success/warning/danger/info），size，truncate/ellipsis |
| 3 | `Icon` | Icon | SVG 图标系统，支持大小/颜色，内置 Element Plus 图标集 |
| 4 | `Link` | Link | 文字链接，带 underline、disabled、类型色 |
| 5 | `Input` | Input | 文本输入框，clearable, password 可见切换，prefix/suffix 插槽，大/中/小 尺寸 |
| 6 | `InputNumber` | InputNumber | 数字输入框，increase/decrease 按钮，min/max/step/precision |
| 7 | `Textarea` | Input(textarea) | 多行文本输入，autosize，字数统计 |
| 8 | `Checkbox` | Checkbox | 勾选框，indeterminate 三态，CheckboxGroup 组 |
| 9 | `Radio` | Radio | 单选框，RadioGroup，RadioButton（按钮式单选） |
| 10 | `Switch` | Switch | 开关，自定义开/关文字、图标，loading 状态 |
| 11 | `Divider` | Divider | 分割线，horizontal/vertical，带文字内容位置 |
| 12 | `Space` | Space | 间距容器，horizontal/vertical，自定义 gap，wrap |

#### P1 - 表单扩展（Phase 2）

| # | 控件名 | EP 对应 | 说明 |
|---|---|---|---|
| 13 | `Select` | Select | 下拉选择器，filterable, multiple, clearable, remote-search, 分组 |
| 14 | `Slider` | Slider | 滑块，range 双端，marks 刻度，vertical，tooltip |
| 15 | `Rate` | Rate | 评分，半星，自定义图标/颜色，文字描述 |
| 16 | `ColorPicker` | ColorPicker | 颜色选择器，预定义颜色，alpha 通道 |
| 17 | `DatePicker` | DatePicker | 日期选择器，year/month/date/week/datetime，range |
| 18 | `TimePicker` | TimePicker | 时间选择器，固定时间点/任意时间，range |
| 19 | `Upload` | Upload | 文件上传，drag, picture-card, file-list |
| 20 | `Transfer` | Transfer | 穿梭框，filterable，自定义数据项 |
| 21 | `Cascader` | Cascader | 级联选择器，多级选项，filterable |
| 22 | `Form` / `FormItem` | Form | 表单容器 + 表单项，label 布局，校验规则，error 信息 |

#### P2 - 数据展示（Phase 3）

| # | 控件名 | EP 对应 | 说明 |
|---|---|---|---|
| 23 | `Tag` | Tag | 标签，closable，类型色，hit 边框，round，大小 |
| 24 | `Badge` | Badge | 徽标数，max 封顶值，小圆点，自定义内容 |
| 25 | `Avatar` | Avatar | 头像，icon/image/text，circle/square，尺寸，fit |
| 26 | `Progress` | Progress | 进度条，line/circle/dashboard，percentage，条纹/动画 |
| 27 | `Statistic` | Statistic | 统计数值，前缀/后缀，倒计时 Countdown |
| 28 | `Pagination` | Pagination | 分页，sizes/jumper/total/pager-count，布局自定义 |
| 29 | `Tooltip` | Tooltip | 文字提示，placement 12方位，触发方式，主题 dark/light |
| 30 | `Popover` | Popover | 弹出框，比 tooltip 更丰富的内容，trigger 方式 |
| 31 | `Image` | Image | 图片，lazy loading，preview 预览，fit 模式，placeholder/error 插槽 |
| 32 | `Skeleton` | Skeleton | 骨架屏，animated，rows，不同形状段落 |
| 33 | `Empty` | Empty | 空状态占位，自定义图片/描述/底部内容 |
| 34 | `Result` | Result | 结果页，success/warning/info/error，icon/title/sub-title/extra |
| 35 | `Segmented` | Segmented | 分段控制器，block，disabled 选项 |

#### P3 - 导航类（Phase 4）

| # | 控件名 | EP 对应 | 说明 |
|---|---|---|---|
| 36 | `Tabs` / `TabPane` | Tabs | 选项卡，card/border-card 类型，可增删，滚动 |
| 37 | `Breadcrumb` | Breadcrumb | 面包屑，自定义分隔符 |
| 38 | `Dropdown` | Dropdown | 下拉菜单，split-button, command 事件，嵌套 |
| 39 | `Steps` | Steps | 步骤条，horizontal/vertical，simple/icon，状态 |
| 40 | `Menu` / `MenuItem` / `SubMenu` | Menu | 导航菜单，horizontal/vertical，collapse 折叠，router |
| 41 | `Backtop` | Backtop | 回到顶部，visibility-height，自定义内容 |
| 42 | `PageHeader` | PageHeader | 页头，返回按钮，标题/内容/extra 插槽 |
| 43 | `Anchor` | Anchor | 锚点，滚动定位，container 指定 |

---

### 第二层：容器（Containers）— 布局与动画容器

| # | 容器名 | EP 对应 | 说明 |
|---|---|---|---|
| 44 | `Dialog` | Dialog | 对话框，modal，自定义 header/body/footer，fullscreen，draggable，嵌套，关闭前确认 |
| 45 | `Drawer` | Drawer | 抽屉，direction(上/下/左/右)，自定义尺寸，嵌套 |
| 46 | `Card` | Card | 卡片，header/body 插槽，shadow(always/hover/never) |
| 47 | `Collapse` / `CollapseItem` | Collapse | 折叠面板，accordion 模式，自定义 title |
| 48 | `Container` / `Header` / `Aside` / `Main` / `Footer` | Container | 布局容器，方向自动判断 |
| 49 | `ScrollArea` | Scrollbar | 自定义滚动条，always/hover 显示，min-thumb-size |
| 50 | `Carousel` | Carousel | 走马灯，indicator/arrow，autoplay，vertical，card 模式 |
| 51 | `Splitter` | Splitter | 分割面板，horizontal/vertical，可拖拽调整，collapse |
| 52 | `Waterfall` | - | 瀑布流容器，自适应列数 |
| 53 | `ModalBottomSheet` | - | 移动端底部弹出面板 (移动端增强) |

---

### 第三层：组件（Composed Components）— 多控件组合

| # | 组件名 | EP 对应 | 说明 |
|---|---|---|---|
| 54 | `SearchBar` | - | 搜索栏 = Input + Button + ClearIcon + 推荐下拉列表 |
| 55 | `TreeMenu` | Tree/Menu | 树形菜单 = 多级可展开节点 + 选中/disabled/hover 状态 + 图标 |
| 56 | `Tree` | Tree | 树形控件 = 可展开节点 + checkbox + lazy-load + drag + filter + 自定义内容 |
| 57 | `TreeSelect` | TreeSelect | 树形选择器 = Select + Tree |
| 58 | `Table` | Table | 表格 = 表头排序 + 列筛选 + 固定列/头 + 展开行 + 多选 + 合计 + 树形数据 |
| 59 | `VirtualList` | VirtualList | 虚拟滚动列表 = 大数据量高性能渲染 |
| 60 | `Descriptions` | Descriptions | 描述列表 = label-value 对的列表展示，多列布局，border |
| 61 | `Timeline` | Timeline | 时间线 = 带时间戳的活动列表，自定义节点图标/颜色 |
| 62 | `Calendar` | Calendar | 日历 = 月视图，自定义日期单元格 |
| 63 | `Autocomplete` | Autocomplete | 自动补全 = Input + 建议下拉列表 + debounce + highlight |
| 64 | `Mention` | Mention | 提及 = Input/Textarea + @ 触发 + 候选人下拉 |
| 65 | `InputTag` | InputTag | 标签输入 = Input + Tag 列表 + 回退删除 |
| 66 | `Alert` | Alert | 警告 = 类型色条 + 图标 + 标题 + 描述 + closable |
| 67 | `Message` | Message | 消息提示 = 顶部弹出，自动消失，可堆叠 |
| 68 | `MessageBox` | MessageBox | 消息弹框 = Dialog 封装，alert/confirm/prompt 三种模式 |
| 69 | `Notification` | Notification | 通知 = 角落弹出，标题 + 描述 + 图标 + 关闭 |
| 70 | `Popconfirm` | Popconfirm | 气泡确认框 = Popover + 确认/取消按钮 |
| 71 | `Tour` / `TourStep` | Tour | 漫游式引导 = 高亮目标元素 + 说明弹窗 + 步骤导航 |
| 72 | `Loading` | Loading | 加载 = 全屏/区域遮罩 + spinner + 文字 |
| 73 | `InfiniteScroll` | InfiniteScroll | 无限滚动 = 滚动到底部自动加载更多 |
| 74 | `Watermark` | Watermark | 水印 = 全页面/区域文字/图片水印 |
| 75 | `Affix` | Affix | 固钉 = 滚动时固定在可视区域 |

---

### 第四层：页面模板（Page Templates）— 通用业务页面

| # | 模板名 | 说明 |
|---|---|---|
| 76 | `SearchSelectPage` | 搜索选择页 = SearchBar + 结果列表(可选中/反选) + 已选列表(可删除) + 确定/取消 |
| 77 | `CrudTablePage` | CRUD 表格页 = 筛选栏 + Table + Pagination + 新增/编辑 Dialog + 删除确认 |
| 78 | `FormPage` | 表单页 = Form + FormItems + 提交/重置按钮 + 校验反馈 |
| 79 | `DetailPage` | 详情页 = PageHeader + Descriptions + Tabs 分区 |
| 80 | `LoginPage` | 登录页 = Logo + Form(用户名/密码/验证码) + 记住密码 + 其他登录方式 |
| 81 | `SettingsPage` | 设置页 = Menu(侧边栏) + 右侧 Form/Switch/Select 设置项 |
| 82 | `DashboardPage` | 仪表盘 = Statistic 卡片 + 图表区 + 列表/Table |
| 83 | `TreeListPage` | 树形列表页 = 左侧 TreeMenu + 右侧 Table/List |
| 84 | `WizardPage` | 向导页 = Steps + 分步 Form + 上一步/下一步/提交 |
| 85 | `ErrorPage` | 错误页 = Result(403/404/500) + 返回/重试按钮 |

---

## 四、代码目录结构

```
compose-nexus-core/
├── src/commonMain/kotlin/io/github/xingray/compose/nexus/
│   ├── foundation/           ← 基础工具
│   │   ├── BuildModifier.kt
│   │   ├── LocalContentColor.kt
│   │   ├── LocalTextStyle.kt
│   │   └── ProvideContentColorTextStyle.kt
│   ├── theme/                ← 主题系统 (设计令牌)
│   │   ├── NexusTheme.kt
│   │   ├── ColorScheme.kt
│   │   ├── Typography.kt
│   │   ├── Sizes.kt
│   │   ├── Shapes.kt
│   │   ├── Shadows.kt
│   │   └── Motion.kt
│   ├── controls/             ← 第一层：控件
│   │   ├── button/
│   │   ├── text/
│   │   ├── icon/
│   │   ├── input/
│   │   ├── checkbox/
│   │   ├── radio/
│   │   ├── switch/
│   │   └── ...
│   ├── containers/           ← 第二层：容器
│   │   ├── dialog/
│   │   ├── drawer/
│   │   ├── card/
│   │   ├── collapse/
│   │   └── ...
│   ├── components/           ← 第三层：组件
│   │   ├── searchbar/
│   │   ├── treemenu/
│   │   ├── table/
│   │   ├── alert/
│   │   └── ...
│   └── templates/            ← 第四层：页面模板
│       ├── SearchSelectPage.kt
│       ├── CrudTablePage.kt
│       └── ...
├── src/androidMain/
├── src/iosMain/
├── src/jvmMain/
├── src/jsMain/
└── src/wasmJsMain/
```

---

## 五、开发阶段

| 阶段 | 内容 | 控件数 |
|---|---|---|
| **Phase 0** | 基础设施：NexusTheme 主题系统 + 设计令牌 + foundation 工具 | - |
| **Phase 1** | P0 核心控件：Button, Text, Icon, Link, Input, Checkbox, Radio, Switch, Divider, Space, Tag, Badge | 12 |
| **Phase 2** | P1 表单 + 容器：Select, Slider, Form, Dialog, Drawer, Card, Collapse, Tabs, Dropdown, Tooltip | 10 |
| **Phase 3** | P2 数据展示：Table, Tree, Pagination, Progress, Avatar, Skeleton, Empty, Carousel, Alert, Message, Notification | 11 |
| **Phase 4** | P3 导航 + 高级：Menu, Steps, Breadcrumb, TreeMenu, SearchBar, Autocomplete, DatePicker, TimePicker, Loading, Tour | 10 |
| **Phase 5** | 组合组件 + 虚拟滚动：VirtualList, Transfer, Cascader, TreeSelect, Calendar, InputTag, Mention, InfiniteScroll | 8 |
| **Phase 6** | 页面模板：SearchSelectPage, CrudTablePage, FormPage, DetailPage, LoginPage, etc. | 10 |

---

## 六、参考项目

| 项目 | 用途 |
|---|---|
| [element-plus](https://github.com/element-plus/element-plus) | UI 风格、设计令牌、组件列表参考 |
| [compose-unstyled](https://github.com/composablehorizons/compose-unstyled) | **核心实现模式参考**：状态管理、Scope 组合、行为与样式分离 |
| [compose-cupertino](https://github.com/alexzhirkevich/compose-cupertino) | Compose Multiplatform 跨平台组件库实现参考 |
| [Material3](https://github.com/androidx/androidx/tree/androidx-main/compose/material3) | Compose 主题系统、组件实现参考 |
| [Material](https://github.com/androidx/androidx/tree/androidx-main/compose/material) | Compose 组件实现参考 |
| [JetBrains Compose Components](https://github.com/JetBrains/compose-multiplatform/tree/master/components) | 跨平台组件实现参考 |
