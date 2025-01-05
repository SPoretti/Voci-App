# Status LED [Component]

The `StatusLED` component is a visual indicator that simulates an LED with customizable color and pulsating behavior.

---

## Overview

- **Purpose**: Provides a visual cue to represent status using a glowing LED.
- **Key Features**:
    - Customizable color.
    - Optional pulsating animation.
    - Adaptable with `Modifier` for flexible UI integration.

---

## Parameters

| Parameter     | Type       | Default Value | Description                             |
|---------------|------------|---------------|-----------------------------------------|
| `color`       | `Color`    | -             | The color of the LED.                   |
| `isPulsating` | `Boolean`  | `true`        | Determines if the LED should pulsate.   |
| `modifier`    | `Modifier` | `Modifier`    | Modifier to customize the LED's layout. |

---

## Usage Example

```kotlin
StatusLED(
    color = Color.Red,
    isPulsating = true,
    modifier = Modifier.padding(8.dp)
)

```

---

## Features

1. **Pulsating Animation**:
    - The `isPulsating` parameter enables a smooth glowing effect by animating the LED's alpha value between 0.5 and 1.
    - The animation duration is randomized between 1000ms and 1500ms for a natural look.
    - Uses `rememberInfiniteTransition` to create an infinite repeating animation.

2. **Customizable Appearance**:
    - The `color` parameter defines the LED's primary color.
    - A radial gradient shader creates the glowing effect, blending the primary color into transparency.

3. **Static LED Mode**:
    - When `isPulsating` is set to `false`, the LED displays a static glow without animation.

4. **Flexible Integration**:
    - The `modifier` parameter allows developers to integrate the LED seamlessly into any layout.
    - Supports standard Compose `Modifier` functions such as `padding`, `size`, and `clip`.

---

**Note**: The `StatusLED` component is ideal for indicating statuses such as active, idle, or error states in user interfaces.
