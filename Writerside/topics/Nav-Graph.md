# Nav Graph [Component]

The `NavGraph` component sets up the navigation structure for the app, handling all routes and animations between different screens. It configures the navigation controller, padding values, and snackbar host state for seamless transitions between the app's screens.

---

## Overview

- **Purpose**: Provides the navigation flow between various screens of the app with customized transition animations.
- **Key Features**:
    - Defines routes and destinations for different screens such as Home, User Profile, Requests, Homeless, Updates, and more.
    - Supports slide and vertical transitions with configurable animation durations.
    - Integrates with snackbar for displaying messages.

---

## Parameters

| Parameter           | Type                | Description                                                   |
|---------------------|---------------------|---------------------------------------------------------------|
| `navController`     | `NavHostController` | Navigation controller for managing navigation across the app. |
| `paddingValues`     | `PaddingValues`     | Padding values for adjusting the layout of the content.       |
| `snackbarHostState` | `SnackbarHostState` | Host state for displaying snackbar messages.                  |

---

## Usage Example
Navigating to a screen and passing props to it:
```kotlin
navController.navigate("UpdateAddFormScreen/${buttonData.route}/$homelessId")
```

---

## Features

1. **Routes and Transitions**:
    - The navigation graph defines routes for multiple screens, including:
        - **Home**
        - **Sign In / Sign Up**
        - **User Profile**
        - **Requests**
        - **Homeless Profile**
        - **Updates**
        - **Maps**
    - Each route has associated transition animations for entering and exiting the screen, with options for horizontal and vertical slides.

2. **Dynamic Routing**:
    - The routes can accept dynamic parameters, such as `homelessId` and `creatorId`, for fetching and displaying relevant data on each screen.

3. **Snackbar Integration**:
    - Uses the `snackbarHostState` to display snackbar messages, providing feedback to the user.

4. **Animation Control**:
    - The component allows defining custom animation durations and slide directions (horizontal or vertical) for navigation transitions.

---

## Route Overview

| Route Name                                        | Screen/Component          | Parameters                   | Enter Transition                            | Exit Transition                          |
|---------------------------------------------------|---------------------------|------------------------------|---------------------------------------------|------------------------------------------|
| `Screens.Home.route`                              | `HomeScreen`              | None                         | Slide from left (if coming from `Requests`) | Slide out right (if going to `Requests`) |
| `signIn`                                          | `SignInScreen`            | None                         | None                                        | None                                     |
| `signUp`                                          | `SignUpScreen`            | None                         | None                                        | None                                     |
| `Screens.UserProfile.route`                       | `UserProfileScreen`       | None                         | Slide in from right                         | Slide out to right                       |
| `ProfileVolontario/{creatorId}`                   | `ProfileVolunteerScreen`  | `creatorId`                  | Slide in from right                         | Slide out to right                       |
| `Screens.UpdateUserProfile.route`                 | `UpdateUserProfileScreen` | None                         | None                                        | None                                     |
| `requests`                                        | `RequestsScreen`          | None                         | Slide from right (if coming from `Home`)    | Slide out left (if going to `Home`)      |
| `requestsHistory`                                 | `RequestsHistoryScreen`   | None                         | Slide in from right                         | Slide out to right                       |
| `RequestDetailsScreen/{requestId}`                | `RequestDetailsScreen`    | `requestId`                  | Slide in from right                         | Slide out to right                       |
| `ProfileHomeless/{homelessId}`                    | `ProfileHomelessScreen`   | `homelessId`                 | Slide in from right                         | Slide out to right                       |
| `UpdatesAddScreen/{homelessId}`                   | `UpdateAddScreen`         | `homelessId`                 | Slide in from bottom                        | Slide out to bottom                      |
| `UpdateAddFormScreen/{buttonOption}/{homelessId}` | `UpdateAddFormScreen`     | `buttonOption`, `homelessId` | Slide in from bottom                        | Slide out to bottom                      |
| `apiTesting`                                      | `ApiTesting`              | None                         | None                                        | None                                     |
| `HomelessesMap/{homelessId}`                      | `HomelessesMap`           | `homelessId`                 | Slide in from bottom                        | Slide out to bottom                      |

---

## Navigation Flow Diagram

![](NavGraph.png)

---

## Implementation Details

- **Navigation Flow**: The navigation graph establishes clear routes and destinations, using route parameters for dynamic content.
- **Transition Animations**: Customizable slide transitions, both horizontally and vertically, are applied based on the screen's context.
- **Parameterized Routes**: The app supports dynamic parameters, allowing routes to be flexible and adaptable to various contexts.
- **Snackbar Integration**: A global snackbar host is utilized for displaying feedback to users during navigation.
- **Flexible Transitions**: Each screen has tailored transition animations for smoother navigation, enhancing the user experience.

---
