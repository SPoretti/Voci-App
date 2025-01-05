# Homeless [Entity]

## Overview

The `Homeless` entity represents a homeless individual in the Voci app's Room database. This class stores personal details of a homeless individual, including their name, gender, location, age, pets, nationality, and description. It also includes information regarding their status and the area they are located in.

## Fields

- **`id`** (`String`): Unique identifier for the homeless individual. Generated using `UUID.randomUUID()`.
- **`name`** (`String`): The name of the homeless individual.
- **`gender`** (`Gender`): The gender of the homeless individual. Defaults to `Unspecified`.
- **`location`** (`String`): The current location of the homeless individual.
- **`age`** (`String`): The age of the homeless individual.
- **`pets`** (`String`): Information on whether the homeless individual has pets. Defaults to "No".
- **`nationality`** (`String`): The nationality of the homeless individual.
- **`description`** (`String`): A description providing additional information about the homeless individual.
- **`status`** (`UpdateStatus`): The current status of the homeless individual. Defaults to `GREEN`.
- **`area`** (`Area`): The area where the homeless individual is located. Defaults to `OVEST`.

## Usage

The `Homeless` entity is part of the Room database and is used to store and retrieve data related to homeless individuals. This information is crucial for matching volunteers with homeless individuals and managing requests or updates related to them.

## Known Limitations

- **Gender Handling:** The `Gender` enum should be handled carefully to ensure it aligns with the system's predefined gender options.
- **Location Validation:** The app should consider adding validation to ensure the `location` field accurately reflects the homeless individual's whereabouts.
- **Age Representation:** The `age` field is currently a `String`, which may need further validation or structuring (e.g., converting it to an `Int`).

## Notes

- **Dependencies:** Relies on the `Gender`, `UpdateStatus`, and `Area` enums for managing gender, status, and area data, respectively.
- **Relationships:** May be linked with other entities, such as `Request` or `Update`, depending on the app's logic.

## Future Improvements

- Implement better handling for the `gender` and `age` fields, including validation and more flexible representations.
- Introduce a more robust system for tracking the homeless individual's status and location, especially for those who move between areas frequently.
- Consider adding additional fields to track the individual's health, needs, or other specific details that could aid volunteers.

## Diagram

### Homeless Entity Diagram

```mermaid
classDiagram
    class Homeless {
        +id: String
        +name: String
        +gender: Gender
        +location: String
        +age: String
        +pets: String
        +nationality: String
        +description: String
        +status: UpdateStatus
        +area: Area
    }

    Homeless --* Gender : has
    Homeless --* UpdateStatus : has
    Homeless --* Area : has
