package com.kartollika.slider.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class DrinkitTypography(
  val headline32Bold: TextStyle = TextStyle.Default,
  val headline32Regular: TextStyle = TextStyle.Default,
  val headline28Regular: TextStyle = TextStyle.Default,
  val headline24: TextStyle = TextStyle.Default,
  val headline22: TextStyle = TextStyle.Default,
  val headline20Medium: TextStyle = TextStyle.Default,
  val headline20Regular: TextStyle = TextStyle.Default,
  val headline18Bold: TextStyle = TextStyle.Default,
  val headline14Bold: TextStyle = TextStyle.Default,
  val headline18Regular: TextStyle = TextStyle.Default,
  val label16Medium: TextStyle = TextStyle.Default,
  val label16Regular: TextStyle = TextStyle.Default,
  val label14Medium: TextStyle = TextStyle.Default,
  val label14Regular: TextStyle = TextStyle.Default,
  val label12: TextStyle = TextStyle.Default,
  val label10: TextStyle = TextStyle.Default,
)

val Typography = DrinkitTypography(
  headline32Bold = TextStyle(
    fontSize = 32.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 40.sp,
  ),
  headline32Regular = TextStyle(
    fontSize = 32.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 40.sp,
  ),
  headline28Regular = TextStyle(
    fontSize = 28.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 34.sp,
  ),
  headline24 = TextStyle(
    fontSize = 24.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 28.sp,
  ),
  headline22 = TextStyle(
    fontSize = 22.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 28.sp,
  ),
  headline20Medium = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 26.sp,
    letterSpacing = 0.4.sp
  ),
  headline20Regular = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 26.sp,
    letterSpacing = 0.4.sp
  ),
  headline18Bold = TextStyle(
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 22.sp,
    letterSpacing = 0.6.sp
  ),
  headline18Regular = TextStyle(
    fontSize = 18.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 24.sp,
    letterSpacing = 0.6.sp
  ),
  label16Medium = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 22.sp,
  ),
  label16Regular = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 22.sp,
  ),
  headline14Bold = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Bold,
    lineHeight = 14.sp,
    letterSpacing = 0.4.sp
  ),
  label14Medium = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Medium,
    lineHeight = 22.sp,
    letterSpacing = 0.4.sp
  ),
  label14Regular = TextStyle(
    fontSize = 14.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 18.sp,
    letterSpacing = 0.4.sp
  ),
  label12 = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 16.sp,
    letterSpacing = 0.4.sp
  ),
  label10 = TextStyle(
    fontSize = 10.sp,
    fontWeight = FontWeight.Normal,
    lineHeight = 16.sp,
    letterSpacing = 0.4.sp
  ),
)