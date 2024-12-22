package com.example.vociapp.ui.components.utils

import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalView

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.hapticFeedback(): Modifier = composed {
    val view = LocalView.current
    pointerInteropFilter { event ->
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }
        }
        false
    }
}