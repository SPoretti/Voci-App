# Haptic Feedback [Extension]

The `hapticFeedback` extension function adds haptic feedback to any Composable element using the `Modifier`.

---

## Overview

- **Purpose**: Enhances user experience by providing tactile feedback when interacting with UI elements.
- **Key Features**:
    - Performs haptic feedback on `MotionEvent.ACTION_DOWN`.
    - Simple integration with any Composable through a `Modifier`.

---

## Usage Example

```kotlin
Button(
    onClick = { /* Handle click */ },
    modifier = Modifier.hapticFeedback()
) {
    Text("Click Me")
}
```

---

## Implementation Details

1. **Haptic Feedback Type**:
    - The extension uses `HapticFeedbackConstants.VIRTUAL_KEY`, which is ideal for simulating virtual button interactions.

2. **Pointer Interception**:
    - The `pointerInteropFilter` is used to intercept pointer events and detect when the `MotionEvent.ACTION_DOWN` action occurs.

3. **Context Awareness**:
    - The function uses `LocalView.current` to access the current Android `View`, ensuring the feedback is applied correctly to the current Composable.

4. **Composable Integration**:
    - By composing the function within `Modifier.composed`, it ensures that the extension works seamlessly with other modifiers.

---

**Note**: This extension relies on the device's haptic feedback hardware and settings. On devices where haptic feedback is disabled, this functionality will have no effect.
