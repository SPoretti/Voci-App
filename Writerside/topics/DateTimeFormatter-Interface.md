# DateTimeFormatter [Interface]

## Overview

`DateTimeFormatter` is an interface that provides methods for formatting date and time values into human-readable strings. It defines functions for formatting timestamps, dates, and times.

## Features

- **Date and Time Formatting:** Provides methods for formatting timestamps, dates, and times.
- **Human-Readable Strings:** Formats date and time values into strings that are easy for users to understand.

## Key Components

- **`formatTimestamp(timestamp: Long)`:** Formats a timestamp into a string representation.
- **`formatDate(timestamp: Long)`:** Formats a date into a string representation.
- **`formatTime(timestamp: Long)`:** Formats a time into a string representation.

## Known Limitations

- **Implementation-Specific:** The specific formatting patterns are determined by the implementation of this interface.

## Notes

- **Usage:** Used to format date and time values for display in the UI or for other purposes.
- **Implementations:** The `DateTimeFormatterImpl` class provides a concrete implementation of this interface using `SimpleDateFormat`.

