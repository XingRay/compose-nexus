package io.github.xingray.compose.nexus.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.xingray.compose.nexus.controls.NexusButton
import io.github.xingray.compose.nexus.controls.NexusCheckbox
import io.github.xingray.compose.nexus.controls.NexusDivider
import io.github.xingray.compose.nexus.controls.NexusInput
import io.github.xingray.compose.nexus.controls.NexusText
import io.github.xingray.compose.nexus.theme.NexusTheme
import io.github.xingray.compose.nexus.theme.NexusType

/**
 * Login page state holder.
 */
@Stable
class LoginPageState {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var rememberMe by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
}

@Composable
fun rememberLoginPageState(): LoginPageState = remember { LoginPageState() }

/**
 * LoginPage — a centered login form template.
 *
 * Layout: Logo → Title → Username/Password inputs → Remember me → Login button → Extra links.
 *
 * @param state Login page state.
 * @param modifier Modifier.
 * @param title Application title.
 * @param subtitle Optional subtitle.
 * @param cardWidth Login card width.
 * @param onLogin Callback with username and password.
 * @param logo Optional logo composable.
 * @param extraActions Optional extra actions below the login button (e.g., "Forgot password", "Register").
 */
@Composable
fun NexusLoginPage(
    state: LoginPageState = rememberLoginPageState(),
    modifier: Modifier = Modifier,
    title: String = "Login",
    subtitle: String? = null,
    cardWidth: Dp = 400.dp,
    onLogin: ((username: String, password: String) -> Unit)? = null,
    logo: (@Composable () -> Unit)? = null,
    extraActions: (@Composable () -> Unit)? = null,
) {
    val colorScheme = NexusTheme.colorScheme
    val typography = NexusTheme.typography
    val shapes = NexusTheme.shapes
    val shadows = NexusTheme.shadows

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background.page),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .width(cardWidth)
                .shadow(shadows.light.elevation, shapes.base)
                .clip(shapes.base)
                .background(colorScheme.fill.blank)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Logo
            if (logo != null) {
                logo()
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Title
            NexusText(
                text = title,
                color = colorScheme.text.primary,
                style = typography.extraLarge,
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                NexusText(
                    text = subtitle,
                    color = colorScheme.text.secondary,
                    style = typography.base,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Username
            NexusInput(
                value = state.username,
                onValueChange = { state.username = it },
                placeholder = "Username",
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            NexusInput(
                value = state.password,
                onValueChange = { state.password = it },
                placeholder = "Password",
                modifier = Modifier.fillMaxWidth(),
                showPassword = true,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Remember me
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NexusCheckbox(
                    checked = state.rememberMe,
                    onCheckedChange = { state.rememberMe = it },
                )
                NexusText(
                    text = "Remember me",
                    color = colorScheme.text.regular,
                    style = typography.small,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Login button
            NexusButton(
                onClick = { onLogin?.invoke(state.username, state.password) },
                type = NexusType.Primary,
                loading = state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                NexusText(text = "Login")
            }

            // Extra actions
            if (extraActions != null) {
                Spacer(modifier = Modifier.height(16.dp))
                NexusDivider()
                Spacer(modifier = Modifier.height(12.dp))
                extraActions()
            }
        }
    }
}
