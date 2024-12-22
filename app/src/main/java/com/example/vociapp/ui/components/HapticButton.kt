package com.example.vociapp.ui.components

import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun HapticButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "My Button"
) {
    AndroidView<Button>(
        factory = { context ->
            Button(context).apply {
                setOnTouchListener(HapticTouchListener())
                setOnClickListener { onClick() }
                setPadding(16.dp.value.toInt(), 8.dp.value.toInt(), 16.dp.value.toInt(), 8.dp.value.toInt())
            }
        },
        modifier = modifier,
        update = { view ->
            view.text = text
        }
    )
}

class HapticTouchListener : View.OnTouchListener {
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // Provide haptic feedback only on ACTION_DOWN
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            }
            MotionEvent.ACTION_UP -> {
                // Check for click and call performClick
                if (event.action == MotionEvent.ACTION_UP) {
                    view.performClick()
                }
            }
        }
        return true // Consume the touch event
    }
}