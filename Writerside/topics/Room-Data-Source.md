# RoomDataSource [Class]

## Overview

The `RoomDataSource` class serves as the offline data source for the Voci app. It acts as an intermediary between the Room database and the app's logic, handling operations related to `Request`, `Homeless`, `Volunteer`, `Update`, and `SyncAction` entities. This class provides methods to perform CRUD operations (Create, Read, Update, Delete) on these entities, ensuring that the data is properly stored and fetched from the local Room database.

## Methods

### Request Functions

- **`insertRequest(request: Request)`**  
  Inserts a new `Request` into the database.

- **`getRequests(): Flow<Resource<List<Request>>>`**  
  Retrieves all `Request` entities from the database as a `Flow`. Emits a `Resource` which can be `Loading`, `Success`, or `Error`.

- **`getRequestsSnapshot(): List<Request>`**  
  Retrieves all `Request` entities from the database as a snapshot (non-flow result).

- **`insertOrUpdateRequest(request: Request)`**  
  Inserts a new `Request` or updates an existing one if it already exists.

- **`updateRequest(request: Request)`**  
  Updates an existing `Request` in the database.

- **`deleteRequest(request: Request)`**  
  Deletes a specific `Request` from the database.

- **`deleteRequestById(requestId: String)`**  
  Deletes a `Request` by its ID.

- **`getRequestById(requestId: String): Request`**  
  Retrieves a `Request` by its ID.

### Homeless Functions

- **`insertHomeless(homeless: Homeless)`**  
  Inserts a new `Homeless` entity into the database.

- **`getHomelesses(): Flow<Resource<List<Homeless>>>`**  
  Retrieves all `Homeless` entities as a `Flow`. Emits a `Resource` with the status of the operation.

- **`getHomelessesSnapshot(): List<Homeless>`**  
  Retrieves all `Homeless` entities as a snapshot (non-flow result).

- **`getHomelessById(homelessID: String): Homeless?`**  
  Retrieves a `Homeless` entity by its ID.

- **`updateHomeless(homeless: Homeless)`**  
  Updates an existing `Homeless` entity.

- **`insertOrUpdateHomeless(homeless: Homeless)`**  
  Inserts a new `Homeless` or updates an existing one.

- **`deleteHomelessById(homelessID: String)`**  
  Deletes a `Homeless` entity by its ID.

### Volunteer Functions

- **`insertVolunteer(volunteer: Volunteer)`**  
  Inserts a new `Volunteer` entity into the database.

- **`getVolunteerById(id: String): Volunteer?`**  
  Retrieves a `Volunteer` by their ID.

- **`getVolunteerByNickname(nickname: String): Volunteer?`**  
  Retrieves a `Volunteer` by their nickname.

- **`getVolunteerByEmail(email: String): Volunteer?`**  
  Retrieves a `Volunteer` by their email address.

- **`getVolunteerIdByEmail(email: String): String?`**  
  Retrieves the ID of a `Volunteer` by their email address.

- **`updateVolunteer(volunteer: Volunteer)`**  
  Updates an existing `Volunteer` entity.

- **`getVolunteers(): Flow<List<Volunteer>>`**  
  Retrieves all `Volunteer` entities as a `Flow`.

- **`getVolunteersSnapshot(): List<Volunteer>`**  
  Retrieves all `Volunteer` entities as a snapshot (non-flow result).

- **`insertOrUpdateVolunteer(volunteer: Volunteer)`**  
  Inserts a new `Volunteer` or updates an existing one.

- **`deleteVolunteerById(volunteerId: String)`**  
  Deletes a `Volunteer` entity by its ID.

### Update Functions

- **`insertUpdate(update: Update)`**  
  Inserts a new `Update` entity into the database.

- **`getUpdates(): Flow<Resource<List<Update>>>`**  
  Retrieves all `Update` entities as a `Flow`. Emits a `Resource` with the status of the operation.

- **`getUpdatesSnapshot(): List<Update>`**  
  Retrieves all `Update` entities as a snapshot (non-flow result).

- **`insertOrUpdateUpdate(update: Update)`**  
  Inserts a new `Update` or updates an existing one.

- **`deleteUpdateById(updateId: String)`**  
  Deletes an `Update` entity by its ID.

### Sync Functions

- **`isSyncQueueEmpty(): Boolean`**  
  Checks if the sync queue is empty.

- **`addSyncAction(entityType: String, operation: String, data: Any)`**  
  Adds a new sync action to the sync queue. The data is serialized to JSON before storing.

- **`deleteSyncAction(syncAction: SyncAction)`**  
  Deletes a `SyncAction` from the sync queue.

- **`getPendingSyncActions(timestamp: Long): Flow<List<SyncAction>>`**  
  Retrieves all pending sync actions that have a timestamp greater than the provided one.

## Usage

The `RoomDataSource` class abstracts interactions with the Room database. It provides simple, high-level methods for inserting, updating, and retrieving data for `Request`, `Homeless`, `Volunteer`, `Update`, and `SyncAction` entities. It also supports managing the sync queue, facilitating offline data synchronization with a server.

## Known Limitations

- **Asynchronous Operations:** Many methods are suspending functions (`suspend`), requiring appropriate coroutine management in the calling code.
- **Flow Handling:** Some methods use `Flow` for asynchronous data updates, which requires collecting the flow in the calling code.

## Notes

- **Dependencies:** Relies on Room database DAOs (`HomelessDao`, `VolunteerDao`, `RequestDao`, `UpdateDao`, `SyncQueueDao`) to interact with the database.

## Future Improvements

- Implement error handling in methods that interact with the database to improve resilience.
- Enhance sync functionality by adding a retry mechanism or conflict resolution for offline data sync.
- Consider implementing batch operations for sync actions to optimize network usage.

## Diagram

### RoomDataSource Class Diagram

```mermaid
classDiagram
    class RoomDataSource {
        +insertRequest(request: Request)
        +getRequests(): Flow<Resource<List<Request>>>
        +getRequestsSnapshot(): List<Request>
        +insertOrUpdateRequest(request: Request)
        +updateRequest(request: Request)
        +deleteRequest(request: Request)
        +deleteRequestById(requestId: String)
        +getRequestById(requestId: String): Request
        +insertHomeless(homeless: Homeless)
        +getHomelesses(): Flow<Resource<List<Homeless>>>
        +getHomelessesSnapshot(): List<Homeless>
        +getHomelessById(homelessID: String): Homeless?
        +updateHomeless(homeless: Homeless)
        +insertOrUpdateHomeless(homeless: Homeless)
        +deleteHomelessById(homelessID: String)
        +insertVolunteer(volunteer: Volunteer)
        +getVolunteerById(id: String): Volunteer?
        +getVolunteerByNickname(nickname: String): Volunteer?
        +getVolunteerByEmail(email: String): Volunteer?
        +getVolunteerIdByEmail(email: String): String?
        +updateVolunteer(volunteer: Volunteer)
        +getVolunteers(): Flow<List<Volunteer>>
        +getVolunteersSnapshot(): List<Volunteer>
        +insertOrUpdateVolunteer(volunteer: Volunteer)
        +deleteVolunteerById(volunteerId: String)
        +insertUpdate(update: Update)
        +getUpdates(): Flow<Resource<List<Update>>>
        +getUpdatesSnapshot(): List<Update>
        +insertOrUpdateUpdate(update: Update)
        +deleteUpdateById(updateId: String)
        +isSyncQueueEmpty(): Boolean
        +addSyncAction(entityType: String, operation: String, data: Any)
        +deleteSyncAction(syncAction: SyncAction)
        +getPendingSyncActions(timestamp: Long): Flow<List<SyncAction>>
    }
