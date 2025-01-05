# VociAppRoomDatabase [Singleton Class]

## Overview

`VociAppRoomDatabase` is the Room database instance for the Voci app. It manages the database connection and handles various entities with their associated DAOs. This class follows the Singleton design pattern to ensure only one instance of the database is used.

## Known Limitations

- **Destructive Migration:** Using `fallbackToDestructiveMigration()` can delete all existing data (used for debugging purposes).
- **Manual DAO Management:** Developers must ensure DAOs are correctly implemented and managed.

## Notes

- **Dependencies:** The database relies on entities, DAOs, and converters for full functionality.
- **Future Improvements:**
    - Implement custom migrations to preserve data on schema changes.
    - Support schema export for production environments.
