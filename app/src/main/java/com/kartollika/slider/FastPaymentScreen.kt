package com.kartollika.slider

import android.content.Context
import android.os.Vibrator
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.RepeatMode.Restart
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kartollika.slider.fastpayment.Stack
import com.kartollika.slider.fastpayment.compose.FastPaymentButton
import com.kartollika.slider.fastpayment.compose.FastPaymentDraggableState
import com.kartollika.slider.fastpayment.compose.FastPaymentProduct
import com.kartollika.slider.fastpayment.compose.FastPaymentState
import com.kartollika.slider.fastpayment.compose.FastPaymentState.CartPaymentMethodVO
import com.kartollika.slider.fastpayment.compose.FastPaymentState.CartStateVO.IDLE
import com.kartollika.slider.fastpayment.compose.FastPaymentState.CartStateVO.PAYING
import com.kartollika.slider.fastpayment.compose.FastPaymentState.CartStateVO.SYNC
import com.kartollika.slider.fastpayment.compose.FastPaymentState.CartStateVO.SYNC_ERROR
import com.kartollika.slider.fastpayment.compose.FastPaymentState.EstimatedOrderTimeState.EstimatedOrderTimeAvailable
import com.kartollika.slider.fastpayment.compose.FastPaymentState.EstimatedOrderTimeState.EstimatedOrderTimeSpeedType.Normal
import com.kartollika.slider.fastpayment.compose.FastPaymentState.EstimatedOrderTimeState.NoEstimatedOrderTime
import com.kartollika.slider.fastpayment.compose.FastPaymentState.FastPaymentError
import com.kartollika.slider.fastpayment.compose.FastPaymentState.LoyaltyVO.Active
import com.kartollika.slider.fastpayment.compose.FastPaymentState.LoyaltyVO.NoLoyalty
import com.kartollika.slider.fastpayment.compose.SwipeButtonAnchor
import com.kartollika.slider.fastpayment.compose.rememberFastPaymentDraggableState
import com.kartollika.slider.fastpayment.haptic.ProgressVibrator
import com.kartollika.slider.placeholder.PlaceholderHighlight
import com.kartollika.slider.placeholder.placeholder
import com.kartollika.slider.placeholder.shimmer
import com.kartollika.slider.staff.ErrorIcon
import com.kartollika.slider.staff.Image
import com.kartollika.slider.staff.getString
import com.kartollika.slider.staff.image
import com.kartollika.slider.staff.painter
import com.kartollika.slider.ui.theme.FastPaymentSliderTheme
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit.MILLISECONDS

val LocalDarkThemeActive = compositionLocalOf {
  false
}

private val IDLE_HINT_DELAY = 2.seconds
private val SHIMMER_IDLE_DURATION = 2.seconds
private val SHIMMER_PAYING_DURATION = 1.seconds

private val StackImageModifier = Modifier
  .fillMaxSize()
  .padding(8.dp)

private val IdleHighlight: (Color) -> PlaceholderHighlight = { highlightColor ->
  PlaceholderHighlight.shimmer(
    highlightColor = highlightColor,
    animationSpec = infiniteRepeatable<Float>(
      animation = tween(
        durationMillis = SHIMMER_IDLE_DURATION.toInt(MILLISECONDS),
        delayMillis = 200
      ),
      repeatMode = Restart
    )
  )
}

private val PayingHighlight: (Color) -> PlaceholderHighlight = { highlightColor ->
  PlaceholderHighlight.shimmer(
    highlightColor = highlightColor,
    animationSpec = infiniteRepeatable<Float>(
      animation = tween(
        durationMillis = SHIMMER_PAYING_DURATION.toInt(MILLISECONDS),
        delayMillis = 200
      ),
      repeatMode = Restart
    )
  )
}

private const val StackItemCrossfadeDuration = 150

@Composable
@Suppress("LongMethod")
fun FastPaymentScreen(
  darkThemeChange: (Boolean) -> Unit = {},
) {
  val initialString = getString(R.string.error_sample_1)
  var errorMessage by remember(initialString) {
    mutableStateOf(initialString)
  }

  var fastPaymentState by remember {
    mutableStateOf(
      FastPaymentState(
        id = "",
        totalPrice = "$4.85",
        products = persistentListOf(
          FastPaymentProduct(
            image = image(R.drawable.croissant_example)
          )
        ),
        cartState = IDLE,
        paymentMethod = CartPaymentMethodVO(
          icon = 0,
          isGPay = false,
          isFake = false,
          isNewCard = false,
          isSavedCard = false,
          isSberPay = false,
          isApp2App = false,
          isSbp = false,
        ),
        loyalty = NoLoyalty,
        estimatedOrderTime = NoEstimatedOrderTime,
        draggableSliderVisible = true,
        error = null
      )
    )
  }

  val fastPaymentDraggableState = rememberFastPaymentDraggableState()

  SideEffect {
    fastPaymentState = fastPaymentState.copy(
      error = fastPaymentState.error?.copy(message = errorMessage)
    )
  }

  LaunchedEffect(fastPaymentState.cartState) {
    fastPaymentDraggableState.setPaymentState(fastPaymentState.cartState)
  }

  val context = LocalContext.current
  val progressVibrator = remember {
    ProgressVibrator(
      vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    )
  }

  FastPaymentVibrationHandler(
    fastPaymentState = fastPaymentState,
    fastPaymentDraggableState = fastPaymentDraggableState,
    progressVibrator = progressVibrator
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .systemBarsPadding()
      .imePadding(),
  ) {
    FastPaymentControls(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 32.dp),
      fastPaymentState = fastPaymentState,
      fastPaymentUpdate = { newState ->
        fastPaymentState = newState
      },
      errorText = errorMessage,
      errorTextChanged = { newErrorText ->
        errorMessage = newErrorText
      },
      darkThemeChanged = darkThemeChange,
      fastPaymentDraggableState = fastPaymentDraggableState
    )

    FastPaymentSliderTheme(
      darkTheme = LocalDarkThemeActive.current
    ) {
      FastPaymentView(
        modifier = Modifier
          .padding(bottom = 32.dp)
          .padding(horizontal = 32.dp)
          .align(Alignment.CenterHorizontally),
        fastPaymentState = fastPaymentState,
        fastPaymentDraggableState = fastPaymentDraggableState,
        onSwiped = {
          fastPaymentState = fastPaymentState.paying()
        }
      )
    }
  }
}

@Suppress("NonSkippableComposable")
@Composable
private fun FastPaymentVibrationHandler(
  fastPaymentState: FastPaymentState,
  fastPaymentDraggableState: FastPaymentDraggableState,
  progressVibrator: ProgressVibrator,
) {
  // Effect to control vibration enabled or disabled
  // Enable it when slider is right at the start
  // Disable it, when the state is either not IDLE or when returning from end to start
  LaunchedEffect(fastPaymentState, fastPaymentDraggableState) {
    snapshotFlow { fastPaymentDraggableState.progress }
      .collect { progress ->
        handleVibrationEnabled(
          progress = progress,
          cartState = fastPaymentState.cartState,
          targetValue = fastPaymentDraggableState.targetValue,
          progressVibrator = progressVibrator
        )
      }
  }

  // Effect to vibrate on progress change
  LaunchedEffect(fastPaymentState, fastPaymentDraggableState) {
    if (fastPaymentState.cartState != IDLE) return@LaunchedEffect

    snapshotFlow { fastPaymentDraggableState.progress }
      .collect(progressVibrator::changeProgress)
  }
}

private fun handleVibrationEnabled(
  progress: Float,
  cartState: FastPaymentState.CartStateVO,
  targetValue: SwipeButtonAnchor,
  progressVibrator: ProgressVibrator,
) {
  // Simply disable vibration when not in IDLE state
  if (cartState != IDLE) {
    progressVibrator.isEnabled = false
    return
  }

  // When the progress is animated to the beginning, the state has the following parameters:
  // targetValue - the nearest anchor to which the animation will snap when the finger is released
  // progress. At the end point, it is 1f
  // Therefore, the moment of vibration activation
  // we calculate as the simultaneous fulfillment of two conditions:
  // targetValue == Start and progress == 1f
  if (!progressVibrator.isEnabled) {
    val vibrationEnabled = progress == 1f && targetValue == SwipeButtonAnchor.Start
    progressVibrator.isEnabled = vibrationEnabled
  }
}

@Composable
private fun FastPaymentView(
  fastPaymentState: FastPaymentState,
  fastPaymentDraggableState: FastPaymentDraggableState,
  modifier: Modifier = Modifier,
  onSwiped: () -> Unit = {},
) {
  val draggableIsDragged by fastPaymentDraggableState.interactionSource.collectIsDraggedAsState()

  val minWidth = remember(fastPaymentState.draggableSliderVisible) {
    if (fastPaymentState.draggableSliderVisible) {
      288.dp
    } else {
      216.dp
    }
  }

  FastPaymentButton(
    modifier = modifier
      .widthIn(min = minWidth, max = 700.dp)
      .height(60.dp),
    fastPaymentState = fastPaymentState,
    fastPaymentDraggableState = fastPaymentDraggableState,
    thumbContent = {
      ThumbContent()
    },
    pullToPayContent = {
      PullToPayContent()
    },
    centerContent = {
      CenterContent(fastPaymentState)
    },
    endContent = {
      EndContent(fastPaymentState)
    },
    errorContent = {
      ErrorContent(fastPaymentState)
    },
    payingContent = {
      Text(
        modifier = Modifier
          .fillMaxSize()
          .wrapContentSize(),
        text = stringResource(R.string.payment_in_progress),
        color = FastPaymentSliderTheme.drinkitColors.textIcon80,
        style = FastPaymentSliderTheme.typography.headline18Regular,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    },
    shimmer = {
      Shimmer(
        fastPaymentState = fastPaymentState,
        visible = isShimmerVisible(draggableIsDragged, fastPaymentState)
      )
    },
    onSwiped = onSwiped,
  )
}

private fun isShimmerVisible(
  isDragging: Boolean,
  fastPaymentState: FastPaymentState,
): Boolean {
  val canShowShimmer =
    fastPaymentState.cartState == PAYING || fastPaymentState.idleHintVisible
  return canShowShimmer && !isDragging
}

@Composable
private fun Shimmer(
  fastPaymentState: FastPaymentState,
  visible: Boolean = true,
) {
  // Using only one box and putting `if` in modifier keeps playing the old shimmer
  if (fastPaymentState.cartState == PAYING) {
    Box(
      Modifier
        .fillMaxSize()
        .placeholder(
          visible = visible,
          shape = CircleShape,
          color = Color.Transparent,
          highlight = PayingHighlight(FastPaymentSliderTheme.drinkitColors.textIcon10),
        )
    )
  } else {
    Box(
      Modifier
        .fillMaxSize()
        .placeholder(
          visible = visible,
          shape = CircleShape,
          color = Color.Transparent,
          highlight = IdleHighlight(FastPaymentSliderTheme.drinkitColors.textIcon10),
        )
    )
  }
}

@Composable
private fun ThumbContent() {
  Image(
    modifier = Modifier
      .fillMaxSize()
      .padding(4.dp)
      .background(FastPaymentSliderTheme.drinkitColors.backgroundPrimary, CircleShape),
    painter = painterResource(R.drawable.icon_visa_black),
    contentDescription = null,
    contentScale = ContentScale.Inside,
  )
}

@Composable
private fun CenterContent(fastPaymentState: FastPaymentState) {
  Column(
    modifier = Modifier
      .fillMaxHeight()
      .wrapContentHeight()
      .padding(horizontal = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      if (hasLoyalty(fastPaymentState)) {
        Image(
          painter = painterResource(
            R.drawable.loyalty_badge
          ),
          contentDescription = null
        )
      }

      Text(
        text = fastPaymentState.totalPrice,
        style = FastPaymentSliderTheme.typography.headline20Regular,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = FastPaymentSliderTheme.drinkitColors.textIcon100
      )
    }

    if (hasEstimatedTime(fastPaymentState)) {
      Text(
        text = stringResource(R.string.fast_payment_estimated_time),
        style = FastPaymentSliderTheme.typography.label12,
        color = FastPaymentSliderTheme.drinkitColors.textIcon100,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

@Composable
private fun PullToPayContent() {
  Text(
    modifier = Modifier
      .fillMaxSize()
      .wrapContentHeight()
      .padding(end = 8.dp),
    text = stringResource(R.string.pull_to_pay),
    textAlign = TextAlign.Center,
    style = FastPaymentSliderTheme.typography.headline18Regular,
    color = FastPaymentSliderTheme.drinkitColors.textIcon100
  )
}

@Composable
private fun EndContent(fastPaymentState: FastPaymentState) {
  if (fastPaymentState.products.isEmpty()) return
  Stack(
    modifier = Modifier
      .padding(4.dp),
    stackCount = fastPaymentState.products.size,
    zIndex = { index -> index },
  ) { index ->
    val isLastStackItem = index == fastPaymentState.products.lastIndex
    val isSyncing = fastPaymentState.cartState == SYNC

    val isStackItemInSync = isSyncing && isLastStackItem
    val product = fastPaymentState.products[index]

    if (isLastStackItem) {
      Crossfade(
        targetState = isStackItemInSync,
        animationSpec = tween(StackItemCrossfadeDuration)
      ) { syncing ->
        if (syncing) {
          CircularProgressIndicator(
            modifier = Modifier
              .fillMaxSize()
              .padding(12.dp),
            strokeWidth = 4.dp,
            strokeCap = StrokeCap.Round,
          )
        } else {
          StackImage(
            modifier = StackImageModifier,
            image = product.image
          )
        }
      }
    } else {
      StackImage(
        modifier = StackImageModifier,
        image = product.image
      )
    }
  }
}

@Composable
private fun StackImage(
  image: Image,
  modifier: Modifier = Modifier,
) {
  Image(
    modifier = modifier,
    painter = image.painter(),
    contentDescription = null,
    contentScale = ContentScale.Inside
  )
}

@Composable
private fun ErrorContent(fastPaymentState: FastPaymentState) {
  Row(
    modifier = Modifier
      .fillMaxSize()
      .background(FastPaymentSliderTheme.drinkitColors.textIconError100, CircleShape)
      .padding(horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text(
      text = fastPaymentState.error?.message
        .orEmpty(),
      modifier = Modifier
        .weight(1f)
        .wrapContentWidth(),
      color = FastPaymentSliderTheme.drinkitColors.white100,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      style = FastPaymentSliderTheme.typography.headline20Regular
    )
    Image(
      modifier = Modifier
        .size(26.dp),
      imageVector = ErrorIcon,
      colorFilter = ColorFilter.tint(FastPaymentSliderTheme.drinkitColors.white100),
      contentDescription = null
    )
  }
}

@Composable
private fun hasLoyalty(fastPaymentState: FastPaymentState) =
  fastPaymentState.loyalty is Active

@Composable
private fun hasEstimatedTime(fastPaymentState: FastPaymentState) =
  fastPaymentState.estimatedOrderTime is EstimatedOrderTimeAvailable

@Composable
private fun FastPaymentControls(
  fastPaymentState: FastPaymentState,
  fastPaymentDraggableState: FastPaymentDraggableState,
  modifier: Modifier = Modifier,
  fastPaymentUpdate: (FastPaymentState) -> Unit = { it },
  errorText: String = "",
  errorTextChanged: (String) -> Unit = {},
  darkThemeChanged: (Boolean) -> Unit = {},
) {
  Column(
    modifier = modifier
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Price(fastPaymentState, fastPaymentUpdate)
    ErrorText(errorText, errorTextChanged)
    CartItems(fastPaymentState, fastPaymentUpdate)
    States(fastPaymentState, fastPaymentUpdate)
    EstimatedTime(fastPaymentState, fastPaymentUpdate)
    DraggableSliderVisibility(fastPaymentState, fastPaymentUpdate)
    Theme(darkThemeChanged)
    IdleHint(fastPaymentDraggableState, fastPaymentState, fastPaymentUpdate)
  }
}

@Suppress("LongMethod")
@Composable
private fun IdleHint(
  fastPaymentDraggableState: FastPaymentDraggableState,
  fastPaymentState: FastPaymentState,
  fastPaymentUpdate: (FastPaymentState) -> Unit,
) {
  var idleHintEnabled by remember {
    mutableStateOf(false)
  }
  val draggableIsDragged by fastPaymentDraggableState.interactionSource.collectIsDraggedAsState()
  val fastPaymentStateUpdated by rememberUpdatedState(fastPaymentState)

  // Initializing idle hint to pull the slider to pay
  LaunchedEffect(draggableIsDragged, fastPaymentState.cartState) {
    if (draggableIsDragged) return@LaunchedEffect
    if (fastPaymentState.cartState != IDLE) return@LaunchedEffect

    while (true) {
      delay(IDLE_HINT_DELAY)
      val paymentState = fastPaymentStateUpdated.copy(
        idleHintVisible = !fastPaymentStateUpdated.idleHintVisible && idleHintEnabled
      )
      fastPaymentUpdate(paymentState)
    }
  }

  // Resetting idle hint when drag is finished.
  // Resetting also happens when state updates
  LaunchedEffect(draggableIsDragged) {
    if (draggableIsDragged) return@LaunchedEffect
    fastPaymentUpdate(
      fastPaymentStateUpdated.copy(
        idleHintVisible = false
      )
    )
  }

  Column {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Checkbox(
        checked = idleHintEnabled,
        colors = CheckboxDefaults.colors(
          checkedColor = FastPaymentSliderTheme.drinkitColors.textIconAccent,
          uncheckedColor = FastPaymentSliderTheme.drinkitColors.textIconAccent,
        ),
        onCheckedChange = { checked ->
          idleHintEnabled = checked
        }
      )
      Text(stringResource(R.string.enable_hint_label))
    }
  }
}

@Composable
private fun Theme(darkThemeChanged: (Boolean) -> Unit) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Checkbox(
      checked = LocalDarkThemeActive.current,
      colors = CheckboxDefaults.colors(
        checkedColor = FastPaymentSliderTheme.drinkitColors.textIconAccent,
        uncheckedColor = FastPaymentSliderTheme.drinkitColors.textIconAccent
      ),
      onCheckedChange = { darkThemeActive ->
        darkThemeChanged(darkThemeActive)
      }
    )

    Text(stringResource(R.string.dark_theme))
  }
}

@Composable
fun DraggableSliderVisibility(
  fastPaymentState: FastPaymentState,
  fastPaymentUpdate: (FastPaymentState) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Checkbox(
      checked = fastPaymentState.draggableSliderVisible,
      colors = CheckboxDefaults.colors(
        checkedColor = FastPaymentSliderTheme.drinkitColors.textIconAccent,
        uncheckedColor = FastPaymentSliderTheme.drinkitColors.textIconAccent
      ),
      onCheckedChange = { checked ->
        fastPaymentUpdate(
          fastPaymentState.copy(
            draggableSliderVisible = checked
          )
        )
      }
    )

    Text(stringResource(R.string.show_draggable_slider))
  }
}

@Suppress("LongMethod")
@Composable
private fun States(
  fastPaymentState: FastPaymentState,
  fastPaymentUpdate: (FastPaymentState) -> Unit,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Text(stringResource(R.string.current_status, currentCartState(fastPaymentState)))
    Row(
      modifier = Modifier
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Button(
        modifier = Modifier.weight(1f),
        onClick = {
          fastPaymentUpdate(
            fastPaymentState.idle()
          )
        }
      ) {
        Text(text = getString(R.string.idle))
      }

      Button(
        modifier = Modifier.weight(1f),
        onClick = {
          fastPaymentUpdate(
            fastPaymentState.paying()
          )
        }
      ) {
        Text(text = getString(R.string.paying))
      }
    }

    Row(
      modifier = Modifier
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Button(
        modifier = Modifier.weight(1f),
        onClick = {
//          val error = FastPaymentError(getString(R.string.error_sample_1))
          val error = FastPaymentError("")
          fastPaymentUpdate(
            fastPaymentState.error(error)
          )
        }
      ) {
        Text(text = getString(R.string.error))
      }

      Button(
        modifier = Modifier.weight(1f),
        onClick = {
          fastPaymentUpdate(
            fastPaymentState.sync()
          )
        }
      ) {
        Text(text = getString(R.string.sync))
      }
    }
  }
}

@Composable
private fun currentCartState(fastPaymentState: FastPaymentState): String {
  return when {
    fastPaymentState.error != null -> getString(R.string.error)
    fastPaymentState.cartState == PAYING -> getString(R.string.paying)
    fastPaymentState.cartState == SYNC -> getString(R.string.sync)
    fastPaymentState.cartState == IDLE -> getString(R.string.idle)
    fastPaymentState.cartState == SYNC_ERROR -> getString(R.string.sync_error)
    else -> getString(R.string.unknown)
  }
}

@Composable
private fun CartItems(
  fastPaymentState: FastPaymentState,
  fastPaymentUpdate: (FastPaymentState) -> Unit,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Text(stringResource(R.string.cart_products_count, fastPaymentState.products.size))
    Slider(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      value = fastPaymentState.products.size.toFloat(),
      valueRange = 0f..3f,
      steps = 2,
      onValueChange = { newCount ->
        fastPaymentUpdate(
          fastPaymentState.copy(
            products = buildList {
              repeat(newCount.roundToInt()) {
                add(
                  FastPaymentProduct(
                    image = image(R.drawable.croissant_example)
                  )
                )
              }
            }.toPersistentList()
          )
        )
      }
    )
  }
}

@Composable
private fun EstimatedTime(
  fastPaymentState: FastPaymentState,
  fastPaymentUpdate: (FastPaymentState) -> Unit,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Checkbox(
      checked = fastPaymentState.estimatedOrderTime is EstimatedOrderTimeAvailable,
      colors = CheckboxDefaults.colors(
        checkedColor = FastPaymentSliderTheme.drinkitColors.textIconAccent,
        uncheckedColor = FastPaymentSliderTheme.drinkitColors.textIconAccent
      ),
      onCheckedChange = { checked ->
        fastPaymentUpdate(
          fastPaymentState.copy(
            estimatedOrderTime = if (!checked) {
              NoEstimatedOrderTime
            } else {
              EstimatedOrderTimeAvailable(
                estimatedTimeText = "5-10 min",
                estimatedOrderTimeType = Normal
              )
            }
          )
        )
      }
    )

    Text(stringResource(R.string.estimated_time))
  }
}

@Composable
private fun Loyalty(
  fastPaymentState: FastPaymentState,
  fastPaymentUpdate: (FastPaymentState) -> Unit,
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Checkbox(
      checked = fastPaymentState.loyalty == Active,
      colors = CheckboxDefaults.colors(
        checkedColor = FastPaymentSliderTheme.drinkitColors.textIconAccent,
        uncheckedColor = FastPaymentSliderTheme.drinkitColors.textIconAccent
      ),
      onCheckedChange = { checked ->
        fastPaymentUpdate(
          fastPaymentState.copy(
            loyalty = if (checked) Active else NoLoyalty
          )
        )
      }
    )

    Text(stringResource(R.string.loyalty))
  }
}

@Composable
private fun Price(
  fastPaymentState: FastPaymentState,
  fastPaymentUpdate: (FastPaymentState) -> Unit,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Text(stringResource(R.string.price_label))
    OutlinedTextField(
      modifier = Modifier
        .fillMaxWidth(),
      value = fastPaymentState.totalPrice,
      colors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color.Black.copy(alpha = 0.1f),
        focusedContainerColor = Color.Black.copy(alpha = 0.2f),
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Black.copy(alpha = 0.3f),
      ),
      shape = CircleShape,
      onValueChange = { value ->
        fastPaymentUpdate(fastPaymentState.copy(totalPrice = value))
      }
    )
  }
}

@Composable
private fun ErrorText(
  errorText: String,
  fastPaymentUpdate: (String) -> Unit,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Text(stringResource(R.string.error_message_label))
    OutlinedTextField(
      modifier = Modifier
        .fillMaxWidth(),
      value = errorText,
      colors = TextFieldDefaults.colors(
        unfocusedContainerColor = Color.Black.copy(alpha = 0.1f),
        focusedContainerColor = Color.Black.copy(alpha = 0.2f),
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Black.copy(alpha = 0.3f),
      ),
      shape = CircleShape,
      onValueChange = fastPaymentUpdate
    )
  }
}