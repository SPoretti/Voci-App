package com.voci.app.ui.components.core

import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalView

// Extension function to add haptic feedback to a composable
@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.hapticFeedback(): Modifier = composed {
    val view = LocalView.current            // Get the current view
    pointerInteropFilter { event ->         // Intercept pointer events
        when (event.actionMasked) {         // Check the action type
            MotionEvent.ACTION_DOWN -> {    // Perform haptic feedback on down event
                // Perform haptic feedback
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }
        }
        false   // Return false to continue with the event
    }
}