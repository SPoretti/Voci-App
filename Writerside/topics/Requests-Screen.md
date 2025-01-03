# Requests Screen [Screen]

## Overview

`RequestsScreen` is a Composable function that serves as the main interface for viewing and managing a list of requests. It allows users to:

- View a list of pending requests.
- Sort requests based on selected criteria (e.g., latest or oldest).
- Navigate to the request details screen.
- Access the request history screen.
- Add new requests using a floating action button (FAB).
- Swipe to modify the status of a Request from TODO to DONE

## Features

- **Dynamic UI Updates**: Reflects the current state of the requests list using data from the `RequestViewModel`.
- **User Actions**:
    - Sort the list using custom sort options.
    - Add a new request through a FAB.
    - Navigate to request details and history screens.
    - Swipe to modify the status of a Request from TODO to DONE

## Key Components

- **Data Initialization**:  
  Utilizes `RequestViewModel`, `VolunteerViewModel`, and `HomelessViewModel` to fetch and manage request data.

- **UI Structure**:
    - **Header Section**: Contains sort buttons and a history navigation button.
    - **Request List Section**: Displays a scrollable list of swipable requests filtered by `TODO` status and sorted by the selected option.
    - **FAB Section**: Provides a button to add new requests.

- **Dialog for Adding Requests**:  
  Opens when the FAB is clicked, enabling users to input details for a new request.

## Navigation

- **Navigates From**:
    - Entry point for users managing requests.

- **Navigates To**:
    - `RequestDetailsScreen`: Opens when a request in the list is selected.
    - `RequestsHistoryScreen`: Opens when the history button is clicked.

## Known Limitations

- **Error Handling**: Snackbar messages are used to display feedback but lack detailed error handling mechanisms.
- **Data Dependencies**: Assumes that requests are correctly fetched and populated in `RequestViewModel`.

## Notes

- **Dependencies**:
    - ViewModels are retrieved via the `ServiceLocator`.
    - Uses `SortButtons`, `RequestList`, and `AddRequestDialog` Composables for modularity.
    - Relies on the `SnackbarHostState` to display messages.
- **Future Improvements**:
    - Enhance error handling with specific messages and retry options.
    - Add animations for better UX during list updates.
