# Request [Entity]

## Overview

The `Request` entity is used to store requests in the Voci app's Room database. It tracks the details of a request made by a volunteer to assist a homeless individual. This entity contains information about the request's status, the creator of the request, the associated homeless individual, and the area in which the request was made. It also includes two foreign keys: one for the `Homeless` entity and another for the `Volunteer` entity.

## Fields

- **`id`** (`String`): Unique identifier for the request. Generated using `UUID.randomUUID()`.
- **`creatorId`** (`String?`): The ID of the volunteer who created the request. This is a foreign key linking to the `Volunteer` entity.
- **`homelessID`** (`String`): The ID of the homeless individual associated with the request. This is a foreign key linking to the `Homeless` entity.
- **`title`** (`String`): The title of the request.
- **`description`** (`String`): A detailed description of the request.
- **`status`** (`RequestStatus`): The current status of the request. Can be either `TODO` or `DONE`.
- **`timestamp`** (`Long`): The timestamp when the request was created.
- **`iconCategory`** (`IconCategory`): The icon category representing the request (shirt, pants, shoes). Defaults to `OTHER`.
- **`area`** (`Area`): The area associated with the request. Defaults to `OVEST`.

## Usage

The `Request` entity is part of the Room database and is used to manage volunteer requests. It contains all the details necessary to track the status and creator of a request, as well as the associated homeless individual and the area in which the request is made.

## Known Limitations

- **Foreign Key Handling:** The foreign keys (`creatorId` and `homelessID`) may lead to cascading deletes or updates, which could affect related entities.
- **Status Updates:** The `RequestStatus` field only supports two states (`TODO` and `DONE`), which may limit flexibility in tracking the lifecycle of a request.

## Notes

- **Dependencies:** Relies on the `Homeless`, `Volunteer`, `RequestStatus`, `IconCategory`, and `Area` enums for its proper functionality.

## Future Improvements

- Add more status options for requests (e.g., `IN_PROGRESS`, `CANCELLED`).
- Consider implementing a more robust way of handling the foreign key constraints, especially regarding the deletion of related entities.
- Introduce custom validation for request fields, such as ensuring non-empty descriptions and titles.

## Diagram

### Request Entity Diagram

```mermaid
classDiagram
    class Request {
        +id: String
        +creatorId: String?
        +homelessID: String
        +title: String
        +description: String
        +status: RequestStatus
        +timestamp: Long
        +iconCategory: IconCategory
        +area: Area
    }

    Request --* RequestStatus : has
    Request --* IconCategory : has
    Request --* Area : has
    Request --> Volunteer : creatorId (FK)
    Request --> Homeless : homelessID (FK)
