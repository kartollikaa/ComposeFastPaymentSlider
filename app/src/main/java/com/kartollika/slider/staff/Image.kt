package com.kartollika.slider.staff

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

@Immutable
sealed interface Image {
  @Immutable
  data class ImageRes internal constructor(val imageRes: Int) : Image
}

@Composable
fun Image.painter(): Painter {
  return when (this) {
    is Image.ImageRes -> painterResource(id = imageRes)
  }
}

/**
 * Used for getting data to load in XML Coil or when actual data is required
 */
fun Image.data(): Any {
  return when (val image = this) {
    is Image.ImageRes -> image.imageRes
  }
}

/**
 * Creates image of some resource from android bundle
 */
fun image(imageRes: Int): Image.ImageRes = Image.ImageRes(imageRes)