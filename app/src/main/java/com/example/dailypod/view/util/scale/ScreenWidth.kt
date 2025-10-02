package com.example.dailypod.view.util.scale

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun getCurrentScreenWidth(): Float {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp.toFloat()
}
