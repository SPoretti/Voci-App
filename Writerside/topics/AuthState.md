# AuthState [Sealed Class]

## Overview

`AuthState` is a sealed class that represents the different authentication states of a user in the application. It provides a type-safe way to handle user authentication and authorization logic.

## Features

- **Type Safety:** Ensures that only valid authentication states are used within the application.
- **State Management:** Provides a clear representation of the user's authentication status.
- **Extensibility:** Allows for adding new authentication states in the future if needed.

## Key Components

- **`Uninitialized`:** Represents the initial state before any authentication checks have been performed.
- **`Unauthenticated`:** Represents the state where the user is not logged in or their credentials are invalid.
- **`Authenticated`:** Represents the state where the user is successfully logged in and their credentials have been verified. It includes the `FirebaseUser` object containing user information.

## Known Limitations

- **Limited States:** Currently defines three basic authentication states. More complex scenarios might require additional states.

## Notes

- **Dependencies:** Relies on the `FirebaseUser` class from Firebase Authentication.
- **Usage:** Typically used in conjunction with a state management solution (e.g., ViewModel) to track and update the user's authentication state.