package io.github.xingray.compose_nexus

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
