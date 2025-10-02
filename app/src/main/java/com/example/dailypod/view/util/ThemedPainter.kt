package com.example.dailypod.view.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

/**
 * Composable function that remembers a themed painter based on the current app theme
 * @param baseImageResId The base drawable resource ID
 * @return A painter resource that automatically adapts to the current theme
 */
@Composable
fun rememberThemedPainter(baseImageResId: Int) =
    painterResource(
        id = baseImageResId,
    )