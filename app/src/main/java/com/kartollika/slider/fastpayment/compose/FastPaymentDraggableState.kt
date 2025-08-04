package com.kartollika.slider.fastpayment.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.kartollika.slider.fastpayment.compose.FastPaymentState.CartStateVO
import com.kartollika.slider.fastpayment.compose.FastPaymentState.CartStateVO.PAYING
import com.kartollika.slider.fastpayment.compose.SwipeButtonAnchor.End
import com.kartollika.slider.fastpayment.compose.SwipeButtonAnchor.Start
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.math.roundToInt

@Immutable
object FastPaymentDraggableDefaults {
  const val DistanceThreshold: Float = 0.8f
  const val VelocityThreshold: Float = 10000f
  const val ContentVisibleThreshold = 0.15f
}

enum class SwipeButtonAnchor {
  Start,
  End,
}

@OptIn(ExperimentalFoundationApi::class)
@Stable
class FastPaymentDraggableState internal constructor(
  positionalThreshold: (totalDistance: Float) -> Float,
  snapAnimationSpec: AnimationSpec<Float>,
  decayAnimationSpec: DecayAnimationSpec<Float>,
  velocityThreshold: () -> Float,
  fastPaymentDraggableDefaults: FastPaymentDraggableDefaults,
) {

  var allWidth by mutableIntStateOf(0)
  var thumbWidth by mutableIntStateOf(0)

  val endOfTrackWidth: Int
    get() = allWidth - thumbWidth

  val progressWidth: Int
    get() = offset.roundToInt() + thumbWidth - progressOffset

  val interactionSource: MutableInteractionSource = MutableInteractionSource()

  val progress: Float
    get() = anchoredDraggableState.progress

  val targetValue: SwipeButtonAnchor
    get() = anchoredDraggableState.targetValue

  var isDraggedActual by mutableStateOf(false)

  private val progressOffsetAnimatable = Animatable(0f)

  val progressOffset: Int
    get() = progressOffsetAnimatable.value.toInt()

  internal val anchoredDraggableState = AnchoredDraggableState(
      initialValue = Start,
      anchors = DraggableAnchors {
        Start at 0f
        End at Float.MAX_VALUE
      },
      positionalThreshold = positionalThreshold,
      velocityThreshold = velocityThreshold,
      snapAnimationSpec = snapAnimationSpec,
      decayAnimationSpec = decayAnimationSpec
  )

  val currentValue: SwipeButtonAnchor
    get() = anchoredDraggableState.currentValue

  internal val offset: Float
    get() = anchoredDraggableState.offset

  internal val contentVisible by derivedStateOf {
    anchoredDraggableState.currentValue == Start &&
        contentVisibleByDragOffset(fastPaymentDraggableDefaults)
  }

  fun updateAnchors(newAnchors: DraggableAnchors<SwipeButtonAnchor>) {
    anchoredDraggableState.updateAnchors(newAnchors)
  }

  suspend fun setPaymentState(cartState: CartStateVO) = supervisorScope {
    launch {
      when (cartState) {
        PAYING -> dragAnimatedTo(End)
        else -> dragAnimatedTo(Start)
      }
    }

    launch {
      // The start X position of progress
      // By design, the progress stretches along the width
      // And on paying state it reaches the end of track accordingly
      val progressOffsetTarget = when (cartState) {
        PAYING -> endOfTrackWidth
        else -> 0
      }

      progressOffsetAnimatable.animateTo(
          targetValue = progressOffsetTarget.toFloat(),
          animationSpec = spring(stiffness = 500f)
      )
    }
  }

  private suspend fun dragAnimatedTo(
    anchor: SwipeButtonAnchor,
  ) {
    anchoredDraggableState.animateTo(
        targetValue = anchor,
    )
  }

  private fun contentVisibleByDragOffset(
    fastPaymentDraggableDefaults: FastPaymentDraggableDefaults,
  ): Boolean {
    val endPosition = anchoredDraggableState.anchors.positionOf(End)
    val contentVisibleThreshold = FastPaymentDraggableDefaults.ContentVisibleThreshold
    return anchoredDraggableState.offset < endPosition * contentVisibleThreshold
  }

  companion object {
    fun saver(
      snapAnimationSpec: AnimationSpec<Float>,
      decayAnimationSpec: DecayAnimationSpec<Float>,
      positionalThreshold: (distance: Float) -> Float,
      velocityThreshold: () -> Float,
      fastPaymentDraggableDefaults: FastPaymentDraggableDefaults,
    ) = Saver<FastPaymentDraggableState, SwipeButtonAnchor>(
        save = { it.anchoredDraggableState.currentValue },
        restore = {
          FastPaymentDraggableState(
              positionalThreshold = positionalThreshold,
              snapAnimationSpec = snapAnimationSpec,
              velocityThreshold = velocityThreshold,
              fastPaymentDraggableDefaults = fastPaymentDraggableDefaults,
              decayAnimationSpec = decayAnimationSpec
          )
        }
    )
  }
}

@Composable
fun rememberFastPaymentDraggableState(
  fastPaymentDraggableDefaults: FastPaymentDraggableDefaults = FastPaymentDraggableDefaults,
  velocityThreshold: () -> Float = { FastPaymentDraggableDefaults.VelocityThreshold },
  positionalThreshold: (totalDistance: Float) -> Float =
    { distance -> distance * FastPaymentDraggableDefaults.DistanceThreshold },
  snapAnimationSpec: AnimationSpec<Float> = tween(),
  decayAnimationSpec: DecayAnimationSpec<Float> = rememberSplineBasedDecay(),
): FastPaymentDraggableState {
  return rememberSaveable(
      saver = FastPaymentDraggableState.saver(
          snapAnimationSpec = snapAnimationSpec,
          positionalThreshold = positionalThreshold,
          velocityThreshold = velocityThreshold,
          fastPaymentDraggableDefaults = fastPaymentDraggableDefaults,
          decayAnimationSpec = decayAnimationSpec
      ),
      init = {
        FastPaymentDraggableState(
            positionalThreshold = positionalThreshold,
            snapAnimationSpec = snapAnimationSpec,
            velocityThreshold = velocityThreshold,
            fastPaymentDraggableDefaults = fastPaymentDraggableDefaults,
            decayAnimationSpec = decayAnimationSpec
        )
      }
  )
}