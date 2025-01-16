# Modify Request Dialog [Component]

## Overview

`ModifyRequestDialog` is a full-screen dialog used for modifying an existing request. It allows users to:

- Update the request's associated homeless individual.
- Edit the request's title and description.
- Confirm or cancel modifications with interactive buttons.

## Features

- **Step-by-Step Interaction**:
    - Step 1: Select a homeless individual from the list or search bar.
    - Step 2: Edit the title and description of the request.

- **Dynamic UI States**: The UI updates based on the current step, providing context-specific input fields and actions.
- **Validation**: Ensures that the confirm button is only enabled when the required fields are non-empty.

## Key Components

- **Data Initialization**:
    - ViewModels: `HomelessViewModel` and `RequestViewModel` are fetched via the `ServiceLocator`.
    - State Variables: `requestTitle`, `requestDescription`, `homelessID`, `selectedHomeless`, and `step` to manage the current state and user inputs.

- **UI Structure**:
    - **Step 1: Homeless Selection**:
        - A search bar to filter homeless individuals.
        - A scrollable list displaying homeless individuals.
        - Progresses to Step 2 upon selection.
    - **Step 2: Modify Request Details**:
        - `OutlinedTextField` components for editing the title and description.
        - A confirm button for saving changes and dismissing the dialog.
    - Buttons:
        - "Annulla" (Cancel): Dismisses the dialog or navigates back to Step 1.

- **Data Validation**:
    - The confirm button is disabled while the request is being processed or if required fields are empty.

## Navigation

- **Invoked From**:
    - Screens such as `RequestDetailsScreen` that support modifying requests.

- **Navigates To**:
    - Does not directly navigate to another screen but modifies request data, which may update the parent screen.

## Known Limitations

- **Error Handling**: Does not display specific error messages for database failures or invalid data.
- **UI Feedback**: Limited feedback for processing states; users might not see a loading indicator while saving data.

## Notes

- **Dependencies**:
    - `ServiceLocator` to obtain the required ViewModels.
    - `HomelessList` and `SearchBar` Composables for modularity.
