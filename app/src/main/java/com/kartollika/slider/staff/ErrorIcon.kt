package com.kartollika.slider.staff

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ErrorIcon: ImageVector
  get() {
    if (_Vector != null) {
      return _Vector!!
    }
    _Vector = ImageVector.Builder(
        name = "Vector",
        defaultWidth = 28.dp,
        defaultHeight = 28.dp,
        viewportWidth = 28f,
        viewportHeight = 28f
    )
        .apply {
          path(fill = SolidColor(Color(0xFFFFFFFF))) {
            moveTo(13.948f, 27.229f)
            curveTo(21.213f, 27.229f, 27.229f, 21.201f, 27.229f, 13.948f)
            curveTo(27.229f, 6.682f, 21.2f, 0.667f, 13.935f, 0.667f)
            curveTo(6.682f, 0.667f, 0.667f, 6.682f, 0.667f, 13.948f)
            curveTo(0.667f, 21.201f, 6.695f, 27.229f, 13.948f, 27.229f)
            close()
            moveTo(13.948f, 16.37f)
            curveTo(13.271f, 16.37f, 12.893f, 15.992f, 12.88f, 15.302f)
            lineTo(12.711f, 8.206f)
            curveTo(12.698f, 7.516f, 13.206f, 7.021f, 13.935f, 7.021f)
            curveTo(14.651f, 7.021f, 15.185f, 7.529f, 15.172f, 8.219f)
            lineTo(14.989f, 15.302f)
            curveTo(14.976f, 16.005f, 14.599f, 16.37f, 13.948f, 16.37f)
            close()
            moveTo(13.948f, 20.732f)
            curveTo(13.167f, 20.732f, 12.489f, 20.107f, 12.489f, 19.338f)
            curveTo(12.489f, 18.57f, 13.153f, 17.932f, 13.948f, 17.932f)
            curveTo(14.742f, 17.932f, 15.406f, 18.557f, 15.406f, 19.338f)
            curveTo(15.406f, 20.12f, 14.729f, 20.732f, 13.948f, 20.732f)
            close()
          }
        }
        .build()

    return _Vector!!
  }

@Suppress("ObjectPropertyName")
private var _Vector: ImageVector? = null
