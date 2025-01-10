# Requests History [Screen]

## Overview

`RequestsHistoryScreen` is a Composable function that provides a view of the completed requests in the system. It allows users to:

- View a list of requests marked as `DONE`.
- Sort the list by different criteria such as "Latest" or "Oldest".
- Swipe a request to delete it.

## Features

- **Dynamic UI States**:
    - Displays only completed requests (`RequestStatus.DONE`).
    - Updates the UI dynamically based on the selected sorting option.
- **User Actions**:
    - Sort the list of completed requests using dedicated buttons.

## Key Components

- **Data Initialization**:
    - ViewModels: `RequestViewModel` and `HomelessViewModel` are initialized via the `ServiceLocator`.
    - Sort Options: Provides "Latest" and "Oldest" sorting options, initialized using `SortOption` objects.
    - Requests Data: Requests are fetched and observed through `requestViewModel.requests`.

- **UI Structure**:
    - **Top Section**: Row with sorting buttons for user interaction.
    - **Main Content**: A scrollable list of requests filtered to show only completed (`DONE`) requests.

- **Interactivity**:
    - Sorting Buttons: Allow users to sort the displayed requests by timestamp.

## Navigation

- **Navigates From**:
    - Invoked from `RequestsScreen` via the history button.

- **Navigates To**:
    - `RequestDetailsScreen`: When a specific request is selected from the list.

## Notes

- **Dependencies**:
    - `ServiceLocator` to obtain ViewModels.
    - `RequestList` Composable for displaying the filtered and sorted list of requests.
    - `SortButtons` Composable for modular and reusable sorting functionality.
