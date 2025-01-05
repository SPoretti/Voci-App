# UpdatesRepository [Class]

The `UpdatesRepository` class manages `Update` objects and provides functionality for adding, updating, deleting, and fetching updates from both a local Room database and a remote Firestore database. It also handles offline functionality by queuing operations for synchronization when the device is offline and automatically syncing them once the device is online.

## Constructor

### `UpdatesRepository(firestoreDataSource: FirestoreDataSource, roomDataSource: RoomDataSource, networkManager: NetworkManager)`

The constructor requires instances of the following classes:
- **firestoreDataSource**: Handles remote Firestore operations.
- **roomDataSource**: Handles local Room database operations.
- **networkManager**: Used to check the device's network connectivity status.

- **Parameters**:
    - `firestoreDataSource`: An instance of `FirestoreDataSource` to interact with Firestore.
    - `roomDataSource`: An instance of `RoomDataSource` to interact with the local Room database.
    - `networkManager`: An instance of `NetworkManager` to check the device's network connectivity.

---

## Methods

### `addUpdate(update: Update): Resource<Update>`

Adds a new `Update` object both locally and remotely (if online). If the device is offline, the data is saved locally and queued for later synchronization.

- **Parameters**:
    - `update`: The `Update` object to be added.
- **Returns**:
    - `Resource.Success`: The `Update` object if added successfully.
    - `Resource.Error`: An error message if an issue occurs (e.g., offline or Firestore failure).

### `getUpdates(): Flow<Resource<List<Update>>>`

Fetches a list of all `Update` objects stored in the local Room database.

- **Returns**:
    - `Flow<Resource<List<Update>>>`: A flow that emits the list of updates from the local Room database.

### `syncPendingActions()`

Syncs any pending actions (add, update, or delete) that were queued while the device was offline. This method only works if the device is online.

- **Returns**:
    - `Unit`: No return value. The method synchronizes the pending actions with Firestore.

### `fetchUpdatesFromFirestoreToRoom()`

Fetches the list of `Update` objects from Firestore and updates the local Room database. If the device is offline, no action is performed.

- **Returns**:
    - `Unit`: No return value. The method updates the local Room database with data from Firestore.

---

## Error Handling

- The repository uses `Resource` wrappers to handle success and error responses. `Resource.Success` is returned when operations are successful, and `Resource.Error` is returned when an error occurs.
- Error messages for failed operations, such as being offline or Firestore errors, are included in the `Resource.Error` responses.

---

## Notes

- The repository ensures offline functionality by using Room as the local database. When the device is offline, actions are queued for synchronization with Firestore once the device reconnects to the network.
- The `syncPendingActions` method processes any actions that were not completed while the device was offline.
- The `fetchUpdatesFromFirestoreToRoom` method ensures that the local Room database is kept up-to-date with Firestore, and it removes any records that no longer exist remotely.

---
