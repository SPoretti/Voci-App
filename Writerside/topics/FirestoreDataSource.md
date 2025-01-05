# FirestoreDataSource [Class]

This class provides methods for interacting with Firebase Firestore to manage data related to requests, homeless individuals, volunteers, and updates. The class includes functions for adding, retrieving, updating, and deleting these records from Firestore.

## Constructor

### `FirestoreDataSource(firestore: FirebaseFirestore)`

The constructor requires an instance of `FirebaseFirestore` which is used to interact with Firestore.

- **Parameters**:
    - `firestore`: The FirebaseFirestore instance used to perform operations on Firestore.

---

## Methods

### Request Functions

#### `addRequest(request: Request): Resource<String>`

Adds a new request to Firestore.

- **Parameters**:
    - `request`: The `Request` object to be added.
- **Returns**:
    - `Resource.Success`: The ID of the added request if successful.
    - `Resource.Error`: An error message if an exception occurs.

#### `getRequests(): Resource<List<Request>>`

Fetches a list of all requests from Firestore.

- **Returns**:
    - `Resource.Success`: A list of `Request` objects if successful.
    - `Resource.Error`: An error message if an exception occurs.

#### `updateRequest(request: Request): Resource<Unit>`

Updates an existing request in Firestore.

- **Parameters**:
    - `request`: The `Request` object to update.
- **Returns**:
    - `Resource.Success`: Indicates the update was successful.
    - `Resource.Error`: An error message if the request was not found or an exception occurs.

#### `deleteRequest(request: Request): Resource<Unit>`

Deletes a request from Firestore.

- **Parameters**:
    - `request`: The `Request` object to delete.
- **Returns**:
    - `Resource.Success`: Indicates the deletion was successful.
    - `Resource.Error`: An error message if the request was not found or an exception occurs.

#### `getRequestById(requestId: String): Resource<Request>`

Fetches a request by its ID from Firestore.

- **Parameters**:
    - `requestId`: The ID of the request.
- **Returns**:
    - `Resource.Success`: The `Request` object if found.
    - `Resource.Error`: An error message if the request was not found.

---

### Homeless Functions

#### `addHomeless(homeless: Homeless): Resource<String>`

Adds a new homeless individual to Firestore.

- **Parameters**:
    - `homeless`: The `Homeless` object to be added.
- **Returns**:
    - `Resource.Success`: The ID of the added homeless record if successful.
    - `Resource.Error`: An error message if an exception occurs.

#### `getHomelesses(): Resource<List<Homeless>>`

Fetches a list of all homeless individuals from Firestore.

- **Returns**:
    - `Resource.Success`: A list of `Homeless` objects if successful.
    - `Resource.Error`: An error message if an exception occurs.

#### `getHomelessById(homelessID: String): Resource<Homeless>`

Fetches a specific homeless individual by ID from Firestore.

- **Parameters**:
    - `homelessID`: The ID of the homeless individual.
- **Returns**:
    - `Resource.Success`: The `Homeless` object if found.
    - `Resource.Error`: An error message if the homeless individual was not found.

#### `updateHomeless(homeless: Homeless): Resource<Unit>`

Updates an existing homeless individual record in Firestore.

- **Parameters**:
    - `homeless`: The `Homeless` object to update.
- **Returns**:
    - `Resource.Success`: Indicates the update was successful.
    - `Resource.Error`: An error message if the homeless individual was not found or an exception occurs.

#### `deleteHomelessById(id: Any)`

This method is not implemented yet but can be used to delete a homeless individual by ID.

- **Parameters**:
    - `id`: The ID of the homeless individual to delete.

---

### Volunteer Functions

#### `addVolunteer(volunteer: Volunteer): Resource<String>`

Adds a new volunteer to Firestore.

- **Parameters**:
    - `volunteer`: The `Volunteer` object to be added.
- **Returns**:
    - `Resource.Success`: The ID of the added volunteer if successful.
    - `Resource.Error`: An error message if an exception occurs.

#### `getVolunteers(): Resource<List<Volunteer>>`

Fetches a list of all volunteers from Firestore.

- **Returns**:
    - `Resource.Success`: A list of `Volunteer` objects if successful.
    - `Resource.Error`: An error message if an exception occurs.

#### `updateVolunteer(volunteer: Volunteer): Resource<Unit>`

Updates an existing volunteer record in Firestore.

- **Parameters**:
    - `volunteer`: The `Volunteer` object to update.
- **Returns**:
    - `Resource.Success`: Indicates the update was successful.
    - `Resource.Error`: An error message if the volunteer was not found or an exception occurs.

---

### Updates Functions

#### `addUpdate(update: Update): Resource<String>`

Adds a new update record to Firestore.

- **Parameters**:
    - `update`: The `Update` object to be added.
- **Returns**:
    - `Resource.Success`: The ID of the added update if successful.
    - `Resource.Error`: An error message if an exception occurs.

#### `getUpdates(): Resource<List<Update>>`

Fetches a list of all updates from Firestore.

- **Returns**:
    - `Resource.Success`: A list of `Update` objects if successful.
    - `Resource.Error`: An error message if an exception occurs.

#### `updateUpdate(data: Update)`

This method is not implemented yet but can be used to update an existing update record.

- **Parameters**:
    - `data`: The `Update` object to be updated.

#### `deleteUpdate(id: String)`

This method is not implemented yet but can be used to delete an update record by ID.

- **Parameters**:
    - `id`: The ID of the update to delete.

---

## Notes

- The methods are designed to handle Firestore operations asynchronously and return results wrapped in a `Resource` wrapper, either `Success` or `Error`.
- Error handling is included for each method to catch and report issues such as missing records or connectivity problems.
- The `deleteHomelessById` and `updateUpdate` methods are placeholders and are not yet implemented.

---
