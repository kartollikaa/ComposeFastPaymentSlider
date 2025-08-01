package com.kartollika.slider.modifier

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun Modifier.noIndicationClickable(
  enabled: Boolean = true,
  onClick: () -> Unit,
): Modifier {
  val interactionSource = remember { NoopMutableInteractionSource }

  return this then Modifier
    .clickable(
      enabled = enabled,
      indication = null,
      onClick = onClick,
      interactionSource = interactionSource
    )
}

private object NoopMutableInteractionSource : MutableInteractionSource {
  override val interactions: Flow<Interaction>
    get() = emptyFlow()

  override suspend fun emit(interaction: Interaction) {
  }

  override fun tryEmit(interaction: Interaction): Boolean {
    return true
  }
}
