# Add Request Dialog [Component]

## Overview

`AddRequestDialog` is a multi-step dialog used for adding a new request for a homeless individual. It allows users to:

- Select a homeless individual from the list.
- Add a title, description, and icon category for the request.
- Confirm the request addition after filling out the necessary information.

## Features

- **Step-by-Step Interaction**:
    - Step 1: Select a homeless individual from the list.
    - Step 2: Provide a title, description, and select an icon category for the request.

- **Dynamic UI States**: The dialog UI adapts based on the current step of the process.
- **Field Validation**: The confirm button in Step 2 is only enabled when the title and description are filled out.

## Key Components

- **Data Initialization**:
    - ViewModels: `HomelessViewModel`, `RequestViewModel`, and `VolunteerViewModel` are fetched via the `LocalServiceLocator`.
    - State Variables: `step` (to manage the current step), `requestTitle`, `requestDescription`, `homelessID`, `selectedIconCategory`, and `isAddingRequest` to control the dialog's state and inputs.

- **UI Structure**:
    - **Step 1: Homeless Selection**:
        - A search bar for filtering the homeless list.
        - A list of homeless individuals displayed, which updates based on the search query.
        - The user selects a homeless individual to move to Step 2.
    - **Step 2: Modify Request Details**:
        - Displays the selected homeless individual.
        - `OutlinedTextField` components for entering the request title and description.
        - `IconSelector` for selecting an icon category related to the request.
    - Buttons:
        - "Avanti" (Next): Moves to Step 2 after selecting a homeless individual.
        - "Aggiungi" (Add): Confirms and adds the request to the database, enabled only when the title and description are filled.
        - "Annulla" (Cancel): Dismisses the dialog or moves back to Step 1.

- **Data Validation**:
    - The confirm button in Step 2 is only enabled when the title and description fields are non-empty and the request is not already being added.

## Navigation

- **Invoked From**:
    -  FAB in the Requests Screen.

- **Navigates To**:
    - Does not directly navigate to another screen but triggers the addition of the request in the database, which may update the parent screen.

## Known Limitations

- **Error Handling**: There is no specific error feedback for database failures or issues beyond empty fields.
- **UI Feedback**: There is no loading indicator or progress feedback during the process of adding the request.

## Notes

- **Dependencies**:
    - `ServiceLocator` to obtain the required ViewModels.
    - `HomelessListItem` and `DialogSearchBar` for homeless selection.
    - `OutlinedTextField` for input fields.
    - `IconSelector` for selecting the icon category.
    - `Button` and `OutlinedButton` for interactive actions.
