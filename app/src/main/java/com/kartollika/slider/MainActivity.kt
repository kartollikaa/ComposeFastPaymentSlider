package com.kartollika.slider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kartollika.slider.ui.theme.FastPaymentSliderTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      var darkTheme by remember {
        mutableStateOf(false)
      }

      CompositionLocalProvider(
        LocalDarkThemeActive provides darkTheme
      ) {
        FastPaymentSliderTheme(
          darkTheme = false
        ) {
          Surface(
            modifier = Modifier
              .fillMaxSize()
          ) {
            FastPaymentScreen(
              darkThemeChange = { darkThemeActive ->
                darkTheme = darkThemeActive
              }
            )
          }
        }
      }
    }
  }
}