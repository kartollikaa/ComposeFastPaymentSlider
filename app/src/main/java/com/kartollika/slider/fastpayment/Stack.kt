package com.kartollika.slider.fastpayment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.kartollika.slider.R
import com.kartollika.slider.fastpayment.compose.FastPaymentButtonTags.SLIDER_ITEM
import com.kartollika.slider.staff.image
import com.kartollika.slider.staff.painter
import com.kartollika.slider.ui.theme.FastPaymentSliderTheme
import com.kartollika.slider.preview.annotations.ThemePreviews

private val DefaultStackOverlapping = (-24).dp

@Composable
fun Stack(
  stackCount: Int,
  modifier: Modifier = Modifier,
  stackOverlapping: Dp = DefaultStackOverlapping,
  zIndex: (Int) -> Int = { -it },
  stackItemContent: @Composable (position: Int) -> Unit = {},
) {
  Row(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(stackOverlapping)
  ) {
    repeat(stackCount) { index ->
      Box(
          modifier = Modifier
              .zIndex(zIndex(index).toFloat())
              .fillMaxHeight()
              .aspectRatio(1f)
              .background(FastPaymentSliderTheme.drinkitColors.buttonTertiaryNormal, CircleShape)
              .testTag(SLIDER_ITEM + index),
      ) {
        stackItemContent(index)
      }
    }
  }
}

@ThemePreviews
@Composable
private fun ImageStackWithOnePreview() {
  FastPaymentSliderTheme {
    Surface {
      Stack(
        modifier = Modifier
          .height(32.dp),
        stackOverlapping = (-8).dp,
        stackCount = 1,
      ) {
        Image(
          modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
          painter = image(R.drawable.vector_placeholder).painter(),
          contentDescription = null,
          contentScale = ContentScale.Inside
        )
      }
    }
  }
}

@ThemePreviews
@Composable
private fun ImageStackWithTwoPreview() {
  FastPaymentSliderTheme {
    Surface {
      Stack(
          modifier = Modifier
              .height(32.dp),
          stackOverlapping = (-8).dp,
          stackCount = 2
      ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            painter = image(R.drawable.vector_placeholder).painter(),
            contentDescription = null,
            contentScale = ContentScale.Inside
        )
      }
    }
  }
}

@ThemePreviews
@Composable
private fun ImageStackWithThrePreview() {
  FastPaymentSliderTheme {
    Surface {
      Stack(
          modifier = Modifier
              .height(32.dp),
          stackOverlapping = (-8).dp,
          stackCount = 3,
      ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            painter = image(R.drawable.vector_placeholder).painter(),
            contentDescription = null,
            contentScale = ContentScale.Inside
        )
      }
    }
  }
}

@Preview
@Composable
private fun ImageStackInsideLongBoxPreview() {
  FastPaymentSliderTheme {
    Surface {
      Box(
          modifier = Modifier
              .fillMaxWidth()
              .height(60.dp)
      ) {
        Stack(
            modifier = Modifier.align(Alignment.CenterEnd),
            stackCount = 2,
        ) {
          Image(
              modifier = Modifier
                  .fillMaxSize()
                  .padding(8.dp),
              painter = image(R.drawable.vector_placeholder).painter(),
              contentDescription = null,
              contentScale = ContentScale.Inside
          )
        }
      }
    }
  }
}