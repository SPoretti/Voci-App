# Voci app Documentation

![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)

## Introduction

### Overview
This project was created as an assignment for the University of Milano Bicocca course "Dispositivi Mobili".

### Idea
The idea is to create an app that would help catalog and manage requests and updates about homeless people in Milan. It was born because of a member of the team who is also a member of the VoCi ONLUS, an organization that aims to aid the homeless people of their city.
Website: [VoCi ONLUS](https://www.volontaricittadini.it)

### Team
We are students of the University of [Milano Bicocca](https://www.unimib.it/), and we are interested in making an app that not only will get us a good mark but that can be used and can be helpful to a good cause.
- Samuele "DreoX" - 904280
- Matthias "Inutiliax" - 894374
- Olcio "Tu padre" - 000000
- Enrico "Il magnifico" - 000000
- Gabriele "Fish" - 000000

## Technologies

### IDEs
We used Android Studio to write and test the code and Writerside to write the documentation. Both come from a well-established company in the coding world called "JetBrains". Android Studio is built in conjunction with Google.

### Languages and Libraries
Our app is primarily written in Kotlin, a modern, concise, and type-safe programming language designed for Android development. It leverages various libraries to enhance functionality:
- **Jetpack**: A suite of libraries from Google that simplify common Android development tasks. This includes libraries for navigation, lifecycle management, UI components, and more.
- **Material Design 3**: The latest iteration of Google's Material Design system for Android. It provides a consistent and beautiful design language with pre-built components like buttons, text fields, and cards.
- **Gradle**: A build automation tool that manages dependencies, builds your app, and packages it for distribution.
- **Firebase**: Google's mobile app development platform. We utilize Firebase to:
   - **Store user data and app data**: Firebase Firestore, a NoSQL cloud database, provides flexible and scalable data storage for your app.
   - **Firebase Authentication (Auth)**: Simplifies user authentication and management, allowing users to sign in with various methods like email/password or social logins.
- **Git & GitHub**: Basic tools for version control and code sharing. We could have used GitLabs but we were more familiar with Github having used it for previous courses (Also we can't give all the data to Google, we have to share it with Microsoft as well).

## Project

### Folder structure

```
    vociapp/
    ├── MainActivity.kt
    ├── ui/
    │   ├── navigation/
    │   │   ├── Screens.kt
    │   │   └── NavGraph.kt
    │   ├── screens/
    │   │   ├── auth/
    │   │   │   ├── SignInScreen.kt
    │   │   │   └── SignUpScreen.kt
    │   │   └── profile/
    │   │       ├── UserProfileScreen.kt
    │   │       └── UpdateProfileScreen.kt
    │   ├── theme/
    │   │   └── Theme.kt
    │   └── viewmodels/
    │       ├── AuthViewModel.kt
    │       └── AuthState.kt
    └── data/
        └── Data.kt
```

## Getting Started

### Prerequisites
- Android Studio installed
- Kotlin plugin enabled
- Firebase project setup with Firestore and Authentication enabled

### Installation
1. Clone the repository from GitHub.
2. Open the project in Android Studio.
3. Sync the Gradle files to download dependencies.
4. Configure Firebase by adding the `google-services.json` file to the `app` directory.
5. Run the project on an Android emulator or physical device.

## Acknowledgements

We would like to thank:
- Our professor for the guidance and support throughout the course.
- VoCi ONLUS for inspiring the project concept and their ongoing work to aid the homeless community.
