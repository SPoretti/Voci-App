import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

// .gradientBackground {
//                ThemeUtils.createLinearGradientBrush(color1 = MaterialTheme.colorScheme.secondary, color2 = MaterialTheme.colorScheme.background)
//            }
// TODO: [ThemeUtils.kt] Gradient background kind of working but not useful + missing docs.

object ThemeUtils {

    fun Modifier.gradientBackground(brushProvider: @Composable () -> Brush) = composed {
        var size by remember { mutableStateOf(IntSize.Zero) }
        onGloballyPositioned { coordinates ->
            size = coordinates.size
        }
        background(brush = brushProvider.invoke())
    }

    fun createLinearGradientBrush(
        color1: Color,
        color2: Color,
        start: Offset = Offset.Zero,
        end: Offset = Offset(1000f, 1000f),   // Fix Not working bottom right (Not taking the actual max values
        tileMode: TileMode = TileMode.Clamp
    ): Brush {
        return Brush.linearGradient(
            colors = listOf(color1, color2),
            start = start,
            end = end,
            tileMode = tileMode
        )
    }
}
