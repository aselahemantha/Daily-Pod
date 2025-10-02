package com.example.dailypod.view.util.scale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable fun Float.scaleFontSize(
    referenceWidth: Float = 430f,
    targetWidth: Float = getCurrentScreenWidth(),
): TextUnit {
    val context = LocalContext.current
    val maxFontScale = 1f
    val fontScale = remember { context.resources.configuration.fontScale.coerceAtMost(maxFontScale) }
    val scaleFactor = targetWidth / referenceWidth
    return (this * scaleFactor * fontScale).sp
}
