package io.gitlab.ilyadreamix.swissknife.dialogs

import androidx.compose.runtime.Composable

@Composable
internal expect fun SKDialogHost(content: @Composable () -> Unit)
