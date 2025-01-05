# Home [Screen]

## Overview

`HomeScreen` is a Composable function that provides a user interface for managing and viewing a list of homeless individuals in the system. It allows users to:

- View a list of homeless individuals.
- Search and filter the list based on user input.
- Refresh the data using a pull-to-refresh mechanism.
- Open a drawer menu for navigation.
- Add a new homeless individual via a dialog.

## Features

- **Dynamic UI States**:
    - Displays the list of homeless individuals, either from the full list or filtered by a search query.
    - Allows users to refresh the list of homeless individuals with a pull-to-refresh gesture.
    - Displays a Snackbar message based on the `SnackbarMessage` state in the `HomelessViewModel`.

- **User Actions**:
    - Search: Users can search for homeless individuals by typing in the search bar.
    - Pull-to-Refresh: Users can refresh the list by triggering a pull-to-refresh action.
    - Add New Homeless: A Floating Action Button (FAB) is provided for adding a new homeless individual.

## Key Components

- **Data Initialization**:
    - ViewModel: `HomelessViewModel` is initialized using `ServiceLocator`.
    - Homeless Data: Data is fetched using `homelessViewModel.getHomelesses()` and displayed based on the search query.
    - Snackbar Message: Displays messages through `SnackbarHostState` triggered by `homelessViewModel.snackbarMessage`.

- **UI Structure**:
    - **Top Section**: Includes a custom search bar with leading and trailing icons for navigation and profile access.
    - **Main Content**: Displays a list of homeless individuals, with filtering based on the search query and a pull-to-refresh mechanism.
    - **Bottom Section**: Includes a FAB that opens a dialog for adding new homeless individuals.

- **Interactivity**:
    - Search Bar: Filters the displayed list based on the user’s input.
    - Pull-to-Refresh: Refreshes the homeless data by fetching the latest information.
    - Add Homeless Dialog: Opens a dialog to add a new homeless individual when the FAB is clicked.

## Navigation

- **Navigates From**:
    - `RequestsScreen`: Home screen can be accessed from the main navigation.

- **Navigates To**:
    - `DrawerContent`: Opens the navigation drawer.
    - `HomelessList`: Displays the list of homeless individuals.
    - `AddHomelessDialog`: Opens the dialog to add a new homeless individual.

## Notes

- **Dependencies**:
    - `ServiceLocator` to obtain the `HomelessViewModel`.
    - `PullToRefreshBox` for implementing pull-to-refresh functionality.
    - `SearchBar` for searching and filtering homeless individuals.
    - `HomelessList` for displaying the list of homeless individuals.
    - `CustomFAB` for the floating action button to add a new homeless.
    - `AddHomelessDialog` for adding a new homeless individual.
