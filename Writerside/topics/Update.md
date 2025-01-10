# Update.kt [Data Class]

## Overview

`Update` is a data class representing an update entity for the Room database. It is used to store information about updates linked to a homeless person and the volunteer who created them. The entity establishes foreign key relationships with the `Homeless` and `Volunteer` entities, ensuring proper data integrity.

## Features

- **Room Database Entity:** Defined as a Room entity stored in the `updates` table.
- **Foreign Key Relationships:**
    - Links to the `Homeless` entity via `homelessID`.
    - Links to the `Volunteer` entity via `creatorId`.
- **Unique Identifiers:** Each update has a unique identifier (`id`) generated using `UUID.randomUUID()`.
- **Status Tracking:** Uses the `UpdateStatus` enum to indicate the nature of the update.
- **Timestamped Records:** Records the creation time of updates using the `timestamp` field.
- **Area Association:** Associates updates with specific areas using the `Area` enum.

## Key Components

- **`id`:** A unique identifier for each update.
    - **Type:** `String`
    - **Default Value:** A randomly generated UUID string.
    - **Details:** Serves as the primary key in the Room database table.
- **`creatorId`:** The ID of the volunteer who created the update.
    - **Type:** `String`
    - **Default Value:** Empty string (`""`).
    - **Foreign Key:** References the `id` column in the `Volunteer` table.
    - **OnDelete Behavior:** `SET DEFAULT`
- **`homelessID`:** The ID of the homeless person associated with the update.
    - **Type:** `String`
    - **Default Value:** Empty string (`""`).
    - **Foreign Key:** References the `id` column in the `Homeless` table.
    - **OnDelete Behavior:** `CASCADE`
- **`title`:** The title of the update.
    - **Type:** `String`
    - **Default Value:** Empty string (`""`).
- **`description`:** A detailed description of the update.
    - **Type:** `String`
    - **Default Value:** Empty string (`""`).
- **`status`:** The current status of the update, represented by the `UpdateStatus` enum.
    - **Type:** `UpdateStatus`
    - **Default Value:** `UpdateStatus.GREEN`
- **`timestamp`:** The timestamp indicating when the update was created.
    - **Type:** `Long`
    - **Default Value:** `System.currentTimeMillis()`
- **`area`:** The area associated with the update, represented by the `Area` enum.
    - **Type:** `Area`
    - **Default Value:** `Area.OVEST`

## Enums

### `UpdateStatus`
- **`GREEN`:** Indicates everything is normal or positive.
- **`YELLOW`:** Indicates caution or a minor issue.
- **`RED`:** Indicates a critical issue or urgent situation.
- **`GRAY`:** Indicates an unknown or indeterminate status.

## Known Limitations

- **Data Validation:** Does not validate fields like `title` or `description`, which might be left empty or contain incorrect data.
- **Status Flexibility:** The `UpdateStatus` enum is limited to predefined values and may require expansion to support other use cases.
- **Foreign Key Constraints:** Relies on Room to enforce foreign key constraints and behaviors.

## Notes

- **Room Integration:** Foreign key constraints ensure proper relationships with `Homeless` and `Volunteer` entities.
- **Dependencies:** Requ
