package io.github.xingray.compose_nexus.templates

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
import io.github.xingray.compose_nexus.controls.NexusButton
import io.github.xingray.compose_nexus.controls.NexusText
import io.github.xingray.compose_nexus.theme.NexusTheme
import io.github.xingray.compose_nexus.theme.NexusType

/**
 * Error type for [NexusErrorPage].
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
    errorType: ErrorType = ErrorType.NotFound,
    title: String? = null,
    subtitle: String? = null,
    errorCode: String? = null,
    onGoBack: (() -> Unit)? = null,
    onRetry: (() -> Unit)? = null,
    extraActions: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography

    val resolvedCode = errorCode ?: when (errorType) {
        ErrorType.NotFound -> "404"
        ErrorType.Forbidden -> "403"
        ErrorType.ServerError -> "500"
        ErrorType.Custom -> "Error"
    }

    val resolvedTitle = title ?: when (errorType) {
        ErrorType.NotFound -> "Page Not Found"
        ErrorType.Forbidden -> "Access Denied"
        ErrorType.ServerError -> "Server Error"
        ErrorType.Custom -> "Something Went Wrong"
    }

    val resolvedSubtitle = subtitle ?: when (errorType) {
        ErrorType.NotFound -> "The page you are looking for does not exist."
        ErrorType.Forbidden -> "You do not have permission to access this page."
        ErrorType.ServerError -> "An unexpected error occurred. Please try again later."
        ErrorType.Custom -> "Please check your connection or try again."
    }

    val accentColor: Color = when (errorType) {
        ErrorType.NotFound -> colorScheme.info.base
        ErrorType.Forbidden -> colorScheme.warning.base
        ErrorType.ServerError -> colorScheme.danger.base
        ErrorType.Custom -> colorScheme.primary.base
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
            NexusText(
                text = resolvedCode,
                color = accentColor.copy(alpha = 0.3f),
                style = typography.extraLarge,
                modifier = Modifier.padding(bottom = 8.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            NexusText(
                text = resolvedTitle,
                color = colorScheme.text.primary,
                style = typography.extraLarge,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            NexusText(
                text = resolvedSubtitle,
                color = colorScheme.text.secondary,
                style = typography.base,
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (onGoBack != null) {
                    NexusButton(
                        onClick = onGoBack,
                        type = NexusType.Primary,
                    ) {
                        NexusText(text = "Go Home")
                    }
                }
                if (onRetry != null) {
                    NexusButton(onClick = onRetry) {
                        NexusText(text = "Retry")
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
