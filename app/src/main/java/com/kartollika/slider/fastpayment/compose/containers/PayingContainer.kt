package com.kartollika.slider.fastpayment.compose.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier

@Composable
@NonRestartableComposable
fun PayingContainer(
  modifier: Modifier = Modifier,
  payingContent: @Composable () -> Unit,
) {
  Box(
      modifier = modifier
  ) {
    payingContent()
  }
}