# SyncAction [Entity]

## Overview

The `SyncAction` entity is used to store and queue synchronization actions that are performed offline and later synced online. This entity tracks actions like "add", "update", or "delete" for different entities, along with the associated data and the timestamp when the action was queued.

## Fields

- **`id`** (`Long`): Unique identifier for the sync action. This is auto-generated.
- **`entityType`** (`String`): The type of entity being synchronized (e.g., "Homeless", "Volunteer").
- **`operation`** (`String`): The operation to be performed on the entity (e.g., "add", "update", "delete").
- **`data`** (`String`): The data to sync, which could be a serialized object or a JSON string.
- **`timestamp`** (`Long`): The timestamp when the sync action was queued.

## Usage

The `SyncAction` entity is part of the Room database and is used for managing synchronization actions. It helps track the changes made offline, which will later be synced to the server when the app goes online.

## Known Limitations

- **Data Size:** Depending on the size of the `data` field (which could contain serialized objects or JSON), this may impact the size of the database.
- **Error Handling:** The entity does not include detailed error handling for synchronization failures.

## Notes

- **Dependencies:** Relies on the Room database for storing sync actions and managing their persistence.

## Future Improvements

- Implement more robust error handling for synchronization failures.
- Add support for retrying failed sync actions automatically.
- Consider implementing a mechanism to track the status of each sync action (e.g., "pending", "successful", "failed").

## Diagram

### SyncAction Entity Diagram

```mermaid
classDiagram
    class SyncAction {
        +id: Long
        +entityType: String
        +operation: String
        +data: String
        +timestamp: Long
    }
