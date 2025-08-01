/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kartollika.slider.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val blue = Color(0xFF334CDB)
val white = Color(0xFFFFFFFF)

val lightOnPrimaryColor = Color(0xFFFFFFFF)
val lightErrorColor = Color(0x66D72E2B)
val lightButtonPrimaryNormal = Color(0xFF334CDB)
val lightButtonPrimaryPressed = Color(0xFF182DA8)
val lightButtonTertiaryNormal = Color(0xFFFFFFFF)
val lightButtonTertiaryPressed = Color(0xFFDAE2E5)
val lightButtonSecondaryNormal = Color(0xFFF2F7F9)
val lightButtonSecondaryPressed = Color(0xFFE4EBF0)
val lightButtonTransparentNormal = Color(0x140C1827)
val lightButtonTransparentPressed = Color(0x0D0C1827)
val lightBackgroundPrimary = Color(0xFFFFFFFF)
val lightBackgroundTertiary = Color(0xFFF1F3F5)
val lightBackgroundHighlight = Color(0xFFF2F7F9)
val lightTextIconAccent = Color(0xFF334CDB)
val lightInteriorPageBackground = Color(0xFFFFFEF8)
val lightTextIcon100 = Color(0xFF041627)
val lightTextIcon80 = Color(0xCC041627)
val lightTextIcon60 = Color(0x99041627)
val lightTextIcon30 = Color(0x4D041627)
val lightTextIcon20 = Color(0x33041627)
val lightTextIcon10 = Color(0x1A041627)
val lightTextIcon5 = Color(0x0D041627)
val lightBlackWhite60 = Color(0x99FFFFFF)
val lightBlackWhite80 = Color(0xCCFFFFFF)
val lightBlackWhite100 = Color(0xFFFFFFFF)
val lightTextIconError100 = Color(0xFFEE0B10)
val lightTextIconError50 = Color(0x80EE0B10)

val darkOnPrimaryColor = Color(0xFFFFFFFF)
val darkErrorColor = Color(0x66D72E2B)
val darkButtonPrimaryNormal = Color(0xFF334CDB)
val darkButtonPrimaryPressed = Color(0xFF182DA8)
val darkButtonTertiaryNormal = Color(0xFF05111C)
val darkButtonTertiaryPressed = Color(0xFF212A35)
val darkButtonSecondaryNormal = Color(0xFF22272E)
val darkButtonSecondaryPressed = Color(0xFF161B21)
val darkButtonTransparentNormal = Color(0x14FFFFFF)
val darkButtonTransparentPressed = Color(0x0DFFFFFF)
val darkBackgroundPrimary = Color(0xFF0A0C0F)
val darkBackgroundTertiary = Color(0xFF22262D)
val darkBackgroundHighlight = Color(0xFF161A20)
val darkTextIconAccent = Color(0xFF4961EC)
val darkInteriorPageBackground = Color(0xFF000C16)
val darkTextIcon100 = Color(0xFFFFFFFF)
val darkTextIcon80 = Color(0xCCFFFFFF)
val darkTextIcon60 = Color(0x99FFFFFF)
val darkTextIcon30 = Color(0x4DFFFFFF)
val darkTextIcon20 = Color(0x33FFFFFF)
val darkTextIcon10 = Color(0x1AFFFFFF)
val darkTextIcon5 = Color(0x0DFFFFFF)
val darkBlackWhite60 = Color(0x99041627)
val darkBlackWhite80 = Color(0xCC041627)
val darkBlackWhite100 = Color(0xFF041627)
val darkTextIconError100 = Color(0xFFF9181D)
val darkTextIconError50 = Color(0x80F9181D)

@Immutable
data class DrinkitColors(
  val backgroundPrimary: Color = Color.Unspecified,
  val backgroundTertiary: Color = Color.Unspecified,
  val backgroundHighlight: Color = Color.Unspecified,
  val textIconAccent: Color = Color.Unspecified,
  val textIcon100: Color = Color.Unspecified,
  val textIcon80: Color = Color.Unspecified,
  val textIcon60: Color = Color.Unspecified,
  val textIcon30: Color = Color.Unspecified,
  val textIcon20: Color = Color.Unspecified,
  val textIcon10: Color = Color.Unspecified,
  val textIcon5: Color = Color.Unspecified,
  val blackWhite60: Color = Color.Unspecified,
  val blackWhite80: Color = Color.Unspecified,
  val blackWhite100: Color = Color.Unspecified,
  val blueWhite: Color = Color.Unspecified,
  val textIconError100: Color = Color.Unspecified,
  val textIconError50: Color = Color.Unspecified,
  val textIconSuccess: Color = Color(0xFF28BE64),
  val buttonPrimaryNormal: Color = Color.Unspecified,
  val buttonPrimaryPressed: Color = Color.Unspecified,
  val buttonTertiaryNormal: Color = Color.Unspecified,
  val buttonTertiaryPressed: Color = Color.Unspecified,
  val buttonSecondaryNormal: Color = Color.Unspecified,
  val buttonSecondaryPressed: Color = Color.Unspecified,
  val buttonTransparentNormal: Color = Color.Unspecified,
  val buttonTransparentPressed: Color = Color.Unspecified,
  val interiorPageBackground: Color = Color.Unspecified,
  val black100: Color = Color(0xFF041627),
  val black80: Color = Color(0xCC041627),
  val black60: Color = Color(0x99041627),
  val black30: Color = Color(0x4D041627),
  val black20: Color = Color(0x33041627),
  val brand01: Color = Color(0xFF182DA8),
  val brand02: Color = Color(0xFF334CDB),
  val white100: Color = Color(0xFFFFFFFF),
  val white80: Color = Color(0xCCFFFFFF),
  val white60: Color = Color(0x99FFFFFF),
  val white30: Color = Color(0x4DFFFFFF),
  val white20: Color = Color(0x33FFFFFF),

    // undefined
  val isLight: Boolean = true,
  val yellow: Color = Color(0xFFFEC401),
)