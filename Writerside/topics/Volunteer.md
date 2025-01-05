# Volunteer [Entity]

## Overview

The `Volunteer` entity represents a volunteer in the Voci app's Room database. This class contains basic information about the volunteer, such as their personal details, contact information, and the areas they are associated with. It also tracks the list of preferred homeless IDs.

## Fields

- **`id`** (`String`): Unique identifier for the volunteer. Generated using `UUID.randomUUID()`.
- **`name`** (`String`): The first name of the volunteer.
- **`surname`** (`String`): The last name of the volunteer.
- **`nickname`** (`String`): The nickname of the volunteer (optional).
- **`phone_number`** (`String`): The phone number of the volunteer.
- **`email`** (`String`): The email address of the volunteer.
- **`preferredHomelessIds`** (`List<String>`): List of IDs of homeless people that the volunteer prefers to help.
- **`area`** (`Area`): The area associated with the volunteer. Defaults to `OVEST`.

## Usage

The `Volunteer` entity is part of the Room database and is used for storing and retrieving volunteer-related data. The `preferredHomelessIds` field allows volunteers to express their preferences for assisting specific homeless people, which could be used in the matching process for requests.

## Known Limitations

- **Area Handling:** The `Area` enum should be handled properly to ensure it matches the system's predefined areas.
- **Phone Number Validation:** The app should include proper validation for phone numbers.

## Notes

- **Dependencies:** Relies on the `Area` enum and Room database.

## Future Improvements

- Add validation for phone numbers and emails.
- Consider implementing a more flexible mechanism for managing preferred homeless individuals (e.g., relationships between volunteers and specific homeless IDs).


## Diagram

### Volunteer Entity Diagram

```mermaid
classDiagram
    class Volunteer {
        +id: String
        +name: String
        +surname: String
        +nickname: String
        +phone_number: String
        +email: String
        +preferredHomelessIds: List<String>
        +area: Area
    }

    Volunteer --* Area : has
