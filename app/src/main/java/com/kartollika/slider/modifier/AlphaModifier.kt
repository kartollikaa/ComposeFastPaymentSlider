package com.kartollika.slider.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.alpha(alphaProvider: () -> Float): Modifier =
  this then graphicsLayer {
    this.alpha = alphaProvider()
  }