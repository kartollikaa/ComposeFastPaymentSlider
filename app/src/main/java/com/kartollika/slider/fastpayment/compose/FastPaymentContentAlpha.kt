package com.kartollika.slider.fastpayment.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.kartollika.slider.fastpayment.compose.FastPaymentState.CartStateVO.PAYING

internal class FastPaymentContentAlpha(
  val pullToPayAlpha: State<Float>,
  val payingAlpha: State<Float>,
  val endContentAlpha: State<Float>,
  val centerContentAlpha: State<Float>,
)

@Composable
internal fun updateContentAlphaTransitions(
  fastPaymentState: FastPaymentState,
  fastPaymentDraggableState: FastPaymentDraggableState,
): FastPaymentContentAlpha {
  val dragHintVisible by remember(fastPaymentDraggableState, fastPaymentState.idleHintVisible) {
    derivedStateOf {
      fastPaymentDraggableState.isDraggedActual || fastPaymentState.idleHintVisible
    }
  }

  val pullToPayAlpha = animateFloatAsState(
      when {
        fastPaymentDraggableState.contentVisible && dragHintVisible -> 1f
        else -> 0f
      }
  )

  val payingAlpha = animateFloatAsState(
      when {
        fastPaymentState.cartState == PAYING -> 1f
        else -> 0f
      }
  )

  val endContentAlpha = animateFloatAsState(
      when {
        isEndContentVisible(
            fastPaymentState = fastPaymentState,
            contentVisible = fastPaymentDraggableState.contentVisible,
            isDraggedActual = fastPaymentDraggableState.isDraggedActual
        ) -> 1f

        else -> 0f
      }
  )

  val centerContentAlpha = animateFloatAsState(
      when {
        fastPaymentDraggableState.contentVisible && !dragHintVisible -> 1f
        else -> 0f
      }
  )

  return remember(
      fastPaymentState,
      fastPaymentDraggableState,
  ) {
    FastPaymentContentAlpha(
        pullToPayAlpha = pullToPayAlpha,
        payingAlpha = payingAlpha,
        endContentAlpha = endContentAlpha,
        centerContentAlpha = centerContentAlpha,
    )
  }
}

private fun isEndContentVisible(
  fastPaymentState: FastPaymentState,
  contentVisible: Boolean,
  isDraggedActual: Boolean,
): Boolean {
  return !fastPaymentState.idleHintVisible && contentVisible && !isDraggedActual
}