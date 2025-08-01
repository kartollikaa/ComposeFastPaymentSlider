package com.kartollika.slider.fastpayment.haptic

import android.os.VibrationEffect
import android.os.Vibrator

class ProgressVibrator(
  private val vibrator: Vibrator,
  private val distanceDetector: DistanceDetector = DistanceDetector(tickStep = 0.1f),
) {
  var isEnabled: Boolean = true

  fun changeProgress(progress: Float) {
    if (distanceDetector.canTick(progress)) {
      vibrateOnProgress(progress)
    }
  }

  private fun vibrateOnProgress(progress: Float) {
    if (!isEnabled) return
    val amplitude = getVibrationAmplitude(progress)
    val vibrationEffect = VibrationEffect.createOneShot(TICK_OREO_DURATION, amplitude)
    vibrator.vibrate(vibrationEffect)
  }

  private fun getVibrationAmplitude(progress: Float): Int {
    return (progress * 100).toInt().coerceIn(AMPLITUDES)
  }

  companion object {
    private const val PERCENT_100 = 100
    val AMPLITUDES = 1..255
    private const val TICK_OLD_DURATION = 10L
    private const val TICK_OREO_DURATION = 10L
  }
}