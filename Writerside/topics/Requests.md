# Requests [Package]

## Overview

The Requests package is a core component of the application, responsible for managing user interactions related to clothing requests for the homeless. It provides screens to view, modify, and manage active and completed requests. This package adheres to the MVVM architecture and leverages Jetpack Compose for UI development.

### Features

- **Request Management**: Add, modify, delete, and complete requests.
- **State Tracking**: Requests are categorized as `Active` or `Completed`.
- **Sorting and Filtering**: Supports sorting requests by creation date (latest or oldest).

## Navigation Flow

1. **RequestsScreen** (default): Accessed from the bottom navigation bar. Displays active requests.
2. **RequestsHistoryScreen**: Accessed from the top-right button on `RequestsScreen`. Displays completed requests.
3. **RequestDetailsScreen**: Accessed by clicking on a request item in either list. Allows modification of a request.

## Screens

- **RequestsScreen**:  
  Displays the list of active requests with swipe-to-complete functionality.  
  **Navigation**: Bottom bar item.

- **RequestsHistoryScreen**:  
  Displays completed requests.  
  **Navigation**: Top-right button on `RequestsScreen`.

- **RequestDetailsScreen**:  
  Shows detailed information about a request and allows modifications.  
  **Navigation**: Tap on a request in `RequestsScreen` or `RequestsHistoryScreen`.

## Known Limitations

- **Sorting**: Limited to chronological order. Future updates could include additional filters (e.g., category or urgency).
- **Deleting Requests**: Irreversible and lacks confirmation. Adding a confirmation dialog is planned for a future release.

## Future Improvements

- Introduce filtering by request category, urgency, or location.
- Enhance accessibility for better screen reader support.
- Optimize performance for handling larger datasets.

