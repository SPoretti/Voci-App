# Request [Viewmodel]

The `RequestViewModel` class manages the state and logic for handling requests in the application. It acts as a bridge between the UI and the data layer, ensuring that data is retrieved, updated, and properly maintained.

## State Variables

### Snackbar Message
- **Variable:** `_snackbarMessage`
- **Public Access:** `snackbarMessage`
- **Type:** `StateFlow<String>`
- **Description:** Holds the message to be displayed in the snackbar. Updated when a user action occurs (e.g., adding, updating, or deleting a request).

### Requests
- **Variable:** `_requests`
- **Public Access:** `requests`
- **Type:** `StateFlow<Resource<List<Request>>>`
- **Description:** Contains the full list of requests fetched from the repository. Supports loading, success, and error states.

### Active Requests
- **Variable:** `_activeRequests`
- **Public Access:** `activeRequests`
- **Type:** `StateFlow<Resource<List<Request>>>`
- **Description:** Contains a list of active requests filtered from the full list.

### Completed Requests
- **Variable:** `_completedRequests`
- **Public Access:** `completedRequests`
- **Type:** `StateFlow<Resource<List<Request>>>`
- **Description:** Contains a list of completed requests filtered from the full list.

### Request by ID
- **Variable:** `_requestById`
- **Public Access:** `requestById`
- **Type:** `StateFlow<Resource<Request>>`
- **Description:** Contains details for a specific request identified by its ID.

### Requests by Homeless ID
- **Variable:** `_requestsByHomelessId`
- **Public Access:** `requestsByHomelessId`
- **Type:** `StateFlow<Resource<List<Request>>>`
- **Description:** Contains requests filtered by a specific homeless individual's ID.

### Firebase Auth
- **Variable:** `firebaseAuth`
- **Type:** `FirebaseAuth`
- **Description:** Tracks the Firebase authentication state to fetch requests for authenticated users.

---

## Methods

### fetchRequests
- **Description:** Fetches requests from Firestore and updates the local Room database.
- **Usage:** Called during Firebase authentication state changes or initialization.

### getRequests
- **Description:** Retrieves the list of requests from the local database and updates the `_requests` state variable. Also triggers fetching active and completed requests.

### getActiveRequests
- **Description:** Retrieves active requests from the local database and updates the `_activeRequests` state variable.

### getCompletedRequests
- **Description:** Retrieves completed requests from the local database and updates the `_completedRequests` state variable.

### getRequestById
- **Description:** Retrieves a specific request by its ID from the local database and updates the `_requestById` state variable.
- **Parameters:**
    - `requestId`: `String` - The ID of the request to fetch.

### getRequestsByHomelessId
- **Description:** Retrieves requests associated with a specific homeless individual's ID and updates the `_requestsByHomelessId` state variable.
- **Parameters:**
    - `homelessId`: `String` - The ID of the homeless individual.

### addRequest
- **Description:** Adds a new request to the local database. Updates the snackbar message and refreshes the requests list.
- **Parameters:**
    - `request`: `Request` - The request object to be added.

### updateRequest
- **Description:** Updates an existing request in the local database. Refreshes the requests list.
- **Parameters:**
    - `request`: `Request` - The updated request object.

### deleteRequest
- **Description:** Deletes a request from the local database. Updates the snackbar message and refreshes the requests list.
- **Parameters:**
    - `request`: `Request` - The request object to be deleted.

### requestDone
- **Description:** Marks a request's status as "DONE" and updates it in the local database.
- **Parameters:**
    - `request`: `Request` - The request to mark as done.

### requestTodo
- **Description:** Marks a request's status as "TODO" and updates it in the local database.
- **Parameters:**
    - `request`: `Request` - The request to mark as TODO.

### clearSnackbarMessage
- **Description:** Clears the snackbar message state.

---

## Firebase Auth Listener
- **Description:**
    - Monitors the authentication state of the user. Automatically fetches requests when a user signs in or updates their authentication state.

---

## Dependencies
- **RequestRepository:**
    - Handles data operations, including Firestore synchronization and local database updates.
- **FirebaseAuth:**
    - Tracks user authentication states.

---

This documentation provides an overview of the `RequestViewModel`, its responsibilities, and how it interacts with state and data. Let me know if further refinements are needed!

