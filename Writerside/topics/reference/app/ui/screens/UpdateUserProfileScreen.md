# UpdateUserProfileScreen (UpdateUserProfileScreen.kt)

This screen allows users to update their profile information, including their display name and profile picture URL.

## Functionality

The `UpdateUserProfileScreen` composable provides the following functionality:

1.  **Displays Current Profile:** It fetches and displays the user's current profile information, including their display name and profile picture URL, if available.
2.  **Input Fields:** It provides input fields for the user to edit their display name and profile picture URL.
3.  **Update Button:** It includes an "Update Profile" button that triggers the profile update process when clicked.
4.  **Error Handling:** It handles potential errors during the update process and displays error messages to the user.
5.  **Navigation:** It uses the `navController` to navigate back to the previous screen after a successful update.

## Parameters

The `UpdateUserProfileScreen` composable accepts the following parameters:

*   **`navController`:** A `NavHostController` instance used for navigation between screens.
*   **`authViewModel`:** An `AuthViewModel` instance providing authentication and profile functionalities.

## Usage

The `UpdateUserProfileScreen` composable is typically used within a navigation graph as follows:

## Implementation Details

The `UpdateUserProfileScreen` composable uses the following key components:

*   **`OutlinedTextField`:** For input fields to capture the user's display name and profile picture URL.
*   **`Button`:** To trigger the profile update process.
*   **`LaunchedEffect`:** To perform the profile update asynchronously and handle the result.
*   **`Card`:** To visually group the profile information and input fields.

## Additional Notes

*   The `ProfileTextField` composable is a custom composable used for the input fields.
*   The `authViewModel` is responsible for communicating with the authentication and profile data sources.
*   Error handling is implemented to provide feedback to the user in case of update failures.