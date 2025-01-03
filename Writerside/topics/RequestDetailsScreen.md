# Request Details Screen [Screen]

## Overview

`RequestDetailsScreen` is a Composable function that provides a detailed view of a single request. It allows users to:

- View the request's title, description, timestamp, creator, and recipient details.
- Modify the request using a floating action button (FAB) that opens a dialog.
- Navigate to related profiles (creator and recipient).
- Handle loading, success, and error states for the request data.

## Features

- **Dynamic UI States**: Displays appropriate UI components based on the state of the request resource (`Loading`, `Success`, `Error`).
- **User Actions**:
    - Modify a request using the FAB.
    - Navigate to volunteer and homeless profiles using chips.
- **Interactive Elements**:
    - Status LED indicating the request's current status (`TODO` or `DONE`).
    - Timestamp formatted using a `DateTimeFormatter`.

## Key Components

- **Data Initialization**:  
  Initializes `RequestViewModel`, `VolunteerViewModel`, and `HomelessViewModel` to fetch and display relevant data (request, creator, and recipient).

- **UI Structure**:
    - **Top Section**: Icon and status LED.
    - **Details Section**: Title, timestamp, and description.
    - **Creator and Recipient Section**: Chips for navigation.

- **Dialog for Modification**:  
  Opens when the FAB is clicked, allowing users to modify the request details.

## Navigation

- **Navigates From**:
    - `RequestsScreen` or `RequestsHistoryScreen` by tapping on a request.

- **Navigates To**:
    - `profileVolontario/{creatorId}`: Volunteer profile of the creator.
    - `profileHomeless/{homelessId}`: Profile of the homeless recipient.

## Known Limitations

- **Error Handling**: Limited to displaying a generic message and a "Go back" button in case of errors.
- **Data Source**: Relies on `homelessNames` already loaded in memory from the parent screen.

## Notes

- **Dependencies**:
    - ViewModels are obtained from a `ServiceLocator` (`LocalServiceLocator.current`).
    - Uses `RequestChip` and `ModifyRequestDialog` Composables for modularity.
