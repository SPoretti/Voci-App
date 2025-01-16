package com.voci.app.ui.components.volunteers

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomSnackbar(snackbarData: SnackbarData, isBottomBarVisible: Boolean = false) {
    val context = LocalContext.current
    val rootView = LocalView.current

    DisposableEffect(context) {
        val callback = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
        }

        rootView.viewTreeObserver.addOnGlobalLayoutListener(callback)

        onDispose {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(callback)
        }
    }

    val snackbarModifier = Modifier
        .consumeWindowInsets(
            if (!isBottomBarVisible) { WindowInsets(0.dp, 0.dp, 0.dp, 85.dp) }
            else { WindowInsets(0.dp, 0.dp, 0.dp, 170.dp) }
        )
        .imePadding()
        .wrapContentHeight()
        .clip(RoundedCornerShape(12.dp))
        .background(MaterialTheme.colorScheme.primary)
        .padding(16.dp)

    Box(
        modifier = snackbarModifier
    ) {
        Text(
            text = snackbarData.visuals.message,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
    }
}


