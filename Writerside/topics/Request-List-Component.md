# Request List [Component]

The `RequestList` component is designed to display a list of requests with filtering and sorting capabilities. It supports swipe-to-dismiss functionality, allowing users to perform actions like completing or deleting requests.

---

## Overview

- **Purpose**: Displays a list of requests based on the provided filter and sort options.
- **Key Features**:
    - Filters requests based on the selected filter option.
    - Sorts requests according to the selected sort option.
    - Implements swipe-to-dismiss functionality to either complete or delete a request.
    - Provides appropriate feedback and actions depending on the request status.

---

## Parameters

| Parameter       | Type                      | Description                                               |
|-----------------|---------------------------|-----------------------------------------------------------|
| `requests`      | `Resource<List<Request>>` | A resource that holds the list of requests.               |
| `filterOption`  | `RequestStatus`           | The filter option used to show specific request statuses. |
| `sortOption`    | `SortOption`              | The sort option used to order the requests.               |
| `navController` | `NavHostController`       | The navigation controller used for handling navigation.   |

---

## Usage Example

```kotlin
RequestList(
    requests = resource,
    filterOption = RequestStatus.TODO,
    sortOption = SortOption.DATE,
    navController = navController
)
```

---

## Features

1. **Data Filtering and Sorting**:
    - The list of requests is filtered based on the `filterOption` and sorted using the `sortOption`.
    - The filtering ensures that only requests with the selected status are displayed.
    - The sorting is done according to the specified sorting criteria in `SortOption`.

2. **Swipe-to-Dismiss Functionality**:
    - Requests can be swiped to the left or right for performing actions.
    - If the request is on the "TODO" page, swiping from left to right will mark the request as complete.
    - If the request is on the "DONE" page, swiping from left to right will delete the request.

3. **Loading and Error States**:
    - Displays a `CircularProgressIndicator` while the data is loading.
    - If there are no requests, a message is displayed stating "Non ci sono richieste attive."
    - If an error occurs, an error message is shown, and a button is provided to go back.

4. **UI Customization**:
    - The swipe-to-dismiss background color changes based on the action: blue for "complete" and red for "delete".
    - Each request item is displayed in a `LazyVerticalGrid` with customizable content.

5. **Haptic Feedback**:
    - Provides haptic feedback when a request is swiped, ensuring a tactile interaction.

---

**Note**: The `RequestListItem` component should be used to define the content of each individual request in the list.

---
