package io.github.xingray.compose.nexus.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.theme.ComponentSize
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class NexusUploadStatus {
    Ready,
    Uploading,
    Success,
    Fail,
}

enum class NexusUploadListType {
    Text,
    Picture,
    PictureCard,
}

data class NexusUploadRawFile(
    val name: String,
    val size: Long = 0L,
    val type: String = "",
    val url: String? = null,
    val isDirectory: Boolean = false,
    val uid: Long = _root_ide_package_.io.github.xingray.compose.nexus.controls.nextUploadUid(),
)

data class NexusUploadFile(
    val uid: Long,
    val name: String,
    val size: Long = 0L,
    val status: io.github.xingray.compose.nexus.controls.NexusUploadStatus = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadStatus.Ready,
    val percentage: Float = 0f,
    val response: Any? = null,
    val url: String? = null,
    val raw: io.github.xingray.compose.nexus.controls.NexusUploadRawFile? = null,
)

@Stable
class UploadState(
    initialFiles: List<io.github.xingray.compose.nexus.controls.NexusUploadFile> = emptyList(),
) {
    val fileList = mutableStateListOf<io.github.xingray.compose.nexus.controls.NexusUploadFile>().apply { addAll(initialFiles) }

    internal var submitImpl: (() -> Unit)? = null
    internal var abortImpl: ((io.github.xingray.compose.nexus.controls.NexusUploadFile?) -> Unit)? = null
    internal var handleStartImpl: ((io.github.xingray.compose.nexus.controls.NexusUploadRawFile) -> Unit)? = null
    internal var handleRemoveImpl: ((io.github.xingray.compose.nexus.controls.NexusUploadFile) -> Unit)? = null

    fun submit() {
        submitImpl?.invoke()
    }

    fun abort(file: io.github.xingray.compose.nexus.controls.NexusUploadFile? = null) {
        abortImpl?.invoke(file)
    }

    fun handleStart(rawFile: io.github.xingray.compose.nexus.controls.NexusUploadRawFile) {
        handleStartImpl?.invoke(rawFile)
    }

    fun handleRemove(file: io.github.xingray.compose.nexus.controls.NexusUploadFile) {
        handleRemoveImpl?.invoke(file)
    }

    fun clearFiles(status: Set<io.github.xingray.compose.nexus.controls.NexusUploadStatus>? = null) {
        if (status == null) {
            fileList.clear()
        } else {
            fileList.removeAll { it.status in status }
        }
    }

    internal fun addFile(file: io.github.xingray.compose.nexus.controls.NexusUploadFile) {
        fileList.add(file)
    }

    internal fun updateFile(uid: Long, transform: (io.github.xingray.compose.nexus.controls.NexusUploadFile) -> io.github.xingray.compose.nexus.controls.NexusUploadFile) {
        val index = fileList.indexOfFirst { it.uid == uid }
        if (index >= 0) {
            fileList[index] = transform(fileList[index])
        }
    }

    internal fun removeByUid(uid: Long): io.github.xingray.compose.nexus.controls.NexusUploadFile? {
        val index = fileList.indexOfFirst { it.uid == uid }
        return if (index >= 0) {
            fileList.removeAt(index)
        } else {
            null
        }
    }
}

@Composable
fun rememberUploadState(
    initialFiles: List<io.github.xingray.compose.nexus.controls.NexusUploadFile> = emptyList(),
): io.github.xingray.compose.nexus.controls.UploadState = remember { _root_ide_package_.io.github.xingray.compose.nexus.controls.UploadState(initialFiles) }

@Composable
fun NexusUpload(
    state: io.github.xingray.compose.nexus.controls.UploadState = _root_ide_package_.io.github.xingray.compose.nexus.controls.rememberUploadState(),
    modifier: Modifier = Modifier,
    action: String = "#",
    method: String = "post",
    multiple: Boolean = false,
    showFileList: Boolean = true,
    drag: Boolean = false,
    accept: String = "",
    listType: io.github.xingray.compose.nexus.controls.NexusUploadListType = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadListType.Text,
    autoUpload: Boolean = true,
    disabled: Boolean = false,
    limit: Int? = null,
    directory: Boolean = false,
    trigger: (@Composable () -> Unit)? = null,
    tip: (@Composable () -> Unit)? = null,
    fileContent: (@Composable (file: io.github.xingray.compose.nexus.controls.NexusUploadFile, index: Int) -> Unit)? = null,
    onSelectFiles: (() -> List<io.github.xingray.compose.nexus.controls.NexusUploadRawFile>)? = null,
    onPreview: ((io.github.xingray.compose.nexus.controls.NexusUploadFile) -> Unit)? = null,
    onRemove: ((io.github.xingray.compose.nexus.controls.NexusUploadFile, List<io.github.xingray.compose.nexus.controls.NexusUploadFile>) -> Unit)? = null,
    onSuccess: ((Any?, io.github.xingray.compose.nexus.controls.NexusUploadFile, List<io.github.xingray.compose.nexus.controls.NexusUploadFile>) -> Unit)? = null,
    onError: ((Throwable, io.github.xingray.compose.nexus.controls.NexusUploadFile, List<io.github.xingray.compose.nexus.controls.NexusUploadFile>) -> Unit)? = null,
    onProgress: ((Float, io.github.xingray.compose.nexus.controls.NexusUploadFile, List<io.github.xingray.compose.nexus.controls.NexusUploadFile>) -> Unit)? = null,
    onChange: ((io.github.xingray.compose.nexus.controls.NexusUploadFile, List<io.github.xingray.compose.nexus.controls.NexusUploadFile>) -> Unit)? = null,
    onExceed: ((List<io.github.xingray.compose.nexus.controls.NexusUploadRawFile>, List<io.github.xingray.compose.nexus.controls.NexusUploadFile>) -> Unit)? = null,
    beforeUpload: (suspend (io.github.xingray.compose.nexus.controls.NexusUploadRawFile) -> Boolean)? = null,
    beforeRemove: (suspend (io.github.xingray.compose.nexus.controls.NexusUploadFile, List<io.github.xingray.compose.nexus.controls.NexusUploadFile>) -> Boolean)? = null,
    httpRequest: (suspend (io.github.xingray.compose.nexus.controls.NexusUploadRawFile) -> Result<Any?>)? = null,
) {
    @Suppress("UNUSED_VARIABLE")
    val _action = action
    @Suppress("UNUSED_VARIABLE")
    val _method = method
    @Suppress("UNUSED_VARIABLE")
    val _accept = accept
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val shapes = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.shapes
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography
    val scope = rememberCoroutineScope()
    val runningJobs = remember { mutableMapOf<Long, Job>() }
    var uploadQueue: ((io.github.xingray.compose.nexus.controls.NexusUploadFile) -> Unit)? = null

    fun addRawFile(rawFile: io.github.xingray.compose.nexus.controls.NexusUploadRawFile) {
        scope.launch {
            val allowedByDirectory = if (directory) rawFile.isDirectory || rawFile.name.contains("/") else !rawFile.isDirectory
            if (!allowedByDirectory) return@launch
            val allowed = beforeUpload?.invoke(rawFile) ?: true
            if (!allowed) return@launch

            val file = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadFile(
                uid = rawFile.uid,
                name = rawFile.name,
                size = rawFile.size,
                status = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadStatus.Ready,
                percentage = 0f,
                url = rawFile.url,
                raw = rawFile,
            )
            state.addFile(file)
            onChange?.invoke(file, state.fileList.toList())
            if (autoUpload) {
                uploadQueue?.invoke(file)
            }
        }
    }

    fun removeFile(file: io.github.xingray.compose.nexus.controls.NexusUploadFile) {
        scope.launch {
            val allow = beforeRemove?.invoke(file, state.fileList.toList()) ?: true
            if (!allow) return@launch
            runningJobs[file.uid]?.cancel()
            state.removeByUid(file.uid)
            onRemove?.invoke(file, state.fileList.toList())
        }
    }

    fun upload(file: io.github.xingray.compose.nexus.controls.NexusUploadFile) {
        if (file.status == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadStatus.Uploading) return
        val raw = file.raw ?: _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadRawFile(name = file.name, size = file.size, url = file.url, uid = file.uid)
        val job = scope.launch {
            state.updateFile(file.uid) {
                it.copy(status = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadStatus.Uploading, percentage = 0f)
            }
            listOf(20f, 55f, 80f).forEach { p ->
                delay(90)
                state.updateFile(file.uid) { current ->
                    current.copy(percentage = p)
                }
                state.fileList.firstOrNull { it.uid == file.uid }?.let { current ->
                    onProgress?.invoke(p, current, state.fileList.toList())
                }
            }

            val requestResult = runCatching {
                httpRequest?.invoke(raw) ?: Result.success(mapOf("ok" to true))
            }.getOrElse {
                Result.failure(it)
            }

            requestResult.fold(
                onSuccess = { result ->
                    state.updateFile(file.uid) { current ->
                        current.copy(
                            status = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadStatus.Success,
                            percentage = 100f,
                            response = result,
                        )
                    }
                    state.fileList.firstOrNull { it.uid == file.uid }?.let { current ->
                        onSuccess?.invoke(result, current, state.fileList.toList())
                        onChange?.invoke(current, state.fileList.toList())
                    }
                },
                onFailure = { err ->
                    state.updateFile(file.uid) { current ->
                        current.copy(status = _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadStatus.Fail)
                    }
                    state.fileList.firstOrNull { it.uid == file.uid }?.let { current ->
                        onError?.invoke(err, current, state.fileList.toList())
                        onChange?.invoke(current, state.fileList.toList())
                    }
                },
            )
        }
        runningJobs[file.uid] = job
    }

    fun selectFiles() {
        if (disabled) return
        val files = onSelectFiles?.invoke()
            ?: listOf(
                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadRawFile(
                    name = "mock-${Random.nextInt(1000)}.txt",
                    size = Random.nextLong(1024, 200 * 1024),
                    type = "text/plain",
                ),
            )
        val picked = if (multiple) files else files.take(1)
        if (limit != null && state.fileList.size + picked.size > limit) {
            onExceed?.invoke(picked, state.fileList.toList())
            return
        }
        picked.forEach { addRawFile(it) }
    }

    uploadQueue = { upload(it) }

    state.submitImpl = {
        state.fileList
            .filter { it.status == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadStatus.Ready }
            .forEach { upload(it) }
    }
    state.abortImpl = { target ->
        if (target == null) {
            runningJobs.values.forEach { it.cancel() }
        } else {
            runningJobs[target.uid]?.cancel()
        }
    }
    state.handleStartImpl = { raw -> addRawFile(raw) }
    state.handleRemoveImpl = { file -> removeFile(file) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val uploadAreaModifier = Modifier
            .fillMaxWidth()
            .clip(shapes.base)
            .border(
                width = 1.dp,
                color = if (drag) colorScheme.border.base else Color.Transparent,
                shape = shapes.base,
            )
            .background(if (drag) colorScheme.fill.light else Color.Transparent)
            .clickable(enabled = !disabled) { selectFiles() }
            .pointerHoverIcon(PointerIcon.Hand)
            .padding(12.dp)

        Box(
            modifier = uploadAreaModifier,
            contentAlignment = Alignment.Center,
        ) {
            if (trigger != null) {
                trigger()
            } else {
                if (drag) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "Drop file here or click to upload", style = typography.small)
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                            text = if (directory) "Directory mode enabled" else "Mock file selector",
                            style = typography.extraSmall,
                            color = colorScheme.text.secondary,
                        )
                    }
                } else {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                        onClick = { selectFiles() },
                        type = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary,
                        disabled = disabled,
                    ) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "Select File")
                    }
                }
            }
        }

        if (tip != null) {
            tip()
        }

        if (showFileList) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 240.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                state.fileList.forEachIndexed { index, file ->
                    val statusColor = when (file.status) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadStatus.Ready -> colorScheme.text.secondary
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadStatus.Uploading -> colorScheme.primary.base
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadStatus.Success -> colorScheme.success.base
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadStatus.Fail -> colorScheme.danger.base
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shapes.base)
                            .background(colorScheme.fill.lighter)
                            .clickable { onPreview?.invoke(file) }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        if (listType != _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadListType.Text) {
                            Box(
                                modifier = Modifier
                                    .size(if (listType == _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusUploadListType.PictureCard) 46.dp else 34.dp)
                                    .clip(shapes.base)
                                    .background(colorScheme.fill.light),
                                contentAlignment = Alignment.Center,
                            ) {
                                _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "IMG", style = typography.extraSmall, color = colorScheme.text.placeholder)
                            }
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            if (fileContent != null) {
                                fileContent(file, index)
                            } else {
                                Column {
                                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = file.name, style = typography.small)
                                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                                        text = "${_root_ide_package_.io.github.xingray.compose.nexus.controls.formatBytes(file.size)} • ${file.status.name.lowercase()} ${file.percentage.toInt()}%",
                                        style = typography.extraSmall,
                                        color = statusColor,
                                    )
                                }
                            }
                        }
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                            text = "Remove",
                            style = typography.extraSmall,
                            color = colorScheme.danger.base,
                            modifier = Modifier
                                .clickable { removeFile(file) }
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                        )
                    }
                }
            }
        }
    }
}

private fun formatBytes(size: Long): String {
    return when {
        size >= 1024 * 1024 -> "${_root_ide_package_.io.github.xingray.compose.nexus.controls.oneDecimal(size / 1024f / 1024f)} MB"
        size >= 1024 -> "${_root_ide_package_.io.github.xingray.compose.nexus.controls.oneDecimal(size / 1024f)} KB"
        else -> "$size B"
    }
}

private fun oneDecimal(value: Float): String {
    val scaled = (value * 10f).toInt() / 10f
    val integerPart = scaled.toInt()
    val decimalPart = ((scaled - integerPart) * 10f).toInt()
    return "$integerPart.$decimalPart"
}

private var uploadUidSeed = 0L

private fun nextUploadUid(): Long {
    _root_ide_package_.io.github.xingray.compose.nexus.controls.uploadUidSeed += 1
    return _root_ide_package_.io.github.xingray.compose.nexus.controls.uploadUidSeed
}
