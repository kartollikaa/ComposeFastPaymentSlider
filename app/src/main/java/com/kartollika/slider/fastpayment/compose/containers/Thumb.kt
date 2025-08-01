package com.kartollika.slider.fastpayment.compose.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier

@Composable
@NonRestartableComposable
fun Thumb(
  modifier: Modifier = Modifier,
  thumbContent: @Composable (() -> Unit),
) {
  Box(
      modifier = modifier
  ) {
    thumbContent()
  }
}