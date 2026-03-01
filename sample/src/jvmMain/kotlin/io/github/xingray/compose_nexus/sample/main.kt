package io.github.xingray.compose_nexus.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "compose_nexus sample",
    ) {
        App()
    }
}
