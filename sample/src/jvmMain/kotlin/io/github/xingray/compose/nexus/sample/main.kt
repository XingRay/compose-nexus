package io.github.xingray.compose.nexus.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.xingray.compose.nexus.sample.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "compose.nexus sample",
    ) {
        App()
    }
}
