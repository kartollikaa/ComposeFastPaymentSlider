package com.kartollika.slider.ui.theme

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.lang.Long.parseLong
import java.lang.Long.toHexString
import java.lang.reflect.Field
import androidx.compose.material3.MaterialTheme as Material3Theme

private val Material3LightColorPalette = lightColorScheme(
  primary = lightButtonPrimaryNormal,
  onPrimary = lightOnPrimaryColor,
  surface = lightBackgroundPrimary,
  onSurface = lightTextIcon100,
  error = lightErrorColor,
)

private val Material3DarkColorPalette = darkColorScheme(
  primary = darkButtonPrimaryNormal,
  onPrimary = darkOnPrimaryColor,
  surface = darkBackgroundPrimary,
  onSurface = darkTextIcon100,
  error = darkErrorColor,
)

val DrinkitLightColorPalette = DrinkitColors(
  blueWhite = blue,
  buttonPrimaryNormal = lightButtonPrimaryNormal,
  buttonPrimaryPressed = lightButtonPrimaryPressed,
  buttonTertiaryNormal = lightButtonTertiaryNormal,
  buttonTertiaryPressed = lightButtonTertiaryPressed,
  buttonSecondaryNormal = lightButtonSecondaryNormal,
  buttonSecondaryPressed = lightButtonSecondaryPressed,
  buttonTransparentNormal = lightButtonTransparentNormal,
  buttonTransparentPressed = lightButtonTransparentPressed,
  backgroundPrimary = lightBackgroundPrimary,
  backgroundTertiary = lightBackgroundTertiary,
  backgroundHighlight = lightBackgroundHighlight,
  textIconAccent = lightTextIconAccent,
  interiorPageBackground = lightInteriorPageBackground,
  textIcon100 = lightTextIcon100,
  textIcon80 = lightTextIcon80,
  textIcon60 = lightTextIcon60,
  textIcon30 = lightTextIcon30,
  textIcon20 = lightTextIcon20,
  textIcon10 = lightTextIcon10,
  textIcon5 = lightTextIcon5,
  blackWhite60 = lightBlackWhite60,
  blackWhite80 = lightBlackWhite80,
  blackWhite100 = lightBlackWhite100,
  textIconError100 = lightTextIconError100,
  textIconError50 = lightTextIconError50,
  isLight = true
)

val DrinkitDarkColorPalette = DrinkitColors(
  blueWhite = white,
  buttonPrimaryNormal = darkButtonPrimaryNormal,
  buttonPrimaryPressed = darkButtonPrimaryPressed,
  buttonTertiaryNormal = darkButtonTertiaryNormal,
  buttonTertiaryPressed = darkButtonTertiaryPressed,
  buttonSecondaryNormal = darkButtonSecondaryNormal,
  buttonSecondaryPressed = darkButtonSecondaryPressed,
  buttonTransparentNormal = darkButtonTransparentNormal,
  buttonTransparentPressed = darkButtonTransparentPressed,
  backgroundPrimary = darkBackgroundPrimary,
  backgroundTertiary = darkBackgroundTertiary,
  backgroundHighlight = darkBackgroundHighlight,
  textIconAccent = darkTextIconAccent,
  interiorPageBackground = darkInteriorPageBackground,
  textIcon100 = darkTextIcon100,
  textIcon80 = darkTextIcon80,
  textIcon60 = darkTextIcon60,
  textIcon30 = darkTextIcon30,
  textIcon20 = darkTextIcon20,
  textIcon10 = darkTextIcon10,
  textIcon5 = darkTextIcon5,
  blackWhite60 = darkBlackWhite60,
  blackWhite80 = darkBlackWhite80,
  blackWhite100 = darkBlackWhite100,
  textIconError100 = darkTextIconError100,
  textIconError50 = darkTextIconError50,
  isLight = false
)

val LocalDrinkitTypography = staticCompositionLocalOf {
  DrinkitTypography()
}

val LocalDrinkitColors = staticCompositionLocalOf {
  DrinkitColors()
}

@Composable
fun FastPaymentSliderTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) {
    Material3DarkColorPalette
  } else {
    Material3LightColorPalette
  }

  val drinkitColors: DrinkitColors = if (darkTheme) {
    DrinkitDarkColorPalette
  } else {
    DrinkitLightColorPalette
  }

  CompositionLocalProvider(
    LocalDrinkitTypography provides Typography,
    LocalDrinkitColors provides drinkitColors,
  ) {
    Material3Theme(
      colorScheme = colorScheme,
    ) {
      CompositionLocalProvider(
        LocalTextStyle provides FastPaymentSliderTheme.typography.label16Regular,
        LocalContentColor provides LocalDrinkitColors.current.textIcon100,
      ) {
        content()
      }
    }
  }
}

object FastPaymentSliderTheme {
  val typography: DrinkitTypography
    @Composable
    @ReadOnlyComposable
    get() = LocalDrinkitTypography.current

  val drinkitColors: DrinkitColors
    @Composable
    @ReadOnlyComposable
    get() = LocalDrinkitColors.current
}

@Preview(name = "light", heightDp = 500)
@Preview(name = "dark", uiMode = UI_MODE_NIGHT_YES, heightDp = 500)
@Composable
private fun DrinkitPalettePreview() {
  FastPaymentSliderTheme {
    val drinkitColors = FastPaymentSliderTheme.drinkitColors
    val colors: List<Pair<String, Long>> = remember {
      drinkitColors::class.java.declaredFields
        .filter { it.type == Long::class.java }
        .map { colorRaw ->
          val color = getColorFromTheme(drinkitColors, colorRaw)
          val name = colorRaw.name
          name to color
        }

    }
    Column(
      modifier = Modifier.wrapContentHeight()
    ) {
      colors.chunked(3)
        .forEach { chunk ->
          Row {
            chunk.forEach { (name, color) ->
              Box(
                modifier = Modifier
                  .width(80.dp)
                  .height(30.dp)
                  .background(Color(parseLong(toHexString(color).take(8), 16))),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = name,
                  fontSize = 5.sp,
                )
              }
            }
          }
        }
    }
  }
}

private fun getColorFromTheme(
  palette: DrinkitColors,
  colorRaw: Field,
): Long {
  val field = palette::class.java.getDeclaredField(colorRaw.name)
  field.isAccessible = true
  return field.get(palette) as Long
}