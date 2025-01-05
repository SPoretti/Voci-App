# Update Add Form Screen [Component]

The `UpdateAddFormScreen` component provides a form interface for creating or adding updates related to the health or location status of a homeless individual. This screen integrates with view models and supports navigation actions.

---

## Overview

- **Purpose**: Allows users to add an update about the health or location status of a homeless individual using a form.
- **Key Features**:
    - Dynamically adjusts form content based on the button option.
    - Supports navigation actions to go back or submit the form.
    - Adds a status update to the database records upon form submission.

---

## Parameters

| Parameter       | Type            | Description                                                                  |
|-----------------|-----------------|------------------------------------------------------------------------------|
| `navController` | `NavController` | Navigation controller for navigating between screens.                        |
| `buttonOption`  | `ButtonOption`  | Determines the title, description, and color associated with input.          |
| `homelessId`    | `String`        | The unique ID of the homeless individual for whom the update is being added. |

---

## Usage Example

```kotlin
UpdateAddFormScreen(
    navController = navController,
    buttonOption = ButtonOption.Green,
    homelessId = "12345"
)
```

---

## Features

1. **Dynamic Form Fields**:
    - Updates the title and description fields based on the selected `buttonOption`.
    - Retrieves and displays the data of the homeless individual associated with the provided `homelessId`.

2. **Interactive Buttons**:
    - Includes a "Cancel" button to navigate back to the previous screen.
    - Features an "Add" button to submit the form and add the update to the database.

3. **Integration with ViewModels**:
    - Utilizes `UpdatesViewModel` to add updates to the database.
    - Interacts with `VolunteerViewModel` to fetch the current user.
    - Accesses `HomelessViewModel` to fetch data for the specified homeless individual.

4. **Navigation Support**:
    - Navigates back to the home screen upon successful form submission.

---

## Implementation Details

- **Dynamic Behavior**: The form dynamically adjusts its fields based on the `buttonOption`, providing context-specific titles and descriptions.
- **Haptic Feedback**: Both the "Cancel" and "Add" buttons include haptic feedback for a tactile user experience.
- **Validation**: Ensures that all required fields are filled before submission.
- **Accessibility**: Designed to be accessible with clear labels and feedback mechanisms.
