# Add Homeless Dialog [Component]

## Overview

`AddHomelessDialog` is a multi-step dialog used for adding a homeless individual to the database. It allows users to:

- Input the homeless individual’s name, location, age, nationality, pets, gender, and description.
- Navigate between different input steps.
- Validate required fields before proceeding.
- Confirm or cancel adding the individual.

## Features

- **Step-by-Step Interaction**:
    - Step 1: Enter name and location (required fields).
    - Step 2: Enter age and nationality.
    - Step 3: Enter pets and gender.
    - Step 4: Enter description.

- **Dynamic UI States**: The UI changes based on the current step, showing relevant input fields for each step.
- **Field Validation**: Ensures the name and location are filled in Step 1 before moving to the next step.

## Key Components

- **Data Initialization**:
    - State Variables: `currentStep`, `nameError`, `locationError`, `homeless` (data object), `isAddingHomeless`.
    - Step Variables: `name`, `location`, `age`, `nationality`, `pets`, `gender`, `description`.

- **UI Structure**:
    - **Step 1: Name and Location**:
        - `OutlinedTextField` components for inputting the name and location.
        - Error messages for empty fields.
    - **Step 2: Age and Nationality**:
        - `OutlinedTextField` components for age and nationality.
    - **Step 3: Pets and Gender**:
        - `OutlinedTextField` for pets and a custom `GenderSelector` for selecting gender.
    - **Step 4: Description**:
        - `OutlinedTextField` for entering the description (max 10 lines).
    - Buttons:
        - "Avanti" (Next): Advances to the next step.
        - "Aggiungi" (Add): Confirms and adds the homeless individual to the database.
        - "Indietro" (Back): Goes back to the previous step.
        - "Annulla" (Cancel): Dismisses the dialog.

- **Data Validation**:
    - The dialog prevents proceeding to the next step until the required fields (name and location) are filled in Step 1.

## Navigation

- **Invoked From**:
    - FAB in the home screen.

- **Navigates To**:
    - Does not directly navigate to another screen but triggers the `onAdd` callback with the homeless data when the process is complete.

## Known Limitations

- **Error Handling**: Limited error handling for issues beyond empty fields (e.g., database errors).
- **UI Feedback**: There is no specific loading indicator during the process of adding the homeless individual.

## Notes

- **Dependencies**:
    - `Homeless` data model to hold the information for the homeless individual.
    - `GenderSelector` for selecting gender.
