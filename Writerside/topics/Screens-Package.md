# Screens [Package]

The `Screens` package contains the primary screens of the application. Each screen serves as a distinct entry point for different functionalities and workflows. The package is structured into sub-packages to group related screens and components logically, ensuring better organization and maintainability.

## Structure

Below is the general structure of the `Screens` package. Each sub-package represents a major feature or domain of the application.

<!-- Add folder tree here -->

## Sub-Packages

### Auth
The `Auth` package includes screens related to user authentication. These screens manage the user sign-in and sign-up processes:
- **Sign In**: Allows users to log into the application.
- **Sign Up**: Enables new users to register for an account.

### Home
The `Home` screen serves as the central hub of the application, providing access to key features and navigation to other screens.

### Profiles
The `Profiles` package handles screens related to user profiles. It is further divided into:
- **Homeless Profile**: Displays detailed information about a homeless individual.
- **Volunteer Profile**: Contains the volunteer's profile and related information.

### Requests
The `Requests` package consists of screens focused on managing user requests. These include:
- **Request Details**: Shows detailed information about a specific request.
- **Requests History**: Displays a list of past requests made by the user.
- **Requests Screen**: Lists all active or pending requests.

### Updates
The `Updates` package is dedicated to screens for creating and managing updates. It includes:
- **Update**: Displays a list of updates.
- **Update Add**: A screen for adding new updates.
- **Update Add Form**: A component used within the update creation workflow.

## Usage
The screens in this package are the main building blocks of the application's navigation structure. They interact with components from the `Components` package to display data and provide functionality in a cohesive manner.
