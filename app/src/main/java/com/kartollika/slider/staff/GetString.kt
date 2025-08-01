package com.kartollika.slider.staff

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun getString(@StringRes stringRes: Int) = ContextCompat.getString(LocalContext.current, stringRes)