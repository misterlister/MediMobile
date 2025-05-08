package com.example.medimobile.ui.components.templates

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun MediTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    label: String? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val colorScheme = MaterialTheme.colorScheme
    val colors = TextFieldDefaults.colors(
        focusedTextColor = colorScheme.onSurface,
        unfocusedTextColor = colorScheme.onSurface,
        disabledTextColor = colorScheme.onSurface.copy(alpha = 0.4f),

        focusedContainerColor = colorScheme.surfaceVariant,
        unfocusedContainerColor = colorScheme.primaryContainer,
        disabledContainerColor = colorScheme.primaryContainer.copy(alpha = 0.3f),

        focusedPlaceholderColor = colorScheme.onSurface.copy(alpha = 0.55f),
        unfocusedPlaceholderColor = colorScheme.onSurface.copy(alpha = 0.7f),
        disabledPlaceholderColor = colorScheme.onSurface.copy(alpha = 0.2f),
    )

    TextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        placeholder = placeholder,
        label = label?.let { { Text(it) } },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        isError = isError,
        colors = colors,
        modifier = modifier,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource
    )
}
