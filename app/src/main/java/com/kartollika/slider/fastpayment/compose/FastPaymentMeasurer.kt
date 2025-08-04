package com.kartollika.slider.fastpayment.compose

import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

private val Density.CenterContentStartOffsetWithoutSlider
  get() = 8.dp.roundToPx()

private val Density.CenterContentStartOffsetWithSlider
  get() = 0.dp.roundToPx()

class FastPaymentMeasurer(
    private val fastPaymentDraggableState: FastPaymentDraggableState,
) : MeasurePolicy {
  override fun MeasureScope.measure(
    measurables: List<Measurable>,
    constraints: Constraints,
  ): MeasureResult {
    val measurables = measurables.associateBy { it.layoutId }

    // Without loosing minWidth and minHeight,
    // widths of measured composables will be max because of the parent
    // https://stackoverflow.com/a/68606264
    val looseConstraints = constraints.copy(
        minWidth = 0,
        minHeight = 0,
        maxWidth = if (constraints.hasBoundedWidth) {
          constraints.maxWidth
        } else {
          // Some big value to constrain maximum width
          1000.dp.roundToPx()
        }
    )

    val thumbPlaceable = measurables[FastPaymentLayoutId.Thumb]?.measure(looseConstraints)
    val thumbWidth = thumbPlaceable?.width ?: 0

    val endContentPlaceable = measurables[FastPaymentLayoutId.End]!!.measure(looseConstraints)
    val endWidth = endContentPlaceable.width

    val centerMaxWidth = getCenterMaxWidth(
        looseConstraints = looseConstraints,
        thumbWidth = thumbWidth,
        endWidth = endWidth
    )

    val centerPlaceable = measurables[FastPaymentLayoutId.Center]!!.measure(
        looseConstraints.copy(
            maxWidth = centerMaxWidth,
        )
    )
    val centerWidth = centerPlaceable.width

    val occupiedWidth =
      (thumbWidth + endWidth + centerWidth)
          .coerceIn(constraints.minWidth, looseConstraints.maxWidth)

    val availableSliderConstraints = looseConstraints.copy(
        maxWidth = occupiedWidth
    )
    val shimmerPlaceable = measurables[FastPaymentLayoutId.Shimmer]!!
        .measure(availableSliderConstraints)
    val errorPlaceable = measurables[FastPaymentLayoutId.Error]!!
        .measure(availableSliderConstraints)
    val payingPlaceable = measurables[FastPaymentLayoutId.Paying]!!
        .measure(
            looseConstraints.copy(
                maxWidth = occupiedWidth - thumbWidth
            )
        )
    val pullToPayPlaceable = measurables[FastPaymentLayoutId.PullToPay]!!
        .measure(
            looseConstraints.copy(
                maxWidth = occupiedWidth - thumbWidth
            )
        )

    val backgroundPlaceable = measurables[FastPaymentLayoutId.Background]!!
        .measure(availableSliderConstraints)

    val progressWidth = fastPaymentDraggableState.progressWidth
        .coerceIn(thumbWidth, occupiedWidth)

    val progressPlaceable = measurables[FastPaymentLayoutId.Progress]!!
        .measure(
            looseConstraints.copy(
                minWidth = progressWidth,
                maxWidth = progressWidth
            )
        )

    val occupiedCenter = occupiedWidth - thumbWidth - endWidth
    val centerStartX = getCenterStartX(thumbWidth, occupiedCenter, centerWidth)

    return layout(occupiedWidth, constraints.maxHeight) {
      backgroundPlaceable.placeRelative(0, 0)
      centerPlaceable.placeRelative(centerStartX, 0)
      shimmerPlaceable.placeRelative(0, 0)
      pullToPayPlaceable.placeRelative(thumbWidth, 0)
      progressPlaceable.placeRelative(fastPaymentDraggableState.progressOffset, 0)
      payingPlaceable.placeRelative(
        x = fastPaymentDraggableState.progressOffset - fastPaymentDraggableState.endOfTrackWidth,
        y = 0
      )
      endContentPlaceable.placeRelative(occupiedWidth - endContentPlaceable.width, 0)
      thumbPlaceable?.placeRelative(fastPaymentDraggableState.offset.toInt(), 0)
      errorPlaceable.placeRelative(0, 0)
    }
  }
}

private fun MeasureScope.getCenterMaxWidth(
  looseConstraints: Constraints,
  thumbWidth: Int,
  endWidth: Int,
): Int {
  val alreadyOccupiedWidth = thumbWidth + endWidth
  val centerOffsetFromThumb = getCenterOffsetFromThumb(thumbWidth)
  return looseConstraints.maxWidth - alreadyOccupiedWidth - centerOffsetFromThumb
}

private fun MeasureScope.getCenterStartX(
  thumbWidth: Int,
  occupiedCenter: Int,
  centerWidth: Int,
): Int {
  val offsetToStartInsideCenter = (occupiedCenter - centerWidth) / 2
  val offsetFromThumb = getCenterOffsetFromThumb(thumbWidth)
  return thumbWidth + offsetFromThumb + offsetToStartInsideCenter
}

fun MeasureScope.getCenterOffsetFromThumb(
  thumbWidth: Int,
): Int {
  return if (thumbWidth != 0) {
    CenterContentStartOffsetWithSlider
  } else {
    CenterContentStartOffsetWithoutSlider
  }
}