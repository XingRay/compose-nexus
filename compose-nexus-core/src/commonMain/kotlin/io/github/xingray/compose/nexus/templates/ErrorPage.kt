package io.github.xingray.compose.nexus.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.controls.NexusButton
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType

/**
 * Error type for [io.github.xingray.compose.nexus.templates.NexusErrorPage].
 */
enum class ErrorType {
    NotFound,       // 404
    Forbidden,      // 403
    ServerError,    // 500
    Custom,
}

/**
 * ErrorPage — a full-page error display template (403, 404, 500, etc.).
 *
 * @param modifier Modifier.
 * @param errorType Error type.
 * @param title Custom title (overrides default for error type).
 * @param subtitle Custom subtitle/description.
 * @param errorCode Custom error code string (e.g., "404").
 * @param onGoBack Callback for the back/home button.
 * @param onRetry Optional callback for a retry button.
 * @param extraActions Optional extra action composable.
 */
@Composable
fun NexusErrorPage(
    modifier: Modifier = Modifier,
    errorType: io.github.xingray.compose.nexus.templates.ErrorType = _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.NotFound,
    title: String? = null,
    subtitle: String? = null,
    errorCode: String? = null,
    onGoBack: (() -> Unit)? = null,
    onRetry: (() -> Unit)? = null,
    extraActions: (@Composable () -> Unit)? = null,
) {
    val colorScheme = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.colorScheme
    val typography = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusTheme.typography

    val resolvedCode = errorCode ?: when (errorType) {
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.NotFound -> "404"
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.Forbidden -> "403"
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.ServerError -> "500"
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.Custom -> "Error"
    }

    val resolvedTitle = title ?: when (errorType) {
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.NotFound -> "Page Not Found"
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.Forbidden -> "Access Denied"
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.ServerError -> "Server Error"
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.Custom -> "Something Went Wrong"
    }

    val resolvedSubtitle = subtitle ?: when (errorType) {
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.NotFound -> "The page you are looking for does not exist."
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.Forbidden -> "You do not have permission to access this page."
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.ServerError -> "An unexpected error occurred. Please try again later."
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.Custom -> "Please check your connection or try again."
    }

    val accentColor: Color = when (errorType) {
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.NotFound -> colorScheme.info.base
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.Forbidden -> colorScheme.warning.base
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.ServerError -> colorScheme.danger.base
        _root_ide_package_.io.github.xingray.compose.nexus.templates.ErrorType.Custom -> colorScheme.primary.base
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background.page),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(40.dp),
        ) {
            // Error code
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = resolvedCode,
                color = accentColor.copy(alpha = 0.3f),
                style = typography.extraLarge,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = resolvedTitle,
                color = colorScheme.text.primary,
                style = typography.extraLarge,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(
                text = resolvedSubtitle,
                color = colorScheme.text.secondary,
                style = typography.base,
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (onGoBack != null) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(
                        onClick = onGoBack,
                        type = _root_ide_package_.io.github.xingray.compose.nexus.theme.NexusType.Primary,
                    ) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "Go Home")
                    }
                }
                if (onRetry != null) {
                    _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusButton(onClick = onRetry) {
                        _root_ide_package_.io.github.xingray.compose.nexus.controls.NexusText(text = "Retry")
                    }
                }
            }

            if (extraActions != null) {
                Spacer(modifier = Modifier.height(16.dp))
                extraActions()
            }
        }
    }
}
