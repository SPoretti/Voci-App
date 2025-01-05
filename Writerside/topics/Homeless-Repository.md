# HomelessRepository [Class]

The `HomelessRepository` class acts as a data repository that manages both local and remote data sources. It provides functionalities to manage `Homeless` records, with offline support and automatic synchronization when the device goes online. The repository interacts with Firestore for remote storage and Room for local storage.

## Constructor

### `HomelessRepository(firestoreDataSource: FirestoreDataSource, roomDataSource: RoomDataSource, networkManager: NetworkManager)`

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

### `addHomeless(homeless: Homeless): Resource<Homeless>`

Adds a new `Homeless` object both locally and remotely (if online). If the device is offline, the data is saved locally and queued for later synchronization.

- **Parameters**:
    - `homeless`: The `Homeless` object to be added.
- **Returns**:
    - `Resource.Success`: The `Homeless` object if added successfully.
    - `Resource.Error`: An error message if an issue occurs (e.g., offline or Firestore failure).

### `updateHomeless(homeless: Homeless): Resource<Homeless>`

Updates an existing `Homeless` object both locally and remotely (if online). If the device is offline, the data is saved locally and queued for later synchronization.

- **Parameters**:
    - `homeless`: The `Homeless` object to be updated.
- **Returns**:
    - `Resource.Success`: The updated `Homeless` object if successful.
    - `Resource.Error`: An error message if an issue occurs (e.g., offline or Firestore failure).

### `getHomelesses(): Flow<Resource<List<Homeless>>>`

Fetches a list of all `Homeless` objects stored in the local Room database.

- **Returns**:
    - `Flow<Resource<List<Homeless>>>`: A flow that emits the list of homeless individuals from the local Room database.

### `getHomelessById(homelessID: String): Resource<Homeless>`

Fetches a specific `Homeless` object by its ID from the local Room database.

- **Parameters**:
    - `homelessID`: The ID of the `Homeless` object.
- **Returns**:
    - `Resource.Success`: The `Homeless` object if found.
    - `Resource.Error`: An error message if the `Homeless` object was not found.

### `syncPendingActions()`

Syncs any pending actions (add, update, or delete) that were queued while the device was offline. This method only works if the device is online.

- **Returns**:
    - `Unit`: No return value. The method synchronizes the pending actions with Firestore.

### `fetchHomelessesFromFirestoreToRoom()`

Fetches the list of `Homeless` objects from Firestore and updates the local Room database. If the device is offline, no action is performed.

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
- The `fetchHomelessesFromFirestoreToRoom` method ensures that the local Room database is kept up-to-date with Firestore, and it removes any records that no longer exist remotely.

---
