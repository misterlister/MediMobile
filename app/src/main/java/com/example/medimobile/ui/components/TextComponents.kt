package com.example.medimobile.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.medimobile.R
import com.example.medimobile.ui.theme.heavyShadow
import com.example.medimobile.ui.theme.lightShadow
import com.example.medimobile.ui.theme.mediumShadow

@Composable
fun AppTitle(
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        text = stringResource(R.string.app_name),
        modifier = modifier,
        style = MaterialTheme.typography.displayMedium.copy(shadow = heavyShadow()),
        textAlign = textAlign
    )
}

@Composable
fun ScreenTitle(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.displaySmall.copy(shadow = mediumShadow()),
        textAlign = textAlign
    )
}

@Composable
fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall.copy(shadow = lightShadow()),
        textAlign = textAlign
    )
}

@Composable
fun UsernameText(
    text: String?,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null
) {
    Text(
        text = text?: stringResource(R.string.no_user),
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = textAlign,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun TopBarText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = textAlign
    )
}