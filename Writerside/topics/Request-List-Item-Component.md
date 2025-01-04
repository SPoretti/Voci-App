# Request List Item [Component]

The `RequestListItem` component is used to display individual request details within a list. It shows the request title, description, timestamp, and an icon representing the request category. Additionally, it includes a clickable chip that links to the homeless profile associated with the request.

---

## Overview

- **Purpose**: Displays detailed information for each request in the list.
- **Key Features**:
    - Shows the request's title, description, and timestamp.
    - Displays an icon representing the request's category.
    - Includes a clickable chip to navigate to the homeless profile associated with the request.

---

## Parameters

| Parameter         | Type                         | Description                                      |
|-------------------|------------------------------|--------------------------------------------------|
| `request`         | `Request`                    | The request data to display.                     |
| `navController`   | `NavHostController`          | The navigation controller to manage navigation.  |

---

## Usage Example

```kotlin
RequestListItem(
    request = requestData,
    navController = navController
)
```

---

## Features

1. **Data Initialization**:
    - Retrieves the names of the homeless from the `HomelessViewModel`.
    - Uses the `request.homelessID` to fetch the corresponding name of the homeless person.
    - Uses a `DateTimeFormatter` to format the request timestamp for display.

2. **Request Display**:
    - Displays the request's title in a bold style with ellipsis overflow if the title is too long.
    - Displays the request's timestamp using the provided date formatter.
    - Shows the request's description, with ellipsis overflow if the description exceeds one line.

3. **Icon for Request Category**:
    - Displays an icon corresponding to the request category (`iconCategoryMap`).
    - The icon is presented within a circular background, aligned vertically in the center.

4. **Clickable Homeless Profile Chip**:
    - A `CustomChip` component is used to display the homeless person's name.
    - Tapping the chip navigates to the profile of the homeless person.

5. **UI Customization**:
    - The layout is arranged with padding, icons, and text styles that match the Material Design principles.
    - The request item is displayed inside a `Surface` with a rounded corner and a shadow elevation for better visual prominence.

---

**Note**: The `CustomChip` component used in this component is reusable and customizable, allowing for dynamic text and icon display. The navigation links for both the request details and homeless profile are managed using the `NavHostController`.

---
