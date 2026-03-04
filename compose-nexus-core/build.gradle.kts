import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktechMavenPublish)
}

val isMacOs = System.getProperty("os.name").startsWith("Mac", ignoreCase = true)

kotlin {
    androidLibrary {
        namespace = "io.github.xingray.compose_nexus"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    if (isMacOs) {
        listOf(
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { iosTarget ->
            iosTarget.binaries.framework {
                baseName = "Nexus"
                isStatic = true
            }
        }
    }

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    js {
        browser()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.ui)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("io.github.xingray", "compose-nexus-core", "0.0.2")

    pom {
        name.set("compose-nexus-core")
        description.set("Compose Multiplatform UI component library inspired by Element Plus")
        url.set("https://github.com/XingRay/compose-nexus")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("XingRay")
                name.set("XingRay")
                email.set("leixing1012@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/XingRay/compose-nexus")
            connection.set("scm:git:git://github.com/XingRay/compose-nexus.git")
            developerConnection.set("scm:git:ssh://github.com/XingRay/compose-nexus.git")
        }
    }
}
