package com.kartollika.slider.fastpayment.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kartollika.slider.R
import com.kartollika.slider.fastpayment.compose.FastPaymentLayoutId.Progress
import com.kartollika.slider.fastpayment.compose.FastPaymentLayoutId.Thumb
import com.kartollika.slider.fastpayment.compose.FastPaymentState.CartPaymentMethodVO
import com.kartollika.slider.fastpayment.compose.FastPaymentState.CartStateVO.IDLE
import com.kartollika.slider.fastpayment.compose.FastPaymentState.CartStateVO.SYNC_ERROR
import com.kartollika.slider.fastpayment.compose.FastPaymentState.EstimatedOrderTimeState
import com.kartollika.slider.fastpayment.compose.FastPaymentState.LoyaltyVO
import com.kartollika.slider.fastpayment.compose.SwipeButtonAnchor.End
import com.kartollika.slider.fastpayment.compose.SwipeButtonAnchor.Start
import com.kartollika.slider.fastpayment.compose.containers.Background
import com.kartollika.slider.fastpayment.compose.containers.CenterContentContainer
import com.kartollika.slider.fastpayment.compose.containers.EndContentContainer
import com.kartollika.slider.fastpayment.compose.containers.PayingContainer
import com.kartollika.slider.fastpayment.compose.containers.PullToPayContainer
import com.kartollika.slider.fastpayment.compose.containers.Thumb
import com.kartollika.slider.modifier.alpha
import com.kartollika.slider.modifier.noIndicationClickable
import com.kartollika.slider.staff.image
import com.kartollika.slider.staff.painter
import com.kartollika.slider.ui.theme.FastPaymentSliderTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import com.kartollika.slider.fastpayment.Stack

val DefaultFastPaymentButtonBackground: @Composable () -> Unit = {
  Box(
      modifier = Modifier
          .fillMaxSize()
          .background(FastPaymentSliderTheme.drinkitColors.backgroundTertiary, CircleShape)
  )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FastPaymentButton(
  fastPaymentState: FastPaymentState,
  fastPaymentDraggableState: FastPaymentDraggableState,
  modifier: Modifier = Modifier,
  thumbContent: @Composable () -> Unit = {},
  centerContent: @Composable () -> Unit = {},
  pullToPayContent: @Composable () -> Unit = {},
  endContent: @Composable () -> Unit = {},
  errorContent: @Composable () -> Unit = {},
  payingContent: @Composable () -> Unit = {},
  shimmer: @Composable () -> Unit = {},
  background: @Composable () -> Unit = DefaultFastPaymentButtonBackground,
  onClick: () -> Unit = {},
  onSwiped: () -> Unit = {},
) {
  // Effect to reset thumb width and therefore update anchors for anchoredDraggable
  SideEffect {
    if (fastPaymentState.draggableSliderVisible) return@SideEffect
    fastPaymentDraggableState.thumbWidth = 0
  }

  // Effect to observe thumb swipe to the end and then call `onSwiped`
  ConfirmPaymentBySwipe(fastPaymentDraggableState, onSwiped)

  // Effect to update anchors for anchoredDraggable
  LaunchedEffect(fastPaymentDraggableState.endOfTrackWidth) {
    fastPaymentDraggableState.updateAnchors(
        newAnchors = DraggableAnchors {
          Start at 0f
          End at fastPaymentDraggableState.endOfTrackWidth.toFloat()
        }
    )

    // After recalculating anchors, update current draggable state to avoid UI overlappings
    fastPaymentDraggableState.setPaymentState(fastPaymentState.cartState)
  }

  // Switches `pull to pay` hint visibility when drag is happening.
  // After the drag is finished, the hint keeps on the screen for 600ms
  PullToPayHintVisibleEffect(
      fastPaymentDraggableState = fastPaymentDraggableState,
      updateIsDragged = { isDragged ->
        fastPaymentDraggableState.isDraggedActual = isDragged
      },
  )

  val containersAlpha = updateContentAlphaTransitions(
      fastPaymentState = fastPaymentState,
      fastPaymentDraggableState = fastPaymentDraggableState,
  )

  Layout(
      modifier = modifier
          .noIndicationClickable(onClick = onClick)
          .clipToBounds()
          .onSizeChanged {
            fastPaymentDraggableState.allWidth = it.width
          },
      content = {
        Background(
            modifier = Modifier
                .layoutId(FastPaymentLayoutId.Background),
            background = background
        )

        CenterContentContainer(
            modifier = Modifier
                .layoutId(FastPaymentLayoutId.Center)
                .alpha { containersAlpha.centerContentAlpha.value },
            centerContent = centerContent,
        )

        PullToPayContainer(
            modifier = Modifier
                .layoutId(FastPaymentLayoutId.PullToPay)
                .alpha { containersAlpha.pullToPayAlpha.value },
            pullToPayContent = pullToPayContent,
        )

        PayingContainer(
            modifier = Modifier
                .layoutId(FastPaymentLayoutId.Paying)
                .alpha { containersAlpha.payingAlpha.value },
            payingContent = payingContent
        )

        Box(
            modifier = Modifier
                .layoutId(FastPaymentLayoutId.Shimmer)
        ) {
          shimmer()
        }

        EndContentContainer(
            modifier = Modifier
                .layoutId(FastPaymentLayoutId.End)
                .alpha { containersAlpha.endContentAlpha.value },
            content = endContent,
        )

        if (fastPaymentState.draggableSliderVisible) {
          Thumb(
              modifier = Modifier
                  .layoutId(Thumb)
                  .width(96.dp)
                  .height(60.dp)
                  .anchoredDraggable(
                      state = fastPaymentDraggableState.anchoredDraggableState,
                      orientation = Horizontal,
                      startDragImmediately = true,
                      enabled = isSliderDraggable(fastPaymentState),
                      interactionSource = fastPaymentDraggableState.interactionSource
                  )
                  .onSizeChanged {
                    fastPaymentDraggableState.thumbWidth = it.width
                  },
              thumbContent = thumbContent
          )
        }

        // Progress background
        Progress(
            modifier = Modifier
                .layoutId(Progress)
        )

        Box(
            modifier = Modifier
                .layoutId(FastPaymentLayoutId.Error)
        ) {
          AnimatedVisibility(
              visible = fastPaymentState.error != null,
              enter = fadeIn(animationSpec = tween(durationMillis = 150)),
              exit = fadeOut(animationSpec = tween(durationMillis = 150))
          ) {
            errorContent()
          }
        }
      },
      measurePolicy = FastPaymentMeasurer(
          fastPaymentDraggableState = fastPaymentDraggableState,
      )
  )
}

@Composable
private fun Progress(
  modifier: Modifier = Modifier,
) {
  Box(
      modifier = modifier
          .fillMaxHeight()
          .padding(4.dp)
          .shadow(elevation = 2.dp, shape = CircleShape)
          .background(FastPaymentSliderTheme.drinkitColors.backgroundPrimary, CircleShape)
  )
}

/**
 * The payment confirmation is called when swiped the slider to the end and pull gesture is over
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ConfirmPaymentBySwipe(
  fastPaymentDraggableState: FastPaymentDraggableState,
  onSwiped: () -> Unit,
) {
  val draggableIsDragged by fastPaymentDraggableState.interactionSource.collectIsDraggedAsState()

  LaunchedEffect(draggableIsDragged) {
    if (draggableIsDragged) return@LaunchedEffect
    val anchoredDraggableState = fastPaymentDraggableState.anchoredDraggableState
    val swipeProgress = anchoredDraggableState.progress(from = Start, to = End)
    if (swipeProgress > 0.8f) {
      onSwiped()
    } else {
      anchoredDraggableState.animateTo(Start)
    }
  }
}

private fun isSliderDraggable(fastPaymentState: FastPaymentState) =
  fastPaymentState.cartState == IDLE || fastPaymentState.cartState == SYNC_ERROR

/**
 * Effect to show `Pull to pay` hint when thumb touched and hide it with the delay
 * Updates isDraggedActual – boolean state which is read by compose
 */
@Composable
private fun PullToPayHintVisibleEffect(
  fastPaymentDraggableState: FastPaymentDraggableState,
  updateIsDragged: (Boolean) -> Unit = { },
) {
  val draggableIsDragged by fastPaymentDraggableState.interactionSource.collectIsDraggedAsState()

  LaunchedEffect(draggableIsDragged) {
    if (draggableIsDragged) {
      updateIsDragged(true)
    } else {
      // Delay to hide `Pull to pay` hint
      delay(600)
      updateIsDragged(false)
    }
  }
}

private val FastPaymentDummyState = FastPaymentState(
    id = "fames",
    totalPrice = "180 Р",
    products = persistentListOf(
        FastPaymentProduct(image(R.drawable.vector_placeholder)),
        FastPaymentProduct(image(R.drawable.vector_placeholder)),
    ),
    cartState = IDLE,
    paymentMethod = CartPaymentMethodVO(
        icon = 8240,
        isGPay = false,
        isFake = false,
        isNewCard = false,
        isSavedCard = false,
        isSberPay = false,
        isSbp = false,
        isApp2App = false
    ),
    loyalty = LoyaltyVO.NoLoyalty,
    estimatedOrderTime = EstimatedOrderTimeState.NoEstimatedOrderTime,
    draggableSliderVisible = true
)

@Composable
private fun FastPaymentPreviewContent(
  state: FastPaymentState,
  fastPaymentDraggableState: FastPaymentDraggableState,
  onSwiped: () -> Unit = {},
) {
  FastPaymentButton(
      fastPaymentState = state,
      fastPaymentDraggableState = fastPaymentDraggableState,
      modifier = Modifier
          .width(450.dp)
          .height(60.dp),
      thumbContent = {
        Image(
            modifier = Modifier
                .width(96.dp)
                .height(60.dp)
                .padding(4.dp)
                .background(FastPaymentSliderTheme.drinkitColors.backgroundPrimary, CircleShape),
            painter = painterResource(R.drawable.icon_visa_black),
            contentDescription = null,
            contentScale = ContentScale.Inside,
        )
      },
      pullToPayContent = {
        Text(
            text = "Тяни для оплаты",
            style = FastPaymentSliderTheme.typography.headline20Regular
        )
      },
      centerContent = {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
          Column(
              horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Image(
                  painter = painterResource(R.drawable.loyalty_badge),
                  contentDescription = null
              )

              Text(
                  text = state.totalPrice,
                  style = FastPaymentSliderTheme.typography.headline20Regular
              )
            }

            Text(
                text = "Время ожидания",
                style = FastPaymentSliderTheme.typography.label12
            )
          }
        }
      },
      endContent = {
        Stack(
            modifier = Modifier
                .padding(4.dp),
            stackCount = state.products.size,
        ) { index ->
          val image = state.products[index].image
          Image(
              modifier = Modifier
                  .fillMaxSize()
                  .padding(8.dp),
              painter = image.painter(),
              contentDescription = null,
              contentScale = ContentScale.Inside
          )
        }
      },
      errorContent = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red, CircleShape)
                .padding(horizontal = 16.dp)
        ) {
          Text(
              text = state.error?.message
                  .orEmpty(),
              modifier = Modifier.align(Alignment.Center),
              color = FastPaymentSliderTheme.drinkitColors.white100,
          )
          Image(
              modifier = Modifier
                  .align(Alignment.CenterEnd)
                  .size(32.dp),
              painter = painterResource(R.drawable.vector_icon_red_error),
              contentDescription = null
          )
        }
      },
      onSwiped = onSwiped
  )
}

object FastPaymentButtonTags {
  const val FAST_PAYMENT_CONTAINER = "FastPaymentContainer"
  const val PAYMENT_ICON = "PaymentMethodIcon"
  const val SLIDER_ITEM = "SliderItem"
  const val TOTAL_PRICE_LABEL = "TotalPriceLabel"
  const val THUMB_CONTENT = "SliderThumbContentTestTag"
}