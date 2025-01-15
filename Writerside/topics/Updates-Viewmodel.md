# Updates [Viewmodel]

The `UpdatesViewModel` class manages the state and logic for handling updates in the application. It serves as an intermediary between the UI and the data layer, ensuring updates are retrieved, added, and maintained.

## State Variables

### Snackbar Message
- **Variable:** `_snackbarMessage`
- **Public Access:** `snackbarMessage`
- **Type:** `StateFlow<String>`
- **Description:** Holds the message to be displayed in the snackbar. It is updated when a user action occurs (e.g., adding an update).

### Updates
- **Variable:** `_updates`
- **Public Access:** `updates`
- **Type:** `StateFlow<Resource<List<Update>>>`
- **Description:** Contains the full list of updates fetched from the repository. Supports loading, success, and error states.

### Updates by Homeless ID
- **Variable:** `_updatesByHomelessId`
- **Public Access:** `updatesByHomelessId`
- **Type:** `StateFlow<Resource<List<Update>>>`
- **Description:** Contains updates filtered by a specific homeless individual's ID.

---

## Methods

### fetchUpdates
- **Description:** Fetches updates from Firestore and updates the local Room database.
- **Usage:** Called during initialization to ensure updates are fetched at the start.

### getUpdates
- **Description:** Retrieves the list of updates from the repository and updates the `_updates` state variable.
- **Usage:** Called during initialization to populate the updates list.

### getUpdatesByHomelessId
- **Description:** Retrieves updates associated with a specific homeless individual's ID and updates the `_updatesByHomelessId` state variable.
- **Parameters:**
    - `homelessId`: `String` - The ID of the homeless individual.

### addUpdate
- **Description:** Adds a new update to the repository. Updates the snackbar message based on the result and refreshes the updates list.
- **Parameters:**
    - `update`: `Update` - The update object to be added.
- **Usage:** Called when a new update is created and needs to be saved to the repository.

---

## Dependencies

- **UpdatesRepository:**
    - Handles data operations, including Firestore synchronization and local database updates.

---
