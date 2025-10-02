package com.example.dailypod.view.util.scale

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable fun Float.scaleElevation(
    referenceWidth: Float = 430f,
    targetWidth: Float = getCurrentScreenWidth(),
): Dp {
    val scaleFactor = targetWidth / referenceWidth
    return (this * scaleFactor).dp
}
