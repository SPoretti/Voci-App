# RequestRepository [Class]

The `RequestRepository` class is responsible for managing `Request` objects, offering functionality to add, update, delete, and fetch requests, both locally (using Room) and remotely (using Firestore). It also supports offline functionality, where actions are queued for synchronization when the device is offline, and automatically synced when the device reconnects to the internet.

## Constructor

### `RequestRepository(firestoreDataSource: FirestoreDataSource, roomDataSource: RoomDataSource, networkManager: NetworkManager)`

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

### `addRequest(request: Request): Resource<Request>`

Adds a new `Request` object both locally and remotely (if online). If the device is offline, the data is saved locally and queued for later synchronization.

- **Parameters**:
    - `request`: The `Request` object to be added.
- **Returns**:
    - `Resource.Success`: The `Request` object if added successfully.
    - `Resource.Error`: An error message if an issue occurs (e.g., offline or Firestore failure).

### `updateRequest(request: Request): Resource<Request>`

Updates an existing `Request` object both locally and remotely (if online). If the device is offline, the data is saved locally and queued for later synchronization.

- **Parameters**:
    - `request`: The `Request` object to be updated.
- **Returns**:
    - `Resource.Success`: The updated `Request` object if successful.
    - `Resource.Error`: An error message if an issue occurs (e.g., offline or Firestore failure).

### `getRequests(): Flow<Resource<List<Request>>>`

Fetches a list of all `Request` objects stored in the local Room database.

- **Returns**:
    - `Flow<Resource<List<Request>>>`: A flow that emits the list of requests from the local Room database.

### `deleteRequest(request: Request): Resource<Unit>`

Deletes a specific `Request` object both locally and remotely (if online). If the device is offline, the data is deleted locally and queued for later synchronization.

- **Parameters**:
    - `request`: The `Request` object to be deleted.
- **Returns**:
    - `Resource.Success`: If the request was deleted successfully.
    - `Resource.Error`: An error message if the deletion failed.

### `getRequestById(requestId: String): Resource<Request>`

Fetches a specific `Request` object by its ID from the local Room database.

- **Parameters**:
    - `requestId`: The ID of the `Request` object.
- **Returns**:
    - `Resource.Success`: The `Request` object if found.
    - `Resource.Error`: An error message if the `Request` object was not found.

### `syncPendingActions()`

Syncs any pending actions (add, update, or delete) that were queued while the device was offline. This method only works if the device is online.

- **Returns**:
    - `Unit`: No return value. The method synchronizes the pending actions with Firestore.

### `fetchRequestsFromFirestoreToRoom()`

Fetches the list of `Request` objects from Firestore and updates the local Room database. If the device is offline, no action is performed.

- **Returns**:
    - `Unit`: No return value. The method updates the local Room database with data from Firestore.

---

## Error Handling

- The repository uses `Resource` wrappers to handle success and error responses. `Resource.Success` is returned when operations are successful, and `Resource.Error` is returned when an error occurs.
- Error messages for failed operations, such as being offline or Firestore errors, are included in the `Resource.Error` responses.

---

## Notes

- The repository ensures offline functionality by using Room as the local database. When the device is offline, actions are queued for synchronization with Firestore once the device reconnects to the network.
- The `syncPendingActions` method is responsible for processing any operations that were not completed while the device was offline.
- The `fetchRequestsFromFirestoreToRoom` method ensures that the local Room database is kept up-to-date with Firestore, and it removes any records that no longer exist remotely.

---
****