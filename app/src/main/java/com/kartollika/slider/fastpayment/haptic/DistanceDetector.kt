package com.kartollika.slider.fastpayment.haptic

import kotlin.math.abs

class DistanceDetector(
  private val tickStep: Float,
) {
  private var lastTickProgress = 0f

  fun canTick(newProgress: Float): Boolean {
    val travelledEnough = abs(lastTickProgress - newProgress) > tickStep

    if (travelledEnough) {
      lastTickProgress = newProgress
      return true
    }
    return false
  }
}